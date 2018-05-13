package com.wanli.swing.frame;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.wanli.swing.frame.listener.DeleteQuestionListener;
import com.wanli.swing.frame.listener.OpenFileDialogBtnListener;
import com.wanli.swing.frame.listener.UpdateQuestionListener;
import com.wanli.swing.frame.updateQuestion.listener.QuestionUpdateRefreshBtnListener;
import com.wanli.utils.StaticVariable;

public class QuestionManagerShell {

	private Composite parent;	// 存储一切与主窗口相关的信息
	
	public QuestionManagerShell(Composite parent) {
		this.parent = parent;
		new ManagerShell(parent.getShell()).open();
	}
}

class ManagerShell extends Dialog {

	protected Object result;
	protected Shell shell;
	private TableItem[] items;	// 显示所有的题目
	private int BTNWIDTH = 90;	// 按钮显示的宽度
	private double NOCLOUMN_WIDTH_PROPORTION = 0.15;
	private double QUESCLOUMN_WIDTH_PROPORTION = 0.6;
	private double OPERCLOUMN_WIDTH_PROPORTION = 0.23;
	private int SHELL_WIDTH = 800;
	private int SHELL_HEIGHT = 800;
	
	public ManagerShell(Shell shell) {
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
	 * 创建窗口
	 */
	protected void createContents() {
		// 创建一个窗口
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText("问题管理");
//        shell.setImage(new Image(arg0, arg1));
        shell.setSize(SHELL_WIDTH, SHELL_HEIGHT);
        shell.setImage(SWTResourceManager.getImage("image/quesManager.png"));
        shell.addShellListener(new ShellAdapter() {
        	@Override
        	public void shellClosed(ShellEvent e) {
//    			MessageBox messageBox = new MessageBox(shell, SWT.YES | SWT.NO);
//    			messageBox.setText("警告");
//    			messageBox.setMessage("确定要退出吗？");
//    			if (e.doit = messageBox.open() == SWT.YES) {
				StaticVariable.choiceList.clear();
				StaticVariable.trueOrFalseList.clear();
				StaticVariable.fillblanksList.clear();
//    			} 
    		}
        	
		});
        // 使窗口居中显示
        center(shell.getDisplay(), shell);
        // 定义一个网格布局
        GridLayout gridLayout = new GridLayout(2, false);
        // 设置窗口布局
        shell.setLayout(gridLayout);
        createClass(shell);
	}
	
	protected void createClass(Composite parent) {
		Button openFile = new Button(parent, SWT.NONE);
		openFile.setText("打开文件");
		openFile.addSelectionListener(new OpenFileDialogBtnListener());
		Button refresh = new Button(parent, SWT.NONE);
		refresh.setText("刷新");
		refresh.addSelectionListener(new QuestionUpdateRefreshBtnListener());
		TableViewer tableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		StaticVariable.table = tableViewer.getTable();
		StaticVariable.table.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridData gridTab = new GridData(GridData.FILL_BOTH);
		gridTab.horizontalSpan = 2;
		StaticVariable.table.setLayoutData(gridTab);
		// 定义表中的列
		TableColumn noColumn = new TableColumn(StaticVariable.table, SWT.LEFT);
		noColumn.setText("编    号");
		noColumn.setWidth((int)(parent.getSize().x * NOCLOUMN_WIDTH_PROPORTION));
		TableColumn quesColumn = new TableColumn(StaticVariable.table, SWT.LEFT);
		quesColumn.setText("问    题");
		quesColumn.setWidth((int)(parent.getSize().x * QUESCLOUMN_WIDTH_PROPORTION));
		TableColumn operColumn = new TableColumn(StaticVariable.table, SWT.LEFT);
		operColumn.setText("操    作");
		operColumn.setWidth((int)(parent.getSize().x * OPERCLOUMN_WIDTH_PROPORTION));
		if (StaticVariable.tableName != null && StaticVariable.tableName.toString() != "") {
			// 填充表格
//			fillTable();			
		}
		// 设置表头可见
		StaticVariable.table.setHeaderVisible(true);
		// 设置表格线可见
		StaticVariable.table.setLinesVisible(true);
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
