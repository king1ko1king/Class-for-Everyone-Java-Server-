package com.wanli.swing.frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import com.wanli.swing.frame.listener.AskQuestionTableListener;
import com.wanli.utils.StaticVariable;

public class MessageShell extends Dialog {
	// ���ô���Ĭ�ϴ�С
	private static int SHELL_WIDTH = 400;
	private static int SHELL_HEIGHT = 400;
	protected Object result;
	protected Shell shell;
	// Ҫ��ʾ������
	private String question;
	public MessageShell(Shell shell, String question) {
		super(shell);
		this.question = question;
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
        shell.setText("����");
        shell.setSize(SHELL_WIDTH, SHELL_HEIGHT);
        // ʹ���ھ�����ʾ
        center(shell.getDisplay(), shell);
        // ����һ�����񲼾�
        GridLayout gridLayout = new GridLayout(2, false);
//        // �ò�������������ϱ߿�ı߾�Ϊ20
//        gridLayout.marginHeight = 20;
//        // �ò��������֮��Ĵ�ֱ���Ϊ20
//        gridLayout.verticalSpacing = 20;
//        // �ò��������������߿�ı߾�Ϊ40
//        gridLayout.marginLeft = 40;
//        // �ò�������������ұ߿�ı߾�Ϊ40
//        gridLayout.marginRight = 40;
        // ���ô��ڲ���
        shell.setLayout(gridLayout);
        createClass(shell);
	}
	
	protected void createClass(Composite parent) {
		StyledText text = new StyledText(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
		text.setFont(new Font(parent.getDisplay(), "Arial", 18, SWT.NONE));
		String userName = question.substring(0, question.lastIndexOf(":") + 1);
		String theQuestion = question.substring(question.lastIndexOf(":") + 1);
		text.setText(userName + "\n" + theQuestion);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		text.setLayoutData(gridData);
		Button confirm = new Button(parent, SWT.NONE);
		confirm.setText(" ���Ϊ�Ѷ� ");
		confirm.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int index = AskQuestionTableListener.index;
				StaticVariable.unanswerMap.remove(StaticVariable.askQuestions.getItem(index));					
				if (StaticVariable.unanswerMap.size() <= 0) {
					StaticVariable.askQuestion.setText("����");
				} else {
					StaticVariable.askQuestion.setText(StaticVariable.unanswerMap.size() + " ����");
				}
				TableItem item = StaticVariable.askQuestions.getItem(AskQuestionTableListener.index);
				item.setImage(new Image(StaticVariable.parent.getDisplay(), "image/answered.png"));
				shell.dispose();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
			}
		});
//		// ���ô���Ĭ�ϰ󶨵İ�ťΪconfirm��ť
//		shell.setDefaultButton(confirm);
		Button cancle = new Button(parent, SWT.NONE);
		cancle.setText(" ȡ          �� ");
//		// ����ȡ����ťˮƽ�Ҷ���
//		GridData cancleGrid = new GridData(GridData.HORIZONTAL_ALIGN_END);
//		cancle.setLayoutData(cancleGrid);
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