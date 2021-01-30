package com.example.maehara_ptc;

import Utilidades.voids;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.GridView;

import java.util.ArrayList;

public class informes_registros extends AppCompatActivity {
    public static  WebView wv;
    ArrayList birdList=new ArrayList<>();
    @Override
    public void onBackPressed() {
        voids.volver_atras( this,this,menu_informes.class,null,2);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informes_registros);


        wv=(WebView)findViewById(R.id.wv);
        voids.llenar_registrados();
    }
}