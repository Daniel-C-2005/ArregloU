package edu.ejercicios.datos;

import java.sql.*;

public class Conexion {
    //Direccion de la base de datos
    private static final String JBDC_URL = "jdbc:mysql://localhost:3306/p2a?useSSL=false&serverTimezone=UTC";
    //Usuario de la base de datos
    private static final String JDBC_USER = "root";
    //Contraseña de la base de datos
    private static final String JBDC_PASSWORD = "CrisCrd20.";
    //Creando la conexion a la base de datos
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JBDC_URL, JDBC_USER, JBDC_PASSWORD);
    }
    //ciera la conexion al resulset
    public static void close(ResultSet rs){
        try{
            if (rs != null) {
                rs.close();
            }


        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }
    //cierra la conexion al statement
    public static void close(PreparedStatement stmt){
        try{
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }
    //cierra la conexion a la base de datos
    public static void close(Connection conn){
        try{
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }
}
