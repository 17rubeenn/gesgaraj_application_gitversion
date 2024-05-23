package es.gtec.gesgaraj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.anychart.AnyChartView;

public class ColumnChart extends AppCompatActivity {
    Button btn_salircolumn;
    AnyChartView anyChartView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.column_chart);

        btn_salircolumn = findViewById(R.id.btn_salircolumn);
        anyChartView2 = findViewById(R.id.any_chart_view2);

        btn_salircolumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ColumnChart.this, MainPage.class);
                startActivity(i);
                finish();
            }
        });
    }


}