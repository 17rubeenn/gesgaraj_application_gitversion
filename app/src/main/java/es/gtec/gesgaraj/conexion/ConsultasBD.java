package es.gtec.gesgaraj.conexion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import es.gtec.gesgaraj.ColumnChart;
import es.gtec.gesgaraj.PieChart;

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

    private double[] obtenerDatosFacturacion(){
        double[] datos2 = new double[7];
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlserver://" + ip + ";databaseName=" + basedatos, usuario, password);
            Statement statement = connection.createStatement();
            String query = "SELECT Lunes, Martes, Miercoles, Jueves, Viernes, Sabado, Domingo FROM facturacion";
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                datos2[0] = resultSet.getDouble("Lunes");
                datos2[1] = resultSet.getDouble("Martes");
                datos2[2] = resultSet.getDouble("Miercoles");
                datos2[3] = resultSet.getDouble("Jueves");
                datos2[4] = resultSet.getDouble("Viernes");
                datos2[5] = resultSet.getDouble("Sabado");
                datos2[6] = resultSet.getDouble("Domingo");


            }
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }return datos2;
    }
    private void pasarDatosOcupacionAColumnChart(){
        double[] datosFacturacion = obtenerDatosFacturacion();

        Intent intent = new Intent(this, ColumnChart.class);
        intent.putExtra("Lunes", datosFacturacion[0]);
        intent.putExtra("Martes", datosFacturacion[1]);
        intent.putExtra("Miercoles", datosFacturacion[2]);
        intent.putExtra("Jueves", datosFacturacion[3]);
        intent.putExtra("Viernes", datosFacturacion[4]);
        intent.putExtra("Sabado", datosFacturacion[5]);
        intent.putExtra("Domingo", datosFacturacion[6]);
        startActivity(intent);

        finish();
    }


}