package es.gtec.gesgaraj;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Radar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import es.gtec.gesgaraj.conexion.ConsultasBD;

public class RadarChart extends AppCompatActivity {
    Button btn_salirradar;
    AnyChartView anyChartView7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radar_chart);

        btn_salirradar = findViewById(R.id.btn_salirradar);
        anyChartView7 = findViewById(R.id.any_chart_view7);

        // Crear instancia de ConsultasBD
        ConsultasBD consultasBD = new ConsultasBD();

        // Obtener datos de vehículo por día desde la base de datos
        HashMap<String, Integer> datosVehiculoPorDia = consultasBD.obtenerDia();

        // Configurar el gráfico de radar
        setupRadarChart(datosVehiculoPorDia);

        btn_salirradar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RadarChart.this, MainPage.class);
                startActivity(i);
                finish();
            }
        });
    }

    // Método para configurar el gráfico de radar
    private void setupRadarChart(HashMap<String, Integer> datos) {
        if (datos != null) {
            Radar radar = AnyChart.radar();
            radar.title("Datos por día");

            // Configuración de la escala
            radar.yScale().minimum(0d);
            radar.yScale().minimumGap(0d);
            radar.yScale().ticks().interval(1d);

            // Configuración de las etiquetas del eje X
            radar.xAxis().labels().padding(5d, 5d, 5d, 5d);

            // Crear una lista de DataEntry para los datos
            List<DataEntry> data = new ArrayList<>();
            for (String key : datos.keySet()) {
                data.add(new ValueDataEntry(key, datos.get(key)));
            }

            // Establecer los datos en el gráfico
            radar.data(data);

            // Configurar el gráfico en la vista de AnyChart
            anyChartView7.setChart(radar);
        }
    }
}