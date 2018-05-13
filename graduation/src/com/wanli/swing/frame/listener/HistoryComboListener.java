package com.wanli.swing.frame.listener;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.wanli.swing.service.DBService;
import com.wanli.utils.HistoryChartTableUtil;
import com.wanli.utils.StaticVariable;

/**
 * 历史表下拉框的监听器
 * @author wanli
 *
 */
public class HistoryComboListener implements SelectionListener {

	private Combo historyCombo;		// 选择历史表的下拉框
	private DBService dbService;	// 操作数据库
	
	public HistoryComboListener(Combo historyCombo) {
		this.historyCombo = historyCombo;
		dbService = new DBService();
	}
	
	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		// 要查找的表名
		String tableName = historyCombo.getText();
		// 判断数据库中是否有表存在，若当前table已经有显示数据，则清空所有数据
		if (StaticVariable.historyTab.getColumnCount() != 0) {
			int deleteColumn = StaticVariable.historyTab.getColumnCount();
			int deleteRow = StaticVariable.historyTab.getItemCount();
			for (int i = 0; i < deleteRow; i++) {
				StaticVariable.historyTab.remove(0);
			}
			for (int i = 0; i < deleteColumn; i++) {
				StaticVariable.historyTab.getColumn(0).dispose();
			}
		}
		// 获取选择的表的总列数
		int columnNum = dbService.getTableColumn(tableName);
		// 获得该表的所有数据
		List<String[]> records = dbService.getScoreData(tableName);
		// 根据数据库中表中列的数量，定义table要显示的列
		for (int i = 0; i < columnNum; i++) {
			if (i == 0) {
				TableColumn Column_name = new TableColumn(StaticVariable.historyTab, SWT.NONE);
				Column_name.setText("用户名");
				Column_name.setWidth(100);						
			} else {
				TableColumn Column_name = new TableColumn(StaticVariable.historyTab, SWT.NONE);
				Column_name.setText("题" + i);
				Column_name.setWidth(100);
				// 除了首列，为其他所有的列添加点击监听器
				Column_name.addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						String column = Column_name.getText().substring(1);
						StaticVariable.statisticalData = dbService.getStatisticalData(StaticVariable.historyCombo.getText());
						String questionType = dbService.getAllQuestionType(StaticVariable.historyCombo.getText()).get(Integer.parseInt(column));
						// 弹出图表统计数据
						new HistoryChartTableUtil(StaticVariable.parent.getShell(), Integer.parseInt(column), questionType).open();
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
					}
				});
			}
		}
		for (String[] record: records) {
			// 将所有数据显示在table中
			new TableItem(StaticVariable.historyTab, SWT.NONE).setText(record);
		}
	}

}
