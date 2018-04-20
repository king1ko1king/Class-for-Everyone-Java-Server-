package com.wanli.swing.frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.wanli.swing.frame.listener.CreateXmlFileBtnListener;
import com.wanli.swing.frame.listener.QuestionTypeListener;
import com.wanli.swing.frame.questiontype.BeginningComposite;
import com.wanli.utils.StaticVariable;

/**
 * ���ζԻ���
 * @author wanli
 *
 */
public class PrepareLessons {

	private Composite parent;	// �洢һ������������ص���Ϣ
	
	public PrepareLessons(Composite parent) {
		this.parent = parent;
		// ִ�д��ڵ���
		new PrepareShell(parent.getShell()).open();
		// ÿ�γɹ�����xml�ļ����߹رմ����ļ��Ĵ��ڶ���������д洢��Ŀ��list
		StaticVariable.choiceList.clear();
		StaticVariable.trueOrFalseList.clear();
		StaticVariable.fillblanksList.clear();
	}
	
}

class PrepareShell extends Dialog {

	protected Object result;
	protected Shell shell;
//	private Composite questionCom;
	
	public PrepareShell(Shell shell) {
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
	
	/**
	 * ��������
	 */
	protected void createContents() {
		// ����һ������
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);
        shell.setText("��ǰ����");
        shell.setSize(500, 500);
        
        shell.addShellListener(new ShellAdapter() {
        	@Override
        	public void shellClosed(ShellEvent e) {
    			MessageBox messageBox = new MessageBox(shell, SWT.YES | SWT.NO);
    			messageBox.setText("����");
    			messageBox.setMessage("ȷ��Ҫ�˳���");
    			if (e.doit = messageBox.open() == SWT.YES) {
    				StaticVariable.choiceList.clear();
    				StaticVariable.trueOrFalseList.clear();
    				StaticVariable.fillblanksList.clear();
    			} 
    		}
        	
		});
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
		// ѡ�����������б��
		Combo questionType = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
		questionType.add("��ѡ������");
		questionType.add("ѡ����");
		questionType.add("�Ƿ���");
		questionType.add("�����");
		questionType.select(0);
		questionType.addSelectionListener(new QuestionTypeListener(parent, questionType));

		// ����Xml�ļ���ť
		Button create = new Button(parent, SWT.NONE);
		create.setText("��    ��");
		create.addSelectionListener(new CreateXmlFileBtnListener(shell));

		// ����������ؼ��ʹ�����ť�Ĳ���
		GridData fill = new GridData(GridData.FILL_HORIZONTAL);
		questionType.setLayoutData(fill);
		create.setLayoutData(fill);
		StaticVariable.questionCom = new BeginningComposite(parent, SWT.NONE);
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
