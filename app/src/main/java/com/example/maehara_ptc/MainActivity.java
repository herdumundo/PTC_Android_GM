package com.example.maehara_ptc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import Utilidades.contenedor_usuario;
import Utilidades.voids;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Connection connect;
     private ProgressDialog progress_sincro;
    TextView txt_usuario,txt_pass;
    String mensaje="";
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_usuario=(TextView)findViewById(R.id.txt_responsable);
        txt_pass=(TextView)findViewById(R.id.txt_pass);
        voids.conexion_sqlite(this);
    }

    private void sincronizar_usuarios() {
        try {
            borrar_usuario();
            SQLiteDatabase db=voids.conn.getReadableDatabase();
            ConnectionHelperGrupomaehara conexion = new  ConnectionHelperGrupomaehara();
            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            String query = "select * from usuarios where clasificadora in ('A','B','O','H') and rol <> 'i'";
            ResultSet rs = stmt.executeQuery(query);
            while ( rs.next())
            {

                ContentValues values=new ContentValues();
                values.put("nombre",rs.getString("nombre"));
                values.put("usuario",rs.getString("usuario"));
                values.put("password",rs.getString("password"));
                values.put("cod_usuario",rs.getString("cod_usuario"));
                values.put("rol",rs.getString("rol"));
                values.put("clasificadora",rs.getString("clasificadora"));
                db.insert("usuarios", "cod_usuario",values);
            }
            db.close();
            rs.close();
            mensaje="PROCESO FINALIZADO CON EXITO";
        }catch(Exception e){
            mensaje=e.toString();
        }}
    private void sincronizar_empacadoras() {
        try {
            borrar_empacadoras();
            SQLiteDatabase db= voids.conn.getReadableDatabase();
            ConnectionHelperGrupomaehara conexion = new ConnectionHelperGrupomaehara();
            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            String query = "select * from huevos_empacadoras where estado='A'";
            ResultSet rs = stmt.executeQuery(query);
            while ( rs.next())
            {

                ContentValues values=new ContentValues();
                values.put("id",rs.getString("id"));
                values.put("empacadora",rs.getString("empacadora"));
                values.put("tipo_huevo",rs.getString("tipo_huevo"));
                db.insert("empacadoras", "id",values);
            }
            db.close();
            rs.close();
            mensaje="PROCESO FINALIZADO CON EXITO";
        }catch(Exception e){
            mensaje=e.toString();
        }}
     private void sincronizar_motivo_retencion() {
        try {
            borrar_motivo_retencion();
            SQLiteDatabase db=voids.conn.getReadableDatabase();
            ConnectionHelperGrupomaehara conexion = new ConnectionHelperGrupomaehara();
            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from motivo_retencion  ");
            while ( rs.next())
            {

                ContentValues values=new ContentValues();
                values.put("id",rs.getString("id"));
                values.put("descripcion",rs.getString("descripcion"));
                values.put("tipo",rs.getString("tipo"));

                db.insert("motivo_retencion", "id",values);
            }
            db.close();
            rs.close();
            mensaje="PROCESO FINALIZADO CON EXITO";
        }catch(Exception e){
            mensaje=e.toString();
        }}


    private void borrar_usuario(){
        SQLiteDatabase db1=voids.conn.getReadableDatabase();
        db1.execSQL("delete from usuarios");
        db1.close();
    }
    private void borrar_motivo_retencion(){
        SQLiteDatabase db1=voids.conn.getReadableDatabase();
        db1.execSQL("delete from motivo_retencion");
        db1.close();
    }

     private void borrar_empacadoras(){
        SQLiteDatabase db1=voids.conn.getReadableDatabase();
        db1.execSQL("delete from empacadoras");
        db1.close();
    }
    public void sincronizar(View v){
        new AlertDialog.Builder(MainActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("SINCRONIZACION DE USUARIOS.")
                .setMessage("DESEA ACTUALIZAR USUARIOS DISPONIBLES?.")
                .setPositiveButton("SI", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progress_sincro = ProgressDialog.show(MainActivity.this, "SINCRONIZANDO",
                                "ESPERE...", true);
                        new MainActivity.Hilo_sincro().start();

                    }

                })
                .setNegativeButton("NO", null)
                .show();
    }


    class Hilo_sincro extends Thread {
        @Override
        public void run() {

            try {
                sincronizar_usuarios();
                sincronizar_empacadoras();
                sincronizar_motivo_retencion();
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
                            System.out.println(mensaje);
                            progress_sincro.dismiss();

                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void login (View v){

        SQLiteDatabase db=voids.conn.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT usuario,clasificadora,nombre FROM  usuarios " +
        "where  usuario ='"+txt_usuario.getText().toString().trim()+"' " +
        "and    password='"+txt_pass.getText().toString().trim()+"'" ,null);

            if (cursor.moveToNext())
            {
                contenedor_usuario.area=(cursor.getString(1));
                contenedor_usuario.nombre_usuario=(cursor.getString(2));
                contenedor_usuario.usuario=(cursor.getString(0));
                if(cursor.getString(1).equals("O")){
                    contenedor_usuario.categoria="LDO";
                }
                else{
                    contenedor_usuario.categoria="FCO";
                }
                Intent i=new Intent(this, menu_principal.class);
                startActivity(i);
                finish();
            }
            //   Toast.makeText(login2.this, respuesta, Toast.LENGTH_SHORT).show();
            else
            {
                new AlertDialog.Builder(MainActivity.this)
                .setTitle("ATENCION!!!")
                .setMessage("USUARIO INCORRECTO")
                .setNegativeButton("CERRAR", null).show();
            }
             }

}