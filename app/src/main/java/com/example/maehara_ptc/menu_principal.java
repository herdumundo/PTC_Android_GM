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
    public static int total,total_pendientes;

    public static String mensaje_importacion="";
    public void onBackPressed()  {
        voids.volver_atras(this,this,MainActivity.class,"DESEA SALIR DE LA APLICACION?",1);
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
        progress_export = ProgressDialog.show(menu_principal.this, "EXPORTANDO DATOS REGISTRADOS.",
                "ESPERE...", true);
        final voids.T_exportar_regist_menu_principal t_exportar = new voids.T_exportar_regist_menu_principal();
        t_exportar.start();
    }

    public void sincro (View view){

       try {
            //
            voids.connect = voids.conexion.Connections();
            Statement stmt3 = voids.connect.createStatement();
            ResultSet rs3 = stmt3.executeQuery("select count(*) as contador  from  mae_lotes_disponibles_app");
            while (rs3.next()) {
                total=rs3.getInt("contador");
            }
            rs3.close();
           voids.connect.close();
        }catch(Exception e){
        }

      new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("SINCRONIZACION.")
                .setMessage("DESEA SINCRONIZAR LOS CARROS EXISTENTES EN EL SISTEMA?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prodialog =  new ProgressDialog( menu_principal.this);
                        prodialog.setMax(total);
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
                        new menu_principal.t_impotar_lotes().start();

                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }

    class t_impotar_lotes extends Thread {
        @Override
        public void run() {
            try {
                voids.importar_lotes(menu_principal.this,2);
                System.out.println("EL IMPORTADOR SE EJECUTO");
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        prodialog.dismiss();

                        new AlertDialog.Builder( menu_principal.this)
                                .setTitle("ATENCION!")
                                .setMessage(mensaje_importacion)
                                .setNegativeButton("CERRAR", null).show();
                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }






    private void exportar_datos(){
        voids.exportar( 0,1);
    }
}
