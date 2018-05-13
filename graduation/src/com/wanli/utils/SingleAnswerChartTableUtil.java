package com.wanli.utils;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.swtchart.Chart;
import org.swtchart.IBarSeries;
import org.swtchart.ISeries;
import org.swtchart.ISeries.SeriesType;

/**
 * 只有单个答案时显示的图表
 * @author wanli
 *
 */
public class SingleAnswerChartTableUtil extends Dialog {

	protected Object result;
	protected Shell shell;
	private static double[] ySeries = new double[3];									// 这里new double[3]是因为横轴只有正确、错误、未回答三个
	private static double[] optionYSeries = new double[StaticVariable.options.size()];	// StaticVariable.options.size()表示有多少个选项
	private String questionType;
	private String answer;

	private static final String[] cagetorySeries = { "正确", "错误", "未回答" };
	private String[] optionXSeries = new String[StaticVariable.options.size()];
	
	public SingleAnswerChartTableUtil(Shell parent, String tableName) {
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
		// 定义选项卡
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		GridData gridTab = new GridData(GridData.FILL_BOTH);
		tabFolder.setLayoutData(gridTab);
		// 定义第一个选项卡
		final TabItem tatistics = new TabItem(tabFolder, SWT.NONE);
		tatistics.setText("正确率统计");
		{
			answerStatistics(tabFolder, tatistics);
		}
		// 定义第二个选项卡
		if (!questionType.equals("true_or_false")) {
			final TabItem details = new TabItem(tabFolder, SWT.NONE);
			details.setText("详情");
			{
				answerStatisticsDetails(tabFolder, details);
			}			
		}
		// 为选项卡添加监听器
		tabFolder.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (tabFolder.getSelectionIndex() == 1) {
					answerStatistics(tabFolder, tatistics);
				} else if (tabFolder.getSelectionIndex() == 2) {
					
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		return null;
    }
	
	// 答案统计
	private void answerStatistics(TabFolder tabFolder, TabItem tatistics) {
		//为该选项卡定义一个面板
		Composite questionComp = new Composite(tabFolder, SWT.BORDER);
		tatistics.setControl(questionComp);
		//设置该面板为网格式布局
		questionComp.setLayout(new FillLayout());
		// 创建一张图表
		Chart chart = new Chart(questionComp, SWT.NONE);
		chart.getTitle().setText("成绩表");
		GridData gridTab = new GridData(GridData.FILL_BOTH);
		chart.setLayoutData(gridTab);
		// 设置y值
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0).setCategorySeries(cagetorySeries);
		chart.getAxisSet().getXAxis(0).getTick().setTickLabelAngle(45);
		
		// 添加柱形图
		ISeries barSeries = chart.getSeriesSet().createSeries(SeriesType.BAR,
				"答案：" + answer);
		barSeries.setYSeries(ySeries);
		chart.getAxisSet().adjustRange();
	}
	
	// 答案统计详情
	private void answerStatisticsDetails(TabFolder tabFolder, TabItem details) {
		//为该选项卡定义一个面板
		Composite questionComp = new Composite(tabFolder, SWT.BORDER);
		details.setControl(questionComp);
		//设置该面板为网格式布局
		questionComp.setLayout(new FillLayout());
		// 创建一张图表
		Chart chart = new Chart(questionComp, SWT.NONE);
		chart.getTitle().setText("成绩表");
		GridData gridTab = new GridData(GridData.FILL_BOTH);
		chart.setLayoutData(gridTab);
		// 设置y值
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0).setCategorySeries(optionXSeries);
		chart.getAxisSet().getXAxis(0).getTick().setTickLabelAngle(45);
		
		// 添加柱形图
		IBarSeries barSeries = null;
		barSeries = (IBarSeries) chart.getSeriesSet().createSeries(
				SeriesType.BAR, "选项个数");
		
		barSeries.setYSeries(optionYSeries);
		chart.getAxisSet().adjustRange();
	}
	
	// 初始化
	protected void init() {
		if (StaticVariable.unResponse.size() > 0) {
			// 计算正确答案，错误答案，未回答人数的总数
			int unResponse = StaticVariable.unResponse.get(0).intValue();
			int correct = StaticVariable.correct.get(0).intValue();
			int error = StaticVariable.error.get(0).intValue();
			unResponse = StaticVariable.users.size() - correct - error;
//			System.out.println("users:" + StaticVariable.users.size());
			StaticVariable.unResponse.set(0, new Integer(unResponse));
//			System.out.println("correct" + correct);
//			System.out.println("error" + error);
//			System.out.println("unResponse" + unResponse);
			// 初始化每个柱形图的数据
			ySeries[0] = StaticVariable.correct.get(0).intValue();
			ySeries[1] = StaticVariable.error.get(0).intValue();
			ySeries[2] = StaticVariable.unResponse.get(0).intValue();
			// 将答案保存到answer
			int index = StaticVariable.questionSelect.getSelectionIndex();
//			String question = StaticVariable.questionsMap.get(Integer.toString(index));
			String question = StaticVariable.questionsList.get(index - 1);
			String[] strs = question.split("#\\^");
			questionType = strs[0];
			answer = strs[2];
			// 统计每个选项的个数
			int i = 1;
			for (Map.Entry<String, Integer> option: StaticVariable.options.entrySet()) {
				optionYSeries[i - 1] = option.getValue();
				optionXSeries[i - 1] = option.getKey();
				i++;
			}
//			System.out.println(StaticVariable.options);
		}
	}
	
}
