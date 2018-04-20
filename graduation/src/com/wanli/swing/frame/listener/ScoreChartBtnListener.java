package com.wanli.swing.frame.listener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;

import com.wanli.utils.MultipleAnswersChartTableUtil;
import com.wanli.utils.SingleAnswerChartTableUtil;
import com.wanli.utils.StaticVariable;

/**
 * ͼ��ť������
 * @author wanli
 *
 */
public class ScoreChartBtnListener implements SelectionListener {
	private Composite parent;
	public ScoreChartBtnListener(Composite parent) {
		this.parent = parent;
	}
	
	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		// StaticVariable.correct.size() > 1��ʾѧ���лش����⣬������Ĵ��ж��
		if (StaticVariable.correct.size() > 1) {
			new MultipleAnswersChartTableUtil(parent.getShell()).open();
		} else if (StaticVariable.correct.size() == 1) {
			// ��ֻ��һ��
			new SingleAnswerChartTableUtil(parent.getShell(), StaticVariable.tableName).open();			
		} else {
			// ��û��ѧ���ش����⣬�޷��鿴ͳ��ͼ
			MessageBox messageBox = new MessageBox(parent.getShell(), SWT.YES);
			messageBox.setText("����");
			messageBox.setMessage("��û��ͬѧ�ش����⣬�޷��鿴ͳ��ͼ������");
			messageBox.open();
		}
			
	}
}

