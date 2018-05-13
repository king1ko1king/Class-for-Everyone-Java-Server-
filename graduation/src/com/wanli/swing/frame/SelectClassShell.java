package com.wanli.swing.frame;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.wanli.swing.service.DBServiceUser;
import com.wanli.utils.StaticVariable;

public class SelectClassShell extends Dialog {
	protected Object result;
	protected Shell shell;
	private DBServiceUser dbService;
	public SelectClassShell(Shell shell) {
		super(shell);
		dbService = new DBServiceUser();
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
		// 创建一个窗口
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText("选择班级");
        shell.setSize(400, 250);
        // 使窗口居中显示
        center(shell.getDisplay(), shell);
        // 定义一个网格布局
        GridLayout gridLayout = new GridLayout(2, false);
        // 该布局中组件距离上边框的边距为20
        gridLayout.marginHeight = 20;
        // 该布局中组件之间的垂直间隔为20
        gridLayout.verticalSpacing = 20;
        // 该布局中组件距离左边框的边距为40
        gridLayout.marginLeft = 40;
        // 该布局中组件距离右边框的边距为40
        gridLayout.marginRight = 40;
        // 设置窗口布局
        shell.setLayout(gridLayout);
        createClass(shell);
	}
	
	protected void createClass(Composite parent) {
		Label className = new Label(parent, SWT.NONE);
		className.setText("选择班级或专业：");
		Combo selectClass = new Combo(parent, SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		selectClass.setLayoutData(gridData);
		selectClass.add("请选择");
		selectClass.select(0);
		List<String> allClass = dbService.getAllClass();
		if (allClass.size() > 0) {
			for (int i = 0; i < allClass.size(); i++) {
				selectClass.add(allClass.get(i));
			}
		}
		Label tipLabel = new Label(parent, SWT.NONE);
		tipLabel.setText("提示:");
		tipLabel.setFont(new Font(parent.getDisplay(), "微软雅黑", 12, SWT.BOLD));
		Label tipTextLabel = new Label(parent, SWT.NONE);
		tipTextLabel.setText("选择一个班级或专业，若没有则自行输入！");
		// 设置questionText为充满式布局，且水平占两列
		GridData tipGrid = new GridData(GridData.FILL_BOTH);
		tipGrid.horizontalSpan = 2;
		tipTextLabel.setLayoutData(tipGrid);
		Button confirm = new Button(parent, SWT.NONE);
		confirm.setText(" 确      认 ");
		confirm.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// 没有输入教室名则弹出提示框
				if (selectClass.getText().trim().equals("请选择") || selectClass.getText().trim() == "") {
					MessageBox messageBox = new MessageBox(parent.getShell());
					messageBox.setMessage("请选择班级或者专业");
					messageBox.open();
				} else {
					StaticVariable.classOrSepcialtyName = selectClass.getText();
					String[] allclass = selectClass.getItems();
					if (!allClass.contains(StaticVariable.classOrSepcialtyName)) {
						dbService.addClass(StaticVariable.classOrSepcialtyName);
					}
					shell.dispose();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
			}
		});
		// 设置窗口默认绑定的按钮为confirm按钮
		shell.setDefaultButton(confirm);
		selectClass.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent keyEvent) {
			}
			
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				// 按回车即可触发确认按钮事件
				if (keyEvent.keyCode == SWT.CR) {
					shell.setDefaultButton(confirm);
				}
			}
		});
		Button cancle = new Button(parent, SWT.NONE);
		cancle.setText(" 取     消 ");
		// 设置取消按钮水平右对齐
		GridData cancleGrid = new GridData(GridData.HORIZONTAL_ALIGN_END);
		cancle.setLayoutData(cancleGrid);
		cancle.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				StaticVariable.classOrSepcialtyName = "";
				shell.dispose();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
	}
	
	/**
	 * 使对话框居中显示
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
