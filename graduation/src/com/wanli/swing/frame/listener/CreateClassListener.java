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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
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

/**
 * �½����ҶԻ���
 * @author wanli
 *
 */
public class CreateClassListener extends SelectionAdapter {

	private Composite parent;
	
	public CreateClassListener(Composite parent) {
		this.parent = parent;
		new CreateClassShell(parent.getShell()).open();
	}
	
	@Override
	public void widgetSelected(SelectionEvent arg0) {
	}
	
}

class CreateClassShell extends Dialog {
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText("Category Axis");
        shell.setSize(300, 150);
        center(shell.getDisplay(), shell);
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginHeight = 20;
        gridLayout.verticalSpacing = 20;
        gridLayout.marginLeft = 40;
        gridLayout.marginRight = 40;
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

