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
    public static ProgressDialog prodialog;
    Connection connect;
    TextView txt_total_pendientes,txt_estado;
    int total,total_pendientes;
    String mensaje="";
    int color_mensaje=0;
    private volatile boolean flag = true;
    String mensaje_importacion="";
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
        pendientes();
        voids.conexion_sqlite(this);

        final T_pendientes thread = new T_pendientes();
        thread.start();

    }

    public void ir_liberados(View v){
        flag=false;
        Intent i=new Intent(this, registro_liberados.class);
        startActivity(i);
        finish();
    }

    public void ir_menu_informes(View v){
        flag=false;
        Intent i=new Intent(this, menu_informes.class);
        startActivity(i);
        finish();
    }
    private void pendientes()
    {
        try {
            SQLiteDatabase db=voids.conn.getReadableDatabase();
            Cursor cursor=db.rawQuery("SELECT  count(*)  FROM lotes where   estado_registro =1  " ,null);
            while (cursor.moveToNext())
            {
                total_pendientes=cursor.getInt(0);
            }
            txt_total_pendientes.setText(String.valueOf(total_pendientes));
        }catch(Exception e)
        {
        }
    }
    public void exportar (View view){

        exportar_datos();
    }

    public void sincro (View view){

       try {
            //
            ConnectionHelperGrupomaehara conexion = new ConnectionHelperGrupomaehara();
            connect = conexion.Connections();
            Statement stmt3 = connect.createStatement();
            ResultSet rs3 = stmt3.executeQuery("select count(*) as contador  from  mae_lotes_disponibles_app");
            while (rs3.next()) {
                total=rs3.getInt("contador");
            }

            rs3.close();
            connect.close();
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
                        new menu_principal.t_lotes().start();

                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }
    class t_lotes extends Thread {
        @Override
        public void run() {

            try {
                importar_lotes();
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

    private void importar_lotes() {
        try {
            SQLiteDatabase db_estado=voids.conn.getReadableDatabase();
            db_estado.execSQL("DELETE FROM lotes_sql ");
            db_estado.close();
            SQLiteDatabase db=voids.conn.getReadableDatabase();
            ConnectionHelperGrupomaehara conexion = new ConnectionHelperGrupomaehara();
            connect = conexion.Connections();

            CallableStatement callableStatement=null;
            callableStatement = connect.prepareCall("{call mae_cch_insertar_lotes_disponibles_app( ?,?)}");
            callableStatement .setInt("@parametro1",1);
            callableStatement.registerOutParameter("@mensaje", Types.INTEGER);
            callableStatement.execute();

            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select *  from  mae_lotes_disponibles_app");

            int c=0;

            while (rs.next())
            {
                ContentValues values=new ContentValues();
                values.put("cod_interno",rs.getString("cod_interno"));
                values.put("tipo_huevo",rs.getString("tipo_huevo"));
                values.put("cod_carrito",rs.getString("cod_carrito"));
                values.put("cod_lote",rs.getString("cod_lote"));
                values.put("cantidad",rs.getString("cantidad"));
                values.put("fecha_puesta",rs.getString("fecha_puesta"));
                values.put("estado_liberacion",rs.getString("estado_liberacion"));
                values.put("clasificadora",rs.getString("clasificadora"));

                db.insert("lotes_sql", null,values);
                c++;
                prodialog.setProgress(c);
            }

            db.close();
            rs.close();
            voids.conn.close();
            mensaje_importacion="LOTES ACTUALIZADOS CORRECTAMENTE.";
        }catch(Exception e){
            mensaje_importacion=e.toString();
        }}


    private  void test_conexion(){
        conexion c = new conexion();

        if(c.getConexion()!=null){
            try {


                pendientes();
                mensaje="EN LINEA";
                color_mensaje=0xFF00FF00;
                System.out.println("EN LINEA");

            }catch(Exception e)
            {
                mensaje=e.toString();
            }

        }
        else {
            pendientes();
            mensaje="SIN CONEXION";
            color_mensaje=0xFFFF0000;
            System.out.println("SIN CONEXION AL SERVER");
        }
    }

    class T_pendientes extends Thread
    {

        @Override
        public void run()
        {  while (flag)
        {
            try {

                Thread.sleep((long) 5000);
                test_conexion();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //   System.out.println("CONECTADO");

                        txt_estado.setText(mensaje);
                        txt_estado.setTextColor(color_mensaje);
                        txt_total_pendientes.setText(String.valueOf(total_pendientes));
                     }
                });

            } catch (InterruptedException e) {
            }
        }

        }
    }

    private void exportar_datos(){
        voids.exportar(this,0,1);
    }
}
