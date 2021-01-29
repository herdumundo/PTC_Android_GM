package com.example.maehara_ptc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import Utilidades.voids;
import androidx.appcompat.app.AppCompatActivity;


public class lista_exportaciones_fallidas extends AppCompatActivity {
   public static ListView listView;
    public void onBackPressed()  {
        voids.volver_atras(this,this,menu_informes.class,"DESEA IR AL MENU DE INFORMES?",2);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_exportaciones_fallidas);
        listView= (ListView) findViewById(R.id.lv_exportaciones);
        llenar_grilla();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int pos, long l) {
                //  final int id_registro =Integer.parseInt(listaRecogidas.get(pos).getIdregistro().toString());
                String  informacion="Nro registro: "+voids.lista_exportaciones_fails.get(pos).getCod_interno()+"\n";
                informacion+="Nro. carro: "+voids.lista_exportaciones_fails.get(pos).getcod_carrito()+"\n";
                informacion+="Cantidad: "+voids.lista_exportaciones_fails.get(pos).getCantidad()+"\n";
                informacion+="Tipo huevo: "+voids.lista_exportaciones_fails.get(pos).getTipo_huevo()+"\n";
                informacion+="Fecha clasificacion: "+voids.lista_exportaciones_fails.get(pos).getFecha_clasificacion()+"\n";
                informacion+="Fecha puesta: "+voids.lista_exportaciones_fails.get(pos).getFecha_puesta()+"\n";
                informacion+="Estado: "+voids.lista_exportaciones_fails.get(pos).getestado()+"\n";


                AlertDialog.Builder builder = new AlertDialog.Builder( lista_exportaciones_fallidas.this);
                builder.setMessage(informacion)
                            .setCancelable(true)
                        .setPositiveButton("REINTENTAR ENVIO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                AlertDialog.Builder builder_2 = new AlertDialog.Builder(lista_exportaciones_fallidas.this);

                                builder_2.setMessage("ESTA SEGURO QUE DESEA ENVIAR EL REGISTRO?")
                                        .setCancelable(false)
                                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                voids.exportar(lista_exportaciones_fallidas.this,Integer.parseInt(voids.lista_exportaciones_fails.get(pos).getCod_interno()),2);
                                                llenar_grilla();
                                                dialog.cancel();
                                            }
                                        })
                                        .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert_2 = builder_2.create();
                                alert_2.show();


                            }
                        })

                        .setNegativeButton("ELIMINAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        AlertDialog.Builder builder_2 = new AlertDialog.Builder(lista_exportaciones_fallidas.this);

                        builder_2.setMessage("ESTA SEGURO QUE DESEA ELIMINAR EL REGISTRO FALLIDO?")
                                .setCancelable(false)
                                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        //  eliminar_aviarios.eliminar_aviario_pendiente(lista_recogidas.this,id_registro);
                                        voids.Eliminar_registro(Integer.parseInt(voids.lista_exportaciones_fails.get(pos).getCod_interno()));
                                        llenar_grilla();
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert_2 = builder_2.create();
                        alert_2.show();


                    }
                });
                 AlertDialog alert = builder.create();
                alert.show();
            }
        });
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