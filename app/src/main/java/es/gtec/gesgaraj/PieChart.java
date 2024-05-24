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
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;

import es.gtec.gesgaraj.conexion.ConexionBD; // Importa la clase ConexionBD

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import java.util.List;

public class PieChart extends AppCompatActivity {
    Button btn_salirpie;
    AnyChartView anyChartView;
    ConexionBD conexionBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pie_chart);

        btn_salirpie = findViewById(R.id.btn_salirpie);
        anyChartView = findViewById(R.id.any_chart_view);
        conexionBD = new ConexionBD(); // iniciacion variable
        Intent intent = getIntent();
        int ocupado = intent.getIntExtra("ocupado", 0);
        int vacio = intent.getIntExtra("vacio", 0);
        int reservado = intent.getIntExtra("reservado", 0);

        setupPieChart(ocupado, vacio, reservado);

        btn_salirpie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PieChart.this, MainPage.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void setupPieChart(int ocupado, int vacio, int reservado){

        if (ocupado == 0 && vacio == 0 && reservado == 0) {
            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            try {
                connection = conexionBD.connect();
                String query = "SELECT ocupado, vacio, reservado FROM ocupacion";
                statement = connection.prepareStatement(query);
                resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    ocupado = resultSet.getInt("ocupado");
                    vacio = resultSet.getInt("vacio");
                    reservado = resultSet.getInt("reservado");
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Error de conexión SQL: ", e.getMessage());
            } finally {
                try {
                    if (resultSet != null) resultSet.close();
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();
        dataEntries.add(new ValueDataEntry("Ocupado", ocupado));
        dataEntries.add(new ValueDataEntry("Vacio", vacio));
        dataEntries.add(new ValueDataEntry("Reservado", reservado));
        pie.data(dataEntries);

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Ocupacion en tiempo real")
                .padding(0d, 0d, 20d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);
        anyChartView.setChart(pie);
    }
}