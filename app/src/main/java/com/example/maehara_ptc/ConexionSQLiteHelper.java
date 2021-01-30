package com.example.maehara_ptc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {


   public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Lotes_PTC.db";

    public ConexionSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE usuarios (cod_usuario INTEGER PRIMARY KEY,usuario TEXT ,password TEXT, clasificadora TEXT,rol TEXT, nombre TEXT)");
        db.execSQL("CREATE TABLE empacadoras (id INTEGER PRIMARY KEY, clasificadora TEXT,empacadora INTEGER, tipo_huevo TEXT)");
        db.execSQL("CREATE TABLE motivo_retencion (id INTEGER PRIMARY KEY,descripcion TEXT,tipo TEXT)");
        db.execSQL("CREATE TABLE lotes_sql (cod_interno INTEGER,tipo_huevo INTEGER,cod_carrito INTEGER,cod_lote TEXT,cantidad INTEGER,fecha_puesta TEXT,estado_liberacion TEXT,clasificadora TEXT)");
        db.execSQL("CREATE TABLE estados_registros (id INTEGER PRIMARY KEY,descripcion TEXT)");

        db.execSQL("CREATE TABLE lotes (cod_interno INTEGER PRIMARY KEY AUTOINCREMENT, fecha TEXT NOT NULL,fecha_puesta TEXT NOT NULL,cod_carrito  INTEGER NOT NULL,tipo_huevo TEXT NOT NULL,cod_clasificacion TEXT NOT NULL,hora_clasificacion TEXT NOT NULL," +
                "cod_lote TEXT NOT NULL,resp_clasificacion TEXT NOT NULL,resp_control_calidad TEXT NOT NULL,usuario_upd TEXT NOT NULL,u_medida TEXT NOT NULL,cantidad INTEGER NOT NULL," +
                "clasificadora TEXT NOT NULL,clasificadora_actual TEXT NOT NULL,empacadora TEXT NOT NULL," +
                "aviario TEXT ,tipo_almacenamiento TEXT,liberado_por TEXT,comentario TEXT,codigo_borroso TEXT,tipo_maples TEXT,codigo_especial TEXT," +
                "estado_liberacion TEXT,estado TEXT,id_sql_server INTEGER,estado_registro INTEGER,codigo_cepillado TEXT)");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva)
    {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS empacadoras");
        db.execSQL("DROP TABLE IF EXISTS motivo_retencion");
        db.execSQL("DROP TABLE IF EXISTS lotes_sql");
        db.execSQL("DROP TABLE IF EXISTS lotes");
        db.execSQL("DROP TABLE IF EXISTS estados_registros");
         onCreate(db);
    }




}




