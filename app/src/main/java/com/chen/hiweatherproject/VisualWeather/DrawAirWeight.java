package com.chen.hiweatherproject.VisualWeather;

import android.content.Context;
import android.graphics.Color;

import com.chen.hiweatherproject.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;

public class DrawAirWeight {

    HashMap<String, String> airDatas = new HashMap<>();
    Context context;
    PieChart pieChart;

    public DrawAirWeight(Context context, PieChart pieChart, HashMap<String, String> datas) {
        this.context = context;
        this.pieChart = pieChart;
        this.airDatas = datas;
        initPieChart();
    }

    private void initPieChart(){
        pieChart.setUsePercentValues(false);     // 是否使用百分比显示
        Description description = pieChart.getDescription();
        description.setText("空气成分");  // 设置描述文字
        description.setTextColor(R.color.grey_500);
        pieChart.setHighlightPerTapEnabled(true);       // 设置piechart图标点击Item是否高亮可用
        pieChart.animateX(1000);
        initPieChartData();

        pieChart.setDrawEntryLabels(true); // 设置entry中的描述label是否画进饼状图中
        pieChart.setEntryLabelColor(Color.GRAY);//设置该文字是的颜色
        pieChart.setEntryLabelTextSize(10f);//设置该文字的字体大小

        pieChart.setDrawHoleEnabled(true);//设置圆孔的显隐，也就是内圆
        pieChart.setHoleRadius(28f);//设置内圆的半径。外圆的半径好像是不能设置的，改变控件的宽度和高度，半径会自适应。
        pieChart.setHoleColor(Color.WHITE);//设置内圆的颜色
        pieChart.setDrawCenterText(true);//设置是否显示文字
        pieChart.setCenterText(airDatas.get("level"));//设置饼状图中心的文字
        pieChart.setCenterTextSize(10f);//设置文字的消息
        pieChart.setCenterTextColor(Color.GREEN);//设置文字的颜色
        pieChart.setTransparentCircleRadius(31f);//设置内圆和外圆的一个交叉园的半径，这样会凸显内外部的空间
        pieChart.setTransparentCircleColor(Color.BLACK);//设置透明圆的颜色
        pieChart.setTransparentCircleAlpha(50);//设置透明圆你的透明度

        Legend legend = pieChart.getLegend();//图例
        legend.setEnabled(true);//是否显示
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);//对齐
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);//对齐
        legend.setForm(Legend.LegendForm.DEFAULT);//设置图例的图形样式,默认为圆形
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);//设置图例的排列走向:vertacal相当于分行
        legend.setFormSize(6f);//设置图例的大小
        legend.setTextSize(8f);//设置图注的字体大小
        legend.setFormToTextSpace(4f);//设置图例到图注的距离

        legend.setDrawInside(true);//不是很清楚
        legend.setWordWrapEnabled(false);//设置图列换行(注意使用影响性能,仅适用legend位于图表下面)，我也不知道怎么用的
        legend.setTextColor(Color.BLACK);
    }

    private void initPieChartData(){
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(solveIllegalFormat(this.airDatas.get("PM2.5")), "PM2.5"));
        pieEntries.add(new PieEntry(solveIllegalFormat(this.airDatas.get("PM10")), "PM10"));
        pieEntries.add(new PieEntry(solveIllegalFormat(this.airDatas.get("O3")), "O3"));
        pieEntries.add(new PieEntry(solveIllegalFormat(this.airDatas.get("NO2")), "NO2"));
        pieEntries.add(new PieEntry(solveIllegalFormat(this.airDatas.get("SO2")), "SO2"));
        pieEntries.add(new PieEntry(solveIllegalFormat(this.airDatas.get("CO")), "CO"));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, null);
        pieDataSet.setColors(context.getResources().getColor(R.color.blue_300),
                context.getResources().getColor(R.color.amber_300),
                context.getResources().getColor(R.color.red_300),
                context.getResources().getColor(R.color.green_300),
                context.getResources().getColor(R.color.purple_300),
                context.getResources().getColor(R.color.red_300));
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(10f);

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(context.getResources().getColor(R.color.blue_500));

        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    public int solveIllegalFormat(String value){
        if (value.equals("-")) {
            return 1;
        }else {
            return Integer.parseInt(value);
        }
    }

}
