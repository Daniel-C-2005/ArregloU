package edu.ejercicios.domain;

public class Usuario {
    //Atributos
    private int id_usuario;
    private String username;
    private String password;


    //Constructores
    public Usuario() {
    }
    //Constructor para el metodo selectById
    public Usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
    //Constructor para el metodo insert
    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }
    //Constructor para el metodo update
    public Usuario(int id_usuario, String username, String password) {
        this.id_usuario = id_usuario;
        this.username = username;
        this.password = password;
    }


    //Getters y Setters

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //Sobreescribir el metodo toString
    @Override
    public String toString() {
        return "Usuario{" + "id_usuario=" + id_usuario + ", username=" + username + ", password=" + password + '}';
    }

}
