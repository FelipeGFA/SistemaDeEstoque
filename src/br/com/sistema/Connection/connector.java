/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.sistema.Connection;
import java.sql.*;
/**
 *
 * @author Felipe Firmino
 */
public class connector {
    public static Connection Conector(){
    Connection connection = null;
     //the path for the driver in the lib, in this app it was used the one below
    String driver = "com.mysql.cj.jdbc.Driver";
    //where the data base is installed, in my case it was in my own machine 
    String url = "jdbc:mysql://sql10.freesqldatabase.com:3306/sql10662533";
    String user = "sql10662533";
    String password = "9VUyNsa1k3";
    
    try {
     Class.forName(driver);
     connection = DriverManager.getConnection(url,user,password);
     return connection;
    } catch (Exception e) {
        System.out.println("erro na conexao");
            System.out.println(e.getMessage());                 
            return null;
        }
    }
}
