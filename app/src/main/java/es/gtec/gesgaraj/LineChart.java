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
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.gtec.gesgaraj.conexion.ConexionBD;

public class LineChart extends AppCompatActivity {

    Button btn_salirline;
    AnyChartView anyChartView6;
    ConexionBD conexionBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_chart);

        btn_salirline = findViewById(R.id.btn_salirline);
        anyChartView6 = findViewById(R.id.any_chart_view6);
        conexionBD = new ConexionBD();

        btn_salirline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LineChart.this, MainPage.class);
                startActivity(i);
                finish();
            }
        });


        double[] datosFacturacionAnual = obtenerDatosFacturacionAnual();


        setupLineChart(datosFacturacionAnual);
    }


    private double[] obtenerDatosFacturacionAnual() {
        double[] datosFacturacionAnual = new double[12];
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = conexionBD.connect();
            String query = "SELECT enero, febrero, marzo, abril, mayo, junio, julio, agosto, septiembre, octubre, noviembre, diciembre FROM facturacionanual";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return datosFacturacionAnual;
    }


    private void setupLineChart(double[] datosFacturacionAnual) {
        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Ingresos Mensuales de la Empresa");

        cartesian.yAxis(0).title("Ingresos (miles de dólares)");
        cartesian.xAxis(0).title("Meses").labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new CustomDataEntry("enero", datosFacturacionAnual[0]));
        seriesData.add(new CustomDataEntry("febrero", datosFacturacionAnual[1]));
        seriesData.add(new CustomDataEntry("marzo", datosFacturacionAnual[2]));
        seriesData.add(new CustomDataEntry("abril", datosFacturacionAnual[3]));
        seriesData.add(new CustomDataEntry("mayo", datosFacturacionAnual[4]));
        seriesData.add(new CustomDataEntry("junio", datosFacturacionAnual[5]));
        seriesData.add(new CustomDataEntry("julio", datosFacturacionAnual[6]));
        seriesData.add(new CustomDataEntry("agosto", datosFacturacionAnual[7]));
        seriesData.add(new CustomDataEntry("septiembre", datosFacturacionAnual[8]));
        seriesData.add(new CustomDataEntry("octubre", datosFacturacionAnual[9]));
        seriesData.add(new CustomDataEntry("noviembre", datosFacturacionAnual[10]));
        seriesData.add(new CustomDataEntry("diciembre", datosFacturacionAnual[11]));

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(false); // Deshabilitar la leyenda

        anyChartView6.setChart(cartesian);
    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value) {
            super(x, value);
        }
    }
}