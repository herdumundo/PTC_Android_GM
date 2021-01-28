package Utilidades;

import java.io.Serializable;

/**
 * Created by CHENAO on 7/05/2017.
 */

public class Exportaciones implements Serializable {

    private String cod_carrito;
    private String estado;


    public Exportaciones(String cod_carrito, String estado ) {
        this.cod_carrito = cod_carrito;
        this.estado = estado;
     }

    public Exportaciones(){

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




}
