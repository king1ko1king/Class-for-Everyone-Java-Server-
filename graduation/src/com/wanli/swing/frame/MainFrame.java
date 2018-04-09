package com.wanli.swing.frame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.wanli.classforereryone.server.MyServer;
import com.wanli.classforereryone.server.ServerThread;
import com.wanli.swing.frame.listener.ButtonDownListener;
import com.wanli.swing.frame.listener.CreateClassListener;
import com.wanli.swing.frame.listener.HistoryCharBtnListener;
import com.wanli.swing.frame.listener.HistoryComboListener;
import com.wanli.swing.frame.listener.OnlineTreeListener;
import com.wanli.swing.frame.listener.ScoreChartBtnListener;
import com.wanli.swing.frame.listener.TabFordlerListener;
import com.wanli.swing.service.DBService;
import com.wanli.thread.ListeningSocket;
import com.wanli.utils.StaticVariable;

public class MainFrame extends ApplicationWindow {
	private static String APPNAME = "Class For Everyone";	// ��ǰ���������
	private static String welcome = "��ӭ����";				// ��ʾ��ӭ��ʾ
	private Shell shell;									// �����ڵ�shell��
	private Composite parent;								// �������Composite��
	private Action newCreate;								// �½�����
	private Action openFile;								// ���ļ�
	private Action saveFile;								// �����ļ�
	private Action saveAsFile;								// ���Ϊ
	private Action exit;									// �˳�����
	private Action copyFile;								// ����
	private Action pasteFile;								// ճ��
	private Action cutFile;									// ����
	private Action setFont;									// ��������
	private Action setColor;								// ������ɫ
	private Action selectAll;								// ȫѡ
	private Action formate;									// ��ʽ
	private Action about;									// ����
	private Tree tree;										// ��ʾ�����û��б�
	private Font font;										// ����
	private File file;										// �ļ�
	private Color color;									// ��ɫ
	private StyleRange style, range;						// ���
	private TabFolder tabFolder;							// ѡ�
	private Button first;									// ��ת����һ��
	private Button previous;								// ��һ��
	private Button next;									// ��һ��
	private Button last;									// ���һ��
	private String userName;								// �û���
	boolean changes;										// �ĵ��Ƿ�ı�
	private DBService dbService;							// ��������dao��ҵ��
	private Runtime runtime;								// ��ȡ��ǰ���������ʱ
	private Process process;								// �����洢���õ��ⲿ����

	public MainFrame(String userName) {
		// ���𴰿�
		super(null);
		//����һ���̣߳����������ͻ��˵�����
		new Thread(new MyServer()).start();
		//����һ���̣߳������ͻ��˵����ӣ��������пͻ��������Ͼ���ʾ�ͻ��˵��������
		new Thread(new ListeningSocket()).start();
		this.userName = userName;// ��ȡ�û���
		newCreate = new NewCreate();
		openFile = new OpenFile();
		saveFile = new SaveFileAction();
		saveAsFile = new SaveAsFileAction();
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
		addMenuBar();// ��Ӳ˵���
		addToolBar(SWT.FLAT);// ��ӹ�����
	}

	public void run() {
		setBlockOnOpen(true);
		open();
		Display.getCurrent().dispose();
	}

	@Override
	public Control createContents(Composite parent) {
		// ���ô����С
		Rectangle bounds = parent.getShell().getDisplay().getPrimaryMonitor().getBounds();
		int windowWidth = bounds.width;
		int windowHeight = (int) (bounds.height * 0.98);
		parent.getShell().setSize(windowWidth, (int) (bounds.height * 0.98));
		// ���ô������
		parent.getShell().setText(APPNAME);
		shell = parent.getShell();
		this.parent = parent;
		// ���������
		Composite mainFrame = new Composite(parent, SWT.NONE);
		mainFrame.setLayout(null);
		// ������ʾ����������
		Composite onlineView = new Composite(mainFrame, SWT.BORDER);
		onlineView.setBounds(0, 0, (int) (windowWidth * 0.3), (int) (windowHeight * 0.87));
		// ���ô����������
		Composite createRoom = new Composite(onlineView, SWT.BORDER);
		createRoom.setBounds(0, 0, onlineView.getSize().x, (int) (onlineView.getSize().y * 0.04));
		createRoom.setLayout(new FillLayout());
		
		Button button = new Button(createRoom, SWT.NONE);
		FormData fd_button = new FormData();
		fd_button.left = new FormAttachment(0);
		button.setLayoutData(fd_button);
		button.setText("\u5F00\u542F\u9738\u5C4F");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ServerThread.sendToClient("��������");
			}
		});
		Button screenshot = new Button(createRoom, SWT.NONE);
		screenshot.setText("��ѧ��ͼ");
		screenshot.setLayoutData(fd_button);
		screenshot.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				runtime = Runtime.getRuntime();		// ��ȡ��ǰ���������ʱ
				try {
					// ִ��windowsϵͳ�Դ��Ľ�ͼ���߳��򣬲�ͨ��process����ý���
					process = runtime.exec("C:\\Windows\\System32\\SnippingTool.exe");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		// ������ʾ�����������
		Composite onlineNum = new Composite(onlineView, SWT.BORDER);
		onlineNum.setBounds(0, (int) (onlineView.getSize().y * 0.04), onlineView.getSize().x,
				(int) (onlineView.getSize().y * 0.04));

		// ��ʾ��¼��Ϣ
		Label welcome = new Label(onlineNum, SWT.NONE);
		welcome.setBounds(0, 0, 268, 32);
		welcome.setText(MainFrame.welcome + this.userName);

		// ��ʾ��������
		StaticVariable.onlining = new Label(onlineNum, SWT.NONE);
		StaticVariable.onlining.setBounds(269, 0, 303, 32);
		StaticVariable.onlining.setText(StaticVariable.onlineNumsStr + StaticVariable.onlineNumsInt);

		// ������ʾ�����û��б����
		Composite onlineUser = new Composite(onlineView, SWT.BORDER);
		onlineUser.setBounds(0, onlineNum.getSize().y + createRoom.getSize().y, onlineView.getSize().x,
				(int) (onlineView.getSize().y * 0.92));
		onlineUser.setLayout(new FillLayout(SWT.HORIZONTAL));

		// ��������ʽ��ʾ�����û��б�
		tree = new Tree(onlineUser, SWT.BORDER);
		tree.addMouseListener(new OnlineTreeListener(tree, parent));
		
		// ������ʾ��Ŀ���ɼ�����ʷ��¼�ȵ����
		Composite textView = new Composite(mainFrame, SWT.BORDER);
		textView.setBounds((int) (windowWidth * 0.3), 0, (int) (windowWidth * 0.69), onlineView.getSize().y);
		textView.setLayout(new GridLayout(6, false));

		// ��Ŀ���ɼ�����ʷ��¼ѡ�
		tabFolder = new TabFolder(textView, SWT.NONE);
		GridData gridTab = new GridData(GridData.FILL_BOTH);
		gridTab.horizontalSpan = 6;
		tabFolder.setLayoutData(gridTab);

		// �����һ��ѡ�
		final TabItem question = new TabItem(tabFolder, SWT.NONE);
		question.setText("��Ŀ");
		{
			//Ϊ��ѡ�����һ�����
			Composite questionComp = new Composite(tabFolder, SWT.BORDER);
			question.setControl(questionComp);
			//���ø����Ϊ����ʽ���֣�������Ϊ4��
			questionComp.setLayout(new GridLayout(4, false));
			//���һ����壬��������StyledText�ؼ�
			Composite textComp = new Composite(questionComp, SWT.BORDER);
			//����textComp���Ĳ���Ϊ����ʽ����
			textComp.setLayout(new FillLayout());
			StaticVariable.text = new StyledText(textComp, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
			StaticVariable.text.setRightMargin(40);
			StaticVariable.text.setLeftMargin(40);
			StaticVariable.text.setFont(new Font(parent.getDisplay(), "Arial", 20, SWT.NONE));
			StaticVariable.text.setBounds(0, 0, textComp.getSize().x, questionComp.getSize().y);
			//ΪsocreTableComp�������һ�����Ʋ��ֵĶ���GridTab1�����ø������ˮƽ����ֱ��������ȫ����
			GridData gridTab1 = new GridData(GridData.FILL_BOTH);
			//����socreTableComp��崹ֱռ4��
			gridTab1.horizontalSpan = 4;
			textComp.setLayoutData(gridTab1);
			// ���������ť
			first = new Button(questionComp, SWT.NONE);
			first.setText("����");
			first.setEnabled(false);

			previous = new Button(questionComp, SWT.NONE);
			previous.setText("��һ��");
			previous.setEnabled(false);

			next = new Button(questionComp, SWT.NONE);
			next.setText("��һ��");
			next.setEnabled(false);

			last = new Button(questionComp, SWT.NONE);
			last.setText("ĩ��");
			last.setEnabled(false);
		}

		// ����ڶ���ѡ�
		final TabItem score = new TabItem(tabFolder, SWT.NONE);
		score.setText("�ɼ�");
		{
			//Ϊ��ѡ����һ�����
			Composite scoreComp = new Composite(tabFolder, SWT.BORDER);
			score.setControl(scoreComp);
			//���ø����Ĳ���Ϊ���񲼾֣����ó�һ��
			scoreComp.setLayout(new GridLayout(2, false));
			//���һ����壬�������ñ��
			Composite scoreTableComp = new Composite(scoreComp, SWT.BORDER);
			//����socreTableComp���Ϊ����ʽ����
			scoreTableComp.setLayout(new FillLayout());
			// ������
			StaticVariable.scoreTab = new Table(scoreTableComp, SWT.MULTI);
			// ���ñ�ͷ�ɼ�
			StaticVariable.scoreTab.setHeaderVisible(true);
			// ���������߿ɼ�
			StaticVariable.scoreTab.setLinesVisible(true);
			//ΪsocreTableComp�������һ�����Ʋ��ֵĶ���GridTab2�����ø������ˮƽ����ֱ��������ȫ����
			GridData gridTab2 = new GridData(GridData.FILL_BOTH);
			//����socreTableComp��崹ֱռ����
			gridTab2.horizontalSpan = 2;
			scoreTableComp.setLayoutData(gridTab2);
			//����һ��ˢ�±��İ�ť
			StaticVariable.refresh = new Button(scoreComp, SWT.NONE);
			StaticVariable.refresh.setText("ˢ��");
			StaticVariable.scoreChartBtn = new Button(scoreComp, SWT.NONE);
			StaticVariable.scoreChartBtn.setText("ͼ������");
		}

		// ���������ѡ�
		final TabItem history = new TabItem(tabFolder, SWT.NONE);
		history.setText("��ʷ��¼");
		{
			//Ϊ��ѡ����һ�����
			Composite historyComp = new Composite(tabFolder, SWT.BORDER);
			history.setControl(historyComp);
			//���ø����Ĳ���Ϊ���񲼾֣����ó�����
			historyComp.setLayout(new GridLayout(3, false));
			//���һ����壬�������ñ��
			Composite tableComp = new Composite(historyComp, SWT.BORDER);
			//����tableComp���Ĳ���Ϊ����ʽ����
			tableComp.setLayout(new FillLayout());
			//����һ�ű��
			StaticVariable.historyTab = new Table(tableComp, SWT.MULTI);
			// ���ñ�ͷ�ɼ�
			StaticVariable.historyTab.setHeaderVisible(true);
			// ���������߿ɼ�
			StaticVariable.historyTab.setLinesVisible(true);
			//ΪsocreTableComp�������һ�����Ʋ��ֵĶ���GridTab2�����ø������ˮƽ����ֱ��������ȫ����
			GridData gridTab3 = new GridData(GridData.FILL_BOTH);
			//����socreTableComp��崹ֱռ����
			gridTab3.horizontalSpan = 3;
			tableComp.setLayoutData(gridTab3);

			//����һ��Label�ؼ�
			Label hisTable = new Label(historyComp, SWT.NONE);
			hisTable.setText("ѡ����ʷ��");
			//����һ������������ѡ����ʷ��
			StaticVariable.historyCombo = new Combo(historyComp, SWT.READ_ONLY);
			//Ϊ��������һ�����Ʋ��ֶ���ʹ������ˮƽ����
			GridData moduleGrid = new GridData(GridData.FILL_HORIZONTAL);
			StaticVariable.historyCombo.setLayoutData(moduleGrid);
			//Ϊ��������Ӽ����¼�
			StaticVariable.historyCombo.addSelectionListener(new HistoryComboListener(StaticVariable.historyCombo));
			StaticVariable.historyCharBtn = new Button(historyComp, SWT.NONE);
			StaticVariable.historyCharBtn.setText("ͼ������");
		}

		// Ϊѡ���Ӽ����¼�
		tabFolder.addSelectionListener(new TabFordlerListener(tabFolder, StaticVariable.historyCombo));
		
		Composite statusBar = new Composite(mainFrame, SWT.BORDER);
		statusBar.setBounds(0, (int) (windowHeight * 0.87), windowWidth, 33);

		// �����а�ť��Ӽ�����
		first.addSelectionListener(new ButtonDownListener("first"));
		previous.addSelectionListener(new ButtonDownListener("previous"));
		next.addSelectionListener(new ButtonDownListener("next"));
		last.addSelectionListener(new ButtonDownListener("last"));
		StaticVariable.refresh.addSelectionListener(new ButtonDownListener("refresh"));
		StaticVariable.scoreChartBtn.addSelectionListener(new ScoreChartBtnListener(parent));
		StaticVariable.historyCharBtn.addSelectionListener(new HistoryCharBtnListener(parent));

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

	protected MenuManager createMenuManager() {
		MenuManager menuBar = new MenuManager();
		MenuManager fileMenu = new MenuManager(" �ļ�(&F)");
		MenuManager editorMenu = new MenuManager(" �༭(&E)");
		MenuManager helpMenu = new MenuManager(" ����(&H)");
		// ���ļ��˵�����������˵�
		fileMenu.add(newCreate);
		fileMenu.add(openFile);
		fileMenu.add(new Separator());
		fileMenu.add(saveFile);
		fileMenu.add(saveAsFile);
		fileMenu.add(new Separator());
		fileMenu.add(exit);
		// �ڱ༭�˵�����������˵�
		editorMenu.add(copyFile);
		editorMenu.add(pasteFile);
		editorMenu.add(cutFile);
		editorMenu.add(new Separator());
		editorMenu.add(setFont);
		editorMenu.add(setColor);
		editorMenu.add(new Separator());
		editorMenu.add(selectAll);
		editorMenu.add(formate);
		helpMenu.add(about);
		// �� menuBar ������ļ��˵����༭�˵��Ͱ����˵�
		menuBar.add(fileMenu);
		menuBar.add(editorMenu);
		menuBar.add(helpMenu);
		return menuBar;
	}

	// �ڹ���������ӹ�������ť
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		// gtoolBarManager.add(new NewCreateAction());
		toolBarManager.add(new OpenFileAction());
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
	 * �½����ң��˵���
	 * @author wanli
	 *
	 */
	class NewCreate extends Action {
		public NewCreate() {
			super("NewCreateAction@Ctrl+N", Action.AS_PUSH_BUTTON);
			setText(" �½�����");
		}

		public void run() {
			new CreateClassListener(parent);
			createClass();
		}
	}

	/**
	 * �½����ң�������
	 * @author wanli
	 *
	 */
	class NewCreateAction extends Action {
		public NewCreateAction() {
			super("NewCreateAction@Ctrl+N", Action.AS_PUSH_BUTTON);
			setText(" �½�����");
			try {
				// ����ͼ��
				ImageDescriptor icon = ImageDescriptor.createFromURL(new URL("file:image/create_class.png"));
				setImageDescriptor(icon);
			} catch (MalformedURLException e) {
				System.err.println(e.getMessage());
			}
		}

		public void run() {
			new CreateClassListener(parent);
			createClass();
		}
	}

	/**
	 * ���ļ����˵���
	 * @author wanli
	 *
	 */
	class OpenFile extends Action {
		public OpenFile() {
			super("OpenFileAction@Ctrl+O", Action.AS_PUSH_BUTTON);
			setText(" ��");
		}

		public void run() {
			// �ڴ��µ��ļ�֮ǰ���ж��Ƿ񱣴浱ǰ�ļ�
			if (judgeTextSave())
				OpenTextFile();
		}
	}

	/**
	 * ���ļ���������
	 * @author wanli
	 *
	 */
	class OpenFileAction extends Action {
		public OpenFileAction() {
			super("OpenFileAction@Ctrl+O", Action.AS_PUSH_BUTTON);
			setText(" ��");
			try {
				// ����ͼ��
				ImageDescriptor icon = ImageDescriptor.createFromURL(new URL("file:image/open_file.png"));
				setImageDescriptor(icon);
			} catch (MalformedURLException e) {
				System.err.println(e.getMessage());
			}
		}

		public void run() {
			// �ڴ��µ��ļ�֮ǰ���ж��Ƿ񱣴浱ǰ�ļ�
			if (judgeTextSave())
				OpenTextFile();
		}
	}

	/**
	 * �����ļ�
	 * @author wanli
	 *
	 */
	class SaveFileAction extends Action {
		public SaveFileAction() {
			super("SaveFileAction@Ctrl+S", Action.AS_PUSH_BUTTON);
			setText(" ����");
		}

		public void run() {
			saveTextFile();
		}
	}

	/**
	 * �ļ����Ϊ
	 * @author wanli
	 *
	 */
	class SaveAsFileAction extends Action {
		public SaveAsFileAction() {
			super(" ���Ϊ@Alt+A", Action.AS_PUSH_BUTTON);
		}

		public void run() {
			saveFileAs();
		}
	}

	/**
	 * �˳�����
	 * @author wanli
	 *
	 */
	class ExitAction extends Action {
		public ExitAction() {
			super(" �˳�@Ctrl+E", Action.AS_PUSH_BUTTON);
		}

		public void run() {
			getShell().dispose();
		}
	}

	/**
	 * ����
	 * @author wanli
	 *
	 */
	class CopyFileAction extends Action {
		public CopyFileAction() {
			super("CopyFileAction@Ctrl+C", Action.AS_PUSH_BUTTON);
			setText(" ����");
		}

		public void run() {
			StaticVariable.text.copy();
		}
	}

	/**
	 * ճ��
	 * @author wanli
	 *
	 */
	class PasteFileAction extends Action {
		public PasteFileAction() {
			super("PasteFileAction@Ctrl+V", Action.AS_PUSH_BUTTON);
			setText(" ճ��");
		}

		public void run() {
			StaticVariable.text.paste();
		}
	}

	/**
	 * ����
	 * @author wanli
	 *
	 */
	class CutFileAction extends Action {
		public CutFileAction() {
			super("CutFileAction @Ctrl+X", Action.AS_PUSH_BUTTON);
			setText(" ����");
		}

		public void run() {
			StaticVariable.text.cut();
		}
	}

	/**
	 * ��������
	 * @author wanli
	 *
	 */
	class SetFontAction extends Action {
		public SetFontAction() {
			super(" ��������@Alt+F", Action.AS_PUSH_BUTTON);
		}

		public void run() {
			FontDialog fontDialog = new FontDialog(getShell());
			fontDialog.setFontList((StaticVariable.text.getFont()).getFontData());
			FontData fontData = fontDialog.open();
			if (fontData != null) {
				if (font != null) {
					font.dispose();
				}
				font = new Font(getShell().getDisplay(), fontData);
				StaticVariable.text.setFont(font);
			}
		}
	}

	/**
	 * ����������ɫ
	 * @author wanli
	 *
	 */
	class SetColorAction extends Action {
		public SetColorAction() {
			super(" ������ɫ@Alt+C", Action.AS_PUSH_BUTTON);
		}

		public void run() {
			// ������ɫѡ��Ի���
			ColorDialog dlg = new ColorDialog(getShell());
			// �򿪶Ի���
			RGB rgb = dlg.open();
			if (rgb != null) {
				// ���� color ����
				color = new Color(getShell().getDisplay(), rgb);
				// ���� point ���󣬻�ȡѡ��Χ��
				Point point = StaticVariable.text.getSelectionRange();
				for (int i = point.x; i < point.x + point.y; i++) {
					// ���ѡ�е�������ʽ�ͷ�Χ
					range = StaticVariable.text.getStyleRangeAtOffset(i);
					// �������������������ʽ( ��Ӵ֡�б�塢���»���)
					if (range != null) {
						/**
						 * ����һ����ԭ�� StyleRange ��ֵ��ͬ�� StyleRange
						 */
						style = (StyleRange) range.clone();
						style.start = i;
						style.length = 1;
						// ����ǰ����ɫ
						style.foreground = color;
					} else {

						style = new StyleRange(i, 1, color, null, SWT.NORMAL);
					}
					StaticVariable.text.setStyleRange(style);
				}
			}
		}
	}

	/**
	 * ȫѡ
	 * @author wanli
	 *
	 */
	class SelectAllAction extends Action {
		public SelectAllAction() {
			super(" ȫѡ@Ctrl+A", Action.AS_PUSH_BUTTON);
		}

		public void run() {
			StaticVariable.text.selectAll();
		}
	}

	/**
	 * ��ʽ��
	 * @author wanli
	 *
	 */
	class FormateAction extends Action {
		public FormateAction() {
			super(" ��ʽ��@Ctrl+W", Action.AS_CHECK_BOX);
		}

		public void run() {
			StaticVariable.text.setWordWrap(isChecked());
		}
	}

	/**
	 * ����
	 * @author wanli
	 *
	 */
	class AboutAction extends Action {
		public AboutAction() {
			super(" ����@Ctrl+H", Action.AS_PUSH_BUTTON);
		}

		public void run() {
			MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
			messageBox.setMessage(" ClassForEvery 2.0 �汾�� ");
			messageBox.open();
		}
	}

	class BlodAction extends Action {
		public BlodAction() {
			setText(" �Ӵ�");
		}

		public void run() {
			Point point = StaticVariable.text.getSelectionRange();
			for (int i = point.x; i < point.x + point.y; i++) {
				StyleRange range = StaticVariable.text.getStyleRangeAtOffset(i);
				if (range != null) {
					style = (StyleRange) range.clone();
					style.start = i;
					style.length = 1;
				} else {
					style = new StyleRange(i, 1, null, null, SWT.NORMAL);
				}
				// �Ӵ�����
				style.fontStyle ^= SWT.BOLD;
				StaticVariable.text.setStyleRange(style);
			}
		}
	}

	class ItalicAction extends Action {
		public ItalicAction() {
			setText(" б��");
		}

		public void run() {
			Point point = StaticVariable.text.getSelectionRange();
			for (int i = point.x; i < point.x + point.y; i++) {
				range = StaticVariable.text.getStyleRangeAtOffset(i);
				if (range != null) {
					style = (StyleRange) range.clone();
					style.start = i;
					style.length = 1;
				} else {
					style = new StyleRange(i, 1, null, null, SWT.NORMAL);
				}
				// ����Ϊб��
				style.fontStyle ^= SWT.ITALIC;
				StaticVariable.text.setStyleRange(style);
			}
		}
	}

	class UnderlineAction extends Action {
		public UnderlineAction() {
			setText(" �»���");
		}

		public void run() {
			Point point = StaticVariable.text.getSelectionRange();
			for (int i = point.x; i < point.x + point.y; i++) {
				range = StaticVariable.text.getStyleRangeAtOffset(i);
				if (range != null) {
					style = (StyleRange) range.clone();
					style.start = i;
					style.length = 1;
				} else {
					style = new StyleRange(i, 1, null, null, SWT.NORMAL);
				}
				// �����»���
				style.underline = !style.underline;
				StaticVariable.text.setStyleRange(style);
			}
		}
	}

	boolean judgeTextSave() {
		if (!changes)
			return true;
		MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.YES | SWT.NO | SWT.CANCEL);
		messageBox.setMessage(" �Ƿ񱣴���ļ��ĸ��ģ� ");
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
	 * ���ļ��Ի���
	 * @return
	 */
	boolean OpenTextFile() {
		StaticVariable.index = 1;
		// ����Ի�������Ϊ����
		FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
		// ���öԻ���򿪵��޶�����
		dialog.setFilterExtensions(new String[] { " *.txt", "*.doc", "*.docx"});
		// �򿪶Ի��򣬲����ش��ļ���·��
		String openFile = dialog.open();
		if (openFile == null) {
			return false;
		}
		// ��ָ�����ļ�
		file = new File(openFile);
		String fileExtension = file.getName().substring(file.getName().indexOf("."));
		try {
			//��ȡ��չ��Ϊ.doc��word�ĵ�
			HWPFDocument doc = null;
			//��ȡ��չ��Ϊ.docx��word�ĵ�
			XWPFDocument docx = null;
			XWPFWordExtractor extractor = null;
			//��ȡ��չ��Ϊ.txt���ĵ�
			StringBuffer sb = null;
			String fileText = null;
			BufferedReader reader = null;
			FileInputStream in = null;
			if (fileExtension.equals(".doc")) {
				in = new FileInputStream(file);
				doc = new HWPFDocument(in);
				fileText = doc.getDocumentText();
				StaticVariable.questions = fileText.split(new String("#\\^"));
				StaticVariable.text.setText(StaticVariable.questions[1]);
			} else if (fileExtension.equals(".docx")) {
				in = new FileInputStream(file);
				docx = new XWPFDocument(in);
				extractor = new XWPFWordExtractor(docx);
				fileText = extractor.getText();
				StaticVariable.questions = fileText.split(new String("#\\^"));
				StaticVariable.text.setText(StaticVariable.questions[1]);
			}
			else {
				// ��ȡ�ļ�
				FileReader fileReader = new FileReader(file);
				// ���ַ������ַ����뻺����
				reader = new BufferedReader(fileReader);
				sb = new StringBuffer();
				String line = null;
				while ((line = reader.readLine()) != null) {
					// ͨ�� append() ����ʵ�ֽ��ַ�����ӵ��ַ���������
					sb.append(line);
					sb.append("\r\n");
				}
				// ����ȡ���ļ����ַ���"#^"�ֿ�����Ϊ'^'��ת���ַ�������ǰ��Ҫ��\\
				StaticVariable.questions = sb.toString().split(new String("#\\^"));	
				StaticVariable.text.setText(StaticVariable.questions[1]);
			}
			System.out.println(StaticVariable.questions.length);
			int num = StaticVariable.questions.length - 1;
			String fileName = file.getName();
			shell.setText(APPNAME + "-" + file);
			StaticVariable.tableName = fileName.substring(0, fileName.indexOf("."));
			dbService.createTable(num, StaticVariable.tableName);
			//���ļ������ð�ť
			first.setEnabled(true);
			previous.setEnabled(true);
			next.setEnabled(true);
			last.setEnabled(true);
			if (reader != null) {
				reader.close();				
			}
			if (in != null) {
				in.close();
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * �����ļ��Ի���
	 * @return
	 */
	boolean saveTextFile() {
		if (file == null) {
			// �����ļ�ѡ��Ի�������Ϊ������
			FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
			dialog.setText(" ����");
			// ���öԻ��򱣴���޶�����
			dialog.setFilterExtensions(new String[] { " *.txt", "*.doc", "*.docx"});
			// �򿪶Ի��򣬲����ر����ļ���·��
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
	 * �ж��Ƿ����Ϊ
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
	 * ���Ϊ�Ի���
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
				// �����Ϊ�Ի��򣬲����ر���·��
				fileName = dlg.open();
				if (fileName == null) {
					done = true;
				} else {
					// �жϱ�����ļ��Ƿ��Ѿ�����
					File file = new File(fileName);
					if (file.exists()) {
						// ���ļ����ڣ��򵯳���ʾ�ԵĶԻ���
						MessageBox mb = new MessageBox(dlg.getParent(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
						// ��ʾ�Ե���Ϣ
						mb.setMessage(fileName + " �Ѿ����ڣ��Ƿ񽫸��ļ��滻?");
						/**
						 * ����"yes" ��ť�������ϵ��ļ��滻 ����������д�ļ���
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
	 * �������ң�������
	 */
	public void createClass() {
		if (StaticVariable.className != null && StaticVariable.className != "") {
			TreeItem classroom = new TreeItem(tree, SWT.NONE);
			classroom.setText(StaticVariable.className);
			StaticVariable.rooms.add(classroom);
		}
	}
}
