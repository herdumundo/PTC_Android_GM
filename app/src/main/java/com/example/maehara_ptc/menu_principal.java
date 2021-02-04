package com.example.maehara_ptc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

import Utilidades.contenedor_usuario;
import Utilidades.voids;
import androidx.appcompat.app.AppCompatActivity;


public class menu_principal extends AppCompatActivity {
    public static ProgressDialog prodialog,progress_export;
    public static TextView txt_total_pendientes,txt_estado;
    public static String mensaje_importacion="";
    public static int total,total_pendientes;
    public void onBackPressed()  {
        voids.volver_atras(this,this,MainActivity.class,"DESEA SALIR DE LA APLICACION?",3);
      }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);
        getSupportActionBar().setTitle("USUARIO: "+ contenedor_usuario.nombre_usuario);
        getSupportActionBar().setSubtitle("AREA: "+ contenedor_usuario.area);
        txt_total_pendientes=(TextView)findViewById(R.id.txt_total);
        txt_estado=(TextView)findViewById(R.id.txt_estado);
        voids.pendientes();
        voids.conexion_sqlite(this);

    }

    public void ir_liberados(View v){
        voids.hilo_sincro=false;
        System.out.println("SE CERRO EL HILO DE PENDIENTES.");
        Intent i=new Intent(this, registro_liberados.class);
        startActivity(i);
        finish();
    }

    public void ir_menu_informes(View v){
         voids.hilo_sincro=false;
        System.out.println("SE CERRO EL HILO DE PENDIENTES.");
        Intent i=new Intent(this, menu_informes.class);
        startActivity(i);
        finish();
    }

    public void exportar (View view){
        voids.tipo_exportador=1;
        progress_export = ProgressDialog.show(menu_principal.this, "EXPORTANDO DATOS REGISTRADOS.",
                "ESPERE...", true);
        voids.hilo_sincro=false;
        final voids.h_exportar_menu_principal t_exportar = new voids.h_exportar_menu_principal();
        t_exportar.start();
    }

    public void sincro (View view){
        voids.hilo_sincro=false;

       new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("SINCRONIZACION.")
               .setCancelable(false)
                .setMessage("DESEA SINCRONIZAR LOS CARROS EXISTENTES EN EL SISTEMA?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {


                            voids.tipo_sincro=2;
                            voids.connect_gm = voids.conexion_sincro.Connections();
                            Statement stmt3 =  voids.connect_gm.createStatement();
                            ResultSet rs_cont = stmt3.executeQuery("select count(*) as contador  from  mae_lotes_disponibles_app");
                            if (rs_cont.next()) {
                                total=rs_cont.getInt("contador");
                            }
                            rs_cont.close();
                            stmt3.close();
                        }catch (Exception e){
                            String as=e.toString();

                        }

                         prodialog =  new ProgressDialog( menu_principal.this);
                        prodialog.setMax( total);
                        LayerDrawable progressBarDrawable = new LayerDrawable(
                                new Drawable[]{
                                        new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                new int[]{Color.parseColor("black"),Color.parseColor("black")}),
                                        new ClipDrawable(
                                                new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                        new int[]{Color.parseColor("yellow"),Color.parseColor("yellow")}),
                                                Gravity.START,
                                                ClipDrawable.HORIZONTAL),
                                        new ClipDrawable(
                                                new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                        new int[]{Color.parseColor("yellow"),Color.parseColor("yellow")}),
                                                Gravity.START,
                                                ClipDrawable.HORIZONTAL)
                                });
                        progressBarDrawable.setId(0,android.R.id.background);
                        progressBarDrawable.setId(1,android.R.id.secondaryProgress);
                        progressBarDrawable.setId(2,android.R.id.progress);
                        prodialog.setTitle("DESCARGANDO LOTES DISPONIBLES");
                        prodialog.setMessage("DESCARGANDO...");
                        prodialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        prodialog.setProgressDrawable(progressBarDrawable);
                        prodialog.show();
                        prodialog.setCanceledOnTouchOutside(false);
                        prodialog.setCancelable(false);
                        new voids.h_importar_lotes().start();

                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        voids.hilo_sincro=true;
                        final voids.h_consulta_pendientes threads = new voids.h_consulta_pendientes();
                        threads.start();
                    }
                })
                .show();
    }


    private void exportar_datos(){
        voids.exportar( 0,1);
    }
}
