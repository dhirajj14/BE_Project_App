package com.example.smartdustbin;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class Line_chart extends AppCompatActivity {
    LineChartView lineChartView;
    String[] axisData = {"01-05-19", "02-05-19", "03-05-19", "04-05-19", "05-05-19", "06-05-19", "07-05-19"};
    int[] yAxisData = {50, 20, 15, 30, 20, 60, 15};


    int[] yAxisData2 = {20, 60, 35, 70, 20, 80, 35};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_line_chart);

        lineChartView = findViewById(R.id.chart);

        List yAxisValues = new ArrayList();
        List yAxisValues2 = new ArrayList();
        List axisValues = new ArrayList();


        Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));
        Line line2 = new Line(yAxisValues2).setColor(Color.parseColor("#9C2755"));

        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }
        for (int i = 0; i < yAxisData2.length; i++) {
            yAxisValues2.add(new PointValue(i, yAxisData2[i]));
        }

        List lines = new ArrayList();
        lines.add(line);
        lines.add(line2);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#00574B"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setName("No. of Times Metal Detected");
        yAxis.setTextColor(Color.parseColor("#00574B"));
        yAxis.setTextSize(16);
        yAxis.setLineColor(Color.parseColor("#00574B"));
        data.setAxisYLeft(yAxis);

        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 110;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);
    }
}
