package com.wanli.swing.frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.wanli.swing.frame.updateQuestion.UpdateChoiceComposite;
import com.wanli.swing.frame.updateQuestion.UpdateFillInTheBlanksComposite;
import com.wanli.swing.frame.updateQuestion.UpdateTrueOrFalseComposite;
import com.wanli.utils.StaticVariable;

/**
 * 修改问题对话框
 * @author wanli
 *
 */
public class UpdateQuestionShell {

	private Composite parent;	// 有父窗体一切信息的面板
	private int i;				// 修改的题目的list下标
	private String openFile;	// 打开的文件名
	
	public UpdateQuestionShell(Composite parent, int i, String openFile) {
		this.parent = parent;
		this.i = i;
		this.openFile = openFile;
		new UpdateShell(parent.getShell(), i, openFile).open();
	}
}

/**
 * 弹出框
 * @author wanli
 *
 */
class UpdateShell extends Dialog {

	protected Object result;
	protected Shell shell;
	private int i;
	private String openFile;
	
	public UpdateShell(Shell shell, int i, String openFile) {
		super(shell);
		this.i = i;
		this.openFile = openFile;
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
        shell.setText("问题修改");
        shell.setSize(800, 800);
        shell.setImage(SWTResourceManager.getImage("image/Update.png"));
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
        createShell(shell);
	}
	
	protected void createShell(Composite parent) {
		String question = StaticVariable.questionsList.get(i);
		String[] strs = question.split("#\\^");
		// 判断题目类型
		switch (strs[0]) {
		// 选择题
		case "choice":
			StaticVariable.questionCom = new UpdateChoiceComposite(parent, SWT.BORDER, i, openFile);			
			break;
		// 是非题
		case "true_or_false":
			StaticVariable.questionCom = new UpdateTrueOrFalseComposite(parent, SWT.BORDER, i, openFile);
			break;
		// 填空题
		case "fill_in_the_blanks":
			StaticVariable.questionCom = new UpdateFillInTheBlanksComposite(parent, SWT.BORDER, i, openFile);
			break;
		}
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


