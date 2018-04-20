//package com.wanli.swing.frame.listener;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.widgets.Tree;
//import org.eclipse.swt.widgets.TreeItem;
//
////����û�б�ʹ��
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

/**
 * �½����ҶԻ���
 * @author wanli
 *
 */
public class CreateClassListener extends SelectionAdapter {

	private Composite parent;	// �洢һ������������ص���Ϣ
	
	public CreateClassListener(Composite parent) {
		this.parent = parent;
		// ִ�д��ڵ���
		new CreateClassShell(parent.getShell()).open();
	}
	
	@Override
	public void widgetSelected(SelectionEvent arg0) {
	}
	
}

