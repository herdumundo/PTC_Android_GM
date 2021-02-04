package Utilidades;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.maehara_ptc.ConexionSQLiteHelper;
import com.example.maehara_ptc.ConnectionHelperGrupomaehara;
import com.example.maehara_ptc.informes_registros;
import com.example.maehara_ptc.menu_principal;
import com.example.maehara_ptc.registro_liberados;

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
        public static ConexionSQLiteHelper conn,conn_gm;
        public static Connection connect,connect_gm;
        public static ConnectionHelperGrupomaehara conexion = new ConnectionHelperGrupomaehara();
        public static ConnectionHelperGrupomaehara conexion_sincro = new ConnectionHelperGrupomaehara();
        public static  boolean band_login=false;
        //public static boolean flag = true;
        public static boolean hilo_sincro=true;
        public static boolean hilo_sincro_sub=true;
      //  public static boolean hilo_exportar=false;
        public static String mensaje_conexion_menu_principal="";
        public static int color_conexion_menu_principal=0;
        public static int tipo_sincro=1;
        public static int tipo_exportador=1;

        public static void conexion_sqlite(Context context) {
             conn=      new ConexionSQLiteHelper(context,"BD_SQLITE_GM",null,3);
             conn_gm=   new ConexionSQLiteHelper(context,"BD_SQLITE_GM",null,3);

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
                            else if (tipo==2){
                                registro_liberados.txt_fecha_puesta.setText( df.format((dayOfMonth))+ "/" + df.format((monthOfYear + 1))  + "/" +year );
                            }
                            else if(tipo==3){
                                informes_registros.txt_calendario.setText( df.format((dayOfMonth))+ "/" + df.format((monthOfYear + 1))  + "/" +year );
                                }
                        }
                        }, year, month, day);
            registro_liberados.picker.show();
        }

        public static void exportar( Integer cod_interno,Integer tipo) {
            SQLiteDatabase db=conn.getReadableDatabase();
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
                    rs.close();
                    callableStatement.close();

                    pendientes();
                }
                connect.close();
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
            conn.close();

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
                               hilo_sincro=true;
                               final  h_consulta_pendientes threads = new  h_consulta_pendientes();
                               threads.start();

                           }

                       })
                       .setNegativeButton("NO", null)
                       .show();
           }
            else if(tipo==3){
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
                                hilo_sincro=false;

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
            conn.close();
    }

        public static void Eliminar_registro(int cod_interno) {
            try {
                    SQLiteDatabase db_upd=conn.getReadableDatabase();
                    db_upd.execSQL("UPDATE lotes SET  estado_registro=7  WHERE    cod_interno="+cod_interno+"");
                    db_upd.close();
                    conn.close();
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
                connect.close();
                conn.close();
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
                conn.close();
                connect.close();

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
                conn.close();
                connect.close();
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
                conn.close();
                connect.close();

                mensaje_importador="PROCESO FINALIZADO CON EXITO";
            }catch(Exception e){
                mensaje_importador=e.toString();
            }}

        public static void llenar_registrados(String fecha) {
          try {
              SQLiteDatabase db=conn.getReadableDatabase();
              String html="";

              Cursor cursor=db.rawQuery("select  a.cod_carrito,b.descripcion,a.cod_interno,a.fecha_puesta,a.fecha," +
                      "a.cantidad,a.tipo_huevo,a.empacadora,a.cod_clasificacion,a.hora_clasificacion,a.comentario,a.u_medida, case a.tipo_huevo when 'G' then a.cantidad /180 else a.cantidad/360 end as catidad_tipos " +
                      "from lotes a inner join estados_registros b on a.estado_registro=b.id  where a.estado_registro   in(1,2) and fecha='"+fecha+"'"   ,null);

              while (cursor.moveToNext()){

                  html=html+  "<tr>" +
                          "<td>"+cursor.getString(0)+"</td>" +
                          "<td>"+cursor.getString(3)+"</td>" +
                          "<td>"+cursor.getString(6)+"</td>" +
                          "<td>"+cursor.getString(11)+"</td>" +
                          "<td>"+cursor.getString(12)+"</td>" +
                          "<td>"+cursor.getString(1)+"</td>" +
                          "<td>"+cursor.getString(7)+"</td>" +
                          "<td>"+cursor.getString(8)+"</td>" +
                          "<td>"+cursor.getString(9)+"</td>" +
                          "<td>"+cursor.getString(10)+"</td>" +
                          "</tr>";
              }
              cursor.close();
              conn.close();
              String table = "<table border=1> " +
                      "<thead> " +
                      "<tr>" +
                      "<td>CARRITO</td>" +
                      "<td>PUESTA</td>" +
                      "<td>TIPO</td>" +
                      "<td>U. MEDIDA</td>" +
                      "<td>CANTIDAD</td>" +
                      "<td>ESTADO</td>" +
                      "<td>EMPACADORA</td>" +
                      "<td>CATEGORIA</td>" +
                      "<td>HORARIO</td>" +
                      "<td>COMENTARIO</td>" +
                      "</tr> </thead><tbody>"+html+" </tbody></table>" ;

            //  String url="file:///android_asset/index.html";
              informes_registros.wv.loadData(table, "text/html", "utf-8");
          }
          catch (Exception e){
              String mens=e.toString();

          }


        }

        public static void importar_lotes() {

            try {
                connect_gm =  conexion_sincro.Connections();
                if(conexion_sincro.Connections()!=null){

                SQLiteDatabase db_estado= conn_gm.getReadableDatabase();
                db_estado.execSQL("DELETE FROM lotes_sql ");
                db_estado.close();
                SQLiteDatabase db= conn_gm.getReadableDatabase();

                CallableStatement callableStatement=null;
                callableStatement = connect_gm.prepareCall("{call mae_cch_insertar_lotes_disponibles_app( ?,?)}");
                callableStatement .setInt("@parametro1",1);
                callableStatement.registerOutParameter("@mensaje", Types.INTEGER);
                callableStatement.execute();

                Statement stmt =  connect_gm.createStatement();
                ResultSet rs = stmt.executeQuery("select *  from  mae_lotes_disponibles_app");

                int c=0;

                while (rs.next())
                {
                    ContentValues values=new ContentValues();
                    values.put("cod_interno",rs.getString("cod_interno"));
                    values.put("tipo_huevo",rs.getString("tipo_huevo"));
                    values.put("cod_carrito",rs.getString("cod_carrito"));
                    values.put("cod_lote",rs.getString("cod_lote"));
                    values.put("cantidad",rs.getString("cantidad"));
                    values.put("fecha_puesta",rs.getString("fecha_puesta"));
                    values.put("estado_liberacion",rs.getString("estado_liberacion"));
                    values.put("clasificadora",rs.getString("clasificadora"));

                    db.insert("lotes_sql", null,values);

                    if(tipo_sincro==2){
                        c++;
                        menu_principal.prodialog.setProgress(c);
                    }
                }

                db.close();
                rs.close();
                conn_gm.close();
                connect_gm.close();
                if(tipo_sincro==2){
                    menu_principal.prodialog.dismiss();
                menu_principal.mensaje_importacion="LOTES ACTUALIZADOS CORRECTAMENTE.";
                }
                }
            }catch(Exception e){
                if(tipo_sincro==2){
                menu_principal.mensaje_importacion=e.toString();
                }
            }}

        public static void pendientes()
        {
            try {
                SQLiteDatabase db=voids.conn.getReadableDatabase();
                Cursor cursor=db.rawQuery("SELECT  count(*)  FROM lotes where   estado_registro =1  " ,null);
                while (cursor.moveToNext())
                {
                    menu_principal.total_pendientes=cursor.getInt(0);
                }
                cursor.close();
                conn.close();
                menu_principal.txt_total_pendientes.setText(String.valueOf( menu_principal.total_pendientes));
            }catch(Exception e)
            {
            }
        }
        public static  void test_conexion(){
            try {
            if( conexion.Connections()!=null){

              // pendientes();
                mensaje_conexion_menu_principal="EN LINEA";
                color_conexion_menu_principal=0xFF00FF00;
                 System.out.println("EN LINEA");
                }
            else {
                //pendientes();
                mensaje_conexion_menu_principal="FUERA DE LINEA";
                color_conexion_menu_principal=0xFFFF0000;
                System.out.println("SIN CONEXION AL SERVER");
                }
            }catch(Exception e)
            {
                mensaje_conexion_menu_principal="FUERA DE LINEA";
                color_conexion_menu_principal=0xFFFF0000;
            }
        }


        public static  class h_consulta_pendientes extends Thread
        {
            @Override
            public void run()
            {

                while (hilo_sincro)
                {

                    try {
                    Thread.sleep((long) 2000);

                    if (hilo_sincro_sub){

                        try {
                            Thread.sleep((long) 2000);
                            hilo_sincro_sub=false;
                            System.out.println("CONSULTA CONEXION");
                            test_conexion();
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    hilo_sincro_sub=true;
                                    menu_principal.txt_estado.setText(mensaje_conexion_menu_principal);
                                    menu_principal.txt_estado.setTextColor(color_conexion_menu_principal);
                                    }
                                });
                            } catch (InterruptedException e) {
                        }
                        }
                        }catch (InterruptedException e) {
                    }
                }
            }
        }

        public static class h_exportar_menu_principal extends Thread
        {
            @Override
            public void run()
            {

                System.out.println("HILO LOGIN");
                try {
                    System.out.println("EL EXPORTADOR  INICIA");

                    exportar(0,1);

                    Thread.sleep((long) 1000);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("EL EXPORTADOR  FINALIZO");
                            System.out.println("INICIA HILO DE CONSULTA PENDIENTES");
                            hilo_sincro=true;
                            final  h_consulta_pendientes threads = new  h_consulta_pendientes();
                            threads.start();
                           if(tipo_exportador==1)
                           {
                               menu_principal.progress_export.dismiss();
                           }
                        }
                    });
                } catch (InterruptedException e) {
                }


            }
        }

        public static class h_importar_lotes extends Thread
        {
            @Override
            public void run()
            {
                hilo_sincro=false;
                System.out.println("HILO IMPORTADOR EJECUTANDOSE");
                pendientes();
                try {
                    Thread.sleep((long) 2000);
                        importar_lotes(  );
                        band_login=false;

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("FINALIZO EL HILO IMPORTADOR.");
                            hilo_sincro=true;
                            final  h_consulta_pendientes threads = new  h_consulta_pendientes();
                            threads.start();

                        }
                    });
                    } catch (InterruptedException e) {
                }


            }
        }

    }
