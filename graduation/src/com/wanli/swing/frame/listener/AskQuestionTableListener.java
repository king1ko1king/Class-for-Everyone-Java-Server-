package com.wanli.swing.frame.listener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableItem;

import com.wanli.swing.frame.MessageShell;
import com.wanli.utils.StaticVariable;

public class AskQuestionTableListener extends MouseAdapter {

	private Composite parent;		// �洢һ������������ص���Ϣ
	private String question = "";	// �洢ѧ�����������
	public static int index;		// ��Ǳ��ѡ�е���
	private Menu menu = null;		// �Ҽ��˵�
	
	public AskQuestionTableListener(Composite parent) {
		this.parent = parent;
	}
	
	@Override
	public void mouseDown(MouseEvent event) {
		mouseRightDown(event);
	}
	
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		index = StaticVariable.askQuestions.getSelectionIndex();
		question = StaticVariable.askQuestions.getItem(index).getText();
		// ִ�д��ڵ���
		new MessageShell(parent.getShell(), question).open();		
	}
	
	private void mouseRightDown(MouseEvent event) {
		// ����Ҽ��˵����������հ״�Ҳ�ᵯ���Ҽ��˵�
		if (menu != null) {
			menu.dispose();
		}
		// ��ȡѡ�еĿؼ�
		TableItem selected = StaticVariable.askQuestions.getItem(new Point(event.x, event.y));
		// ���ȡ���У���������Ҽ����
		if (selected != null && event.button == 3) {
			// ����Ҽ��˵�
			menu = new Menu(StaticVariable.askQuestions);
			MenuItem delItem = new MenuItem(menu, SWT.PUSH);
			delItem.setText("ɾ��");
			delItem.addSelectionListener(new SelectionAdapter() {
				@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		MessageBox messageBox = new MessageBox(parent.getShell(), SWT.YES | SWT.NO);
	        		messageBox.setText("ɾ������");
	        		messageBox.setMessage("ȷ��Ҫɾ�����������");
	        		if (messageBox.open() == SWT.YES) {
	        			StaticVariable.unanswerMap.remove(selected);
	        			if (StaticVariable.unanswerMap.size() <= 0) {
	        				StaticVariable.askQuestion.setText("����");
	        			} else {
	        				StaticVariable.askQuestion.setText(StaticVariable.unanswerMap.size() + " ����");	        				
	        			}
		        		selected.dispose();
	        		}
	        	}
			});
			MenuItem readItem = new MenuItem(menu, SWT.PUSH);
			readItem.setText("���Ϊ�Ѷ�");
			readItem.addSelectionListener(new SelectionAdapter() {
				@Override
	        	public void widgetSelected(SelectionEvent e) {
//					int index = AskQuestionTableListener.index;
					StaticVariable.unanswerMap.remove(StaticVariable.askQuestions.getItem(index));
        			if (StaticVariable.unanswerMap.size() <= 0) {
        				StaticVariable.askQuestion.setText("����");
        			} else {
        				StaticVariable.askQuestion.setText(StaticVariable.unanswerMap.size() + " ����");	        				
        			}
        			TableItem item = StaticVariable.askQuestions.getItem(index);
    				item.setImage(new Image(StaticVariable.parent.getDisplay(), "image/answered.png"));
	        	}
			});
			StaticVariable.askQuestions.setMenu(menu);
		}
	}

}
