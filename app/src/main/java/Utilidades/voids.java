package Utilidades;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.DatePicker;

import com.example.maehara_ptc.ConexionSQLiteHelper;
import com.example.maehara_ptc.ConnectionHelperGrupomaehara;
import com.example.maehara_ptc.menu_principal;
import com.example.maehara_ptc.registro_liberados;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class voids {
   public static ConexionSQLiteHelper conn;

    public static void conexion_sqlite(Context context) {
          conn=new ConexionSQLiteHelper(context,"BD_SQLITE_GM",null,1);
      }


    public static ArrayList<Exportaciones> lista_exportaciones_fails;

    public static void calendario(Context context, final int tipo) {


        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        registro_liberados.picker = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        DecimalFormat df = new DecimalFormat("00");
                        SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
                        cldr.set(year, monthOfYear, dayOfMonth);
                        if (tipo==1){
                            registro_liberados.txt_fecha_clasificacion.setText( df.format((dayOfMonth))+ "/" + df.format((monthOfYear + 1))  + "/" +year );
                        }
                        else{
                            registro_liberados.txt_fecha_puesta.setText( df.format((dayOfMonth))+ "/" + df.format((monthOfYear + 1))  + "/" +year );
                            }
                    }


                    }, year, month, day);
        registro_liberados.picker.show();
    }

    public static void exportar(Context context) {
        String detalle_mensaje="";
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context,"bd_usuarios",null,1);

        SQLiteDatabase db=conn.getReadableDatabase();
        ConnectionHelperGrupomaehara conexion = new ConnectionHelperGrupomaehara();
        Connection connect;

        connect = conexion.Connections();
        Cursor cursor;
         try {
            SQLiteDatabase db_UPDATE=conn.getReadableDatabase();

            cursor=db.rawQuery("SELECT  * FROM lotes where   estado_registro =1  " ,null);
            while (cursor.moveToNext()){
                int tipo_mensaje=0;
                CallableStatement callableStatement=null;
                callableStatement = connect.prepareCall("{call mae_cch_pa_liberado_export( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )}");
                callableStatement .setString("@fecha_clasificacion",cursor.getString(1));   //fecha
                callableStatement .setString("@fecha_puesta",cursor.getString(2));          //fecha_puesta
                callableStatement .setString("@cod_carrito",cursor.getString(3));           //cod_carrito
                callableStatement .setString("@tipo_huevo",cursor.getString(4));            //tipo_huevo
                callableStatement .setString("@cod_clasificacion",cursor.getString(5));     //cod_clasificacion
                callableStatement .setString("@hora_clasificacion",cursor.getString(6));    //hora_clasificacion
                callableStatement .setString("@cod_lote",cursor.getString(7));              //cod_lote
                callableStatement .setString("@resp_clasificacion",cursor.getString(8));    //resp_clasificacion
                callableStatement .setString("@resp_control_calidad",cursor.getString(9));  //resp_control_calidad
                callableStatement .setString("@usuario_upd",cursor.getString(10));          //usuario_upd
                callableStatement .setString("@u_medida",cursor.getString(11));             //u_medida
                callableStatement .setInt(   "@cantidad", Integer.parseInt(cursor.getString(12)));             //cantidad
                callableStatement .setString("@clasificadora",cursor.getString(13));        //clasificadora
                callableStatement .setString("@clasificadora_actual",cursor.getString(14)); //clasificadora_actual
                callableStatement .setString("@empacadora",cursor.getString(15));           //empacadora
                callableStatement .setString("@aviario",cursor.getString(16));              //aviario
                callableStatement .setString("@tipo_almacenamiento",cursor.getString(17));  //tipo_almacenamiento
                callableStatement .setString("@liberado_por",cursor.getString(18));         //liberado_por
                callableStatement .setString("@comentario",cursor.getString(19));           //comentario
                callableStatement .setString("@codigo_borroso",cursor.getString(20));       //codigo_borroso
                callableStatement .setString("@tipo_maples",cursor.getString(21));          //tipo_maples
                callableStatement .setString("@codigo_especial",cursor.getString(22));      //codigo_especial
                callableStatement .setString("@estado_liberacion",cursor.getString(23));    //estado_liberacion
                callableStatement .setString("@estado",cursor.getString(24));               //estado

                callableStatement.registerOutParameter("@mensaje", Types.INTEGER);
                //callableStatement.registerOutParameter("@detalle_mensaje", Types.VARCHAR);
                callableStatement.execute();
                tipo_mensaje = callableStatement.getInt("@mensaje");
               // detalle_mensaje= callableStatement.getString("@detalle_mensaje");
               if(tipo_mensaje>0){
                    SQLiteDatabase db_upd=conn.getReadableDatabase();
                    String strSQL = "UPDATE lotes SET  estado_registro="+tipo_mensaje+"  WHERE    cod_interno='"+cursor.getString(0)+"' ";
                    db_upd.execSQL(strSQL);
                    db_UPDATE.close();
                }
            }

        }catch(Exception e)
        {
            String es=e.toString();
            }
    }


    public static void consultarListaexportaciones_fallidas(Context context) {
        ConexionSQLiteHelper conn;
        conn=new ConexionSQLiteHelper(context,"bd_usuarios",null,1);

        SQLiteDatabase db=conn.getReadableDatabase();
        Exportaciones Exportaciones=null;

         lista_exportaciones_fails=new ArrayList<Exportaciones>();

        Cursor cursor=db.rawQuery("select  cod_carrito,fecha_puesta,estado_registro from lotes --where estado_registro not in(1,2)"   ,null);

        while (cursor.moveToNext()){

            Exportaciones=new Exportaciones();
            Exportaciones.setcod_carrito(cursor.getString(0));
            Exportaciones.setestado(cursor.getString(2));
            lista_exportaciones_fails.add(Exportaciones);
        }

    }

    public static void volver_atras(final Context context)  {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("ATENCION!!!.")
                .setMessage("DESEA SALIR DEL REGISTRO PTC?.")
                .setPositiveButton("SI", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, menu_principal.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //finish();
                        context.startActivity(intent);
                    }

                })
                .setNegativeButton("NO", null)
                .show();
    }


    }
