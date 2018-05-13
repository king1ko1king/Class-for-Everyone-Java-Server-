package com.wanli.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.swtchart.Chart;
import org.swtchart.IBarSeries;
import org.swtchart.ISeries.SeriesType;

/**
 * 多个答案时要显示的图表统计数据
 * @author wanli
 *
 */
public class MultipleAnswersChartTableUtil extends Dialog {

	protected Object result;
	protected Shell shell;
	private List<double[]> ySeries = new ArrayList<>();	// 存储所有回答的统计情况
	private String[] cagetorySeries;					// 存储所有答案，用于显示
	
	public MultipleAnswersChartTableUtil(Shell parent) {
		super(parent, SWT.NONE);
		init();
	}
	
	public Object open() {
		createContents(); 
		shell.open();  
        shell.layout();  
        Display display = getParent().getDisplay();  
        while (!shell.isDisposed()) {  
            if (!display.readAndDispatch())  
                display.sleep();  
        }  
        return result;
	}
	
	protected void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);
        shell.setText("图表");
        shell.setImage(SWTResourceManager.getImage("image/quesCount.png"));
        shell.setSize(800, 300);
        shell.setLayout(new FillLayout());
        createChart(shell);
	}

	protected Chart createChart(Composite parent) {
		// create a chart
        Chart chart = new Chart(parent, SWT.NONE);

        // set titles
        chart.getTitle().setText("成绩表");
        chart.getAxisSet().getXAxis(0).getTitle().setText("Y axis");
        chart.getAxisSet().getYAxis(0).getTitle().setText("X axis");

        // set category
        chart.getAxisSet().getXAxis(0).enableCategory(true);
        chart.getAxisSet().getXAxis(0).setCategorySeries(
                new String[] { "正确", "错误", "未回答"});

        for (int i = 0; i < cagetorySeries.length; i++) {
        	IBarSeries barSeries = (IBarSeries) chart.getSeriesSet().createSeries(
                    SeriesType.BAR, cagetorySeries[i]);
        	
        	barSeries.setYSeries(ySeries.get(i));
        	barSeries.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_GREEN - i));
        }
//        // create bar series
//        IBarSeries barSeries1 = (IBarSeries) chart.getSeriesSet().createSeries(
//                SeriesType.BAR, "bar series 1");
//        barSeries1.setBarColor(Display.getDefault().getSystemColor(
//                SWT.COLOR_GREEN));
//
//        IBarSeries barSeries2 = (IBarSeries) chart.getSeriesSet().createSeries(
//                SeriesType.BAR, "bar series 2");
//        barSeries2.setYSeries(ySeries2);

        // adjust the axis range
        chart.getAxisSet().adjustRange();
		return chart;
	}
	
	protected void init() {
		cagetorySeries = new String[StaticVariable.correct.size()];
		List<List<Integer>> allList = new ArrayList<>();// 将所有统计的数量存储到一个list中，用于后面遍历
		// 根据答案的个数初始化ySeries
		for (int i = 0; i < StaticVariable.correct.size(); i++) {
			// 这里new double[3]是因为横轴只有正确、错误、未回答三个
			ySeries.add(new double[3]);
		}
		// 根据正确的数量和错误的数量计算出未回答的数量
		for (int i = 0; i < StaticVariable.correct.size(); i++) {
			int unResponse = StaticVariable.unResponse.get(i).intValue();
			int correct = StaticVariable.correct.get(i).intValue();
			int error = StaticVariable.error.get(i).intValue();
			unResponse = StaticVariable.users.size() - correct - error;
			StaticVariable.unResponse.set(i, new Integer(unResponse));
		}
		allList.add(StaticVariable.correct);
		allList.add(StaticVariable.error);
		allList.add(StaticVariable.unResponse);
//		System.out.println("StaticVariable.unResponse:" + StaticVariable.unResponse);
		System.out.println(allList);
		// 填充ySeries
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < ySeries.size(); j++) {
				ySeries.get(j)[i] = allList.get(i).get(j);									
			}
		}
		// 将所有答案填入cagetorySeries数组
		for (int i = 0; i < cagetorySeries.length; i++) {
			int index = StaticVariable.questionSelect.getSelectionIndex();
//			String question = StaticVariable.questionsMap.get(Integer.toString(index));
			String question = StaticVariable.questionsList.get(index - 1);
			String[] strs = question.split("#\\^");
			cagetorySeries[i] = strs[i + 2];
		}
	}
}
