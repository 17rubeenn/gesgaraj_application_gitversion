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
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PieChart extends AppCompatActivity {
    Button btn_salirpie;
    AnyChartView anyChartView;
    String[] garaje = {"ocupado", "vacio", "reservado"};
    int[] valores = {20, 30, 5};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_chart);

        btn_salirpie = findViewById(R.id.btn_salirpie);
        anyChartView = findViewById(R.id.any_chart_view);
        setupPieChart();

        btn_salirpie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PieChart.this, MainPage.class);
                startActivity(i);
                finish();
            }
        });

    }

    public void setupPieChart(){
        Pie pie = AnyChart.pie();
        List<DataEntry> dateEntries = new ArrayList<>();

        for (int i = 0; i < garaje.length; i++){
            dateEntries.add(new ValueDataEntry(garaje[i], valores[i]));
        }
        pie.data(dateEntries);
        anyChartView.setChart(pie);
    }
}