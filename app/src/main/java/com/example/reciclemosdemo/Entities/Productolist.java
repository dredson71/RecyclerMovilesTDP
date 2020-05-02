package com.example.reciclemosdemo.Entities;

import com.example.reciclemosdemo.Entities.Bolsa;
import com.example.reciclemosdemo.Entities.Producto;

public class Productolist {
    private String nombre;
    private String categoria;
    private double contenido;
    private double peso;
    private String abreviatura;
    private int codcontenido;
    private int codigo;
    private String urlImage;

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getContenido() {
        return contenido;
    }

    public void setContenido(double contenido) {
        this.contenido = contenido;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public int getCodcontenido() {
        return codcontenido;
    }

    public void setCodcontenido(int codcontenido) {
        this.codcontenido = codcontenido;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
