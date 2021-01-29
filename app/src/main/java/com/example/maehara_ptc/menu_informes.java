package com.example.maehara_ptc;

import Utilidades.voids;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class menu_informes extends AppCompatActivity {
    @Override
    public void onBackPressed() {
             voids.volver_atras(this,this,menu_principal.class,"DESEA IR AL MENU PRINCIPAL?",2);
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_informes);
    }

    public void ir_informe_exportacion_fallida(View v){
         Intent i=new Intent(this, lista_exportaciones_fallidas.class);
        startActivity(i);
        finish();
    }
}