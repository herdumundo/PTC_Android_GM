package com.example.maehara_ptc;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import Utilidades.voids;
import androidx.appcompat.app.AppCompatActivity;


public class lista_exportaciones_fallidas extends AppCompatActivity {
   public static ListView listView;
    public void onBackPressed()  {
        voids.volver_atras(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_exportaciones_fallidas);
        listView= (ListView) findViewById(R.id.lv_exportaciones);
        llenar_grilla();
    }

    private void llenar_grilla(){
        voids.consultarListaexportaciones_fallidas(this);
                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.menu.simple_list_item_2, R.id.text1, voids.lista_exportaciones_fails) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(R.id.text1);
                TextView text2 = (TextView) view.findViewById(R.id.text2);

                text1.setText("CARRO NRO.: "+ voids.lista_exportaciones_fails.get(position).getcod_carrito());
                text2.setText("DESCRIPCION DEL ERROR: "+ voids.lista_exportaciones_fails.get(position).getestado());
                view.setBackgroundColor(Color.RED);
                return view;
            }
        };
        listView.setAdapter(adapter);
    }
}