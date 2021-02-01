package com.example.maehara_ptc;

import Utilidades.voids;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class informes_registros extends AppCompatActivity {
    public static  WebView wv;
    public static TextView txt_calendario;
    ArrayList birdList=new ArrayList<>();
    @Override
    public void onBackPressed() {
        voids.volver_atras( this,this,menu_informes.class,null,2);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informes_registros);
        txt_calendario=(TextView)findViewById(R.id.txt_calendario_infor);

        wv=(WebView)findViewById(R.id.wv);

    }

    public void btn_buscar(View v){

        voids.llenar_registrados(txt_calendario.getText().toString());
    }

    public void calendario(View v){

        voids.calendario(this,3);
    }
}