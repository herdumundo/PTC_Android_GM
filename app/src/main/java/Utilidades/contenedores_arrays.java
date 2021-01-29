package Utilidades;

import android.app.Activity;

import com.example.maehara_ptc.SpinnerDialog;
import com.example.maehara_ptc.registro_liberados;

import java.util.ArrayList;

public class contenedores_arrays {

    public static ArrayList<String> tipo_maples = new ArrayList<>();
    public static ArrayList<String> tipos_huevos = new ArrayList<>();
    public static ArrayList<String> u_medida = new ArrayList<>();
    public static ArrayList<String> horas = new ArrayList<>();
    public static ArrayList<String> tipos_aviarios = new ArrayList<>();
    public static ArrayList<String> empacadoras = new ArrayList<>();
    public static ArrayList<String> cantidades = new ArrayList<>();

    public static void cargar_tipos_maples(Activity activity) {
        tipo_maples.clear();
        tipo_maples.add("IM");
        tipo_maples.add("IIM");
        tipo_maples.add("IIH");
        registro_liberados.spinner_tipo_maples = new SpinnerDialog(activity,tipo_maples,"SELECCION DE TIPO MAPLE");

    }

    public static void cargar_empacadoras() {
        empacadoras.clear();
        empacadoras.add("0");
        empacadoras.add("1");
        empacadoras.add("2");
        empacadoras.add("3");
        empacadoras.add("4");
        empacadoras.add("5");
        empacadoras.add("6");
        empacadoras.add("7");
        empacadoras.add("8");
        empacadoras.add("9");
        empacadoras.add("10");
        empacadoras.add("11");
        registro_liberados.cbox_empacadora.setItems(empacadoras);
    }

    public static void cargar_cantidades(Activity activity) {
        cantidades.clear();
        cantidades.add("1");
        cantidades.add("2");
        cantidades.add("3");
        cantidades.add("4");
        cantidades.add("5");
        cantidades.add("6");
        cantidades.add("7");
        cantidades.add("8");
        cantidades.add("9");
        cantidades.add("10");
        cantidades.add("11");
        cantidades.add("12");
        registro_liberados.spinner_cantidades = new SpinnerDialog(activity,cantidades,"SELECCIONE CANTIDAD DE CAJONES");
    }

    public static void cargar_tipos_aviarios(Activity activity) {
        tipos_aviarios.clear();
        tipos_aviarios.add("M");
        tipos_aviarios.add("T");
         registro_liberados.spinner_tipo_aviario = new SpinnerDialog(activity,tipos_aviarios,"SELECCION DE TIPO DE AVIARIO");

    }
    public static void cargar_horas(Activity activity) {
        horas.clear();
        horas.add("00");
        horas.add("01");
        horas.add("02");
        horas.add("03");
        horas.add("04");
        horas.add("05");
        horas.add("06");
        horas.add("07");
        horas.add("08");
        horas.add("09");
        horas.add("10");
        horas.add("11");
        horas.add("12");
        horas.add("13");
        horas.add("14");
        horas.add("15");
        horas.add("16");
        horas.add("17");
        horas.add("18");
        horas.add("19");
        horas.add("20");
        horas.add("21");
        horas.add("22");
        horas.add("23");

        registro_liberados.spinner_hora_inicio = new SpinnerDialog(activity,horas,"SELECCION DE HORARIO INICIAL");
        registro_liberados.spinner_hora_fin = new SpinnerDialog(activity,horas,"SELECCION DE HORARIO FINAL");

    }
    public static void cargar_tipos_huevos(Activity activity) {
        tipos_huevos.clear();
        tipos_huevos.add("A");
        tipos_huevos.add("B");
        tipos_huevos.add("C");
        tipos_huevos.add("4TA");
        tipos_huevos.add("J");
        tipos_huevos.add("G");
        tipos_huevos.add("S");
        registro_liberados.spinner_tipo_huevo = new SpinnerDialog(activity, tipos_huevos,"SELECCION DE TIPO DE HUEVOS");

    }

    public static void cargar_unidad_medida(String tipo_huevo, Activity activity) {
        u_medida.clear();
        if(tipo_huevo.equals("G")){
            u_medida.add("CAJON GIGANTE");
           }
        else {
            u_medida.add("CARRITO NORMAL");
            u_medida.add("CAJON");

        }

        registro_liberados.spinner_u_medida = new SpinnerDialog(activity, u_medida,"SELECCION DE UNIDAD DE MEDIDA");

    }
}
