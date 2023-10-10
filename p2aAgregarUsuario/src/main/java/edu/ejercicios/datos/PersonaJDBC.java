package edu.ejercicios.datos;

import edu.ejercicios.domain.Persona;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonaJDBC {
    //En esta capa de datos vamos a manejar la manipulacion de datos del objeto persona

    //Esta variables es la que apunta a la variable transacciones
    private Connection conexionTransaccional;
    //Constantes para manipulacion de la informacion
    private static final String SQL_SELECT = "SELECT id_persona, nombre, apellido, email, telefono FROM persona";
    private static final String SQL_INSERT = "INSERT INTO persona(nombre, apellido, email, telefono) VALUES(?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE persona SET nombre=?, apellido=?, email=?, telefono=? WHERE id_persona=?";
    private static final String SQL_DELETE = "DELETE FROM persona WHERE id_persona=?";

    //Constructor vacio
    public PersonaJDBC() {
    }

    //Constructor, recibe de parametro el manejador de la transaccion
    public PersonaJDBC(Connection conexionTransaccional) {
        this.conexionTransaccional = conexionTransaccional;
    }

    public List<Persona> select() throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Persona persona = null;
        List<Persona> personas = new ArrayList<Persona>();
        try{
            //Ternario para comparar si la transaccion esta activa
            //Si la transaccion esta activa, entonces retornara la conexionTransaccional
            //Sino creara la transaccion
        conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
        stmt = conn.prepareStatement(SQL_SELECT);
        rs = stmt.executeQuery();
        //Entramos a este ciclo while para iterar los resultados de la consulta
        while(rs.next()){
            int id_persona = rs.getInt("id_persona");
            String nombre = rs.getString("nombre");
            String apellido = rs.getString("apellido");
            String email = rs.getString("email");
            String telefono = rs.getString("telefono");
            persona = new Persona();
            persona.setId_persona(id_persona);
            persona.setNombre(nombre);
            persona.setApellido(apellido);
            persona.setEmail(email);
            persona.setTelefono(telefono);
            personas.add(persona);
        }
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            if(this.conexionTransaccional == null){
                Conexion.close(conn);
            }
        } return personas;

    }
    public int insert(Persona persona) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try{
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setString(1, persona.getNombre());
            stmt.setString(2, persona.getApellido());
            stmt.setString(3, persona.getEmail());
            stmt.setString(4, persona.getTelefono());


            System.out.println("Ejecutando query: " + SQL_INSERT);
            rows = stmt.executeUpdate();
            System.out.println("Registros afectados: " + rows);
        } finally {
            Conexion.close(stmt);
            if(this.conexionTransaccional == null){
                Conexion.close(conn);
            }
        }
        return rows;
    }
    public int update(Persona persona)throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try{
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, persona.getNombre());
            stmt.setString(2, persona.getApellido());
            stmt.setString(3, persona.getEmail());
            stmt.setString(4, persona.getTelefono());
            stmt.setInt(5, persona.getId_persona());

            System.out.println("Ejecutando query: " + SQL_UPDATE);
            rows = stmt.executeUpdate();
            System.out.println("Registros afectados: " + rows);
        } finally {
            Conexion.close(stmt);
            if(this.conexionTransaccional == null){
                Conexion.close(conn);
            }
        }
        return rows;
    }
    public int delete(Persona persona)throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try{
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, persona.getId_persona());

            System.out.println("Ejecutando query: " + SQL_DELETE);
            rows = stmt.executeUpdate();
            System.out.println("Registros afectados: " + rows);
        } finally {
            Conexion.close(stmt);
            if(this.conexionTransaccional == null){
                Conexion.close(conn);
            }
        }
        return rows;
    }
}

