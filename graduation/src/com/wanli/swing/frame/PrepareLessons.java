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
import org.eclipse.wb.swt.SWTResourceManager;

import com.wanli.swing.frame.listener.CreateXmlFileBtnListener;
import com.wanli.swing.frame.listener.QuestionTypeListener;
import com.wanli.swing.frame.questiontype.BeginningComposite;
import com.wanli.utils.StaticVariable;

/**
 * 备课对话框
 * @author wanli
 *
 */
public class PrepareLessons {

	private Composite parent;	// 存储一切与主窗口相关的信息
	
	public PrepareLessons(Composite parent) {
		this.parent = parent;
		// 执行窗口弹出
		new PrepareShell(parent.getShell()).open();
		// 每次成功创建xml文件或者关闭创建文件的窗口都有清空所有存储题目的list
		StaticVariable.choiceList.clear();
		StaticVariable.trueOrFalseList.clear();
		StaticVariable.fillblanksList.clear();
	}
	
	public PrepareLessons(Composite parent, String shellText, String addBtnText, boolean isAppend) {
		this.parent = parent;
		// 执行窗口弹出
		new PrepareShell(parent.getShell(), shellText, addBtnText, isAppend).open();
	}
	
}

class PrepareShell extends Dialog {

	protected Object result;
	protected Shell shell;
	private String shellText = "课前备题";
	private String addBtnText = "创    建";
	private boolean isAppend = false;
//	private Composite questionCom;
	
	public PrepareShell(Shell shell) {
		super(shell);
	}
	
	public PrepareShell(Shell shell, String shellText, String addBtnText, boolean isAppend) {
		super(shell);
		this.shellText = shellText;
		this.addBtnText = addBtnText;
		this.isAppend = isAppend;
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
	 * 创建窗口
	 */
	protected void createContents() {
		// 创建一个窗口
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);
        shell.setText(shellText);
        shell.setSize(800, 800);
        shell.setImage(SWTResourceManager.getImage("image/Prepare.png"));
        shell.addShellListener(new ShellAdapter() {
        	@Override
        	public void shellClosed(ShellEvent e) {
    			MessageBox messageBox = new MessageBox(shell, SWT.YES | SWT.NO);
    			messageBox.setText("警告");
    			messageBox.setMessage("确定要退出吗？");
    			if (e.doit = messageBox.open() == SWT.YES) {
    				StaticVariable.choiceList.clear();
    				StaticVariable.trueOrFalseList.clear();
    				StaticVariable.fillblanksList.clear();
    			} 
    		}
        	
		});
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
		// 选择题型下拉列表框
		Combo questionType = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
		questionType.add("请选择题型");
		questionType.add("选择题");
		questionType.add("是非题");
		questionType.add("填空题");
		questionType.select(0);
		questionType.addSelectionListener(new QuestionTypeListener(parent, questionType));

		// 创建Xml文件按钮
		Button create = new Button(parent, SWT.NONE);
		create.setText(addBtnText);
		create.addSelectionListener(new CreateXmlFileBtnListener(shell, isAppend));

		// 设置下拉框控件和创建按钮的布局
		GridData fill = new GridData(GridData.FILL_HORIZONTAL);
		questionType.setLayoutData(fill);
		create.setLayoutData(fill);
		StaticVariable.questionCom = new BeginningComposite(parent, SWT.NONE);
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
