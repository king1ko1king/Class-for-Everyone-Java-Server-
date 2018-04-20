
package com.wanli.swing;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * ��Ϣ�������࣬���ͻ�������Ϣ���͹���ʱ����
 * @author wanli
 *
 */
public class Mmmm {

	private Composite parent;						// ����һ�й��������ڵ���Ϣ
	
	public Mmmm(Composite parent) {
		this.parent = parent;
		new ShellPop_up(parent.getShell()).open();	// ִ����Ϣ�����ʾ
	}
	
}

/**
 * ������Ϣ��
 * @author wanli
 *
 */
class ShellPop_up extends Dialog {
	private static final int DISPLAY_TIME = 2000;		// ������Ϣ����ʾ��ʱ��2��
    private static final int FADE_TIMER = 50;			// ����ִ��һ֡��ʱ������50����
    private static final int FADE_IN_STEP = 30;			// ����ʱÿһ֡���ӵ�͸����
    private static final int FADE_OUT_STEP = 8;			// ����ʱÿһ֡���ٵ�͸����
    private static final int FINAL_ALPHA = 225;			// ����������ʾ��͸����
	protected Object result;
	protected Shell shell;
	public ShellPop_up(Shell shell) {
		super(shell);
	}
	
	public Object open() {
		createContents(); 
		fadeIn(shell);
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);// ��������
        shell.setText("����");
        shell.setSize(200, 200);// ������ʾ���ڴ�С
        display();				// ���ô�����ʾλ��											
        shell.setLayout(new FillLayout());
        message(shell);
	}
	
	protected void message(Composite parent) {
		// ����һ���������������
		ScrolledComposite composite = new ScrolledComposite(shell, SWT.BORDER | SWT.V_SCROLL);
		// ������ʾ��ʾ��Ϣ
		Label className = new Label(composite, SWT.PUSH | SWT.WRAP);
		className.setText("=============��ͬѧ���ʣ�����");
		className.setBounds(0, 0, 180, 200);
		// ������ʾ��Ϣ�������С
		className.setFont(new Font(parent.getDisplay(), "Arial", 14, SWT.NONE));
		/**
		* ��Ȼ����ť����ӵ��˻�������ϣ�
		* ����Ҫͨ�� setContent() ����������ӵ���Ч��
		*/
		composite.setContent(className);
	}
	
	/**
	 * ʹ��Ϣ�������½���ʾ
	 */
	protected void display() {
		// ��ȡ��ȥ״̬������Ļ�ߴ�
		Rectangle screenNoTaskbar = Display.getDefault().getClientArea(); 
		// ��ȡ����ߴ�
		Rectangle rect = shell.getBounds();
		int x = (screenNoTaskbar.width - rect.width);
		int y = screenNoTaskbar.height - rect.height;
		shell.setLocation(x, y);
	}
	
	/**
	 * ��Ϣ����
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
                    // ��ȡ���ڵ�͸����
                    int cur = _shell.getAlpha();
                    // ÿһ֡����30��͸����
                    cur += FADE_IN_STEP;
                    if (cur > FINAL_ALPHA) {
                        _shell.setAlpha(FINAL_ALPHA);
                        startTimer(_shell);
                        return;
                    }
                    // ������͸����С��������ʾ��͸���ȣ������ִ�и��̣߳�����͸����
                    _shell.setAlpha(cur);
                    Display.getDefault().timerExec(FADE_TIMER, this);
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        };
        // ִ�и��߳�
        Display.getDefault().timerExec(FADE_TIMER, run);
    }

	/**
	 * ��ʾ����
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
        // ִ�и��߳�
        Display.getDefault().timerExec(DISPLAY_TIME, run);

    }

    /**
     * ��������
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
                    // ��ȡ��ǰ���ڵ�͸����
                    int cur = _shell.getAlpha();
                    // ÿһ֡����8��͸����
                    cur -= FADE_OUT_STEP;
                    // �����ڵ�͸���ȱ�Ϊ0ʱ��ʹ������ʧ����ʧЧ
                    if (cur <= 0) {
                        _shell.setAlpha(0);
                        _shell.dispose();
                        return;
                    }
                    // ��͸����û��<=0�������ִ�и��̣߳���С͸����
                    _shell.setAlpha(cur);
                    Display.getDefault().timerExec(FADE_TIMER, this);

                } catch (Exception err) {
                    err.printStackTrace();
                }
            }

        };
        // ִ�и��߳�
        Display.getDefault().timerExec(FADE_TIMER, run);

    }
	
}

