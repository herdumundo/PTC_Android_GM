package Utilidades;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.maehara_ptc.ConexionSQLiteHelper;
import com.example.maehara_ptc.ConnectionHelperGrupomaehara;
import com.example.maehara_ptc.MainActivity;
import com.example.maehara_ptc.R;
import com.example.maehara_ptc.informes_registros;
import com.example.maehara_ptc.menu_principal;
import com.example.maehara_ptc.registro_liberados;
import com.tapadoo.alerter.Alerter;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

    public class voids
    {
    public static String mensaje_importador="";
        public static ConexionSQLiteHelper conn;
        public static Connection connect;

        public static void conexion_sqlite(Context context) {
              conn=new ConexionSQLiteHelper(context,"BD_SQLITE_GM",null,3);

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

        public static void exportar(Context context,Integer cod_interno,Integer tipo) {

            SQLiteDatabase db=conn.getReadableDatabase();
            ConnectionHelperGrupomaehara conexion = new ConnectionHelperGrupomaehara();
            connect = conexion.Connections();
            Cursor cursor;
            try {
                // connect.setAutoCommit(false);
                SQLiteDatabase db_UPDATE=conn.getReadableDatabase();

                if(tipo==1){ //ESTADO UNO ES IGUAL AL EXPORTADOR GLOBAL.
                    cursor=db.rawQuery("SELECT  * FROM lotes where   estado_registro =1  " ,null);
                }
                else {// EN ESTE CASO EXPORTA SOLO EL REGISTRO QUE SE LE DA REENVIAR EN LA CLASE LISTA_EXPORTACIONES_FALLIDAS.
                    cursor=db.rawQuery("SELECT  * FROM lotes where   cod_interno="+cod_interno+"  " ,null);
                }
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
                    callableStatement.executeQuery();

                    ResultSet rs;

                    rs = callableStatement.getResultSet();
                    if (rs.next())
                    {
                        tipo_mensaje=(rs.getInt(1));
                    }
                    if (callableStatement.getMoreResults())
                    {
                        rs = callableStatement.getResultSet();
                        if (rs.next()) {
                        tipo_mensaje=(rs.getInt(1));
                        }
                    }
                    // detalle_mensaje= callableStatement.getString("@detalle_mensaje");
                   if(tipo_mensaje>0){
                        SQLiteDatabase db_upd=conn.getReadableDatabase();
                        String strSQL = "UPDATE lotes SET  estado_registro="+tipo_mensaje+"  WHERE    cod_interno="+cursor.getString(0)+"";
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
             SQLiteDatabase db=conn.getReadableDatabase();
            Exportaciones Exportaciones=null;

             lista_exportaciones_fails=new ArrayList<Exportaciones>();

            Cursor cursor=db.rawQuery("select  a.cod_carrito,b.descripcion,a.cod_interno,a.fecha_puesta,a.fecha,a.cantidad,a.tipo_huevo from lotes a inner join estados_registros b on a.estado_registro=b.id  where a.estado_registro not in(1,2)"   ,null);

            while (cursor.moveToNext()){

                Exportaciones=new Exportaciones();
                Exportaciones.setcod_carrito(cursor.getString(0));
                Exportaciones.setestado(cursor.getString(1));
                Exportaciones.setCod_interno(cursor.getString(2));
                Exportaciones.setFecha_puesta(cursor.getString(3));
                Exportaciones.setFecha_clasificacion(cursor.getString(4));
                Exportaciones.setCantidad(cursor.getString(5));
                Exportaciones.setTipo_huevo(cursor.getString(6));
                lista_exportaciones_fails.add(Exportaciones);
            }
            cursor.close();

        }

        public static void volver_atras(Context context, Activity activity,Class clase_destino,String texto,int tipo)  {


           if(tipo==1){
               new AlertDialog.Builder(context)
                       .setIcon(android.R.drawable.ic_dialog_alert)
                       .setTitle("ATENCION!!!.")
                       .setMessage(texto)
                       .setPositiveButton("SI", new DialogInterface.OnClickListener()
                       {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               Intent intent = new Intent(context, clase_destino);
                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                               activity.finish();
                               context.startActivity(intent);
                           }

                       })
                       .setNegativeButton("NO", null)
                       .show();
           }
           else {
               Intent intent = new Intent(context, clase_destino);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               activity.finish();
               context.startActivity(intent);
           }
        }

        public static void set_fechas(){

        SQLiteDatabase db_consulta=conn.getReadableDatabase();
        Cursor cursor=db_consulta.rawQuery("SELECT strftime('%d/%m/%Y',date('now'))  as fecha_actual"   ,null);
        if (cursor.moveToNext()){
            registro_liberados.txt_fecha_clasificacion.setText(cursor.getString(0));
            registro_liberados.txt_fecha_puesta.setText(cursor.getString(0));
            registro_liberados.fecha_registro=cursor.getString(0);
          }
        cursor.close();
    }

        public static void Eliminar_registro(int cod_interno) {
            try {
                    SQLiteDatabase db_upd=conn.getReadableDatabase();
                    db_upd.execSQL("UPDATE lotes SET  estado_registro=7  WHERE    cod_interno="+cod_interno+"");
                    db_upd.close();
                }
            catch(Exception e)
            {

                String es=e.toString();
            }


        }

        public static void sincronizar_usuarios() {
            try {
                SQLiteDatabase db1= conn.getReadableDatabase();
                db1.execSQL("delete from usuarios");
                db1.close();

                SQLiteDatabase db= conn.getReadableDatabase();
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
                mensaje_importador="PROCESO FINALIZADO CON EXITO";
            }catch(Exception e){
                mensaje_importador=e.toString();
            }}


        public static void sincronizar_motivo_retencion() {
            try {
                SQLiteDatabase db1= conn.getReadableDatabase();
                db1.execSQL("delete from motivo_retencion");
                db1.close();

                SQLiteDatabase db= conn.getReadableDatabase();
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
                mensaje_importador="PROCESO FINALIZADO CON EXITO";
            }catch(Exception e){
                mensaje_importador=e.toString();
            }}


        public static void sincronizar_empacadoras() {
            try {
                SQLiteDatabase db1=conn.getReadableDatabase();
                db1.execSQL("delete from empacadoras");
                db1.close();

                SQLiteDatabase db=conn.getReadableDatabase();
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
                mensaje_importador="PROCESO FINALIZADO CON EXITO";
            }catch(Exception e){
                mensaje_importador=e.toString();
            }}

        public static void sincronizar_estados() {
            try {
                SQLiteDatabase db1=conn.getReadableDatabase();
                db1.execSQL("delete from estados_registros");
                db1.close();

                SQLiteDatabase db=conn.getReadableDatabase();
                ConnectionHelperGrupomaehara conexion = new ConnectionHelperGrupomaehara();
                connect = conexion.Connections();
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery("select * from mae_cch_estados_ptc_app");
                while ( rs.next())
                {

                    ContentValues values=new ContentValues();
                    values.put("id",rs.getString("id"));
                    values.put("descripcion",rs.getString("descripcion"));
                    db.insert("estados_registros", null,values);
                }
                db.close();
                rs.close();
                mensaje_importador="PROCESO FINALIZADO CON EXITO";
            }catch(Exception e){
                mensaje_importador=e.toString();
            }}

        public static void llenar_registrados() {
          try {
              SQLiteDatabase db=conn.getReadableDatabase();
              String html="";

              Cursor cursor=db.rawQuery("select  a.cod_carrito,b.descripcion,a.cod_interno,a.fecha_puesta,a.fecha,a.cantidad,a.tipo_huevo,a.empacadora,a.cod_clasificacion,a.hora_clasificacion,a.comentario from lotes a inner join estados_registros b on a.estado_registro=b.id  where a.estado_registro   in(1,2)"   ,null);

              while (cursor.moveToNext()){

                  html=html+  "<tr>" +
                          "<td>"+cursor.getString(0)+"</td>" +
                          "<td>"+cursor.getString(3)+"</td>" +
                          "<td>"+cursor.getString(6)+"</td>" +
                          "<td>"+cursor.getString(5)+"</td>" +
                          "<td>"+cursor.getString(1)+"</td>" +
                          "<td>"+cursor.getString(7)+"</td>" +
                          "<td>"+cursor.getString(8)+"</td>" +
                          "<td>"+cursor.getString(9)+"</td>" +
                          "<td>"+cursor.getString(10)+"</td>" +
                          "</tr>";
              }
              cursor.close();
              String table = "<table border=1> " +
                      "<thead> " +
                      "<tr>" +
                      "<td>CARRITO</td>" +
                      "<td>PUESTA</td>" +
                      "<td>TIPO</td>" +
                      "<td>CANTIDAD</td>" +
                      "<td>ESTADO</td>" +
                      "<td>EMPACADORA</td>" +
                      "<td>CATEGORIA</td>" +
                      "<td>HORARIO</td>" +
                      "<td>COMENTARIO</td>" +
                      "</tr> </thead><tbody>"+html+" </tbody></table>" ;
              informes_registros.wv.loadData(table, "text/html", "utf-8");
          }
          catch (Exception e){
              String mens=e.toString();

          }


        }
    }
