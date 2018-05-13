//package com.wanli.swing.frame.listener;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.widgets.Tree;
//import org.eclipse.swt.widgets.TreeItem;
//
////该类没有被使用
//
//public class CreateClassListener extends SelectionAdapter {
//
//	private Tree tree;
//	private String userName;
//	private int number = 1;
//	
//	public CreateClassListener(Tree tree, String userName) {
//		this.tree = tree;
//		this.userName = userName;
//	}
//	
//	public void createClass() {
//		TreeItem classroom = new TreeItem(tree, SWT.NONE);
//		classroom.setText(userName + number);
//		number++;
//	}
//	
//	@Override
//	public void widgetSelected(SelectionEvent arg0) {
//		
//		TreeItem classroom = new TreeItem(tree, SWT.NONE);
//		classroom.setText(userName + number);
//		number++;
//	}
//	
//}
package com.wanli.swing.frame.listener;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

import com.wanli.swing.frame.CreateClassShell;
import com.wanli.swing.frame.SelectClassShell;
import com.wanli.utils.StaticVariable;

/**
 * 新建教室对话框
 * @author wanli
 *
 */
public class CreateClassListener extends SelectionAdapter {

	private Composite parent;	// 存储一切与主窗口相关的信息
	
	public CreateClassListener(Composite parent) {
		this.parent = parent;
		// 执行窗口弹出
		new SelectClassShell(parent.getShell()).open();
		if (StaticVariable.classOrSepcialtyName != null && StaticVariable.classOrSepcialtyName != "") {
			new CreateClassShell(parent.getShell()).open();
		}
	}
	
	@Override
	public void widgetSelected(SelectionEvent arg0) {
	}
	
}

