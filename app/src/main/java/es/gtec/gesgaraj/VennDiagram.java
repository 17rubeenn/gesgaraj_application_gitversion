package es.gtec.gesgaraj;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Venn;
import es.gtec.gesgaraj.conexion.ConexionBD; // Importa la clase ConexionBD
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VennDiagram extends AppCompatActivity {

    Button btn_salirvenn;
    AnyChartView anyChartView4;
    ConexionBD conexionBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.venn_diagram);

        btn_salirvenn = findViewById(R.id.btn_salirvenn);
        anyChartView4 = findViewById(R.id.any_chart_view4);
        conexionBD = new ConexionBD(); // Inicializa la variable

        btn_salirvenn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VennDiagram.this, MainPage.class);
                startActivity(i);
                finish();
            }
        });

        // Obtén los datos para el gráfico de Venn
        int mañana = 0;
        int tarde = 0;
        int noche = 0;

        Intent intent = getIntent();
        if (intent.hasExtra("mañana") && intent.hasExtra("tarde") && intent.hasExtra("noche")) {
            mañana = intent.getIntExtra("mañana", 0);
            tarde = intent.getIntExtra("tarde", 0);
            noche = intent.getIntExtra("noche", 0);
        } else {
            // Si los datos no se pasaron desde el intent, obténlos de la base de datos
            HashMap<String, Integer> conteoHoras = obtenerDatosVehiculoPorHorario();
            mañana = conteoHoras.getOrDefault("mañana", 0);
            tarde = conteoHoras.getOrDefault("tarde", 0);
            noche = conteoHoras.getOrDefault("noche", 0);
        }

        // Configuración del gráfico de Venn
        Venn venn = AnyChart.venn();
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Mañana", mañana));
        data.add(new ValueDataEntry("Tarde", tarde));
        data.add(new ValueDataEntry("Noche", noche));
        venn.data(data);

        // Configuración adicional del gráfico
        venn.stroke("2 #FFFFFF");
        venn.labels().format("{%Name}");
        venn.tooltip().titleFormat("{%Name}");

        anyChartView4.setChart(venn);
    }

    public HashMap<String, Integer> obtenerDatosVehiculoPorHorario() {
        HashMap<String, Integer> conteoHoras = new HashMap<>();
        try (Connection connection = conexionBD.connect()) {
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
        } catch (Exception e) {
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
        return hour >= 13 && hour < 19;
    }

    private boolean esNoche(String hora) {
        int hour = Integer.parseInt(hora.split(":")[0]);
        return hour >= 20 || hour < 5;
    }
}