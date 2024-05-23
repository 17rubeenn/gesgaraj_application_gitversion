package es.gtec.gesgaraj.conexion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import es.gtec.gesgaraj.PieChart;
import es.gtec.gesgaraj.R;

public class ConsultasBD extends AppCompatActivity {

    private String ip = "192.168.1.120";
    private String usuario = "sa";
    private String password = "2025";
    private String basedatos = "gesgaraj_ruben";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obtenerDatosOcupacion();
        pasarDatosOcupacionAPieChart();

    }

    private int[] obtenerDatosOcupacion(){
        int[] datos = new int[3];
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlserver://" + ip + ";databaseName=" + basedatos, usuario, password);
            Statement statement = connection.createStatement();
            String query = "SELECT ocupado, vacio, reservado FROM ocupacion";
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                datos[0] = resultSet.getInt("ocupado");
                datos[1] = resultSet.getInt("vacio");
                datos[2] = resultSet.getInt("reservado");


            }
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }return datos;
    }
    private void pasarDatosOcupacionAPieChart(){
        int[] datosOcupacion = obtenerDatosOcupacion();

        Intent intent = new Intent(this, PieChart.class);
        intent.putExtra("ocupado", datosOcupacion[0]);
        intent.putExtra("vacio", datosOcupacion[1]);
        intent.putExtra("reservado", datosOcupacion[2]);
        startActivity(intent);

        finish();
    }



}