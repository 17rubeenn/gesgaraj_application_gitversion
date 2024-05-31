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
import com.anychart.charts.Funnel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.gtec.gesgaraj.conexion.ConsultasBD;

public class FunnelChart extends AppCompatActivity {
    Button btn_salirfunnel;
    AnyChartView anyChartView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.funnel_chart);

        btn_salirfunnel = findViewById(R.id.btn_salirfunnel);
        anyChartView3 = findViewById(R.id.any_chart_view3);

        // Crear instancia de ConsultasBD
        ConsultasBD consultasBD = new ConsultasBD();

        // Obtener datos de vehículo desde la base de datos
        HashMap<String, Integer> datosVehiculo = consultasBD.obtenerDatosVehiculo();

        // Convertir los datos a Bundle
        Bundle extras = consultasBD.convertMapToBundle(datosVehiculo);

        // Configurar el gráfico de embudo
        setupFunnelChart(extras);

        btn_salirfunnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FunnelChart.this, MainPage.class);
                startActivity(i);
                finish();
            }
        });
    }

    // Método para configurar el gráfico de embudo
    private void setupFunnelChart(Bundle extras) {
        Funnel funnel = AnyChart.funnel();
        List<DataEntry> data = new ArrayList<>();

        // Recorrer los extras para obtener los datos de tipo de vehículo y sus recuentos
        for (String key : extras.keySet()) {
            int count = extras.getInt(key);
            data.add(new ValueDataEntry(key, count));
        }

        // Configuración de datos y formato del gráfico de embudo
        funnel.data(data);
        funnel.margin(new String[]{"10", "20%", "10", "20%"});
        funnel.baseWidth("70%").neckWidth("17%");
        funnel.labels().position("outsideleft").format("{%X} - {%Value}");
        funnel.animation(true);

        // Establecer el gráfico de embudo en la vista de AnyChart
        anyChartView3.setChart(funnel);
    }
}