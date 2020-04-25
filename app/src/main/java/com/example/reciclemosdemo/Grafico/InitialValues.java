package com.example.reciclemosdemo.Grafico;

import android.app.Application;

public class InitialValues extends Application {
    private String idUsuario;
    private String observacion;

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
