package com.wanli.swing.frame.listener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TableItem;

import com.wanli.swing.frame.updateQuestion.listener.QuestionUpdateAddListener;
import com.wanli.utils.StaticVariable;
import com.wanli.utils.XmlToJavaBean;

public class OpenFileDialogBtnListener implements SelectionListener {

	private static TableItem[] items;								// 显示所有的题目
	private static int DEL_AND_UPDATE_BTNWIDTH = 90;				// 删除与修改按钮显示的宽度
	private static int ADDQUESTION_BTNWIDTH = 180;					// 追加试题按钮显示的宽度
	private static String openFile;									// 打开的文件名
	private static List<Control> cons = new ArrayList<>();			// 存储所有表格中的控件
	private static List<TableEditor> editors = new ArrayList<>();	// 存储所有的tableEditor
	
	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		StaticVariable.allQuestionList.clear();
		StaticVariable.questionsList.clear();
		// 定义对话框，类型为打开型
		FileDialog dialog = new FileDialog(StaticVariable.parent.getShell(), SWT.OPEN);
		// 设置对话框打开的限定类型
		dialog.setFilterExtensions(new String[] { "*.xml"});
		// 打开对话框，并返回打开文件的路径
		openFile = dialog.open();
		StaticVariable.questionManagerFileName = openFile;
		if (openFile == null) {
			return;
		}
		// 打开指定的文件
		File file = new File(openFile);
		// 将xml文件转为JavaBean
		new XmlToJavaBean(file);
		if (StaticVariable.questionsList.size() > 0) {
			fillTable();			
		}
	}
	
	/**
	 * 填充表格
	 */
	public static void fillTable() {
		// 判断数据库中是否有表存在，若当前table已经有显示数据，则清空所有数据
		if (cons.size() > 0) {
			for (Control control: cons) {
				control.dispose();
			}
		}
		if (editors.size() > 0) {
			for (TableEditor editor: editors) {
				editor.dispose();
			}
		}
		StaticVariable.table.removeAll();
//		StaticVariable.table.pack();
		// 根据题目的个数初始化数组
		items = new TableItem[StaticVariable.questionSelect.getItemCount()];
		for (int i = 0; i < StaticVariable.questionSelect.getItemCount() - 1; i++) {
			// 设置第一列的text
			items[i] = new TableItem(StaticVariable.table, SWT.NONE);
			items[i].setText(0, StaticVariable.questionSelect.getItem(i + 1));
			// 分割题目字符串
//			String question = StaticVariable.questionsMap.get(Integer.toString(i + 1));
			String question = StaticVariable.questionsList.get(i);
			String[] strs = question.split("#\\^");
			// 设置第二列的text
			items[i].setText(1, strs[1]);
			// 创建两个TableEditor对象，用于放置两个按钮
			TableEditor editorDel = new TableEditor(StaticVariable.table);
			TableEditor editorUp = new TableEditor(StaticVariable.table);
			editors.add(editorDel);
			editors.add(editorUp);
			// 设置两个按钮的对齐方式
			editorDel.horizontalAlignment = SWT.LEFT;
			editorUp.horizontalAlignment = SWT.RIGHT;
			// 设置按钮显示的宽度
			editorDel.minimumWidth = DEL_AND_UPDATE_BTNWIDTH;
			editorUp.minimumWidth = DEL_AND_UPDATE_BTNWIDTH;
			// 定义两个按钮控件
			Button buttonDel = new Button(StaticVariable.table, SWT.PUSH);
			Button buttonUp = new Button(StaticVariable.table, SWT.PUSH);
			buttonDel.setText("删除");
			buttonUp.setText("修改");
			cons.add(buttonDel);
			cons.add(buttonUp);
			// 设置按钮的高度，默认与行的高度一致
			buttonDel.computeSize(SWT.DEFAULT, StaticVariable.table.getItemHeight());
			buttonUp.computeSize(SWT.DEFAULT, StaticVariable.table.getItemHeight());
			// 为按钮添加监听器
			buttonDel.addSelectionListener(new DeleteQuestionListener(i, openFile));
			buttonUp.addSelectionListener(new UpdateQuestionListener(i, openFile));
			// 添加按钮控件
			editorDel.setEditor(buttonDel, items[i], 2);
			editorUp.setEditor(buttonUp, items[i], 2);
			if (i == StaticVariable.questionSelect.getItemCount() - 2) {
				items[i + 1] = new TableItem(StaticVariable.table, SWT.NONE);
				TableEditor editorAdd = new TableEditor(StaticVariable.table);
				editors.add(editorAdd);
				editorAdd.horizontalAlignment = SWT.CENTER;
				editorAdd.minimumWidth = ADDQUESTION_BTNWIDTH;
				// 定义一个追加试题按钮
				Button buttonAdd = new Button(StaticVariable.table, SWT.PUSH);
				buttonAdd.setText("追加试题");
				cons.add(buttonAdd);
				// 设置按钮的高度，默认与行的高度一致
				buttonAdd.computeSize(SWT.DEFAULT, StaticVariable.table.getItemHeight());
				// 为按钮添加监听器
				buttonAdd.addSelectionListener(new QuestionUpdateAddListener());
				// 添加按钮控件
				editorAdd.setEditor(buttonAdd, items[i + 1], 2);
			}
		}
	}

}
