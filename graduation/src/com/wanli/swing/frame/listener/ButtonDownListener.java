package com.wanli.swing.frame.listener;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.wanli.swing.service.DBService;
import com.wanli.utils.StaticVariable;

public class ButtonDownListener extends SelectionAdapter {

	private String btnName;
	private DBService dbService;
	private List<String[]> records;
	private int columnNum = 0;
	
	public ButtonDownListener(String btnName) {
		this.btnName = btnName;
		dbService = new DBService();
	}
	
	@Override
    public void widgetSelected(final SelectionEvent e) { //匿名类
        selected(e);
    }

	protected void selected(SelectionEvent e) {
		switch (btnName) {
		case "first":
			StaticVariable.index = 1;
			StaticVariable.text.setText(StaticVariable.questions[StaticVariable.index]);
			break;
		case "previous":
			if (StaticVariable.index > 1) {
				StaticVariable.index -= 1;				
			}
			StaticVariable.text.setText(StaticVariable.questions[StaticVariable.index]);
			break;
		case "next":
			if (StaticVariable.index < StaticVariable.questions.length - 1) {
				StaticVariable.index += 1;				
			}
			StaticVariable.text.setText(StaticVariable.questions[StaticVariable.index]);
			break;
		case "last":
			StaticVariable.index = StaticVariable.questions.length - 1;
			StaticVariable.text.setText(StaticVariable.questions[StaticVariable.index]);
			break;
		case "refresh":
			if (StaticVariable.scoreTab.getColumnCount() != 0) {
				int deleteColumn = StaticVariable.scoreTab.getColumnCount();
				int deleteRow = StaticVariable.scoreTab.getItemCount();
				for (int i = 0; i < deleteRow; i++) {
					StaticVariable.scoreTab.remove(0);
				}
				for (int i = 0; i < deleteColumn; i++) {
					StaticVariable.scoreTab.getColumn(0).dispose();
				}
			}
			if (StaticVariable.tableName != null) {
				columnNum = dbService.getTableColumn(StaticVariable.tableName.toString());
				records = dbService.getScoreData(StaticVariable.tableName.toString());
				for (int i = 0; i < columnNum; i++) {
					if (i == 0) {
						TableColumn Column_name = new TableColumn(StaticVariable.scoreTab, SWT.NONE);
						Column_name.setText("用户名");
						Column_name.setWidth(100);						
					} else {
						TableColumn Column_name = new TableColumn(StaticVariable.scoreTab, SWT.NONE);
						Column_name.setText("题" + i);
						Column_name.setWidth(100);
					}
				}
				for (String[] record: records) {
					new TableItem(StaticVariable.scoreTab, SWT.NONE).setText(record);
				}
			}
			break;
		default:
			break;
		}
	}
	
}
