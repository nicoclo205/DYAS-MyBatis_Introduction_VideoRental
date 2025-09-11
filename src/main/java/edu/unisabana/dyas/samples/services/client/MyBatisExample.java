/*
 * Copyright (C) 2015 cesarvefe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.unisabana.dyas.samples.services.client;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import edu.unisabana.dyas.samples.entities.Cliente;
import edu.unisabana.dyas.samples.entities.Item;
import edu.unisabana.dyas.samples.entities.TipoItem;
import edu.unisabana.dyas.sampleprj.dao.mybatis.mappers.ClienteMapper;
import edu.unisabana.dyas.sampleprj.dao.mybatis.mappers.ItemMapper;

/**
 *
 * @author cesarvefe
 */
public class MyBatisExample {

    /**
     * Método que construye una fábrica de sesiones de MyBatis a partir del
     * archivo de configuración ubicado en src/main/resources
     *
     * @return instancia de SQLSessionFactory
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        SqlSessionFactory sqlSessionFactory = null;
        if (sqlSessionFactory == null) {
            InputStream inputStream;
            try {
                inputStream = Resources.getResourceAsStream("mybatis-config.xml");
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e.getCause());
            }
        }
        return sqlSessionFactory;
    }

    /**
     * Programa principal de ejempo de uso de MyBATIS
     * @param args
     * @throws SQLException 
     */
    public static void main(String args[]) throws SQLException {
        SqlSessionFactory sessionfact = getSqlSessionFactory();
        SqlSession sqlss = sessionfact.openSession();

        try {
            //Crear los mappers
            ClienteMapper cm = sqlss.getMapper(ClienteMapper.class);
            ItemMapper im = sqlss.getMapper(ItemMapper.class);
            
            //PRUEBA 1: Consultar TODOS los clientes
            System.out.println("=== CONSULTANDO TODOS LOS CLIENTES ===");
            List<Cliente> clientes = cm.consultarClientes();
            
            for (Cliente cliente : clientes) {
                System.out.println(cliente);
            }
            
            System.out.println("\n" + "=".repeat(60) + "\n");
            
            //PRUEBA 2: Consultar UN cliente específico
            System.out.println("=== CONSULTANDO CLIENTE ESPECIFICO ===");
            
            Cliente cliente1 = cm.consultarCliente(123456789);
            if (cliente1 != null) {
                System.out.println("ENCONTRADO: " + cliente1);
            } else {
                System.out.println("NO ENCONTRADO");
            }
            
            System.out.println("\n" + "=".repeat(60) + "\n");
            
            //PRUEBA 3: Consultar TODOS los items
            System.out.println("=== CONSULTANDO TODOS LOS ITEMS ===");
            List<Item> items = im.consultarItems();
            
            for (Item item : items) {
                System.out.println(item);
            }
            
            System.out.println("\n" + "=".repeat(60) + "\n");
            
            //PRUEBA 4: Consultar UN item específico
            System.out.println("=== CONSULTANDO ITEM ESPECÍFICO ===");
            
            Item item1 = im.consultarItem(1);
            if (item1 != null) {
                System.out.println("ITEM ENCONTRADO: " + item1);
            } else {
                System.out.println("ITEM NO ENCONTRADO");
            }
            
            System.out.println("\n" + "=".repeat(60) + "\n");
            
            //PRUEBA 5: Insertar un nuevo item
            System.out.println("=== INSERTANDO NUEVO ITEM ===");
            
            // Crear un nuevo TipoItem (Electrónico)
            TipoItem tipoElectronico = new TipoItem();
            tipoElectronico.setID(1); // Usar el tipo Electrónico que ya existe
            tipoElectronico.setDescripcion("Electrónico");
            
            // Crear un nuevo Item
            Item nuevoItem = new Item();
            nuevoItem.setId(4); // ID que no existe en la BD
            nuevoItem.setNombre("Smartphone");
            nuevoItem.setDescrpcion("Smartphone última generación");
            nuevoItem.setFechaLanzamiento(Date.valueOf("2024-01-01"));
            nuevoItem.setTarifaxDia(3000);
            nuevoItem.setFormatoRenta("Diario");
            nuevoItem.setGenero("Tecnología");
            nuevoItem.setTipo(tipoElectronico);
            
            System.out.println("Insertando nuevo item: " + nuevoItem.getNombre());
            im.insertarItem(nuevoItem);
            System.out.println("Item insertado exitosamente!");
            
            System.out.println("\n" + "=".repeat(60) + "\n");
            
            //PRUEBA 6: Verificar que el item se insertó
            System.out.println("=== VERIFICANDO ITEM INSERTADO ===");
            Item itemInsertado = im.consultarItem(4);
            if (itemInsertado != null) {
                System.out.println("ITEM VERIFICADO: " + itemInsertado);
            } else {
                System.out.println("ITEM NO SE INSERTÓ CORRECTAMENTE");
            }
            
            System.out.println("\n" + "=".repeat(60) + "\n");
            
            //PRUEBA 7: Agregar nueva renta con el item insertado
            System.out.println("=== AGREGANDO RENTA CON NUEVO ITEM ===");
            
            Date fechaInicio = Date.valueOf("2024-09-16");
            Date fechaFin = Date.valueOf("2024-09-21");
            
            System.out.println("Agregando renta del nuevo item (Smartphone) a Pedro Rodriguez:");
            cm.agregarItemRentadoACliente(555555555, 4, fechaInicio, fechaFin);
            System.out.println("Renta agregada exitosamente!");
            
            System.out.println("\n" + "=".repeat(60) + "\n");
            
            //PRUEBA 8: Verificar todo el flujo completo
            System.out.println("=== VERIFICACION FINAL - TODOS LOS ITEMS ===");
            List<Item> itemsFinales = im.consultarItems();
            
            for (Item item : itemsFinales) {
                System.out.println(item);
            }
            
            System.out.println("\n=== VERIFICACION FINAL - TODOS LOS CLIENTES ===");
            List<Cliente> clientesFinales = cm.consultarClientes();
            
            for (Cliente cliente : clientesFinales) {
                System.out.println(cliente);
            }
            
            sqlss.commit();
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            sqlss.rollback();
        } finally {
            sqlss.close();
        }
    }
}