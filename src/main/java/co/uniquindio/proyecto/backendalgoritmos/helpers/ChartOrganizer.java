package co.uniquindio.proyecto.backendalgoritmos.helpers;

import java.util.*;

public class ChartOrganizer {

    public static Map<String, Object> organizeChartData(
            Map<String, Object> valores,
            String titulo,
            String yAxisTitle,
            String xAxisTitle
    ) {
        Map<String, Object> chartData = new HashMap<>();

        chartData.put("title", titulo);
        chartData.put("yAxisTitle", yAxisTitle);
        chartData.put("xAxisTitle", xAxisTitle);
        chartData.put("categories", valores.get("categories"));
        chartData.put("series", valores.get("series"));

        return chartData;
    }

}
