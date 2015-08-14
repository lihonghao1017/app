package com.jin91.preciousmetal.ui.price;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.jin91.preciousmetal.R;

import cn.limc.androidcharts.view.BOLLMASlipCandleStickChart;
import cn.limc.androidcharts.view.GridChart;
import cn.limc.androidcharts.view.MACDChart;
import cn.limc.androidcharts.view.SlipLineChart;
import cn.limc.androidcharts.view.SlipStickChart;

/**
 * Created by lijinhua on 2015/5/19.
 */
public class PriceDetailK {

    public static void initMASlipCandleStickChart(Context context, int size, final BOLLMASlipCandleStickChart chartBollMaSlipCandleStick, final MACDChart chartMacd) {
        chartBollMaSlipCandleStick.setAxisXColor(Color.LTGRAY);
        chartBollMaSlipCandleStick.setAxisYColor(Color.LTGRAY);
        chartBollMaSlipCandleStick.setLatitudeColor(Color.GRAY);
        chartBollMaSlipCandleStick.setLongitudeColor(Color.GRAY);
        chartBollMaSlipCandleStick.setBorderColor(Color.LTGRAY);
        chartBollMaSlipCandleStick.setLongitudeFontColor(getColor(context, R.color.chart_line_white));
        chartBollMaSlipCandleStick.setLatitudeFontColor(getColor(context, R.color.chart_line_white));
        chartBollMaSlipCandleStick.setCandleColor(getColor(context, R.color.chart_line_red), getColor(context, R.color.chart_line_green));
        // 最大纬线数
        chartBollMaSlipCandleStick.setLatitudeNum(5);
        // 最大经线数
        chartBollMaSlipCandleStick.setLongitudeNum(3);
        // 最大价格
        chartBollMaSlipCandleStick.setMaxValue(1200);
        // 最小价格
        chartBollMaSlipCandleStick.setMinValue(200);

        chartBollMaSlipCandleStick.setDisplayFrom(size * 4 / 5);

        chartBollMaSlipCandleStick.setDisplayNumber(size / 5);

        chartBollMaSlipCandleStick.setZoomBaseLine(SlipStickChart.ZOOM_BASE_LINE_CENTER);

        chartBollMaSlipCandleStick.setAxisYTitleQuadrantWidth(50);
        chartBollMaSlipCandleStick.setAxisXTitleQuadrantHeight(20);
        chartBollMaSlipCandleStick.setDisplayLongitudeTitle(true);
        chartBollMaSlipCandleStick.setDisplayLatitudeTitle(true);
        chartBollMaSlipCandleStick.setDisplayLongitude(true);
        chartBollMaSlipCandleStick.setDisplayLatitude(true);
        chartBollMaSlipCandleStick.setDashLongitude(false);
        chartBollMaSlipCandleStick.setDashLatitude(false);
        chartBollMaSlipCandleStick.setDisplayCrossXOnTouch(true);
        chartBollMaSlipCandleStick.setDisplayCrossYOnTouch(true);
        chartBollMaSlipCandleStick.setBackgroundColor(getColor(context, R.color.chart_bg_color));

        // chartBollMaSlipCandleStick.setDataQuadrantPaddingTop(5);
        // chartBollMaSlipCandleStick.setDataQuadrantPaddingBottom(5);
        // chartBollMaSlipCandleStick.setDataQuadrantPaddingLeft(5);
        // chartBollMaSlipCandleStick.setDataQuadrantPaddingRight(5);
        chartBollMaSlipCandleStick.setAxisXPosition(GridChart.AXIS_X_POSITION_BOTTOM);
        chartBollMaSlipCandleStick.setAxisYPosition(GridChart.AXIS_Y_POSITION_LEFT);
        chartBollMaSlipCandleStick.setVisibility(View.VISIBLE);
        chartBollMaSlipCandleStick.setMaxValue(Double.MIN_VALUE);
        chartBollMaSlipCandleStick.setMinValue(Double.MAX_VALUE);
        chartBollMaSlipCandleStick.setBollingerbandstyle(BOLLMASlipCandleStickChart.BOLLINGERBAND_NONE);
        // chartBollMaSlipCandleStick.addNotify(new ITouchEventResponse() {
        //
        // @Override
        // public void notifyEvent(GridChart chart) {
        // MASlipCandleStickChart maChart = (MASlipCandleStickChart) chart;
        // chartMacd.setDisplayFrom(maChart.getDisplayFrom());
        // chartMacd.setDisplayNumber(maChart.getDisplayNumber());
        // chartMacd.notifyEvent(maChart);
        // }
        // });
        // macdChart.notifyEvent(macdChart);
    }
    public static void initSlipLineChartK(final Context context, int size, final SlipLineChart chartKLine) {

        chartKLine.setLatitudeTitleColor(getColor(context, R.color.chart_line_red), getColor(context, R.color.chart_line_green));
        chartKLine.setAxisXColor(Color.LTGRAY);
        chartKLine.setAxisYColor(Color.LTGRAY);
        chartKLine.setBorderColor(Color.LTGRAY);
        chartKLine.setLongitudeFontSize(14);
        chartKLine.setLongitudeFontColor(getColor(context, R.color.chart_line_white));
        chartKLine.setLatitudeColor(Color.GRAY);
        chartKLine.setLatitudeFontColor(getColor(context, R.color.chart_line_white));
        chartKLine.setLongitudeColor(Color.GRAY);
        chartKLine.setDisplayFrom(size * 4 / 5);// 所有第几个开始
        chartKLine.setDisplayNumber(size / 5);// 一页显示数目
        chartKLine.setLatitudeNum(2);
        chartKLine.setLongitudeNum(3);
        chartKLine.setZoomBaseLine(SlipLineChart.ZOOM_BASE_LINE_CENTER);
        chartKLine.setMaxValue(Double.MIN_VALUE);
        chartKLine.setMinValue(Double.MAX_VALUE);
        // chartSlipLine.setDataQuadrantPaddingTop(5);
        // chartSlipLine.setDataQuadrantPaddingBottom(5);
        // chartSlipLine.setDataQuadrantPaddingLeft(5);
        // chartSlipLine.setDataQuadrantPaddingRight(5);
        chartKLine.setAxisYTitleQuadrantWidth(50);
        chartKLine.setAxisXTitleQuadrantHeight(-1);
        chartKLine.setDisplayLongitudeTitle(true);
        chartKLine.setDisplayLatitudeTitle(true);
        chartKLine.setDisplayLongitude(true);
        chartKLine.setDisplayLatitude(true);
        chartKLine.setDashLongitude(false);
        chartKLine.setDashLatitude(false);
        chartKLine.setDisplayCrossXOnTouch(true);
        chartKLine.setDisplayCrossYOnTouch(false);
        chartKLine.setAxisYPosition(GridChart.AXIS_Y_POSITION_LEFT);
        chartKLine.setBackgroundColor(getColor(context, R.color.chart_bg_color));
        chartKLine.setAxisXPosition(GridChart.AXIS_X_POSITION_BOTTOM);
        chartKLine.setAxisYPosition(GridChart.AXIS_Y_POSITION_LEFT);
        chartKLine.setTouchEnable(false);
        chartKLine.setVisibility(View.VISIBLE);
    }
    public static void iniMACDChart(Context context, int size, final MACDChart chartMacd) {
        chartMacd.setMaxValue(300000);
        chartMacd.setMinValue(-300000);
        chartMacd.setLatitudeNum(2);
        chartMacd.setLongitudeNum(3);
        chartMacd.setDisplayFrom(size * 4 / 5);
        chartMacd.setDisplayNumber(size / 5);
        chartMacd.setZoomBaseLine(SlipStickChart.ZOOM_BASE_LINE_CENTER);
        chartMacd.setAxisXColor(Color.LTGRAY);
        chartMacd.setAxisYColor(Color.LTGRAY);
        chartMacd.setLatitudeColor(Color.GRAY);
        chartMacd.setLongitudeColor(Color.GRAY);
        chartMacd.setBorderColor(Color.LTGRAY);
        chartMacd.setLongitudeFontColor(getColor(context, R.color.chart_line_white));
        chartMacd.setLatitudeFontColor(getColor(context, R.color.chart_line_white));
        chartMacd.setMacdDisplayType(MACDChart.MACD_DISPLAY_TYPE_STICK);
        chartMacd.setPositiveStickColor(getColor(context, R.color.chart_line_red));
        chartMacd.setNegativeStickColor(getColor(context, R.color.chart_line_green));
        chartMacd.setMacdLineColor(getColor(context, R.color.chart_line_green));
        chartMacd.setDeaLineColor(getColor(context, R.color.chart_line_yellow));
        chartMacd.setDiffLineColor(getColor(context, R.color.chart_line_white));
        // chartMacd.setDataQuadrantPaddingTop(5);
        // chartMacd.setDataQuadrantPaddingBottom(5);
        // chartMacd.setDataQuadrantPaddingLeft(5);
        // chartMacd.setDataQuadrantPaddingRight(5);
        chartMacd.setAxisXPosition(GridChart.AXIS_X_POSITION_BOTTOM);
        chartMacd.setAxisYPosition(GridChart.AXIS_Y_POSITION_LEFT);
        chartMacd.setAxisYTitleQuadrantWidth(50);
        chartMacd.setAxisXTitleQuadrantHeight(20);
        chartMacd.setDisplayLongitudeTitle(true);
        chartMacd.setDisplayLatitudeTitle(true);
        chartMacd.setDisplayLongitude(true);
        chartMacd.setDisplayLatitude(true);
        chartMacd.setDashLongitude(false);
        chartMacd.setDashLatitude(false);
        chartMacd.setDisplayCrossXOnTouch(true);
        chartMacd.setDisplayCrossYOnTouch(false);
        chartMacd.setBackgroundColor(getColor(context, R.color.chart_bg_color));
        chartMacd.setCandleColor(getColor(context, R.color.chart_line_red), getColor(context, R.color.chart_line_green), getColor(context, R.color.chart_line_red), getColor(context, R.color.chart_line_purple), getColor(context, R.color.chart_line_yellow));
        chartMacd.setMaxValue(Double.MIN_VALUE);
        chartMacd.setMinValue(Double.MAX_VALUE);
        chartMacd.setTouchEnable(false);
        chartMacd.setVisibility(View.VISIBLE);
    }
    public static void initSlipLineChartTime(final Context context, int size, final SlipLineChart chartSlipLine_time) {

        chartSlipLine_time.setLatitudeTitleColor(getColor(context, R.color.chart_line_red), getColor(context, R.color.chart_line_green));
        chartSlipLine_time.setAxisXColor(Color.LTGRAY);
        chartSlipLine_time.setAxisYColor(Color.LTGRAY);
        chartSlipLine_time.setBorderColor(Color.LTGRAY);
        chartSlipLine_time.setLongitudeFontSize(14);
        chartSlipLine_time.setLongitudeFontColor(getColor(context, R.color.chart_line_white));
        chartSlipLine_time.setLatitudeColor(Color.GRAY);
        chartSlipLine_time.setLatitudeFontColor(getColor(context, R.color.chart_line_white));
        chartSlipLine_time.setLongitudeColor(Color.GRAY);
        chartSlipLine_time.setMaxValue(Double.MIN_VALUE);
        chartSlipLine_time.setMinValue(Double.MAX_VALUE);
        chartSlipLine_time.setDisplayFrom(0);// 所有第几个开始
        chartSlipLine_time.setDisplayNumber(size);// 一页显示数目
        chartSlipLine_time.setLatitudeNum(5);
        chartSlipLine_time.setLongitudeNum(4);
        chartSlipLine_time.setZoomBaseLine(SlipLineChart.ZOOM_BASE_LINE_LEFT);
        // chartSlipLine.setDataQuadrantPaddingTop(5);
        // chartSlipLine.setDataQuadrantPaddingBottom(5);
        // chartSlipLine.setDataQuadrantPaddingLeft(5);
        // chartSlipLine.setDataQuadrantPaddingRight(5);
        chartSlipLine_time.setAxisYTitleQuadrantWidth(50);
        chartSlipLine_time.setAxisXTitleQuadrantHeight(20);
        chartSlipLine_time.setDisplayLongitudeTitle(true);
        chartSlipLine_time.setDisplayLatitudeTitle(true);
        chartSlipLine_time.setDisplayLongitude(true);
        chartSlipLine_time.setDisplayLatitude(true);
        chartSlipLine_time.setDashLongitude(false);
        chartSlipLine_time.setDashLatitude(false);
        chartSlipLine_time.setDisplayCrossXOnTouch(true);
        chartSlipLine_time.setDisplayCrossYOnTouch(false);
        chartSlipLine_time.setAxisYPosition(GridChart.AXIS_Y_POSITION_LEFT);
        chartSlipLine_time.setBackgroundColor(getColor(context, R.color.chart_bg_color));
        chartSlipLine_time.setAxisXPosition(GridChart.AXIS_X_POSITION_BOTTOM);
        chartSlipLine_time.setAxisYPosition(GridChart.AXIS_Y_POSITION_ALL);
        chartSlipLine_time.setVisibility(View.VISIBLE);
    }
    public static int getColor(Context context, int color) {
        return context.getResources().getColor(color);
    }
}
