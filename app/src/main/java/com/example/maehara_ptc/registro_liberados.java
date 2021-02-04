package com.example.maehara_ptc;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.tapadoo.alerter.Alerter;

import java.sql.Date;

import Utilidades.contenedor_usuario;
import Utilidades.contenedores_arrays;
import Utilidades.voids;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class registro_liberados extends AppCompatActivity {
    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;
    public static SpinnerDialog spinner_tipo_maples,    spinner_tipo_huevo,     spinner_u_medida,
                                spinner_hora_inicio,    spinner_hora_fin,       spinner_tipo_aviario,spinner_cantidades;

    public static TextView  txt_fecha_clasificacion,txt_fecha_puesta,           txt_tipo_maples,    txt_tipo_huevo,
                            txt_u_medida,           txt_carro,                  txt_hora_inicio,    txt_hora_fin,
                            txt_tipo_aviario,       txt_responsable,            txt_cantidad,       txt_liberado_por,
                            txt_comentario;
    Button btn_fecha_puesta,btn_fecha_clasificacion,btnEscanear ;
    public static DatePickerDialog picker;
    public static MultipleSelectionSpinner cbox_empacadora;
    String codigo_borroso="NO";
    String codigo_cepillado="NO";
    String codigo_especial="NO";
    public static String fecha_registro="";
    ToggleButton toggle_codigo_borroso,toggle_codigo_especial,toggle_codigo_cepillado;
    int CantidadUMedida=0;
    String u_medida=null;
    int cantidad_val=0;

    public void onBackPressed()  {
        voids.volver_atras(this,this,menu_principal.class,"DESEA IR AL MENU PRINCIPAL?",1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_liberados);

        getSupportActionBar().setTitle("REGISTRO DE LIBERADOS");
        getSupportActionBar().setSubtitle("AREA: "+ contenedor_usuario.area);


        txt_carro               =   findViewById(R.id.txt_carro);
        txt_fecha_clasificacion =   findViewById(R.id.txt_fecha_clasificacion);
        txt_tipo_maples         =   findViewById(R.id.txt_tipo_maples);
        txt_fecha_puesta        =   findViewById(R.id.txt_fecha_puesta);
        txt_tipo_huevo          =   findViewById(R.id.txt_tipo_huevo);
        txt_u_medida            =   findViewById(R.id.txt_u_medida);
        txt_hora_inicio         =   findViewById(R.id.txt_hora_inicio);
        txt_hora_fin            =   findViewById(R.id.txt_hora_fin);
        txt_responsable         =   findViewById(R.id.txt_responsable);
        txt_liberado_por        =   findViewById(R.id.txt_liberado_por);
        txt_cantidad            =   findViewById(R.id.txt_cantidad);
        txt_comentario          =   findViewById(R.id.txt_comentario);
        txt_tipo_aviario        =   findViewById(R.id.txt_tipo_aviario);
        btn_fecha_clasificacion =   findViewById(R.id.btn_fecha_clasificacion);
        btn_fecha_puesta        =   findViewById(R.id.btn_fecha_puesta);
        btnEscanear             =   findViewById(R.id.btnEscanear);
        toggle_codigo_borroso   =   findViewById(R.id.codigo_borroso);
        toggle_codigo_cepillado =   findViewById(R.id.codigo_cepillado);
        toggle_codigo_especial  =   findViewById(R.id.codigo_especial);
        cbox_empacadora         = (MultipleSelectionSpinner)  findViewById(R.id.cbox_empacadora);
        voids.conexion_sqlite(this);
        contenedores_arrays.cargar_tipos_maples( this);
        contenedores_arrays.cargar_tipos_huevos(this);
        contenedores_arrays.cargar_horas(this);
        contenedores_arrays.cargar_tipos_aviarios(this);
        contenedores_arrays.cargar_empacadoras();
        verificarYPedirPermisosDeCamara();
        voids.set_fechas();
        btnEscanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!permisoCamaraConcedido) {
                    Toast.makeText(registro_liberados.this, "Por favor permite que la app acceda a la cámara", Toast.LENGTH_SHORT).show();
                    permisoSolicitadoDesdeBoton = true;
                    verificarYPedirPermisosDeCamara();
                    return;
                }
                escanear();
            }
        });
        toggle_codigo_borroso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggle_codigo_borroso.isChecked())
                {
                    codigo_borroso="SI";
                }
                else {
                    codigo_borroso="NO";
                }
            }
        });
        toggle_codigo_especial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggle_codigo_especial.isChecked())
                {
                    codigo_especial="SI";
                }
                else {
                    codigo_especial="NO";
                }
            }
        });
        toggle_codigo_cepillado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggle_codigo_cepillado.isChecked())
                {
                    codigo_cepillado="SI";
                }
                else {
                    codigo_cepillado="NO";
                }
            }
        });
        txt_tipo_maples.setOnClickListener(new View.OnClickListener() {  @Override
        public void onClick(View v) {
            spinner_tipo_maples.showSpinerDialog();
            spinner_tipo_maples.closeTitle="CERRAR";

        } } );

        spinner_tipo_maples.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                txt_tipo_maples.setText(contenedores_arrays.tipo_maples.get(i));
            }
        });

        txt_hora_fin.setOnClickListener(new View.OnClickListener() {  @Override
        public void onClick(View v) {
            spinner_hora_fin.showSpinerDialog();
            spinner_hora_fin.closeTitle="CERRAR";

        } } );

        txt_tipo_aviario.setOnClickListener(new View.OnClickListener() {  @Override
        public void onClick(View v) {
            spinner_tipo_aviario.dTitle="SELECCIONAR TIPO DE AVIARIO";
            spinner_tipo_aviario.closeTitle="CERRAR";
            spinner_tipo_aviario.showSpinerDialog();
        } } );
        spinner_tipo_aviario.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                txt_tipo_aviario.setText(contenedores_arrays.tipos_aviarios.get(i));            }

        });
        spinner_tipo_huevo.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                txt_tipo_huevo.setText(contenedores_arrays.tipos_huevos.get(i));

                contenedores_arrays.cargar_unidad_medida(contenedores_arrays.tipos_huevos.get(i).toString(), registro_liberados.this);

                txt_u_medida.setOnClickListener(new View.OnClickListener() {  @Override
                public void onClick(View v) { // ESTA OPCION SE DA ACTIVA, EN EL CASO DE QUE SE QUIERA SELECCIONAR SOLAMENTE ESE COMBOBOX
                    spinner_u_medida.dTitle="SELECCIONAR UNIDAD DE MEDIDA";
                    spinner_u_medida.closeTitle="CERRAR";
                    spinner_u_medida.showSpinerDialog();
                } } );

                spinner_u_medida.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String s, int i) {
                        txt_u_medida.setText(contenedores_arrays.u_medida.get(i));

                        if(contenedores_arrays.u_medida.get(i).equals("CARRITO NORMAL")){
                           txt_cantidad.setText("1");
                            horas_aviarios_dialogs();
                            txt_cantidad.setOnClickListener(new View.OnClickListener() {  @Override
                            public void onClick(View v) {
                                // DEJA INACTIVO EL CUADRO DE CANTIDADES
                            } } );
                        }
                        else {
                            contenedores_arrays.cargar_cantidades(registro_liberados.this);
                            spinner_cantidades.bindOnSpinerListener(new OnSpinerItemClick() {
                                @Override
                                public void onClick(String s, int i) {
                                    txt_cantidad.setText(contenedores_arrays.cantidades .get(i));
                                    horas_aviarios_dialogs();
                                }
                            });
                            spinner_cantidades.dTitle="SELECCIONE CANTIDAD DE CAJONES";
                            spinner_cantidades.closeTitle="CERRAR";

                            spinner_cantidades.showSpinerDialog();

                            txt_cantidad.setOnClickListener(new View.OnClickListener() {  @Override
                            public void onClick(View v) { // ESTA OPCION SE DA ACTIVA, EN EL CASO DE QUE SE QUIERA SELECCIONAR SOLAMENTE ESE COMBOBOX
                                spinner_cantidades.dTitle="SELECCIONE CANTIDAD DE CAJONES";
                                spinner_cantidades.closeTitle="CERRAR";
                                spinner_cantidades.showSpinerDialog();
                            } } );
                        }



                    }
                });
                spinner_u_medida.dTitle="SELECCIONAR UNIDAD DE MEDIDA";
                spinner_u_medida.closeTitle="CERRAR";
                spinner_u_medida.showSpinerDialog();
            }
        });

    }

    public void ir_fecha_clasificacion (View v){
        voids.calendario(this,1);
    }
    public void ir_fecha_puesta (View v){
        voids.calendario(this,2);
    }
    public void ir_tipo (View v){

        spinner_tipo_huevo.dTitle="SELECCIONAR TIPO DE HUEVO";
        spinner_tipo_huevo.closeTitle="CERRAR";
        spinner_tipo_huevo.showSpinerDialog();
    }
    private void verificarYPedirPermisosDeCamara() {
        int estadoDePermiso = ContextCompat.checkSelfPermission(registro_liberados.this, Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            permisoCamaraConcedido = true;
        } else {
            // Si no, pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions(registro_liberados.this,
                    new String[]{Manifest.permission.CAMERA},
                    CODIGO_PERMISOS_CAMARA);
        }
    }
    private void permisoDeCamaraDenegado() {
        // Esto se llama cuando el usuario hace click en "Denegar" o
        // cuando lo denegó anteriormente
        Toast.makeText(registro_liberados.this, "No puedes escanear si no das permiso", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODIGO_PERMISOS_CAMARA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Escanear directamten solo si fue pedido desde el botón
                    if (permisoSolicitadoDesdeBoton) {
                        escanear();
                    }
                    permisoCamaraConcedido = true;
                } else {
                    permisoDeCamaraDenegado();
                }
                break;
        }
    }

    private void escanear() {
        Intent i = new Intent(registro_liberados.this, ActivityEscanear.class);
        startActivityForResult(i, CODIGO_INTENT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String codigo = data.getStringExtra("codigo");
                    txt_carro.setText(codigo);
                }
            }
        }
    }

    public void registrar_liberados(View v){

        if(txt_u_medida.getText().toString().equals("CAJON GIGANTE"))
        {
            CantidadUMedida= Integer.parseInt(txt_cantidad.getText().toString())*180;
            u_medida="CAJ";
            cantidad_val=Integer.parseInt(txt_cantidad.getText().toString());
        }
        else if(txt_u_medida.getText().toString().equals("CAJON"))
        {
            CantidadUMedida= Integer.parseInt(txt_cantidad.getText().toString())*360;
            u_medida="CAJ";
            cantidad_val=Integer.parseInt(txt_cantidad.getText().toString());

        }
        else
        {
            CantidadUMedida= 4320;
            u_medida="CAR";
            cantidad_val=12;
        }
 new AlertDialog.Builder(this)
                       .setIcon(android.R.drawable.ic_dialog_alert)
                       .setTitle("ATENCION!!!.")
                       .setMessage("DESEA REGISTRAR LOS DATOS INGRESADOS?")
                       .setPositiveButton("SI", new DialogInterface.OnClickListener()
                       {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               try {
                                   int cantidad_existente=0;
                                   SQLiteDatabase db_consulta=voids.conn.getReadableDatabase();
                                   Cursor cursor=db_consulta.rawQuery(" select sum(cantidad) from (select case tipo_huevo when 'G' then cantidad/180 else   cantidad/360 end as cantidad ,cod_carrito" +
                                           "                    from lotes where estado_registro=1 " +
                                           "union all select case tipo_huevo when 1 then cantidad/180 else   cantidad/360 end as cantidad,cod_carrito  from lotes_sql  order by 2) " +
                                           "where cod_carrito="+txt_carro.getText().toString().trim()+" " ,null);
                                   if (cursor.moveToNext())
                                   {
                                       cantidad_existente=cursor.getInt(0);
                                   }


                                   if ((cantidad_existente+cantidad_val)>12)
                                   {
                                       Alerter.create(registro_liberados.this)
                                               .setTitle("ATENCION, CANTIDAD EXCEDIDA")
                                               .setText("CAJONES REGISTRADOS:"+cantidad_existente+"")
                                               .setDuration(10000)
                                               .setBackgroundColor(R.color.viewfinder_laser)
                                               .show();
                                   }

                                   else if(Date.parse(fecha_registro)<Date.parse(txt_fecha_clasificacion.getText().toString()))
                                   {
                                       Alerter.create(registro_liberados.this)
                                               .setTitle("ATENCION!")
                                               .setText("ERROR, FECHA DE CLASIFICACION ES MAYOR A LA FECHA DE HOY")
                                               .setDuration(10000)
                                               .setBackgroundColor(R.color.viewfinder_laser)
                                               .show();
                                   }
                                   else if(Date.parse(fecha_registro)<Date.parse(txt_fecha_puesta.getText().toString()))
                                   {
                                       Alerter.create(registro_liberados.this)
                                               .setTitle("ATENCION!")
                                               .setText("ERROR, FECHA DE PUESTA ES MAYOR A LA FECHA DE HOY")
                                               .setDuration(10000)
                                               .setBackgroundColor(R.color.viewfinder_laser)
                                               .show();
                                   }
                                   else if(Date.parse(txt_fecha_clasificacion.getText().toString())<Date.parse(txt_fecha_puesta.getText().toString()))
                                   {
                                       Alerter.create(registro_liberados.this)
                                               .setTitle("ATENCION!")
                                               .setText("ERROR, FECHA DE CLASIFICACION NO PUEDE SER MENOR A LA FECHA DE PUESTA")
                                               .setDuration(10000)
                                               .setBackgroundColor(R.color.viewfinder_laser)
                                               .show();
                                   }
                                   else if (txt_carro.getText().toString().trim().length()==0||txt_fecha_puesta.getText().toString().trim().length()==0||txt_fecha_clasificacion.getText().toString().trim().length()==0
                                           ||txt_tipo_huevo.getText().toString().trim().length()==0||txt_tipo_aviario.getText().toString().trim().length()==0
                                           ||txt_tipo_maples.getText().toString().trim().length()==0||txt_hora_inicio.getText().toString().trim().length()==0||txt_hora_fin.getText().toString().trim().length()==0
                                           ||txt_u_medida.getText().toString().trim().length()==0||txt_cantidad.getText().toString().trim().length()==0||txt_responsable.getText().toString().trim().length()==0
                                           ||txt_liberado_por.getText().toString().trim().length()==0)
                                   {
                                       Alerter.create(registro_liberados.this)
                                               .setTitle("ATENCION!")
                                               .setText("DEBE COMPLETAR LOS DATOS REQUERIDOS. ")
                                               .setDuration(10000)
                                               .setBackgroundColor(R.color.viewfinder_laser)
                                               .show();
                                   }
                                   else if (txt_carro.getText().toString().length()!=6){
                                       Alerter.create(registro_liberados.this)
                                               .setTitle("ATENCION!")
                                               .setText("NRO. DE CARRO NO VALIDO. ")
                                               .setDuration(10000)
                                               .setBackgroundColor(R.color.viewfinder_laser)
                                               .show();

                                   }
                                   else if (txt_carro.getText().toString().substring(0,1).equals("6")||txt_carro.getText().toString().substring(0,1).equals("9")){

                                       String tipo_almacenamiento="";
                                       String cod_carrito_codigo=null;

                                       if(txt_carro.getText().toString().substring(0,1).equals("6")){
                                           tipo_almacenamiento="C";
                                       }
                                       else if(txt_carro.getText().toString().substring(0,1).equals("9")){
                                           tipo_almacenamiento="P";
                                       }

                                       if(txt_tipo_huevo.getText().toString().equals("A")){
                                           cod_carrito_codigo="4";
                                       }
                                       else if(txt_tipo_huevo.getText().toString().equals("B")){
                                           cod_carrito_codigo="5";
                                       }
                                       else if(txt_tipo_huevo.getText().toString().equals("C")){
                                           cod_carrito_codigo="6";
                                       }
                                       else if(txt_tipo_huevo.getText().toString().equals("4TA")){
                                           cod_carrito_codigo="7";
                                       }
                                       else if(txt_tipo_huevo.getText().toString().equals("S")){
                                           cod_carrito_codigo="3";
                                       }
                                       else if(txt_tipo_huevo.getText().toString().equals("J")){
                                           cod_carrito_codigo="2";
                                       }
                                       else if(txt_tipo_huevo.getText().toString().equals("G")){
                                           cod_carrito_codigo="1";
                                       }

                                       SQLiteDatabase db=voids.conn.getReadableDatabase();
                                       ContentValues values=new ContentValues();
                                       values.put("fecha",txt_fecha_clasificacion.getText().toString());
                                       values.put("fecha_puesta",txt_fecha_puesta.getText().toString());
                                       values.put("cod_carrito",txt_carro.getText().toString());
                                       values.put("tipo_huevo",txt_tipo_huevo.getText().toString());
                                       values.put("cod_clasificacion", contenedor_usuario.categoria);
                                       values.put("hora_clasificacion",txt_hora_inicio.getText().toString()+"-"+txt_hora_fin.getText().toString());
                                       values.put("cod_lote",txt_carro.getText().toString()+"_"+txt_fecha_puesta.getText().toString().replaceAll("/","")+"_"+ contenedor_usuario.categoria+"_"+cod_carrito_codigo);
                                       values.put("resp_clasificacion",txt_responsable.getText().toString());
                                       values.put("resp_control_calidad", contenedor_usuario.nombre_usuario);
                                       values.put("usuario_upd", contenedor_usuario.usuario);
                                       values.put("u_medida",u_medida);
                                       values.put("cantidad",CantidadUMedida);
                                       values.put("clasificadora", contenedor_usuario.area);
                                       values.put("clasificadora_actual", contenedor_usuario.area);
                                       values.put("empacadora",cbox_empacadora.getSelectedItemsAsString());
                                       values.put("aviario",txt_tipo_aviario.getText().toString());
                                       values.put("tipo_almacenamiento",tipo_almacenamiento);
                                       values.put("liberado_por",txt_liberado_por.getText().toString());
                                       values.put("comentario",txt_comentario.getText().toString());
                                       values.put("codigo_borroso",codigo_borroso);
                                       values.put("tipo_maples",txt_tipo_maples.getText().toString());
                                       values.put("codigo_especial",codigo_especial);
                                       values.put("estado_liberacion","L");
                                       values.put("estado","A");
                                       values.put("codigo_cepillado",codigo_cepillado);
                                       values.put("estado_registro",1);//PENDIENTE
                                       db.insert("lotes", "cod_interno",values);
                                       voids.tipo_exportador=2;
                                       final voids.h_exportar_menu_principal t_exportar = new voids.h_exportar_menu_principal();
                                       t_exportar.start();

                                       new AlertDialog.Builder( registro_liberados.this)
                                               .setTitle("INFORME!!!")
                                               .setCancelable(false)
                                               .setMessage("REGISTRADO CON EXITO")
                                               .setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                                                   public void onClick(DialogInterface dialog, int id) {
                                                    Intent i=new Intent(registro_liberados.this,menu_principal.class);
                                                    startActivity(i);
                                                    finish();
                                                   }
                                               }).show();
                                   }
                                   else {

                                       new AlertDialog.Builder(registro_liberados.this)
                                               .setTitle("ATENCION!!!")
                                               .setMessage("NRO. DE CARRO NO VALIDO.").show();
                                   }

                               }
                               catch (Exception e){
                                   new AlertDialog.Builder(registro_liberados.this)
                                           .setTitle("ATENCION!!!")
                                           .setMessage(e.toString()).show();
                               }

                           }

                       })
                       .setNegativeButton("NO", null)
                       .show();

           }



    private void horas_aviarios_dialogs(){
        spinner_hora_inicio.showSpinerDialog();
        spinner_hora_inicio.closeTitle="CERRAR";
        txt_hora_inicio.setOnClickListener(new View.OnClickListener() {  @Override
        public void onClick(View v) {
            spinner_hora_inicio.showSpinerDialog();
            spinner_hora_inicio.closeTitle="CERRAR";

        } } );

        spinner_hora_inicio.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                txt_hora_inicio.setText(contenedores_arrays.horas.get(i));

                spinner_hora_fin.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String s, int i) {
                        txt_hora_fin.setText(contenedores_arrays.horas.get(i));

                        spinner_tipo_aviario.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String s, int i) {
                                txt_tipo_aviario.setText(contenedores_arrays.tipos_aviarios.get(i));
                                txt_responsable.requestFocus();
                            }
                        });
                        spinner_tipo_aviario.dTitle="SELECCIONAR TIPO DE AVIARIO";
                        spinner_tipo_aviario.closeTitle="CERRAR";
                        spinner_tipo_aviario.showSpinerDialog();
                    }
                });
                spinner_hora_fin.dTitle="SELECCIONAR HORA FINAL";
                spinner_hora_fin.closeTitle="CERRAR";
                spinner_hora_fin.showSpinerDialog();

            }
        });

    }

}