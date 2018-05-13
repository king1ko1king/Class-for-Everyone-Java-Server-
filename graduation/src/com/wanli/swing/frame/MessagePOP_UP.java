package com.wanli.swing.frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.wanli.utils.StaticVariable;

public class MessagePOP_UP {

	protected Shell shell;
	private static final int DISPLAY_TIME = 2000;		// 定义消息框显示的时长2秒
    private static final int FADE_TIMER = 50;			// 定义执行一帧的时间间隔，50毫秒
    private static final int FADE_IN_STEP = 30;			// 淡入时每一帧增加的透明度
    private static final int FADE_OUT_STEP = 8;			// 淡出时每一帧减少的透明度
    private static final int FINAL_ALPHA = 225;			// 设置最终显示的透明度

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MessagePOP_UP window = new MessagePOP_UP();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		Display display = Display.getDefault();
		shell = new Shell(display, SWT.DIALOG_TRIM);
		shell.setText("提问");
		shell.setSize(200, 200);
		shell.setImage(SWTResourceManager.getImage("image/question.png"));
		display();
		message(shell);
		shell.setLayout(new FillLayout());
		fadeIn(shell);
	}
	
	protected void message(Composite parent) {
		// 创建一个带滚动条的面板
		ScrolledComposite composite = new ScrolledComposite(shell, SWT.V_SCROLL);
		// 用于显示提示信息
		Label className = new Label(composite, SWT.PUSH | SWT.WRAP);
		className.setText("=============有同学提问！！！");
		className.setBounds(0, 0, 180, 200);
		// 设置显示信息的字体大小
		className.setFont(new Font(parent.getDisplay(), "Arial", 14, SWT.NONE));
		/**
		* 虽然将按钮对象加到了滑动面板上，
		* 但还要通过 setContent() 方法设置添加的有效性
		*/
		composite.setContent(className);
	}
	
	/**
	 * 消息框淡入
	 * @param _shell
	 */
	private static void fadeIn(final Shell _shell) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    if (_shell == null || _shell.isDisposed()) {
                        return;
                    }
                    // 获取窗口的透明度
                    int cur = _shell.getAlpha();
                    // 每一帧增加30的透明度
                    cur += FADE_IN_STEP;
                    if (cur > FINAL_ALPHA) {
                        _shell.setAlpha(FINAL_ALPHA);
                        startTimer(_shell);
                        return;
                    }
                    // 若窗口透明度小于最终显示的透明度，则继续执行该线程，增加透明度
                    _shell.setAlpha(cur);
                    Display.getDefault().timerExec(FADE_TIMER, this);
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        };
        // 执行该线程
        Display.getDefault().timerExec(FADE_TIMER, run);
    }

	/**
	 * 显示窗口
	 * @param _shell
	 */
    private static void startTimer(final Shell _shell) {
        Runnable run = new Runnable() {

            @Override
            public void run() {
                try {
                    if (_shell == null || _shell.isDisposed()) {
                        return;
                    }
                    fadeOut(_shell);
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }

        };
        // 执行该线程
        Display.getDefault().timerExec(DISPLAY_TIME, run);

    }

    /**
     * 淡出窗口
     * @param _shell
     */
    private static void fadeOut(final Shell _shell) {
        final Runnable run = new Runnable() {

            @Override
            public void run() {
                try {
                    if (_shell == null || _shell.isDisposed()) {
                        return;
                    }
                    // 获取当前窗口的透明度
                    int cur = _shell.getAlpha();
                    // 每一帧减少8的透明度
                    cur -= FADE_OUT_STEP;
                    // 当窗口的透明度变为0时，使窗口消失并且失效
                    if (cur <= 0) {
                        _shell.setAlpha(0);
                        _shell.dispose();
                        return;
                    }
                    // 若透明度没有<=0，则继续执行该线程，减小透明度
                    _shell.setAlpha(cur);
                    Display.getDefault().timerExec(FADE_TIMER, this);

                } catch (Exception err) {
                    err.printStackTrace();
                }
            }

        };
        // 执行该线程
        Display.getDefault().timerExec(FADE_TIMER, run);

    }
	
	/**
	 * 使消息框在右下角显示
	 */
	protected void display() {
		// 获取除去状态栏的屏幕尺寸
		Rectangle screenNoTaskbar = Display.getDefault().getClientArea(); 
		// 获取窗体尺寸
		Rectangle rect = shell.getBounds();
		int x = screenNoTaskbar.width - rect.width;
		int y = screenNoTaskbar.height - rect.height;
		shell.setLocation(x, y);
	}

}
