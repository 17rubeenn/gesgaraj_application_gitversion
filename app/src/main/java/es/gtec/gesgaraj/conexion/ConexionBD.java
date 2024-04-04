package es.gtec.gesgaraj.conexion;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBD {

    private String ip = "192.168.1.120";                    // DIRECCION "IP" POR DEFECTO DEL SERVIDOR SQL
    private String usuario = "sa";                          // NOMBRE DE "USUARIO" POR DEFECTO DEL SERVIDOR SQL
    private String password = "2025";                       // "CONTRASEÑA" POR DEFECTO DEL SERVIDOR SQL
    private String basedatos = "gesgaraj_ruben";                    // NOMBRE DE LA "BASE DE DATOS" DEL SERVIDOR SQL POR DEFECTO

    @SuppressLint("NewApi")
    public Connection connect() {
        Connection connection = null;
        String ConnectionURL = null;

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + this.ip + "/" + this.basedatos + ";user=" + this.usuario + ";password=" + this.password + ";";
            connection = DriverManager.getConnection(ConnectionURL);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error de conexion SQL: ", e.getMessage());
        }

        return connection;
    }
}
