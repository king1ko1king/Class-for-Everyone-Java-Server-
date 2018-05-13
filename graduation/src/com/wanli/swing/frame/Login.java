package com.wanli.swing.frame;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.wanli.swing.dao.DBDaoUser;
import com.wanli.swing.dao.RegistDao;
import com.wanli.swing.entities.UserBean;
import com.wanli.swing.frame.listener.ComboListener;

public class Login {
	private static String username;				// 登陆的用户名
	private static String password;				// 登陆的密码
	private static Text textPassword;			// 输入密码的文本控件
	private static Login window;				// 当前类的引用
	private static UserBean user;				// 用户信息的bean
	private static Image coverImg;				// 图片
	private static Properties userProp;			// 读取保存的登录信息，用于下拉列表框显示
	private static Properties saveProp;			// 读取保存的登录信息，用于存储上一次退出时的帐号
	private static FileInputStream inStream;	// 输入流，读取信息
	private static FileWriter writer;			// 输出流，写信息
	private static File userFile;				// 读取保存用户信息的文件
	private static File saveFile;				// 读取保存上一次退出时帐号的信息
	private static Set<Object> itSet;			// 保存所有信息
	private static Iterator keys;				// 用于遍历所有信息
	private static Display display;
	private static Shell shell;

	/**
	 * 初始化属性
	 */
	public Login() {
		this.user = new UserBean();
		userProp = new Properties();
		saveProp = new Properties();
		
		// 创建File对象
		userFile = new File("users.properties");		
		saveFile = new File("savecount.properties");
		try {
			// 创建文件输入流
			inStream = new FileInputStream(userFile);
			// 从输入流中加载内容
			userProp.load(inStream);
			inStream = new FileInputStream(saveFile);
			saveProp.load(inStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		itSet = userProp.keySet();// 将获取的信息转为set
		keys = itSet.iterator();
	}
	
	public static void onCreate() {
		display = Display.getDefault();
		shell = new Shell(display, SWT.DIALOG_TRIM);
		shell.setImage(SWTResourceManager.getImage("image/1.jpg"));
		shell.setSize(474, 365);
		center(display, shell);
		shell.setText("Class For Everyone");

		// 窗体整体面板
		Composite globalLayout = new Composite(shell, SWT.NONE);
		globalLayout.setLocation(0, 0);
		globalLayout.setSize(527, 338);
		globalLayout.setLayout(null);

		// 图片显示面板
		Composite classforeveryone = new Composite(globalLayout, SWT.NONE);
		classforeveryone.setBounds(0, 0, 469, 154);
		classforeveryone.setBackgroundImage(SWTResourceManager.getImage("image/1.jpg"));

		// 登录面板
		Composite loginComposite = new Composite(globalLayout, SWT.NONE);
		loginComposite.setBounds(0, 152, 469, 186);

		// 输入用户名，用下拉框显示历史登录信息
		Combo comboUser = new Combo(loginComposite, SWT.NONE);
		comboUser.setBounds(120, 24, 220, 32);
		
		// 密码输入框
		textPassword = new Text(loginComposite, SWT.BORDER | SWT.PASSWORD);
		textPassword.setBounds(120, 55, 220, 32);
		
		// 复选框，保存密码
		Button bRememberMe = new Button(loginComposite, SWT.CHECK);
		bRememberMe.setBounds(120, 93, 100, 28);
		bRememberMe.setForeground(SWTResourceManager.getColor(240, 248, 255));
		bRememberMe.setText("\u8BB0\u4F4F\u5BC6\u7801");
		
		// 复选框，自动登录
		Button bAutoLogin = new Button(loginComposite, SWT.CHECK);
		bAutoLogin.setBounds(240, 93, 100, 28);
		bAutoLogin.setForeground(SWTResourceManager.getColor(240, 248, 255));
		bAutoLogin.setText("\u81EA\u52A8\u767B\u5F55");
		
		// 为下拉框添加点击事件
		comboUser.addSelectionListener(new ComboListener(comboUser, bRememberMe));
		
		// 将获取的信息全部添加到下拉框显示
		while (keys.hasNext()) {
			comboUser.add((String)keys.next());
		}
		itSet = saveProp.keySet();
		keys = itSet.iterator();
		while (keys.hasNext()) {
			username = (String) keys.next();
			password = saveProp.getProperty(username);
			if (username != null) {
				comboUser.setText(username);
				textPassword.setText(password);
			}
			if (password != null && password.length() != 0) {
				bRememberMe.setSelection(true);
			} else {
				bRememberMe.setSelection(false);
			}
		}

		// 注册帐号链接
		Link linkRegist = new Link(loginComposite, SWT.NONE);
		linkRegist.setBounds(350, 24, 100, 25);
		linkRegist.setText("<a>\u6CE8\u518C\u8D26\u53F7</a>");
		// 为超链接添加点击事件
		linkRegist.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent arg0) {

			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				URI uri = null;
				try {
					// 点击后跳转到注册页面
					uri = new URI("http://localhost:8090/graduation/regist.jsp");
					Desktop dtp = Desktop.getDesktop();
					dtp.browse(uri);
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {

			}

		});

		// 忘记密码超链接
		Link linkForget = new Link(loginComposite, SWT.NONE);
		linkForget.setBounds(350, 55, 100, 25);
		linkForget.setText("<a>\u627E\u56DE\u5BC6\u7801</a>");
		// 为超链接添加点击事件
		linkForget.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent arg0) {

			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				URI uri = null;
				try {
					// 点击超链接后跳转到找回密码页面
					uri = new URI("http://localhost:8090/graduation/forgetPassword.jsp");
					Desktop dtp = Desktop.getDesktop();
					dtp.browse(uri);
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {

			}

		});

		// 登录按钮
		Button bLogin = new Button(loginComposite, SWT.NONE);
		bLogin.setBounds(120, 137, 220, 27);
		bLogin.setText("\u767B\u5F55");

		// 登录用户头像
		Composite composite = new Composite(loginComposite, SWT.NONE);
		composite.setBounds(32, 24, 98, 97);

		// 为登录按钮添加点击事件
		bLogin.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// 获取登录的用户名和密码
				window.getUser().setName(comboUser.getText());
				window.getUser().setPassword(textPassword.getText());
				DBDaoUser dao = new DBDaoUser();
				String nickName = dao.getUserByNameAndPassword(window.getUser().getName(), window.getUser().getPassword());
				// 查找数据库确认该用户是否存在，存在则执行保存帐号操作和打开主界面
				if (nickName != null) {
					// 清空保存上一次登录信息的文件，保存这次的登录信息
					saveProp.clear();
					// 判断保存密码复选框是否选中，选中则一起保存密码，否则只保存帐号
					if (bRememberMe.getSelection()) {
						userProp.setProperty(comboUser.getText(), textPassword.getText());
						saveProp.setProperty(comboUser.getText(), textPassword.getText());
						try {
							writer = new FileWriter(userFile);
							userProp.store(writer, null);
							writer = new FileWriter(saveFile);
							saveProp.store(writer, null);
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							if (writer != null) {
								try {
									writer.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					} else {
						userProp.setProperty(comboUser.getText(), "");
						saveProp.setProperty(comboUser.getText(), "");
			        	try {
							writer = new FileWriter(userFile);
							userProp.store(writer, null);
							writer = new FileWriter(saveFile);
							saveProp.store(writer, null);
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							if (writer != null) {
								try {
									writer.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
					shell.dispose();
					// 打开主界面
					new MainFrame(window.getUser().getName()).run();
				} else {
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
					messageBox.setMessage("用户名或密码错误！请重新输入");
					messageBox.open();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}
		});
		// 显示图片
		window.setCoverImg(new Image(display, "image/1.jpg"));
	}

	public static void main(String[] args) {
		window = new Login();// 初始化
		onCreate();
		shell.open();
		shell.layout();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		System.exit(0);
	}
	
	/**
	 * 设置窗口位于屏幕中间
	 * 
	 * @param display：设备
	 * @param shell：要调整位置的窗口对象
	 */
	public static void center(Display display, Shell shell) {
		Rectangle bounds = display.getPrimaryMonitor().getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	public Image getCoverImg() {
		return coverImg;
	}

	public void setCoverImg(Image coverImg) {
		this.coverImg = coverImg;
	}

	public static Shell getShell() {
		return shell;
	}

	public static void setShell(Shell shell) {
		Login.shell = shell;
	}
	
}
