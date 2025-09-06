package com.example.vibecoding1;


public class Producto {
    private long id;
    private String nombre;
    private String estado; // "pendiente" o "comprado"
    private String fecha; // formato yyyy-MM-dd

    public Producto(long id, String nombre, String estado, String fecha) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.fecha = fecha;
    }

    public Producto(String nombre, String estado, String fecha) {
        this(-1, nombre, estado, fecha);
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}

