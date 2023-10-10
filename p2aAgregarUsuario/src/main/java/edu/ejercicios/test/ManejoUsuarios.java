package edu.ejercicios.test;

import edu.ejercicios.datos.Conexion;
import edu.ejercicios.datos.UsuarioJDBC;
import edu.ejercicios.domain.Usuario;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.Scanner;

public class ManejoUsuarios {
    public static void main(String[] args) throws SQLException, NoSuchAlgorithmException {
        Scanner sc = new Scanner(System.in);
        int opcion = 0;
        UsuarioJDBC usuarioJDBC = new UsuarioJDBC(); // Instancia de la clase que maneja la base de datos

        do {
            System.out.println("1.- Iniciar sesión"); // Opción para iniciar sesión
            System.out.println("2.- Registrarse"); // Opción para registrar un nuevo usuario
            System.out.println("3.- Salir"); // Opción para salir del programa
            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    iniciarSesion(usuarioJDBC); // Llama al método para iniciar sesión
                    break;
                case 2:
                    registrarUsuario(usuarioJDBC); // Llama al método para registrar un nuevo usuario
                    break;
                case 3:
                    System.out.println("Saliendo..."); // Mensaje de salida
                    break;
                default:
                    System.out.println("Opción no válida"); // Mensaje de opción no válida
                    break;
            }
        } while (opcion != 3); // Continúa hasta que se elija la opción de salir (3)
    }

    private static void registrarUsuario(UsuarioJDBC usuarioJDBC) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese su usuario:"); // Solicita el nombre de usuario
        String username = sc.nextLine();
        System.out.println("Ingrese su contraseña:"); // Solicita la contraseña
        String password = sc.nextLine();
        Usuario usuario = new Usuario(username, password); // Crea un objeto Usuario con los datos

        try {
            if (usuarioJDBC.insert(usuario) > 0) { // Intenta registrar al usuario en la base de datos
                System.out.println("Usuario registrado"); // Mensaje de éxito
            } else {
                System.out.println("No se pudo registrar el usuario"); // Mensaje de error
            }
        } catch (SQLException | NoSuchAlgorithmException throwables) {
            throwables.printStackTrace(); // Maneja las excepciones, en caso de error
        }
    }

    private static void iniciarSesion(UsuarioJDBC usuarioJDBC) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese su usuario:"); // Solicita el nombre de usuario
        String username = sc.nextLine();
        System.out.println("Ingrese su contraseña:"); // Solicita la contraseña
        String password = sc.nextLine();
        Usuario usuario = new Usuario(username, password); // Crea un objeto Usuario con los datos

        try {
            if (usuarioJDBC.autenticarUsuario(usuario)) { // Intenta autenticar al usuario en la base de datos
                System.out.println("Bienvenido " + usuario.getUsername()); // Mensaje de bienvenida
            } else {
                System.out.println("Usuario o contraseña incorrectos"); // Mensaje de error
            }
        } catch (SQLException | NoSuchAlgorithmException throwables) {
            throwables.printStackTrace(); // Maneja las excepciones, en caso de error
        }

    }
}