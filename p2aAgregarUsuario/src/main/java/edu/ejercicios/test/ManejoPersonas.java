package edu.ejercicios.test;

import edu.ejercicios.datos.Conexion;
import edu.ejercicios.datos.PersonaJDBC;
import edu.ejercicios.domain.Persona;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class ManejoPersonas {

    public static void main(String[] args) {
        //Definimos la variable de conexion
        Connection conexion = null;
        try {
            //Creamos la conexion
            conexion = Conexion.getConnection();


            //el autoCommit esta en true por defecto, con esto lo desactivamos
            //Lo pasamos a false para que no se hagan los cambios automaticamente
            if (conexion.getAutoCommit()) {
                conexion.setAutoCommit(false);
            }
            //Creamos el objeto personaJBDC
            PersonaJDBC personaJDBC = new PersonaJDBC(conexion); //Esto es igual a DAO


           // Persona nuevaPersona = new Persona();
           // nuevaPersona.setNombre("Luis");
           // nuevaPersona.setApellido("Garcia");
           // personaJDBC.insert(nuevaPersona);
            //El commit se puso para que se haga la transaccion(que se haga el cambio)
            //conexion.commit();
            //System.out.println("Se ha hecho commit de la transaccion");

             Scanner sc = new Scanner(System.in);

            //Vamos a actualizar sus datos

            Persona cambioPersona = new Persona();
            cambioPersona.setId_persona(2);
            cambioPersona.setNombre("Cristel");
            cambioPersona.setApellido("Lopez");
            cambioPersona.setEmail("Cristel2005@gmail.com");
            cambioPersona.setTelefono("51280359" +
                    "");
            personaJDBC.update(cambioPersona);
            //Pausa con Sc
            System.out.println("Presiona Enter para continuar");
            sc.nextLine();
            conexion.commit();
            System.out.println("Se ha hecho la transaccion");
        } catch (SQLException ex) {

            ex.printStackTrace(System.out);
            System.out.println("Entramos al rollback");
            try {
                conexion.rollback();
            } catch (Exception ex1) {
                ex1.printStackTrace(System.out);
            }
        }
    }
}
