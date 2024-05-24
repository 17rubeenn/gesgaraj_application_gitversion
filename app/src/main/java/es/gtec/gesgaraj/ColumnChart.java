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
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import es.gtec.gesgaraj.conexion.ConexionBD;

public class ColumnChart extends AppCompatActivity {
    Button btn_salircolumn;
    AnyChartView anyChartView2;
    ConexionBD conexionBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.column_chart);

        btn_salircolumn = findViewById(R.id.btn_salircolumn);
        anyChartView2 = findViewById(R.id.any_chart_view2);
        conexionBD = new ConexionBD();
        Intent intent = getIntent();
        double Lunes = intent.getDoubleExtra("Lunes", 0);
        double Martes = intent.getDoubleExtra("Martes", 0);
        double Miercoles = intent.getDoubleExtra("Miercoles", 0);
        double Jueves = intent.getDoubleExtra("Jueves", 0);
        double Viernes = intent.getDoubleExtra("Viernes", 0);
        double Sabado = intent.getDoubleExtra("Sabado", 0);
        double Domingo = intent.getDoubleExtra("Domingo", 0);

        setupColumnChart(Lunes, Martes, Miercoles, Jueves, Viernes, Sabado, Domingo);

        btn_salircolumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ColumnChart.this, MainPage.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void setupColumnChart(double Lunes, double Martes, double Miercoles, double Jueves, double Viernes, double Sabado, double Domingo) {
        if (Lunes == 0 && Martes == 0 && Miercoles == 0 && Jueves == 0 && Viernes == 0 && Sabado == 0 && Domingo == 0) {
            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            try {
                connection = conexionBD.connect();
                String query = "SELECT Lunes, Martes, Miercoles, Jueves, Viernes, Sabado, Domingo FROM facturacion";
                statement = connection.prepareStatement(query);
                resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    Lunes = resultSet.getDouble("Lunes");
                    Martes = resultSet.getDouble("Martes");
                    Miercoles = resultSet.getDouble("Miercoles");
                    Jueves = resultSet.getDouble("Jueves");
                    Viernes = resultSet.getDouble("Viernes");
                    Sabado = resultSet.getDouble("Sabado");
                    Domingo = resultSet.getDouble("Domingo");
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

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Lunes", Lunes));
        data.add(new ValueDataEntry("Martes", Martes));
        data.add(new ValueDataEntry("Miercoles", Miercoles));
        data.add(new ValueDataEntry("Jueves", Jueves));
        data.add(new ValueDataEntry("Viernes", Viernes));
        data.add(new ValueDataEntry("Sabado", Sabado));
        data.add(new ValueDataEntry("Domingo", Domingo));

        Column column = cartesian.column(data);
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.yScale().minimum(40d);
        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        anyChartView2.setChart(cartesian);
    }
}