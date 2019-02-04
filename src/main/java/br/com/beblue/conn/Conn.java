/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.beblue.conn;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Conn {

    // Carrega driver JDBC
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static EntityManager entidade;

    public EntityManager getEntityManager() {
        if (entidade == null) {
            try {
                Map properties = new HashMap();
                properties.put("allowMultiQueries", "true");
                properties.put("autoReconnect", "true");
                properties.put("checkoutTimeout", "1000");
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("bebluePU", properties);
                entidade = emf.createEntityManager();
            } catch (Exception e) {
                return null;
            }
        }
        return entidade;
    }

}
