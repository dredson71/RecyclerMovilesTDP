package com.example.reciclemosdemo.Entities;

import java.util.Date;

public class Reciclador {
    private int codigo;
    private String nombre;
    private String apellido;
    private Distrito distrito;
    private String zona;
    private Asociacion asociacion;
    private String password;
    private String salt;
    private String direccion;
    private String dni;
    private String email;
    private Date fecha_Nacimiento;
    private String codFormalizado;
    private String celular;
    private String recilador_Imagen;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Distrito getDistrito() {
        return distrito;
    }

    public void setDistrito(Distrito distrito) {
        this.distrito = distrito;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public Asociacion getAsociacion() {
        return asociacion;
    }

    public void setAsociacion(Asociacion asociacion) {
        this.asociacion = asociacion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFecha_Nacimiento() {
        return fecha_Nacimiento;
    }

    public void setFecha_Nacimiento(Date fecha_Nacimiento) {
        this.fecha_Nacimiento = fecha_Nacimiento;
    }

    public String getCodFormalizado() {
        return codFormalizado;
    }

    public void setCodFormalizado(String codFormalizado) {
        this.codFormalizado = codFormalizado;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getRecilador_Imagen() {
        return recilador_Imagen;
    }

    public void setRecilador_Imagen(String recilador_Imagen) {
        this.recilador_Imagen = recilador_Imagen;
    }
}
