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


        ConsultasBD consultasBD = new ConsultasBD();

        HashMap<String, Integer> datosVehiculoPorDia = consultasBD.obtenerDia();

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


    private void setupRadarChart(HashMap<String, Integer> datos) {
        if (datos != null) {
            Radar radar = AnyChart.radar();
            radar.title("Datos por día");

            radar.yScale().minimum(0d);
            radar.yScale().minimumGap(0d);
            radar.yScale().ticks().interval(1d);

            radar.xAxis().labels().padding(5d, 5d, 5d, 5d);

            List<DataEntry> data = new ArrayList<>();
            for (String key : datos.keySet()) {
                data.add(new ValueDataEntry(key, datos.get(key)));
            }

            radar.data(data);


            anyChartView7.setChart(radar);
        }
    }
}