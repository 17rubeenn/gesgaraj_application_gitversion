package es.gtec.gesgaraj.conexion;

import static android.system.Os.connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.gtec.gesgaraj.ColumnChart;
import es.gtec.gesgaraj.FunnelChart;
import es.gtec.gesgaraj.PieChart;
import es.gtec.gesgaraj.VennDiagram;

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

        obtenerDatosFacturacion();
        pasarDatosOcupacionAColumnChart();

        pasarDatosVehiculoAVennDiagram();
        obtenerDatosVehiculoPorHorario();

        pasarDatosOcupacionALineChart();


    }

    private int[] obtenerDatosOcupacion() {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datos;
    }

    private void pasarDatosOcupacionAPieChart() {
        int[] datosOcupacion = obtenerDatosOcupacion();

        Intent intent = new Intent(this, PieChart.class);
        intent.putExtra("ocupado", datosOcupacion[0]);
        intent.putExtra("vacio", datosOcupacion[1]);
        intent.putExtra("reservado", datosOcupacion[2]);
        startActivity(intent);

        finish();
    }

    private double[] obtenerDatosFacturacion() {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datos2;
    }

    private void pasarDatosOcupacionAColumnChart() {
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

    public HashMap<String, Integer> obtenerDatosVehiculo() {
        HashMap<String, Integer> tipoCount = new HashMap<>();
        ConexionBD conexionBD = new ConexionBD();
        try (Connection connection = conexionBD.connect()) {
            String query = "SELECT tipo FROM vehiculo";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String tipo = resultSet.getString("tipo");
                    tipoCount.merge(tipo, 1, Integer::sum);
                }
                return tipoCount;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error de conexión SQL: ", e.getMessage());
        }
        return null;
    }

    public Bundle convertMapToBundle(HashMap<String, Integer> map) {
        Bundle bundle = new Bundle();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            bundle.putInt(entry.getKey(), entry.getValue());
        }
        return bundle;
    }

    private void pasarDatosVehiculoAVennDiagram() {
        HashMap<String, Integer> conteoHoras = obtenerDatosVehiculoPorHorario();
        Intent intent = new Intent(this, VennDiagram.class);
        intent.putExtra("mañana", conteoHoras.getOrDefault("mañana", 0));
        intent.putExtra("tarde", conteoHoras.getOrDefault("tarde", 0));
        intent.putExtra("noche", conteoHoras.getOrDefault("noche", 0));
        startActivity(intent);
        finish();
    }

    public HashMap<String, Integer> obtenerDatosVehiculoPorHorario() {
        HashMap<String, Integer> conteoHoras = new HashMap<>();
        try (Connection connection = DriverManager.getConnection("jdbc:sqlserver://" + ip + ";databaseName=" + basedatos, usuario, password)) {
            String query = "SELECT hentrada, hsalida FROM vehiculo";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                int countMañana = 0;
                int countTarde = 0;
                int countNoche = 0;

                while (resultSet.next()) {
                    String hentrada = resultSet.getString("hentrada");
                    String hsalida = resultSet.getString("hsalida");

                    if (hentrada != null) {
                        if (esMañana(hentrada)) countMañana++;
                        else if (esTarde(hentrada)) countTarde++;
                        else if (esNoche(hentrada)) countNoche++;
                    }

                    if (hsalida != null) {
                        if (esMañana(hsalida)) countMañana++;
                        else if (esTarde(hsalida)) countTarde++;
                        else if (esNoche(hsalida)) countNoche++;
                    }
                }

                conteoHoras.put("mañana", countMañana);
                conteoHoras.put("tarde", countTarde);
                conteoHoras.put("noche", countNoche);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("Error de conexión SQL: ", e.getMessage());
        }
        return conteoHoras;
    }

    private boolean esMañana(String hora) {
        int hour = Integer.parseInt(hora.split(":")[0]);
        return hour >= 6 && hour < 12;
    }

    private boolean esTarde(String hora) {
        int hour = Integer.parseInt(hora.split(":")[0]);
        return hour >= 12 && hour < 18;
    }

    private boolean esNoche(String hora) {
        int hour = Integer.parseInt(hora.split(":")[0]);
        return hour >= 18 || hour < 6;
    }

    public HashMap<String, Integer> obtenerDia() {
        HashMap<String, Integer> tipoCount = new HashMap<>();
        ConexionBD conexionBD = new ConexionBD();
        try (Connection connection = conexionBD.connect()) {
            String query = "SELECT dia FROM vehiculo";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String dia = resultSet.getString("dia");
                    tipoCount.merge(dia, 1, Integer::sum);
                }
                return tipoCount;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error de conexión SQL: ", e.getMessage());
        }
        return null;
    }


    public Bundle convertMapToBundle2(HashMap<String, Integer> map) {
        Bundle bundle2 = new Bundle();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            bundle2.putInt(entry.getKey(), entry.getValue());
        }
        return bundle2;
    }

    private double[] obtenerDatosFacturacionAnual() {
        double[] datosFacturacionAnual = new double[12];
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlserver://tu_ip;databaseName=nombre_base_de_datos", "usuario", "contraseña");
            Statement statement = connection.createStatement();
            String query = "SELECT enero, febrero, marzo, abril, mayo, junio, julio, agosto, septiembre, octubre, noviembre, diciembre FROM facturacionanual";
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                datosFacturacionAnual[0] = resultSet.getDouble("enero");
                datosFacturacionAnual[1] = resultSet.getDouble("febrero");
                datosFacturacionAnual[2] = resultSet.getDouble("marzo");
                datosFacturacionAnual[3] = resultSet.getDouble("abril");
                datosFacturacionAnual[4] = resultSet.getDouble("mayo");
                datosFacturacionAnual[5] = resultSet.getDouble("junio");
                datosFacturacionAnual[6] = resultSet.getDouble("julio");
                datosFacturacionAnual[7] = resultSet.getDouble("agosto");
                datosFacturacionAnual[8] = resultSet.getDouble("septiembre");
                datosFacturacionAnual[9] = resultSet.getDouble("octubre");
                datosFacturacionAnual[10] = resultSet.getDouble("noviembre");
                datosFacturacionAnual[11] = resultSet.getDouble("diciembre");
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datosFacturacionAnual;
    }

    private void pasarDatosOcupacionALineChart() {
        double[] datosFacturacionAnual = obtenerDatosFacturacionAnual();

        Intent intent = new Intent(this, ColumnChart.class);
        intent.putExtra("enero", datosFacturacionAnual[0]);
        intent.putExtra("febrero", datosFacturacionAnual[1]);
        intent.putExtra("marzo", datosFacturacionAnual[2]);
        intent.putExtra("abril", datosFacturacionAnual[3]);
        intent.putExtra("mayo", datosFacturacionAnual[4]);
        intent.putExtra("junio", datosFacturacionAnual[5]);
        intent.putExtra("julio", datosFacturacionAnual[6]);
        intent.putExtra("agosto", datosFacturacionAnual[7]);
        intent.putExtra("septiembre", datosFacturacionAnual[8]);
        intent.putExtra("octubre", datosFacturacionAnual[9]);
        intent.putExtra("noviembre", datosFacturacionAnual[10]);
        intent.putExtra("diciembre", datosFacturacionAnual[11]);
        startActivity(intent);

        finish();
    }


}







