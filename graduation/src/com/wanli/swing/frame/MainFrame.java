package com.wanli.swing.frame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.wanli.classforereryone.server.MyServer;
import com.wanli.classforereryone.server.ServerThread;
import com.wanli.swing.frame.listener.AskQuestionTableListener;
import com.wanli.swing.frame.listener.ButtonDownListener;
import com.wanli.swing.frame.listener.CreateClassListener;
import com.wanli.swing.frame.listener.HistoryCharBtnListener;
import com.wanli.swing.frame.listener.HistoryComboListener;
import com.wanli.swing.frame.listener.OnlineTreeListener;
import com.wanli.swing.frame.listener.QuestionSelectComboListener;
import com.wanli.swing.frame.listener.ScoreChartBtnListener;
import com.wanli.swing.frame.listener.TabFordlerListener;
import com.wanli.swing.frame.text.menu.listener.BoldFont;
import com.wanli.swing.frame.text.menu.listener.CopyText;
import com.wanli.swing.frame.text.menu.listener.CutText;
import com.wanli.swing.frame.text.menu.listener.ItalicText;
import com.wanli.swing.frame.text.menu.listener.PasteText;
import com.wanli.swing.frame.text.menu.listener.SelectAll;
import com.wanli.swing.frame.text.menu.listener.SetTextColor;
import com.wanli.swing.frame.text.menu.listener.UnderlineText;
import com.wanli.swing.service.DBService;
import com.wanli.swing.service.DBServiceUser;
import com.wanli.thread.ListeningSocket;
import com.wanli.utils.StaticVariable;
import com.wanli.utils.XmlToJavaBean;

/**
 * 软件主窗口
 * @author wanli
 *
 */
public class MainFrame extends ApplicationWindow {
	private static String APPNAME = "Class For Everyone";	// 当前软件的名称
	private static String welcome = "欢迎您，";				// 显示欢迎提示
	private Shell shell;									// 主窗口的shell类
	private Action newCreate;								// 新建教室
	private Action openFile;								// 打开文件
//	private Action saveFile;								// 保存文件
//	private Action saveAsFile;								// 另存为
	private Action unsubscribe;								// 注销
	private Action exit;									// 退出程序
	private Action copyFile;								// 复制
	private Action pasteFile;								// 粘贴
	private Action cutFile;									// 剪切
	private Action setFont;									// 设置字体
	private Action setColor;								// 设置颜色
	private Action selectAll;								// 全选
	private Action formate;									// 格式
	private Action about;									// 关于
	private Tree tree;										// 显示在线用户列表
	private Font font;										// 字体
	private File file;										// 文件
	private TabFolder tabFolder;							// 选项卡
//	private Button first;									// 跳转到第一题
//	private Button previous;								// 上一题
//	private Button next;									// 下一题
//	private Button last;									// 最后一题
	private String userName;								// 用户名
	boolean changes;										// 文档是否改变
	private DBService dbService;							// 创建操作dao的业务
	private Runtime runtime;								// 获取当前程序的运行时
	private Process process;								// 用来存储调用的外部进程
	private DBServiceUser serviceUser;	
	private String nickname;
	
	public MainFrame(String userName) {
		// 部署窗口
		super(null);
		// 开启一个线程，用来监听客户端的连接
		new Thread(new MyServer()).start();
		// 开启一个线程，监听客户端的连接，监听到有客户端连接上就显示客户端的连接情况
		new Thread(new ListeningSocket()).start();
		this.userName = userName;// 获取用户名
		newCreate = new NewCreate();
		openFile = new OpenFile();
//		saveFile = new SaveFileAction();
//		saveAsFile = new SaveAsFileAction();
		unsubscribe = new UnsubscribeAction();
		exit = new ExitAction();
		copyFile = new CopyFileAction();
		pasteFile = new PasteFileAction();
		cutFile = new CutFileAction();
		setFont = new SetFontAction();
		setColor = new SetColorAction();
		selectAll = new SelectAllAction();
		formate = new FormateAction();
		about = new AboutAction();
		dbService = new DBService();
		addMenuBar();// 添加菜单栏
		addToolBar(SWT.FLAT);// 添加工具栏
		serviceUser = new DBServiceUser();
		nickname = serviceUser.getNicknameByPhoneOrEmail(userName);
	}

	public void run() {
		// 直到窗口关闭方能从 open() 方法返回
		setBlockOnOpen(true);
		// 打开窗口
		open();
		if (!Display.getCurrent().isDisposed()) {
			// 关闭窗口，释放在操作系统中用到的资源
			Display.getCurrent().dispose();			
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

	// 设置窗体外观
	@Override
	public Control createContents(Composite parent) {
		// 设置窗体大小
		Rectangle bounds = parent.getShell().getDisplay().getPrimaryMonitor().getBounds();
		int windowWidth = bounds.width;
		int windowHeight = (int) (bounds.height * 0.98);
		parent.getShell().getDisplay();
		center(parent.getDisplay(), parent.getShell());
		// 设置窗体标题
		parent.getShell().setText(APPNAME);
		parent.getShell().setImage(SWTResourceManager.getImage("image/1.jpg"));
		shell = parent.getShell();
		StaticVariable.parent = parent;
		// 设置主面板
		Composite mainFrame = new Composite(parent, SWT.NONE);
		mainFrame.setLayout(null);
		// 设置显示在线情况面板
		Composite onlineView = new Composite(mainFrame, SWT.BORDER);
		onlineView.setBounds(0, 0, (int) (windowWidth * 0.3), (int) (windowHeight * 0.87));
		// 设置创建教室面板
		Composite createRoom = new Composite(onlineView, SWT.BORDER);
		createRoom.setBounds(0, 0, onlineView.getSize().x, (int) (onlineView.getSize().y * 0.04));
		createRoom.setLayout(new FillLayout());
		// 创建教室按钮
		Button button = new Button(createRoom, SWT.NONE);
		FormData fd_button = new FormData();
		fd_button.left = new FormAttachment(0);
		button.setLayoutData(fd_button);
		button.setText("\u5F00\u542F\u9738\u5C4F");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ServerThread.sendToAllClient("开启霸屏");
			}
		});
		// 截图按钮
		Button screenshot = new Button(createRoom, SWT.NONE);
		screenshot.setText("教学截图");
		screenshot.setLayoutData(fd_button);
		screenshot.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				runtime = Runtime.getRuntime();		// 获取当前程序的运行时
				try {
					// 执行windows系统自带的截图工具程序，并通过process管理该进程
					process = runtime.exec("C:\\Windows\\System32\\SnippingTool.exe");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
//		Button messBtn = new Button(createRoom, SWT.NONE);
//		messBtn.setText("提问");
//		messBtn.setLayoutData(fd_button);
//		messBtn.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent arg0) {
//				new AskQuestionTableListener(parent);
//			}
//		});

		// 设置显示在线人数面板
		Composite onlineNum = new Composite(onlineView, SWT.BORDER);
		onlineNum.setBounds(0, (int) (onlineView.getSize().y * 0.04), onlineView.getSize().x,
				(int) (onlineView.getSize().y * 0.04));

		// 显示登录信息
		Label welcome = new Label(onlineNum, SWT.NONE);
		welcome.setBounds(0, 0, 300, 32);
		welcome.setText(MainFrame.welcome + this.nickname);

		// 显示在线人数
		StaticVariable.onlining = new Label(onlineNum, SWT.NONE);
		StaticVariable.onlining.setBounds(300, 0, 300, 32);
		StaticVariable.onlining.setText(StaticVariable.onlineNumsStr + StaticVariable.onlineNumsInt);

		// 设置显示在线用户列表面板
		Composite onlineUser = new Composite(onlineView, SWT.BORDER);
		onlineUser.setBounds(0, onlineNum.getSize().y + createRoom.getSize().y, onlineView.getSize().x,
				(int) (onlineView.getSize().y * 0.92));
		onlineUser.setLayout(new FillLayout(SWT.HORIZONTAL));

		// 以树的形式显示在线用户列表
		tree = new Tree(onlineUser, SWT.BORDER);
		tree.addMouseListener(new OnlineTreeListener(tree, parent));
		
		// 设置显示题目，成绩，历史记录等的面板
		Composite textView = new Composite(mainFrame, SWT.BORDER);
		textView.setBounds((int) (windowWidth * 0.3), 0, (int) (windowWidth * 0.69), onlineView.getSize().y);
		textView.setLayout(new GridLayout(6, false));

		// 题目，成绩，历史记录选项卡
		tabFolder = new TabFolder(textView, SWT.NONE);
		GridData gridTab = new GridData(GridData.FILL_BOTH);
		gridTab.horizontalSpan = 6;
		tabFolder.setLayoutData(gridTab);

		// 定义第一个选项卡
		final TabItem question = new TabItem(tabFolder, SWT.NONE);
		question.setText("题目");
		{
			//为该选项卡定义一个面板
			Composite questionComp = new Composite(tabFolder, SWT.BORDER);
			question.setControl(questionComp);
			//设置该面板为网格式布局，且设置为4列
			questionComp.setLayout(new GridLayout(4, false));
			//添加一个面板，用来放置StyledText控件
			Composite textComp = new Composite(questionComp, SWT.BORDER);
			//设置textComp面板的布局为充满式布局
			textComp.setLayout(new FillLayout());
			StaticVariable.text = new StyledText(textComp, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
			StaticVariable.text.setRightMargin(40);
			StaticVariable.text.setLeftMargin(40);
			StaticVariable.text.setFont(new Font(parent.getDisplay(), "Arial", 20, SWT.NONE));
			StaticVariable.text.setBounds(0, 0, textComp.getSize().x, questionComp.getSize().y);
			// 为text添加右键菜单
			Menu menu = new Menu(shell, SWT.POP_UP);
			// 添加全选菜单
			MenuItem selectAllItem = new MenuItem(menu, SWT.PUSH);
			selectAllItem.setText("全选");
			selectAllItem.setImage(new Image(parent.getDisplay(), "image/select_all.png"));
			selectAllItem.addSelectionListener(new SelectAll());
			// 添加复制菜单
			MenuItem copyItem = new MenuItem(menu, SWT.PUSH);
			copyItem.setText("复制");
			copyItem.setImage(new Image(parent.getDisplay(), "image/copy.png"));
			copyItem.addSelectionListener(new CopyText());
			// 添加黏贴菜单
			MenuItem pasteItem = new MenuItem(menu, SWT.PUSH);
			pasteItem.setText("黏贴");
			pasteItem.setImage(new Image(parent.getDisplay(), "image/paste.png"));
			pasteItem.addSelectionListener(new PasteText());
			// 添加剪切菜单
			MenuItem cutItem = new MenuItem(menu, SWT.PUSH);
			cutItem.setText("剪切");
			cutItem.setImage(new Image(parent.getDisplay(), "image/cut.png"));
			cutItem.addSelectionListener(new CutText());
			// 添加加粗菜单
			MenuItem boldItem = new MenuItem(menu, SWT.PUSH);
			boldItem.setText("加粗");
			boldItem.setImage(new Image(parent.getDisplay(), "image/bold.png"));
			boldItem.addSelectionListener(new BoldFont());
			// 添加斜体菜单
			MenuItem italicItem = new MenuItem(menu, SWT.PUSH);
			italicItem.setText("斜体");
			italicItem.setImage(new Image(parent.getDisplay(), "image/italic.png"));
			italicItem.addSelectionListener(new ItalicText());
			// 添加下划线菜单
			MenuItem underlineItem = new MenuItem(menu, SWT.PUSH);
			underlineItem.setText("下划线");
			underlineItem.setImage(new Image(parent.getDisplay(), "image/underline.png"));
			underlineItem.addSelectionListener(new UnderlineText());
			// 添加设置颜色菜单
			MenuItem colorItem = new MenuItem(menu, SWT.PUSH);
			colorItem.setText("设置颜色");
			colorItem.setImage(new Image(parent.getDisplay(), "image/font_color.png"));
			colorItem.addSelectionListener(new SetTextColor());
			StaticVariable.text.setMenu(menu);
			//为socreTableComp面板设置一个控制布局的对象GridTab1，设置该面板在水平、垂直两个方向全充满
			GridData gridTab1 = new GridData(GridData.FILL_BOTH);
			//设置socreTableComp面板垂直占4列
			gridTab1.horizontalSpan = 4;
			textComp.setLayoutData(gridTab1);
			// 定义一个Label控件
			Label choiceQues = new Label(questionComp, SWT.NONE);
			choiceQues.setText("选择题目：");
			// 定义一个下拉框，用于选择题目
			StaticVariable.questionSelect = new Combo(questionComp, SWT.READ_ONLY);
			StaticVariable.questionSelect.add("请选择题目");
			StaticVariable.questionSelect.select(0);
			GridData selectGrid = new GridData(GridData.FILL_HORIZONTAL);
			StaticVariable.questionSelect.setLayoutData(selectGrid);
			StaticVariable.questionSelect.addSelectionListener(new QuestionSelectComboListener());
//			StaticVariable.refresh = new Button(scoreComp, SWT.NONE);
//			StaticVariable.refresh.setText("刷新");
			StaticVariable.scoreChartBtn = new Button(questionComp, SWT.NONE);
			StaticVariable.scoreChartBtn.setText("图表数据");
			StaticVariable.scoreChartBtn.setEnabled(false);
//			first = new Button(questionComp, SWT.NONE);
//			first.setText("首题");
//			first.setEnabled(false);
//
//			previous = new Button(questionComp, SWT.NONE);
//			previous.setText("上一题");
//			previous.setEnabled(false);
//
//			next = new Button(questionComp, SWT.NONE);
//			next.setText("下一题");
//			next.setEnabled(false);
//
//			last = new Button(questionComp, SWT.NONE);
//			last.setText("末题");
//			last.setEnabled(false);
		}

		// 定义第二个选项卡
		final TabItem score = new TabItem(tabFolder, SWT.NONE);
		score.setText("答案");
		{
			//为该选项卡添加一个面板
			Composite answerComp = new Composite(tabFolder, SWT.BORDER);
			score.setControl(answerComp);
			answerComp.setLayout(new FillLayout());
			StaticVariable.answer = new StyledText(answerComp, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
			StaticVariable.answer.setRightMargin(40);
			StaticVariable.answer.setLeftMargin(40);
			StaticVariable.answer.setFont(new Font(parent.getDisplay(), "Arial", 30, SWT.NONE));
			StaticVariable.answer.setBounds(0, 0, answerComp.getSize().x, answerComp.getSize().y);
//			//设置该面板的布局为网格布局，设置成一列
//			scoreComp.setLayout(new GridLayout(2, false));
//			//添加一个面板，用来放置表格
//			Composite scoreTableComp = new Composite(scoreComp, SWT.BORDER);
//			//设置socreTableComp面板为充满式布局
//			scoreTableComp.setLayout(new FillLayout());
//			// 定义表格
//			StaticVariable.scoreTab = new Table(scoreTableComp, SWT.MULTI);
//			// 设置表头可见
//			StaticVariable.scoreTab.setHeaderVisible(true);
//			// 设置网格线可见
//			StaticVariable.scoreTab.setLinesVisible(true);
//			//为socreTableComp面板设置一个控制布局的对象GridTab2，设置该面板在水平、垂直两个方向全充满
//			GridData gridTab2 = new GridData(GridData.FILL_BOTH);
//			//设置socreTableComp面板垂直占两列
//			gridTab2.horizontalSpan = 2;
//			scoreTableComp.setLayoutData(gridTab2);
//			//定义一个刷新表格的按钮
//			StaticVariable.refresh = new Button(scoreComp, SWT.NONE);
//			StaticVariable.refresh.setText("刷新");
//			StaticVariable.scoreChartBtn = new Button(scoreComp, SWT.NONE);
//			StaticVariable.scoreChartBtn.setText("图表数据");
		}

		// 定义第三个选项卡
		final TabItem history = new TabItem(tabFolder, SWT.NONE);
		history.setText("历史记录");
		{
			//为该选项卡添加一个面板
			Composite historyComp = new Composite(tabFolder, SWT.BORDER);
			history.setControl(historyComp);
			//设置该面板的布局为网格布局，设置成两列
			historyComp.setLayout(new GridLayout(3, false));
			//添加一个面板，用来放置表格
			Composite tableComp = new Composite(historyComp, SWT.BORDER);
			//设置tableComp面板的布局为充满式布局
			tableComp.setLayout(new FillLayout());
			//定义一张表格
			StaticVariable.historyTab = new Table(tableComp, SWT.MULTI);
			// 设置表头可见
			StaticVariable.historyTab.setHeaderVisible(true);
			// 设置网格线可见
			StaticVariable.historyTab.setLinesVisible(true);
			//为socreTableComp面板设置一个控制布局的对象GridTab2，设置该面板在水平、垂直两个方向全充满
			GridData gridTab3 = new GridData(GridData.FILL_BOTH);
			//设置socreTableComp面板垂直占两列
			gridTab3.horizontalSpan = 3;
			tableComp.setLayoutData(gridTab3);

			//定义一个Label控件
			Label hisTable = new Label(historyComp, SWT.NONE);
			hisTable.setText("选择历史表：");
			//定义一个下拉框，用来选择历史表
			StaticVariable.historyCombo = new Combo(historyComp, SWT.READ_ONLY);
			//为下拉框定义一个控制布局对象，使下拉框水平充满
			GridData moduleGrid = new GridData(GridData.FILL_HORIZONTAL);
			StaticVariable.historyCombo.setLayoutData(moduleGrid);
			//为下拉框添加监听事件
			StaticVariable.historyCombo.addSelectionListener(new HistoryComboListener(StaticVariable.historyCombo));
//			StaticVariable.historyCharBtn = new Button(historyComp, SWT.NONE);
//			StaticVariable.historyCharBtn.setText("图表数据");
		}
		
		// 定义第四个选项卡
//		final TabItem askQuestion = new TabItem(tabFolder, SWT.NONE);
		StaticVariable.askQuestion = new TabItem(tabFolder, SWT.NONE);
		StaticVariable.askQuestion.setText("提问");
		
		{
			//为该选项卡添加一个面板
			Composite questionComp = new Composite(tabFolder, SWT.BORDER);
			questionComp.setLayout(new FillLayout());
			StaticVariable.askQuestion.setControl(questionComp);
			//设置该面板的布局为网格布局，设置成两列
			questionComp.setLayout(new FillLayout());
			//定义一张表格
			StaticVariable.askQuestions = new Table(questionComp, SWT.MULTI);
			// 设置表头可见
			StaticVariable.askQuestions.setHeaderVisible(true);
			// 设置网格线可见
			StaticVariable.askQuestions.setLinesVisible(true);
			// 定义表中的列
//			TableColumn nameColumn = new TableColumn(StaticVariable.askQuestions, SWT.NONE);
//			nameColumn.setText("学生");
//			nameColumn.setWidth(150);
			TableColumn questionColumn = new TableColumn(StaticVariable.askQuestions, SWT.NONE);
			questionColumn.setText("问题");
			questionColumn.setWidth(textView.getSize().x);
			StaticVariable.askQuestions.addMouseListener(new AskQuestionTableListener(parent));
		}
		
		// 为选项卡添加监听事件
		tabFolder.addSelectionListener(new TabFordlerListener(tabFolder, StaticVariable.historyCombo));
		
		Composite statusBar = new Composite(mainFrame, SWT.BORDER);
		statusBar.setBounds(0, (int) (windowHeight * 0.87), windowWidth, 33);

		// 给所有按钮添加监听器
//		first.addSelectionListener(new ButtonDownListener("first"));
//		previous.addSelectionListener(new ButtonDownListener("previous"));
//		next.addSelectionListener(new ButtonDownListener("next"));
//		last.addSelectionListener(new ButtonDownListener("last"));
//		StaticVariable.refresh.addSelectionListener(new ButtonDownListener("refresh"));
		StaticVariable.scoreChartBtn.addSelectionListener(new ScoreChartBtnListener(parent));
//		StaticVariable.historyCharBtn.addSelectionListener(new HistoryCharBtnListener(parent));

		parent.getShell().addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent arg0) {
				int windowWidth = parent.getShell().getSize().x;
				int windowHeight = parent.getShell().getSize().y;
				onlineView.setBounds(0, 0, (int) (windowWidth * 0.3), (int) (windowHeight - 130));
				// System.out.println(windowHeight + ", " +(int)(windowHeight *
				// 0.87));
				createRoom.setBounds(0, 0, onlineView.getSize().x, 33);
				onlineNum.setBounds(0, 33, onlineView.getSize().x, 33);
				onlineUser.setBounds(0, 66, onlineView.getSize().x, onlineView.getSize().y - 70);
				textView.setBounds((int) (windowWidth * 0.3), 0, (int) (windowWidth * 0.69), onlineView.getSize().y);
				// System.out.println(parent.getShell().getSize().x);
				statusBar.setBounds(0, windowHeight - 33, windowWidth, 33);
			}

			@Override
			public void controlMoved(ControlEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		return mainFrame;
	}

	// 点击关闭窗口按钮时触发的动作
	@Override
	protected void handleShellCloseEvent() { 
		MessageBox messagebox = new MessageBox(getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);  
	    messagebox.setMessage("您确定要退出吗?");  
	    int message = messagebox.open();  
	    if (message == SWT.YES) {
	    	if (StaticVariable.correct.size() > 0) {
				int index = StaticVariable.questionSelect.getSelectionIndex();
//				String question = StaticVariable.questionsMap.get(Integer.toString(index));
				String question = StaticVariable.questionsList.get(index - 1);
				// 分割字符串，将题目答案等分离
				String[] strs = question.split("#\\^");
				if ((strs.length - 4) > 0) {
					// (strs.length - 4) > 0表示有多个答案
					StringBuffer answer = new StringBuffer();	// 存储问题的答案
					StringBuffer corStr = new StringBuffer();	// 存储回答正确的个数，多个答案使用空格分隔
					StringBuffer errStr = new StringBuffer();	// 存储回答错误的个数，多个答案使用空格分隔
					StringBuffer unResStr = new StringBuffer();	// 存储未回答的个数，多个答案使用空格分隔
					// 根据正确的数量和错误的数量计算出未回答的数量
					for (int i = 0; i < StaticVariable.correct.size(); i++) {
						int unResponse = StaticVariable.unResponse.get(i).intValue();
						int correct = StaticVariable.correct.get(i).intValue();
						int error = StaticVariable.error.get(i).intValue();
						unResponse = StaticVariable.users.size() - correct - error;
						StaticVariable.unResponse.set(i, new Integer(unResponse));
						if (i == 0) {
							answer.append(strs[i + 3]);
							corStr.append(correct);
							errStr.append(error);
							unResStr.append(unResponse);
						} else {
							answer.append(" " + strs[i + 3]);
							corStr.append(" " + correct);
							errStr.append(" " + error);
							unResStr.append(" " + unResponse);
						}
					}
					String countStr = answer.toString() + "," + corStr.toString() + "," + errStr.toString() + "," + unResStr.toString();
					StaticVariable.answers.put("统计", countStr);
					System.out.println(countStr);
				} else {
					int unResponse = StaticVariable.unResponse.get(0).intValue();
					int correct = StaticVariable.correct.get(0).intValue();
					int error = StaticVariable.error.get(0).intValue();
					unResponse = StaticVariable.users.size() - correct - error;
					StaticVariable.unResponse.set(0, new Integer(unResponse));
					String countStr = Integer.toString(correct) + "," + Integer.toString(error) + "," + Integer.toString(unResponse);
					StaticVariable.answers.put("统计", countStr);
				}
				System.out.println(StaticVariable.correct.size());
				dbService.addRecord(StaticVariable.tableName.toString(), StaticVariable.answers, index);
			}
	        super.handleShellCloseEvent();  
	    }
		
	}
	protected MenuManager createMenuManager() {
		MenuManager menuBar = new MenuManager();
		MenuManager fileMenu = new MenuManager(" 文件(&F)");
		MenuManager editorMenu = new MenuManager(" 编辑(&E)");
		MenuManager helpMenu = new MenuManager(" 帮助(&H)");
		// 在文件菜单项添加下拉菜单
		fileMenu.add(newCreate);
		fileMenu.add(openFile);
		// 添加分割线
		fileMenu.add(new Separator());
//		fileMenu.add(saveFile);
//		fileMenu.add(saveAsFile);
//		fileMenu.add(unsubscribe);
		// 添加分割线
		fileMenu.add(new Separator());
		fileMenu.add(exit);
		// 在编辑菜单下添加下拉菜单
		editorMenu.add(copyFile);
		editorMenu.add(pasteFile);
		editorMenu.add(cutFile);
		// 添加分割线
		editorMenu.add(new Separator());
		editorMenu.add(setFont);
		editorMenu.add(setColor);
		// 添加分割线
		editorMenu.add(new Separator());
		editorMenu.add(selectAll);
		editorMenu.add(formate);
		helpMenu.add(about);
		// 在 menuBar 上添加文件菜单、编辑菜单和帮助菜单
		menuBar.add(fileMenu);
		menuBar.add(editorMenu);
		menuBar.add(helpMenu);
		return menuBar;
	}

	// 在工具栏上添加工具栏按钮
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		// gtoolBarManager.add(new NewCreateAction());
		toolBarManager.add(new OpenFileAction());
		toolBarManager.add(new CreateXmlFileAction());
		toolBarManager.add(new QuestionManagerAction());
		toolBarManager.add(new NewCreateAction());
		// toolBarManager.add(new SaveFileAction());
		// toolBarManager.add(new Separator());
		// toolBarManager.add(new CopyFileAction());
		// toolBarManager.add(new PasteFileAction());
		// toolBarManager.add(new CutFileAction());
		// toolBarManager.add(new Separator());
		// toolBarManager.add(new BlodAction());
		// toolBarManager.add(new ItalicAction());
		// toolBarManager.add(new UnderlineAction());
		return toolBarManager;
	}

	/**
	 * 新建教室，菜单栏
	 * @author wanli
	 *
	 */
	class NewCreate extends Action {
		public NewCreate() {
			super("NewCreateAction@Ctrl+N", Action.AS_PUSH_BUTTON);
			setText(" 新建教室");
			try {
				// 载入图像
				ImageDescriptor icon = ImageDescriptor.createFromURL(new URL("file:image/createclass.png"));
				setImageDescriptor(icon);
			} catch (MalformedURLException e) {
				System.err.println(e.getMessage());
			}
		}

		public void run() {
			new CreateClassListener(StaticVariable.parent);
			createClass();
		}
	}

	/**
	 * 新建教室，工具栏
	 * @author wanli
	 *
	 */
	class NewCreateAction extends Action {
		public NewCreateAction() {
			super("NewCreateAction@Ctrl+N", Action.AS_PUSH_BUTTON);
			setText(" 新建教室");
			try {
				// 载入图像
				ImageDescriptor icon = ImageDescriptor.createFromURL(new URL("file:image/create_class.png"));
				setImageDescriptor(icon);
			} catch (MalformedURLException e) {
				System.err.println(e.getMessage());
			}
		}

		public void run() {
			new CreateClassListener(StaticVariable.parent);
			createClass();
		}
	}

	/**
	 * 打开文件，菜单栏
	 * @author wanli
	 *
	 */
	class OpenFile extends Action {
		public OpenFile() {
			super("OpenFileAction@Ctrl+O", Action.AS_PUSH_BUTTON);
			setText(" 打开");
			try {
				// 载入图像
				ImageDescriptor icon = ImageDescriptor.createFromURL(new URL("file:image/folder.png"));
				setImageDescriptor(icon);
			} catch (MalformedURLException e) {
				System.err.println(e.getMessage());
			}
		}

		public void run() {
			// 在打开新的文件之前，判断是否保存当前文件
			if (judgeTextSave())
				OpenTextFile();
		}
	}

	/**
	 * 打开文件，工具栏
	 * @author wanli
	 *
	 */
	class OpenFileAction extends Action {
		public OpenFileAction() {
			super("OpenFileAction@Ctrl+O", Action.AS_PUSH_BUTTON);
			setText(" 打开");
			try {
				// 载入图像
				ImageDescriptor icon = ImageDescriptor.createFromURL(new URL("file:image/open_file.png"));
				setImageDescriptor(icon);
			} catch (MalformedURLException e) {
				System.err.println(e.getMessage());
			}
		}

		public void run() {
			// 在打开新的文件之前，判断是否保存当前文件
			if (judgeTextSave())
				OpenTextFile();
		}
	}
	
	/**
	 * 课前备题，工具栏
	 * @author wanli
	 *
	 */
	class CreateXmlFileAction extends Action {
		public CreateXmlFileAction() {
			super("CreateXmlFileAction@Ctrl+Shift+C", Action.AS_PUSH_BUTTON);
			setText("课前备题");
			try {
				// 载入图像
				ImageDescriptor icon = ImageDescriptor.createFromURL(new URL("file:image/before_class.png"));
				setImageDescriptor(icon);
			} catch (MalformedURLException e) {
				System.err.println(e.getMessage());
			}
		}
		
		public void run() {
			new PrepareLessons(StaticVariable.parent);
		}
	}
	
	class QuestionManagerAction extends Action {
		public QuestionManagerAction() {
			super("QuestionManagerAction@Ctrl+Shift+M", Action.AS_PUSH_BUTTON);
			setText("问题管理");
			try {
				// 载入图像
				ImageDescriptor icon = ImageDescriptor.createFromURL(new URL("file:image/manager.png"));
				setImageDescriptor(icon);
			} catch (MalformedURLException e) {
				System.err.println(e.getMessage());
			}
		}
		public void run() {
//			new PrepareLessons(StaticVariable.parent);
			new QuestionManagerShell(StaticVariable.parent);
		}
	}

	/**
	 * 保存文件
	 * @author wanli
	 *
	 */
	class SaveFileAction extends Action {
		public SaveFileAction() {
			super("SaveFileAction@Ctrl+S", Action.AS_PUSH_BUTTON);
			setText(" 保存");
			try {
				// 载入图像
				ImageDescriptor icon = ImageDescriptor.createFromURL(new URL("file:image/save.png"));
				setImageDescriptor(icon);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		public void run() {
			saveTextFile();
		}
	}

	/**
	 * 文件另存为
	 * @author wanli
	 *
	 */
	class SaveAsFileAction extends Action {
		public SaveAsFileAction() {
			super(" 另存为@Alt+A", Action.AS_PUSH_BUTTON);
			// 载入图像
			ImageDescriptor icon;
			try {
				icon = ImageDescriptor.createFromURL(new URL("file:image/saveas.png"));
				setImageDescriptor(icon);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			saveFileAs();
		}
	}
	
	/**
	 * 注销账号
	 * @author wanli
	 *
	 */
	class UnsubscribeAction extends Action {
		public UnsubscribeAction() {
			super(" 注销账号@Ctrl+U", Action.AS_PUSH_BUTTON);
			// 载入图像
			ImageDescriptor icon;
			try {
				icon = ImageDescriptor.createFromURL(new URL("file:image/unsubscribe.png"));
				setImageDescriptor(icon);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			MessageBox messageBox = new MessageBox(shell, SWT.YES | SWT.NO);
			messageBox.setText("警告");
			messageBox.setMessage("确定要注销当前账号吗？");
			if (messageBox.open() == SWT.YES) {
//				Display.getCurrent().dispose();
				shell.dispose();
				
			}
		}
	}

	/**
	 * 退出程序
	 * @author wanli
	 *
	 */
	class ExitAction extends Action {
		public ExitAction() {
			super(" 退出@Ctrl+E", Action.AS_PUSH_BUTTON);
			// 载入图像
			ImageDescriptor icon;
			try {
				icon = ImageDescriptor.createFromURL(new URL("file:image/close.png"));
				setImageDescriptor(icon);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			getShell().dispose();
		}
	}

	/**
	 * 复制
	 * @author wanli
	 *
	 */
	class CopyFileAction extends Action {
		public CopyFileAction() {
			super("CopyFileAction@Ctrl+C", Action.AS_PUSH_BUTTON);
			setText(" 复制");
			// 载入图像
			ImageDescriptor icon;
			try {
				icon = ImageDescriptor.createFromURL(new URL("file:image/copy.png"));
				setImageDescriptor(icon);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			StaticVariable.text.copy();
		}
	}

	/**
	 * 粘贴
	 * @author wanli
	 *
	 */
	class PasteFileAction extends Action {
		public PasteFileAction() {
			super("PasteFileAction@Ctrl+V", Action.AS_PUSH_BUTTON);
			setText(" 粘贴");
			// 载入图像
			ImageDescriptor icon;
			try {
				icon = ImageDescriptor.createFromURL(new URL("file:image/paste.png"));
				setImageDescriptor(icon);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			StaticVariable.text.paste();
		}
	}

	/**
	 * 剪切
	 * @author wanli
	 *
	 */
	class CutFileAction extends Action {
		public CutFileAction() {
			super("CutFileAction @Ctrl+X", Action.AS_PUSH_BUTTON);
			setText(" 剪切");
			// 载入图像
			ImageDescriptor icon;
			try {
				icon = ImageDescriptor.createFromURL(new URL("file:image/cut.png"));
				setImageDescriptor(icon);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			StaticVariable.text.cut();
		}
	}

	/**
	 * 设置字体
	 * @author wanli
	 *
	 */
	class SetFontAction extends Action {
		public SetFontAction() {
			super(" 设置字体@Alt+F", Action.AS_PUSH_BUTTON);
			// 载入图像
			ImageDescriptor icon;
			try {
				icon = ImageDescriptor.createFromURL(new URL("file:image/font.png"));
				setImageDescriptor(icon);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			FontDialog fontDialog = new FontDialog(getShell());
			fontDialog.setFontList((StaticVariable.text.getFont()).getFontData());
			FontData fontData = fontDialog.open();
//			if (fontData != null) {
//				if (font != null) {
//					font.dispose();
//				}
//			}
			font = new Font(getShell().getDisplay(), fontData);
			StaticVariable.text.setFont(font);
		}
	}

	/**
	 * 设置字体颜色
	 * @author wanli
	 *
	 */
	class SetColorAction extends Action {
		public SetColorAction() {
			super(" 设置颜色@Alt+C", Action.AS_PUSH_BUTTON);
			// 载入图像
			ImageDescriptor icon;
			try {
				icon = ImageDescriptor.createFromURL(new URL("file:image/font_color.png"));
				setImageDescriptor(icon);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			// 定义颜色选择对话框
			ColorDialog dlg = new ColorDialog(getShell());
			// 打开对话框
			RGB rgb = dlg.open();
			if (rgb != null) {
				// 定义 color 对象
				StaticVariable.color = new Color(getShell().getDisplay(), rgb);
				// 定义 point 对象，获取选择范围。
				Point point = StaticVariable.text.getSelectionRange();
				for (int i = point.x; i < point.x + point.y; i++) {
					// 获得选中的字体样式和范围
					StaticVariable.range = StaticVariable.text.getStyleRangeAtOffset(i);
					// 如果字体设置了其他样式( 如加粗、斜体、加下划线)
					if (StaticVariable.range != null) {
						/**
						 * 设置一个与原来 StyleRange 的值相同的 StyleRange
						 */
						StaticVariable.style = (StyleRange) StaticVariable.range.clone();
						StaticVariable.style.start = i;
						StaticVariable.style.length = 1;
						// 设置前景颜色
						StaticVariable.style.foreground = StaticVariable.color;
					} else {

						StaticVariable.style = new StyleRange(i, 1, StaticVariable.color, null, SWT.NORMAL);
					}
					StaticVariable.text.setStyleRange(StaticVariable.style);
				}
			}
		}
	}

	/**
	 * 全选
	 * @author wanli
	 *
	 */
	class SelectAllAction extends Action {
		public SelectAllAction() {
			super(" 全选@Ctrl+A", Action.AS_PUSH_BUTTON);
			// 载入图像
			ImageDescriptor icon;
			try {
				icon = ImageDescriptor.createFromURL(new URL("file:image/select_all.png"));
				setImageDescriptor(icon);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			StaticVariable.text.selectAll();
		}
	}

	/**
	 * 格式化
	 * @author wanli
	 *
	 */
	class FormateAction extends Action {
		public FormateAction() {
			super(" 格式化@Ctrl+W", Action.AS_CHECK_BOX);
		}

		public void run() {
			StaticVariable.text.setWordWrap(isChecked());
		}
	}

	/**
	 * 关于
	 * @author wanli
	 *
	 */
	class AboutAction extends Action {
		public AboutAction() {
			super(" 关于@Ctrl+H", Action.AS_PUSH_BUTTON);
		}

		public void run() {
			MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
			messageBox.setMessage(" ClassForEvery 2.0 版本！ ");
			messageBox.open();
		}
	}

	/**
	 * 加粗
	 * @author wanli
	 *
	 */
	class BlodAction extends Action {
		public BlodAction() {
			setText(" 加粗");
		}

		public void run() {
			Point point = StaticVariable.text.getSelectionRange();
			for (int i = point.x; i < point.x + point.y; i++) {
				StyleRange range = StaticVariable.text.getStyleRangeAtOffset(i);
				if (range != null) {
					StaticVariable.style = (StyleRange) range.clone();
					StaticVariable.style.start = i;
					StaticVariable.style.length = 1;
				} else {
					StaticVariable.style = new StyleRange(i, 1, null, null, SWT.NORMAL);
				}
				// 加粗字体
				StaticVariable.style.fontStyle ^= SWT.BOLD;
				StaticVariable.text.setStyleRange(StaticVariable.style);
			}
		}
	}

	/**
	 * 斜体
	 * @author wanli
	 *
	 */
	class ItalicAction extends Action {
		public ItalicAction() {
			setText(" 斜体");
		}

		public void run() {
			Point point = StaticVariable.text.getSelectionRange();
			for (int i = point.x; i < point.x + point.y; i++) {
				StaticVariable.range = StaticVariable.text.getStyleRangeAtOffset(i);
				if (StaticVariable.range != null) {
					StaticVariable.style = (StyleRange) StaticVariable.range.clone();
					StaticVariable.style.start = i;
					StaticVariable.style.length = 1;
				} else {
					StaticVariable.style = new StyleRange(i, 1, null, null, SWT.NORMAL);
				}
				// 设置为斜体
				StaticVariable.style.fontStyle ^= SWT.ITALIC;
				StaticVariable.text.setStyleRange(StaticVariable.style);
			}
		}
	}

	/**
	 * 下划线
	 * @author wanli
	 *
	 */
	class UnderlineAction extends Action {
		public UnderlineAction() {
			setText(" 下划线");
		}

		public void run() {
			Point point = StaticVariable.text.getSelectionRange();
			for (int i = point.x; i < point.x + point.y; i++) {
				StaticVariable.range = StaticVariable.text.getStyleRangeAtOffset(i);
				if (StaticVariable.range != null) {
					StaticVariable.style = (StyleRange) StaticVariable.range.clone();
					StaticVariable.style.start = i;
					StaticVariable.style.length = 1;
				} else {
					StaticVariable.style = new StyleRange(i, 1, null, null, SWT.NORMAL);
				}
				// 设置下划线
				StaticVariable.style.underline = !StaticVariable.style.underline;
				StaticVariable.text.setStyleRange(StaticVariable.style);
			}
		}
	}

	boolean judgeTextSave() {
		if (!changes)
			return true;
		MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.YES | SWT.NO | SWT.CANCEL);
		messageBox.setMessage(" 是否保存对文件的更改？ ");
		messageBox.setText(" ClassForEveryone V2.0");
		int message = messageBox.open();
		if (message == SWT.YES) {
			return saveTextFile();
		} else if (message == SWT.NO) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 打开文件对话框
	 * @return
	 */
	boolean OpenTextFile() {
		if (StaticVariable.rooms.size() > 0) {
			StaticVariable.tableName.setLength(0);
			StaticVariable.index = 1;
			// 定义对话框，类型为打开型
			FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
			// 设置对话框打开的限定类型
			dialog.setFilterExtensions(new String[] { "*.xml"});
			// 打开对话框，并返回打开文件的路径
			String openFile = dialog.open();
			if (openFile == null) {
				return false;
			}
			// 打开指定的文件
			file = new File(openFile);
			new XmlToJavaBean(file);
			shell.setText(APPNAME + "-" + file);
			String fileName = file.getName();
//			StaticVariable.tableName = StaticVariable.classOrSepcialtyName + fileName.substring(0, fileName.indexOf("."));
			StaticVariable.tableName.append(StaticVariable.classOrSepcialtyName)
									.append("_")
									.append(fileName.substring(0, fileName.indexOf(".")));
	//		dbService.createTable(StaticVariable.questionsMap.size(), StaticVariable.tableName);
			dbService.createTable(StaticVariable.allQuestionList, StaticVariable.tableName.toString());
//			System.out.println(StaticVariable.tableName);
			return false;
		} else {
			MessageBox messageBox = new MessageBox(shell, SWT.YES);
			messageBox.setText("警告");
			messageBox.setMessage("还未创建教室，无法打开文件！！！");
			messageBox.open();
			return false;
		}
	}

	/**
	 * 保存文件对话框
	 * @return
	 */
	boolean saveTextFile() {
		if (file == null) {
			// 定义文件选择对话框，类型为保存型
			FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
			dialog.setText(" 保存");
			// 设置对话框保存的限定类型
			dialog.setFilterExtensions(new String[] { " *.txt", "*.doc", "*.docx"});
			// 打开对话框，并返回保存文件的路径
			String saveFile = dialog.open();
			if (saveFile == null) {
				return false;
			}
			file = new File(saveFile);
		}
		try {
			FileWriter writer = new FileWriter(file);
			StringBuffer fileString = new StringBuffer();
			StaticVariable.questions[StaticVariable.index] = StaticVariable.text.getText();
			for (int i = 1; i <= StaticVariable.questions.length - 1; i++) {
				fileString.append("#\\^" + StaticVariable.questions[i] + "\r\n");
			}
			writer.write(fileString.toString());
			writer.close();
			changes = false;
			return true;
		} catch (IOException e) {
		}
		return false;
	}

	/**
	 * 判断是否另存为
	 * @return
	 */
	boolean saveFileAs() {
		SafeSaveDialog dlg = new SafeSaveDialog(getShell());
		String temp = dlg.open();
		if (temp == null) {
			return false;
		}
		file = new File(temp);
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(StaticVariable.text.getText());
			writer.close();
		} catch (IOException e) {
		}
		return false;
	}

	/**
	 * 另存为对话框
	 * @author wanli
	 *
	 */
	class SafeSaveDialog {
		private FileDialog dlg;

		public SafeSaveDialog(Shell shell) {
			dlg = new FileDialog(shell, SWT.SAVE);
			dlg.setFilterExtensions(new String[] { "*.txt", "*.doc", "*.docx"});
		}

		public String open() {
			String fileName = null;
			boolean done = false;
			while (!done) {
				// 打开另存为对话框，并返回保存路径
				fileName = dlg.open();
				if (fileName == null) {
					done = true;
				} else {
					// 判断保存的文件是否已经存在
					File file = new File(fileName);
					if (file.exists()) {
						// 若文件存在，则弹出提示性的对话框
						MessageBox mb = new MessageBox(dlg.getParent(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
						// 提示性的信息
						mb.setMessage(fileName + " 已经存在，是否将该文件替换?");
						/**
						 * 单击"yes" 按钮将磁盘上的文件替换 否则重新填写文件名
						 */
						done = mb.open() == SWT.YES;
					} else {
						done = true;
					}
				}
			}
			return fileName;
		}
	}

	/**
	 * 创建教室，生成树
	 */
	public void createClass() {
		if (StaticVariable.className != null && StaticVariable.className != "") {
			TreeItem classroom = new TreeItem(tree, SWT.NONE);
			classroom.setText(StaticVariable.className);
			StaticVariable.rooms.put(StaticVariable.className, classroom);
			StaticVariable.className = "";
		}
	}
}
