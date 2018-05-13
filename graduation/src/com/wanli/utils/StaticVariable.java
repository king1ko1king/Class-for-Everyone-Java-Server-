package com.wanli.utils;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import com.wanli.swing.entities.ChoiceQuestion;
import com.wanli.swing.entities.FillInTheBlanks;
import com.wanli.swing.entities.OnlineUser;
import com.wanli.swing.entities.QuestionType;
import com.wanli.swing.entities.TrueOrFalse;

/**
 * 存储所有静态属性
 * @author wanli
 *
 */
public class StaticVariable {
	
	public static Composite parent;										// 主窗体的Composite类
	public static String quitSocket = "";								// 记录断开连接的socket，用来移除TreeItem
	public static String onlineNumsStr = "在线人数：";						// 显示在线人数
	public static int onlineNumsInt = 0;								// 统计在线人数
	public static Label onlining;										// 显示在线人数的Label
	public static StyledText text;										// 题目文本显示
	public static StyledText answer;									// 答案文本显示
	public static Button refresh;										// 刷新成绩表按钮
	public static Button scoreChartBtn;									// 以图表的形式显示当前成绩数据
	public static Button historyCharBtn;								// 以图表的形式显示历史成绩数据
	public static Table scoreTab;										// 显示成绩表格
	public static Table historyTab;										// 显示历史成绩表格
	public static Table askQuestions;									// 显示学生提出的问题
	public static Combo historyCombo;									// 所有历史表格的下拉框
	public static String[] questions;									// 保存所有问题
	public static int index = 1;										// 标记第几题
	public static String instruction;									// 服务器给客户端发送的指令
	public static StringBuilder tableName = new StringBuilder();		// 表名，用于查询数据库中的表数据
	public static String classOrSepcialtyName;							// 班级或专业名称
	public static String className;										// 记录创建的教室的名称
	public static TabItem askQuestion;									// 提问选项卡
	public static Map<String, TreeItem> rooms = new HashMap<>();		// 存储创建的所有教室
	public static Map<String, TreeItem> onlineUsers = new HashMap<>();	// 显示所有在线人数的Tree
	public static Map<String, OnlineUser> users = new HashMap<>();		// 保存所有Socket的Map
	public static Map<String, Socket> usersSocket = new HashMap<>();
	public static Map<TableItem, Boolean> unanswerMap = new HashMap<>();// 存储学生提问中为回答的问题 
	public static Map<String, String> answers = new HashMap<>();		// 存储每一题学生回答的所有答案
	public static Map<String, Text> choiceAllText = new HashMap<>();	// 存储选择题面板上的所有Text组件
	public static Map<String, Text> trueOrFalseAllText = new HashMap<>();// 存储是非题面板上的所有Text组件
	public static Map<String, Text> fillblanksAllText = new HashMap<>();// 存储填空题面板上的所有Text组件
	public static Map<String, Integer> options = new HashMap<>();		// 存储选择题所有选项的答案个数
	public static Composite questionCom;								// 课前备题窗口弹出面板
	public static String questionType;									// 题目类型
	public static Button nextOption;									// 下一题
	public static List<ChoiceQuestion> choiceList = new ArrayList<>();	// 存储选择题
	public static List<TrueOrFalse> trueOrFalseList = new ArrayList<>();// 存储是非题
	public static List<FillInTheBlanks> fillblanksList = new ArrayList<>();// 存储填空题
	public static int creQuesIndex = 0;									// 存储创建题目时的下标，标记创建了多少题目
	public static boolean firstOpenPrepareLessonsShell = true;			// 标记是否是软件启动后第一次打开备课窗口
//	public static Map<String, String> questionsMap = new HashMap<>();	// 存储所有的问题
	public static List<String> questionsList = new ArrayList<>();		// 存储所有的问题
	public static Combo questionSelect;									// 选择问题的下拉框
	public static StyleRange style, range;								// 风格
	public static Color color;											// 设置文本颜色
	public static List<Integer> correct = new ArrayList<>();			// 存储所有正确的个数
	public static List<Integer> error = new ArrayList<>();				// 存储所有错误答案的个数
	public static List<Integer> unResponse = new ArrayList<>();			// 存储所有未答题的个数
	public static boolean firstInsert = true;							// 往成绩表中插入数据，用来标记是否是第一次插入
	public static List<String> statisticalData;							// 存储所有统计数据
	public static Table table;											// 管理问题的表格
	public static List<QuestionType> allQuestionList = new ArrayList<>();// 存储任意题型的list
	public static String questionManagerFileName;						// 题目管理时打开的文件名
}
