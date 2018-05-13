package com.wanli.swing.frame.listener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;

import com.wanli.swing.service.DBService;
import com.wanli.utils.ChoiceQuestionUtils;
import com.wanli.utils.StaticVariable;

/**
 * 题目选择下拉框
 * @author wanli
 *
 */
public class QuestionSelectComboListener implements SelectionListener {

	private int option = 65;							// 标记选项
	private String question;							// 获取题目
	private String[] strs;								// 将获取的题目进行分割
	private StringBuffer showQues = new StringBuffer();	// 显示题目
	private StyleRange style;							// 文本显示的风格
	private DBService dbService;						// 操作数据库的服务
	private int index = 0;								// 标记下拉框的当前选中的项
	
	public QuestionSelectComboListener() {
		dbService = new DBService();
		// 定义一个style对象
		style = new StyleRange();
		// 从第一个字符开始
		style.start = 0;
		// 将“选择题:”“是非题:”“填空题:”添加下划线并且加粗
		style.length = 4;
		style.underline = true;
		style.font = new Font(StaticVariable.parent.getDisplay(), "Arial", 30, SWT.BOLD);
	}
	
	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
//		System.out.println(StaticVariable.questionSelect.getSelectionIndex());
//		System.out.println(StaticVariable.questionSelect.getText());
		// StaticVariable.correct.size() > 0表示有学生回答问题，将回答的答案存入数据库
		if (StaticVariable.correct.size() > 0) {
			// 上一次选择的题目
//			String question = StaticVariable.questionsMap.get(Integer.toString(index));
			String question = StaticVariable.questionsList.get(index - 1);
			// 分割字符串，将题目答案等分离
			String[] strs = question.split("#\\^");
			if ((strs.length - 3) > 0) {
				// (strs.length - 3) > 0表示有多个答案
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
						answer.append(strs[i + 2]);
						corStr.append(correct);
						errStr.append(error);
						unResStr.append(unResponse);
					} else {
						answer.append(" " + strs[i + 2]);
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
				String countStr = strs[2] + "," + Integer.toString(correct) + "," + Integer.toString(error) + "," + Integer.toString(unResponse);
				StaticVariable.answers.put("统计", countStr);
			}
			System.out.println(StaticVariable.correct.size());
			System.out.println(StaticVariable.answers);
			dbService.addRecord(StaticVariable.tableName.toString(), StaticVariable.answers, index);
		}
		// 获取当前选中列的下标
		index = StaticVariable.questionSelect.getSelectionIndex();
		// 获取当前选中列的文本
		String selected = StaticVariable.questionSelect.getText();
		if (index != 0) {
			StaticVariable.scoreChartBtn.setEnabled(true);
			// 截取题目类型
			String type = selected.substring(selected.indexOf("-") + 1);
			switch (type) {
			case "选择题":
				// 每选择一题，重置计数的类
				StaticVariable.correct.clear();
				StaticVariable.error.clear();
				StaticVariable.unResponse.clear();
				// 初始化存储正确答案个数，错误答案个数，未回答个数的list
				StaticVariable.correct.add(new Integer(0));
				StaticVariable.error.add(new Integer(0));
				StaticVariable.unResponse.add(new Integer(0));
				// 清空StringBuffer
				showQues.setLength(0);
				// 获取题目
//				question = StaticVariable.questionsMap.get(Integer.toString(index));
				question = StaticVariable.questionsList.get(index - 1);
				// 分割题目
				strs = question.split("#\\^");
				ChoiceQuestionUtils questionUtils = new ChoiceQuestionUtils(strs.length - 3);
				questionUtils.createMap();
				StaticVariable.options = questionUtils.getOptions();
				// 重组题目
				showQues.append("选择题:\n\n");
				showQues.append(strs[1]);
				for (int i = 3; i < strs.length; i++) {
					showQues.append("\n" + (char)option + "." + strs[i]);
					option++;
				}
				option = 65;
				// 显示题目
				StaticVariable.text.setText(showQues.toString());
				// 设置样式
				StaticVariable.text.setStyleRange(style);
				break;

			case "是非题":
				// 每选择一题，重置计数的类
				StaticVariable.correct.clear();
				StaticVariable.error.clear();
				StaticVariable.unResponse.clear();
				// 初始化存储正确答案个数，错误答案个数，未回答个数的list
				StaticVariable.correct.add(new Integer(0));
				StaticVariable.error.add(new Integer(0));
				StaticVariable.unResponse.add(new Integer(0));
				showQues.setLength(0);
//				question = StaticVariable.questionsMap.get(Integer.toString(index));
				question = StaticVariable.questionsList.get(index - 1);
				strs = question.split("#\\^");
				showQues.append("是非题:\n\n");
				showQues.append(strs[1]);
				StaticVariable.text.setText(showQues.toString());
				StaticVariable.text.setStyleRange(style);
				break;
			case "填空题":
				// 每选择一题，重置计数的类
				StaticVariable.correct.clear();
				StaticVariable.error.clear();
				StaticVariable.unResponse.clear();
				question = StaticVariable.questionsList.get(index - 1);
				strs = question.split("#\\^");
				if (strs != null) {
					// 初始化存储正确答案个数，错误答案个数，未回答个数的list
					for (int i = 2; i < strs.length; i++) {
						StaticVariable.correct.add(new Integer(0));
						StaticVariable.error.add(new Integer(0));
						StaticVariable.unResponse.add(new Integer(0));					
					}
					
				}
				showQues.setLength(0);
//				question = StaticVariable.questionsMap.get(Integer.toString(index));
				showQues.append("填空题:\n\n");
				showQues.append(strs[1]);
				StaticVariable.text.setText(showQues.toString());
				StaticVariable.text.setStyleRange(style);
				break;
			}
		} else {
			StaticVariable.scoreChartBtn.setEnabled(false);
			StaticVariable.text.setText("");
		}
	}

}
