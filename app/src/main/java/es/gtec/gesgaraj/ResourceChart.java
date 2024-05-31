package es.gtec.gesgaraj;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Resource;
import com.anychart.enums.TimeTrackingMode;

import java.util.ArrayList;
import java.util.List;

public class ResourceChart extends AppCompatActivity {

    Button btn_salirline;
    AnyChartView anyChartView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_chart);

        btn_salirline = findViewById(R.id.btn_salirline);
        anyChartView6 = findViewById(R.id.any_chart_view6);

        btn_salirline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResourceChart.this, MainPage.class);
                startActivity(i);
                finish();
            }
        });

        Resource resource = AnyChart.resource();

        resource.zoomLevel(1d)
                .timeTrackingMode(TimeTrackingMode.ACTIVITY_PER_CHART)
                .currentStartDate("2023-01-01");

        List<DataEntry> data = new ArrayList<>();


        String[] matriculas = {"7264 MDF", "8392 KLP", "9458 GHT", "2309 BFD"};
        boolean[] sanciones = {true, false, true, false};
        String[] descripciones = {"Con sanción", "Libre de sanciones", "Con sanción", "Libre de sanciones"};
        String[] colores = {"#FF0000", "#00FF00", "#FF0000", "#00FF00"};

        for (int i = 0; i < matriculas.length; i++) {
            data.add(new ResourceDataEntry(
                    matriculas[i],
                    descripciones[i],
                    colores[i]
            ));
        }

        resource.data(data);

        anyChartView6.setChart(resource);
    }

    private class ResourceDataEntry extends DataEntry {
        ResourceDataEntry(String name, String description, String color) {
            setValue("name", name);
            setValue("description", description);
            setValue("color", color);
            setValue("activities", new Activity[]{
                    new Activity(name, new Interval[]{new Interval("2023-01-01", "2023-01-02", 120)}, color)
            });
        }
    }

    private class Activity extends DataEntry {
        Activity(String name, Interval[] intervals, String fill) {
            setValue("name", name);
            setValue("intervals", intervals);
            setValue("fill", fill);
        }
    }

    private class Interval extends DataEntry {
        Interval(String start, String end, Integer minutesPerDay) {
            setValue("start", start);
            setValue("end", end);
            setValue("minutesPerDay", minutesPerDay);
        }
    }
}