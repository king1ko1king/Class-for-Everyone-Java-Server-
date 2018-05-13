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
 * 图表按钮监听器
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
		// StaticVariable.correct.size() > 1表示学生有回答问题，且问题的答案有多个
		if (StaticVariable.correct.size() > 1) {
			new MultipleAnswersChartTableUtil(parent.getShell()).open();
		} else if (StaticVariable.correct.size() == 1) {
			// 答案只有一个
			new SingleAnswerChartTableUtil(parent.getShell(), StaticVariable.tableName.toString()).open();			
		} else {
			// 还没有学生回答问题，无法查看统计图
			MessageBox messageBox = new MessageBox(parent.getShell(), SWT.YES);
			messageBox.setText("警告");
			messageBox.setMessage("还没有同学回答问题，无法查看统计图！！！");
			messageBox.open();
		}
			
	}
}

