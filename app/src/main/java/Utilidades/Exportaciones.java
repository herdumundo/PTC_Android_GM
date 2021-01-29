package Utilidades;

import java.io.Serializable;

/**
 * Created by HERNAN VELAZQUEZ on 29/01/2021.
 */

public class Exportaciones implements Serializable {
    private String cod_interno;
    private String cod_carrito;
    private String estado;
    private String fecha_puesta;
    private String fecha_clasificacion;
    private String tipo_huevo;
    private String cantidad;


    public Exportaciones(String cod_carrito, String estado,String cod_interno,String fecha_puesta, String fecha_clasificacion, String tipo_huevo,String cantidad) {
        this.cod_carrito            = cod_carrito;
        this.estado                 = estado;
        this.cod_interno            = cod_interno;
        this.fecha_puesta           = fecha_puesta;
        this.fecha_clasificacion    = fecha_clasificacion;
        this.tipo_huevo             = tipo_huevo;
        this.cantidad               = cantidad;
     }

    public Exportaciones(){

    }

    public String getCod_interno() {
        return cod_interno;
    }
    public void setCod_interno(String cod_interno) {
        this.cod_interno = cod_interno;
    }

    public String getcod_carrito() {
        return cod_carrito;
    }
    public void setcod_carrito(String cod_carrito) {
        this.cod_carrito = cod_carrito;
    }

    public String getestado() {
        return estado;
    }
    public void setestado(String estado) {
        this.estado = estado;
    }

    public String getFecha_puesta() {
        return fecha_puesta;
    }
    public void setFecha_puesta(String fecha_puesta) {
        this.fecha_puesta = fecha_puesta;
    }

    public String getFecha_clasificacion() {
        return fecha_clasificacion;
    }
    public void setFecha_clasificacion(String fecha_clasificacion) {
        this.fecha_clasificacion = fecha_clasificacion;
    }

    public String getTipo_huevo() {
        return tipo_huevo;
    }
    public void setTipo_huevo(String tipo_huevo) {
        this.tipo_huevo = tipo_huevo;
    }

    public String getCantidad() {
        return cantidad;
    }
    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

}
