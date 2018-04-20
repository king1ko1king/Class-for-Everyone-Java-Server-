package com.wanli.swing.frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.wanli.utils.StaticVariable;

public class CreateClassShell extends Dialog {
	protected Object result;
	protected Shell shell;
	public CreateClassShell(Shell shell) {
		super(shell);	
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
		// ����һ������
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText("��������");
        shell.setSize(300, 150);
        // ʹ���ھ�����ʾ
        center(shell.getDisplay(), shell);
        // ����һ�����񲼾�
        GridLayout gridLayout = new GridLayout(2, false);
        // �ò�������������ϱ߿�ı߾�Ϊ20
        gridLayout.marginHeight = 20;
        // �ò��������֮��Ĵ�ֱ���Ϊ20
        gridLayout.verticalSpacing = 20;
        // �ò��������������߿�ı߾�Ϊ40
        gridLayout.marginLeft = 40;
        // �ò�������������ұ߿�ı߾�Ϊ40
        gridLayout.marginRight = 40;
        // ���ô��ڲ���
        shell.setLayout(gridLayout);
        createClass(shell);
	}
	
	protected void createClass(Composite parent) {
		Label className = new Label(parent, SWT.NONE);
		className.setText("�������ƣ�");
		Text inputName = new Text(parent, SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		inputName.setLayoutData(gridData);
		Button confirm = new Button(parent, SWT.NONE);
		confirm.setText(" ȷ      �� ");
		confirm.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// û������������򵯳���ʾ��
				if (inputName.getText().trim() == "") {
					MessageBox messageBox = new MessageBox(parent.getShell());
					messageBox.setMessage("�������������");
					messageBox.open();
				} else {
					StaticVariable.className = inputName.getText();
					shell.dispose();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
			}
		});
		// ���ô���Ĭ�ϰ󶨵İ�ťΪconfirm��ť
		shell.setDefaultButton(confirm);
		inputName.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent keyEvent) {
			}
			
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				// ���س����ɴ���ȷ�ϰ�ť�¼�
				if (keyEvent.keyCode == SWT.CR) {
					shell.setDefaultButton(confirm);
				}
			}
		});
		Button cancle = new Button(parent, SWT.NONE);
		cancle.setText(" ȡ     �� ");
		// ����ȡ����ťˮƽ�Ҷ���
		GridData cancleGrid = new GridData(GridData.HORIZONTAL_ALIGN_END);
		cancle.setLayoutData(cancleGrid);
		cancle.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				StaticVariable.className = "";
				shell.dispose();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
	}
	
	/**
	 * ʹ�Ի��������ʾ
	 * @param display
	 * @param shell
	 */
	protected void center(Display display, Shell shell) {
		Rectangle bounds = display.getPrimaryMonitor().getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
	}
	
}
