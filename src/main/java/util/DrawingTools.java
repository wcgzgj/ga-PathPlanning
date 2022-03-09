package util;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.util.Map;

/**
 * 后端绘图类
 *
 * @author FARO_Z
 * @date 2022-3-9
 */
public class DrawingTools extends ApplicationFrame {
    private String titleFont;
    private int titleFontSize;
    private String xyFont;
    private int xyFontSize;

    DrawingTools() {
        this("Charts");
    }

    public DrawingTools(String appTitle) {
        super(appTitle);
        this.titleFont = "微软雅黑";
        this.titleFontSize = 20;
        this.xyFont = "微软雅黑";
        this.xyFontSize = 15;
    }

    /**
     * @param appTitle    标题
     * @param chartTitle  图标题
     * @param xName       x轴命名
     * @param yName       y轴命名
     * @param dataSet     数据集
     * @param types       线条种类
     */
    public static void drawLineChart(String appTitle, String chartTitle,
                                     String xName,
                                     String yName,
                                     Map<Double, Double>[] dataSet,
                                     String[] types) {
        DrawingTools tools = new DrawingTools(appTitle);
        IntervalXYDataset dataset = tools.getLineDataset(dataSet, types);
        JFreeChart chart = tools.getLineChart(chartTitle, xName, yName, dataset);

        //绘图模式化
        tools.setChartCSS(chart);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(900, 600));
        tools.setContentPane(chartPanel);
        tools.pack();
        RefineryUtilities.centerFrameOnScreen(tools);
        tools.setVisible(true);
    }

    private JFreeChart getLineChart(String title, String xName, String yName, XYDataset dataset) {
        /**
         * 图标标题，x轴名称，y轴名称，数据集合，图标显示方向，是否使用图示，是否生成工具栏，是否生成URL链接
         */
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                xName,
                yName,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        return chart;
    }



    /**
     * 自定义设置图表字体样式
     *
     * @param chart
     */
    private void setChartCSS(JFreeChart chart) {
        //初始化
        chart.setBackgroundPaint(ChartColor.WHITE);
        XYPlot plot = chart.getXYPlot();

        //标题
        TextTitle textTitle = chart.getTitle();
        textTitle.setFont(new Font(titleFont, Font.BOLD, titleFontSize));
        LegendTitle legendTitle = chart.getLegend();
        legendTitle.setItemFont(new Font(titleFont, Font.PLAIN, titleFontSize));


        //图表xy轴字体设置
        plot.getDomainAxis().setLabelFont(new Font(xyFont, Font.PLAIN, xyFontSize));
        plot.getDomainAxis().setTickLabelFont(new Font(xyFont, Font.PLAIN, xyFontSize));
        plot.getRangeAxis().setTickLabelFont(new Font(xyFont, Font.PLAIN, xyFontSize));
        plot.getRangeAxis().setLabelFont(new Font(xyFont, Font.PLAIN, xyFontSize));

        //设置背景色-xy轴格子色
        plot.setBackgroundPaint(ChartColor.WHITE);
        plot.setRangeGridlinePaint(ChartColor.lightGray);
//        plot.setDomainGridlinePaint(ChartColor.lightGray);

        //折线图渲染
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        plot.setRenderer(renderer);
//        renderer.setPaint(ChartColor.BLACK);
        chart.getLegend().setPosition(RectangleEdge.RIGHT);

    }

    /**
     * @param dataSets int:double
     * @param types    折线的种类
     * @return
     */
    private IntervalXYDataset getLineDataset(Map<Double, Double>[] dataSets, String[] types) {

        XYSeriesCollection dataSet = new XYSeriesCollection();
        int index = 0;
        for (String type : types) {
            XYSeries series = new XYSeries(type);
            for (Map.Entry<Double, Double> data : dataSets[index++].entrySet()) {
                series.add(data.getKey(), data.getValue());
            }
            dataSet.addSeries(series);
        }
        return dataSet;
    }
}