package com.wanli.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.swtchart.Chart;
import org.swtchart.IBarSeries;
import org.swtchart.ISeries.SeriesType;

import com.wanli.swing.frame.MainFrame;
import com.wanli.swing.service.DBService;

public class CharTableUtil extends Dialog {

	protected Object result;
	protected Shell shell;
	private String tableName;
	private DBService dbService;//��ѯ���ݿ⣬��ȡ������
	private List<String[]> records;//����ѯ�ı����ݴ���ö���
	private List<Double> listA = new ArrayList<>();//ͳ�����д𰸸�����Aѡ��ĸ���
	private List<Double> listB = new ArrayList<>();//ͳ�����д𰸸�����Bѡ��ĸ���
	private List<Double> listC = new ArrayList<>();//ͳ�����д𰸸�����Cѡ��ĸ���
	private List<Double> listD = new ArrayList<>();//ͳ�����д𰸸�����Dѡ��ĸ���
	//������ʾ����ͼ
	private double[] answerA;
	private double[] answerB;
	private double[] answerC;
	private double[] answerD;
	private int columnNum = 0;//ͳ�Ʊ���еĸ���
	private String[] questionNo;//�洢��Ŀ��ţ����ں�����ʾ
	
	public CharTableUtil(Shell parent, String tableName) {
		super(parent, SWT.NONE);
		this.tableName = tableName;
		dbService = new DBService();
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
        shell.setText("ͼ��");
        shell.setSize(800, 300);
        shell.setLayout(new FillLayout());
        createChart(shell);
	}
	
	protected Chart createChart(Composite parent) {
		StatisticalQuantities();
        // ����һ��ͼ��
        Chart chart = new Chart(parent, SWT.NONE);

        //����ͼ�����
        chart.getTitle().setText("�ɼ�ͳ��");
        // ����ͼ��ĺ��������������
        chart.getAxisSet().getXAxis(0).getTitle().setText("��");
        chart.getAxisSet().getYAxis(0).getTitle().setText("����");

        // ���ú�����������
        for (int i = 1; i <= columnNum - 1; i++) {
        	questionNo[i - 1] = "��" + i;
        }
        chart.getAxisSet().getXAxis(0).enableCategory(true);
        chart.getAxisSet().getXAxis(0).setCategorySeries(questionNo);

        // ��������ͼ
        IBarSeries barSeriesA = (IBarSeries) chart.getSeriesSet().createSeries(SeriesType.BAR, "A");
        barSeriesA.setYSeries(answerA);
        barSeriesA.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));

        IBarSeries barSeriesB = (IBarSeries) chart.getSeriesSet().createSeries(SeriesType.BAR, "B");
        barSeriesB.setYSeries(answerB);
        barSeriesB.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
        
        IBarSeries barSeriesC = (IBarSeries) chart.getSeriesSet().createSeries(SeriesType.BAR, "C");
        barSeriesC.setYSeries(answerC);
        barSeriesC.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
        
        IBarSeries barSeriesD = (IBarSeries) chart.getSeriesSet().createSeries(SeriesType.BAR, "D");
        barSeriesD.setYSeries(answerD);
        barSeriesD.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_DARK_YELLOW));
        
        // �����᷶Χ
        chart.getAxisSet().adjustRange();

        return chart;
    }
	
	/**
	 * ͳ�Ʊ�����ݣ�������������ͼ�������
	 */
	protected void StatisticalQuantities() {
		columnNum = dbService.getTableColumn(tableName);
		records = dbService.getScoreData(tableName);
		questionNo = new String[columnNum - 1];
		int A = 0;
		int B = 0;
		int C = 0;
		int D = 0;
		answerA = new double[columnNum - 1];
		answerB = new double[columnNum - 1];
		answerC = new double[columnNum - 1];
		answerD = new double[columnNum - 1];
		for (int i = 1; i < columnNum; i++) {
			for (int j = 0; j < records.size(); j++) {
				switch (records.get(j)[i]) {
				case "A":
					A++;
					break;
				case "B":
					B++;
					break;
				case "C":
					C++;
					break;
				case "D":
					D++;
					break;
				}	
			}
			listA.add((double) A);
			listB.add((double) B);
			listC.add((double) C);
			listD.add((double) D);
			A = 0;
			B = 0;
			C = 0;
			D = 0;
		}
		for (int i = 0; i < columnNum - 1; i++) {
			answerA[i] = listA.get(i);
			answerB[i] = listB.get(i);
			answerC[i] = listC.get(i);
			answerD[i] = listD.get(i);
		}
	}
	
}
