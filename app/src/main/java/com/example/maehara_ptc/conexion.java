package com.example.maehara_ptc;


import java.sql.Connection;
import java.sql.DriverManager;

public class conexion {
        private static String bd="grupomaehara";
        private static String user="sa";
        private static String pass="Paraguay2017";
        private static String url="jdbc:jtds:sqlserver://172.16.1.202/"+bd;

        public Connection getConexion(){
            Connection cn = null;
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                cn = DriverManager.getConnection(url, user, pass);
            } catch (Exception e) {
                System.out.println("Error en conexión "+e.getMessage());
            }
            return cn;
        }
    }

