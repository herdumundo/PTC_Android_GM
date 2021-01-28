package Utilidades;

import java.io.Serializable;

/**
 * Created by CHENAO on 7/05/2017.
 */

public class Usuario implements Serializable {

    private String id;
    private String nombre;


    public Usuario(String id, String nombre, String telefono) {
        this.id = id;
        this.nombre = nombre;
     }

    public Usuario(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }




}
