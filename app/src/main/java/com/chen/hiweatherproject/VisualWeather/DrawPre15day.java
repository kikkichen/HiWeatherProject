package com.chen.hiweatherproject.VisualWeather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.chen.hiweatherproject.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

public class DrawPre15day {

    String[] yHighs, yLows, xLabels;
    Context context;
    LineChart lineChart;

    // 构造画图
    public DrawPre15day(Context context, LineChart lineChart, String[] yHighData, String[] yLowData, String[] labels) {
        this.context = context;
        this.lineChart = lineChart;
        this.yHighs = yHighData;
        this.yLows = yLowData;
        this.xLabels = labels;
        initLineChart();
    }

    // 初始化表格数据
    private void initLineChart() {
        Description description = new Description();
        description.setText("");
        description.setTextSize(16);
        lineChart.setDescription(description);
        String[] xData = {"1", "2", "3", "4", "5", "6", "7"};
        String[] yData = this.yHighs;

        String[] xData2 = {"1", "2", "3", "4", "5", "6", "7"};
        String[] yDara2 = this.yLows;


        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(getDataSet(xData, yData, true));
        dataSets.add(getDataSet(xData2, yDara2, false));
        LineData data = new LineData(dataSets);

        lineChart.setData(data);
        lineChart.setAutoScaleMinMaxEnabled(false);
        lineChart.setBorderWidth(1f);//设置边框的宽度（粗细）
        lineChart.setDrawBorders(false);//显示图形的边框（边界）
        lineChart.setDragXEnabled(true);//在放大的情况下，能否拖动x轴
        lineChart.setDragYEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleXEnabled(false);
        lineChart.setScaleYEnabled(false);
        lineChart.setTouchEnabled(true);//设置为false的话，界面就像是一个图片
        lineChart.setBackgroundColor(context.getResources().getColor(R.color.white));
        lineChart.getLegend().setEnabled(false);    // 不绘制线示例

        // 图标放大，且设置可以滚动
        Matrix m = new Matrix();
        m.postScale(1.8f, 1f);
        lineChart.getViewPortHandler().refresh(m, lineChart, false);

        lineChart.setDrawMarkers(true);//设置是否显示

        lineChart.setMarker(new IMarker() {//设置imarker可以设置点击数据的时候出现的图形。
            @Override
            public MPPointF getOffset() {
                return null;
            }

            @Override
            public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
                return null;
            }

            @Override
            public void refreshContent(Entry e, Highlight highlight) {

            }

            @Override
            public void draw(Canvas canvas, float posX, float posY) {
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(Color.GREEN);
                paint.setTextSize(22f);
                canvas.drawText("here", posX, posY, paint);
            }
        });
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setDrawLabels(true);  // 隐藏/显示x轴Label
        lineChart.getXAxis().removeAllLimitLines();
        lineChart.getXAxis().setEnabled(false);

        MyXFormatter formatter = new MyXFormatter(xLabels);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        lineChart.getXAxis().setLabelCount(7);
        lineChart.getXAxis().setValueFormatter(formatter);

//        lineChart.animateX(500);
    }

    // 设置数据集
    private LineDataSet getDataSet(String[] xData, String[] yData, boolean high_sign) {
        // 节点
        ArrayList<Entry> nodeData = new ArrayList<>();
        for (int i=0; i<xData.length; i++) {
            nodeData.add(new Entry(Float.parseFloat(xData[i]), Float.parseFloat(yData[i])));
        }

        LineDataSet lineDataSet = new LineDataSet(nodeData, "Score for first five time");
        lineDataSet.setDrawFilled(true);    // 折线图下是否有阴影填充

        lineDataSet.setDrawCircles(false);       // 数据是否用圆圈表示
        lineDataSet.setCircleColor(context.getResources().getColor(R.color.black));     // 现实数据圆的颜色
        lineDataSet.setCircleRadius(4f);        // 显示数据圆的半径
        lineDataSet.setCircleColors(context.getResources().getColor(R.color.black),
                Color.GRAY,
                context.getResources().getColor(R.color.blue_500),
                context.getResources().getColor(R.color.green_500),
                context.getResources().getColor(R.color.red_500));
        lineDataSet.setDrawCircleHole(false);//数据是否用环形圆圈（同心圆）显示
        lineDataSet.setCircleHoleColor(context.getResources().getColor(R.color.yellow_500));//同心圆内圆的颜色，即圆孔的颜色
        lineDataSet.setCircleHoleRadius(2f);//内圆的半径
        lineDataSet.setColor(context.getResources().getColor(R.color.blue_500));//折线的颜色，以及label前面图形的颜色
        lineDataSet.setValueTextSize(14f);//数据的字体大小
        lineDataSet.setValueTextColor(context.getResources().getColor(R.color.blue_300));
        lineDataSet.setHighlightEnabled(false);//设置是否显示十字架的凸显样式
        lineDataSet.setHighLightColor(Color.BLACK);//设置图形样式的颜色
        lineDataSet.setDrawHorizontalHighlightIndicator(false);//设置凸显样式水平图形显隐
        lineDataSet.setHighlightLineWidth(1f);//点击数据会出现十字架形状的定位显示，设置该十字架的宽度
        lineDataSet.setLineWidth(4f);//设置折线的宽度
        lineDataSet.setFormSize(10f);//label前面的图形的大小
        lineDataSet.setForm(Legend.LegendForm.CIRCLE);//设置图例的图形的形状
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);//设置折线的形状：直线、梯形、贝塞尔曲线

        if (high_sign) {    // 高数值线使用阴影
            // 设置渐变色填充
            Drawable gradient_drawable = ContextCompat.getDrawable(context, R.drawable.gradient_blue_to_white);
            lineDataSet.setFillDrawable(gradient_drawable);     // 阴影填充颜色
        }else {
            lineDataSet.setFillColor(Color.WHITE);
        }


        return lineDataSet;
    }

    public class MyXFormatter extends ValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public MyXFormatter(String[] values) {
            this.mValues = values;
        }
        private static final String TAG = "MyXFormatter";

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            Log.d(TAG, "----->getFormattedValue: "+value);
            return mValues[(int) value % mValues.length];
        }
    }
}
