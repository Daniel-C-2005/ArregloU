package edu.ejercicios.datos;
import edu.ejercicios.domain.Usuario;

import java.security.*; //Librería para encriptar la contraseña
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioJDBC {
    //Variables para encriptar la contraseña
    private Connection conexionTransaccional;

    //SQL_SELECT es para seleccionar a todos los usuarios
    private static final String SQL_SELECT = "SELECT id_usuario, username, password FROM usuario";

    // SQL_SELECT_BY_ID es para seleccionar a un usuario por su ID
    private static final String SQL_SELECT_BY_ID = "SELECT id_usuario, username, password FROM usuario WHERE id_usuario = ?";

    // SQL_VALIDATE es para validar el usuario y contraseña al momento de hacer login
    private static final String SQL_VALIDATE = "SELECT id_usuario, username, password FROM usuario WHERE username = ? AND password = ?";

    //SQL_INSERT es para insertar un nuevo usuario
    private static  final String SQL_INSERT = "INSERT INTO usuario(username, password) VALUES(?, ?)";

    //SQL_UPDATE es para actualizar un usuario
    private static  final String SQL_UPDATE = "UPDATE usuario SET username=?, password=? WHERE id_usuario = ?";

   //SQL_DELETE es para eliminar un usuario
    private static  final String SQL_DELETE = "DELETE FROM usuario WHERE id_usuario=?";


    //Constructor vacio
    public UsuarioJDBC() {

    }

    //Constructor, recibe de parametro el manejador de la transaccion
    public UsuarioJDBC(Connection conexionTransaccional) {
        this.conexionTransaccional = conexionTransaccional;
    }

    //Metodo para encriptar la contraseña usando SHA-256
    public static String getSHA(String input) throws NoSuchAlgorithmException {
        // El metodo getInstance() es llamado con el algoritmo SHA-256
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // Metodo digest() llamado
        // Calcula el mensaje digest de la entrada como bytes
        // Y retorna el arreglo de bytes
        byte[] messageDigest = md.digest(input.getBytes());

        // Convierte el arreglo de bytes en representacion signum
        StringBuilder sb = new StringBuilder();

        // Convierte el arreglo de bytes en representacion hexadecimal
        for (byte b : messageDigest) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    //Ya que usamos dos metodos Select, uno para seleccionar a todos los usuarios y otro para seleccionar a un usuario por su ID
    //Debemos hacer un metodo para no repetir codigo
    //Este metodo recibe de parametro el ID del usuario
    // se crea un método que recibe un ResultSet y devuelve un objeto Persona
    private Usuario crearUsuario(ResultSet rs) throws SQLException {
        int id_usuario = rs.getInt("id_usuario");
        String username = rs.getString("username");
        String password = rs.getString("password");

        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setId_usuario(id_usuario);
        usuarioEncontrado.setUsername(username);
        usuarioEncontrado.setPassword(password);

        return usuarioEncontrado;
    }

    //Metodo para seleccionar a todos los usuarios
    public List<Usuario> select() throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;
        List<Usuario> usuarios = new ArrayList<Usuario>();

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while(rs.next()){
                int id_usuario = rs.getInt("id_usuario");
                String username = rs.getString("username");
                String password = rs.getString("password");

                usuario = new Usuario();
                usuario.setId_usuario(id_usuario);
                usuario.setUsername(username);
                usuario.setPassword(password);

                usuarios.add(usuario);
            }

        }finally {
         Conexion.close(rs);
           Conexion.close(stmt);
              Conexion.close(conn);
                if(this.conexionTransaccional == null){
                    Conexion.close(conn);
                }

        } return  usuarios;
    }

    //Metodo para seleccionar a un usuario por su ID
     public  Usuario selectById(Usuario usuario) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuarioEncontrado = null;

        try{
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, usuario.getId_usuario());
            rs = stmt.executeQuery();
            while(rs.next()){
                usuarioEncontrado = crearUsuario(rs);
            }
        }finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
            if(this.conexionTransaccional == null){
                Conexion.close(conn);
            }
        } return usuarioEncontrado;
     }

    // Método para insertar un usuario en la base de datos
    // Encripta la contraseña antes de almacenarla usando SHA-256

    public int insert(Usuario usuario) throws SQLException, NoSuchAlgorithmException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, getSHA(usuario.getPassword()));

            // Encriptar la contraseña antes de almacenarla en la base de datos
            String hashedPassword = getSHA(usuario.getPassword());
            stmt.setString(2, hashedPassword);

            System.out.println("Ejecutando query: " + SQL_INSERT);
            rows = stmt.executeUpdate();
            System.out.println("Registros afectados: " + rows);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
            if (this.conexionTransaccional == null) {
                Conexion.close(conn);
            }
        }
        return rows;
    }

    // Update modificado para encriptar la contraseña antes de actualizarla en la base de datos
    public  int update(Usuario usuario) throws SQLException, NoSuchAlgorithmException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, getSHA(usuario.getPassword()));
            stmt.setInt(3, usuario.getId_usuario());

            //Encryptamos la contraseña antes de actualizarla
            String hashedPassword = getSHA(usuario.getPassword());
            stmt.setString(2, hashedPassword);

            stmt.setInt(3, usuario.getId_usuario());


            System.out.println("Ejecutando query: " + SQL_UPDATE);
            rows = stmt.executeUpdate();
            System.out.println("Registros afectados: " + rows);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
            if (this.conexionTransaccional == null) {
                Conexion.close(conn);
            }
        }
        return rows;
    }
    public int delete(Usuario usuario) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, usuario.getId_usuario());

            System.out.println("Ejecutando query: " + SQL_DELETE);
            rows = stmt.executeUpdate();
            System.out.println("Registros afectados: " + rows);
        } finally {
            Conexion.close(stmt);
            if (this.conexionTransaccional == null) {
                Conexion.close(conn);
            }
        }

        return rows;
    }

    public boolean autenticarUsuario(Usuario Usuario) throws SQLException, NoSuchAlgorithmException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_VALIDATE);
            stmt.setString(1, Usuario.getUsername());
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Obtener la contraseña almacenada en la base de datos
                String storedPassword = rs.getString("password");

                // Encriptar la contraseña proporcionada y compararla con la almacenada
                String hashedPassword = getSHA(Usuario.getPassword());

                return hashedPassword.equals(storedPassword);
            } else {
                // Si no se encuentra el usuario, la autenticación falla
                return false;
            }
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            if (this.conexionTransaccional == null) {
                Conexion.close(conn);
            }
        }
    }



}