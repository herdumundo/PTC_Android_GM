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
        voids.band_login=true;

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
                voids.sincronizar_usuarios();
                voids.sincronizar_empacadoras();
                voids.sincronizar_motivo_retencion();
                voids.sincronizar_estados();
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        Toast.makeText(MainActivity.this, voids.mensaje_importador, Toast.LENGTH_LONG).show();
                            System.out.println(voids.mensaje_importador);
                            progress_sincro.dismiss();

                    }
                });
            } catch ( Exception e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                progress_sincro.dismiss();
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
                final voids.h_importar_lotes thread = new voids.h_importar_lotes();
                thread.start();
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