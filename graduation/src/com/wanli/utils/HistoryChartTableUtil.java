package com.wanli.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
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
 * 历史表的弹出图表窗口
 * @author wanli
 *
 */
public class HistoryChartTableUtil extends Dialog {
	protected Object result;
	protected Shell shell;
//	private static double[] ySeries = new double[3];
//	private String answer;
	private List<double[]> ySeries = new ArrayList<>();	// 存储所有回答的统计情况
	private String[] cagetorySeries;					// 存储所有答案，用于显示
	private String questionType;

//	private static final String[] cagetorySeries = { "正确", "错误",
//	            "未回答" };
	
	public HistoryChartTableUtil(Shell parent, int index, String questionType) {
		super(parent, SWT.NONE);
		if (StaticVariable.statisticalData.size() > 0) {
			init(StaticVariable.statisticalData.get(index));			
		}
		this.questionType = questionType;
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

        if (cagetorySeries != null) {
        	for (int i = 0; i < cagetorySeries.length; i++) {
        		IBarSeries barSeries = (IBarSeries) chart.getSeriesSet().createSeries(
        				SeriesType.BAR, "答案：" + cagetorySeries[i]);
        		
        		barSeries.setYSeries(ySeries.get(i));
        		barSeries.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_GREEN - i));
        	}        	
        }

        // adjust the axis range
        chart.getAxisSet().adjustRange();
		return chart;
	}
	
	protected void init(String str) {
		String[] strs;
//		System.out.println(str);
		// 字符串存储的格式：{答案1 答案2 答案3...,正确1  正确2 正确3...,错误1 错误2 错误3...,未回答1 未回答2 未回答3...}
		// 将传过来的字符串使用“,”号分割
		if (str != null && str != "") {
			strs = str.split(",");			
		} else {
			return;
		}
		// 判断是否是多个答案
		if (strs[0].split(" ").length > 1) {
			// 根据答案的多少初始化数组
			cagetorySeries = new String[strs[0].split(" ").length];
			// 使用list存储所有正确，错误，未回答的字符串
			// list.add({正确1 正确2 正确3...})
			// list.add({错误1 错误2 错误3...})
			// list.add({未回答1 未回答2 未回答3...})
			List<String> allList = new ArrayList<>();
			for (int i = 1; i < strs.length; i++) {
				allList.add(strs[i]);
			}
			// 根据答案的个数初始化ySeries
			for (int i = 0; i < strs[0].split(" ").length; i++) {
				ySeries.add(new double[strs[0].split(" ").length]);
			}
			// 填充ySeries
			for (int i = 0; i < strs[0].split(" ").length; i++) {
				String[] answers = allList.get(i).split(" ");
				for (int j = 0; j < answers.length; j++) {
					ySeries.get(j)[i] = Double.parseDouble(answers[j]);									
				}
			}
			// 将所有答案填入cagetorySeries数组
			String[] answers = strs[0].split(" ");
			for (int i = 0; i < cagetorySeries.length; i++) {
				cagetorySeries[i] = answers[i];
			}

		} else {
			cagetorySeries = new String[1];
			cagetorySeries[0] = strs[0];
			ySeries.add(new double[3]);
			for (int i = 0; i < ySeries.get(0).length; i++) {
				ySeries.get(0)[i] = Double.parseDouble(strs[i + 1]);
			}
		}
//		int unResponse = StaticVariable.unResponse.get(0).intValue();
//		int correct = StaticVariable.correct.get(0).intValue();
//		int error = StaticVariable.error.get(0).intValue();
//		unResponse = StaticVariable.users.size() - correct - error;
//		StaticVariable.unResponse.set(0, new Integer(unResponse));
//		ySeries[0] = StaticVariable.correct.get(0).intValue();
//		ySeries[1] = StaticVariable.error.get(0).intValue();
//		ySeries[2] = StaticVariable.unResponse.get(0).intValue();
//		// 将答案保存到answer
//		int index = StaticVariable.questionSelect.getSelectionIndex();
//		String question = StaticVariable.questionsMap.get(Integer.toString(index));
//		String[] strs = question.split(",");
//		answer = strs[3];
	}
}
