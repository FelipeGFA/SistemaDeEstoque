/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.sistema.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class connector {

    public static java.sql.Connection connect() {

        java.sql.Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (java.sql.Connection) DriverManager.getConnection(
                    "sql10.freesqldatabase.com",
                    "sql10662533",
                    "9VUyNsa1k3");
            
            System.out.println("Conexao bem sucedida");
        }

        catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
        return conn;
    }
}
