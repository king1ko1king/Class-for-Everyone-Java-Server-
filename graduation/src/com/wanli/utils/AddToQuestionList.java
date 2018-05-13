package com.wanli.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import com.wanli.swing.entities.ChoiceQuestion;
import com.wanli.swing.entities.FillInTheBlanks;
import com.wanli.swing.entities.TrueOrFalse;

/**
 * 操作所有存储题型的List(choiceList, trueOrFalseList, fillblanksList)
 * @author wanli
 *
 */
public class AddToQuestionList {

	// 添加选择题
	public static void addToChoiceList(List<String> optionKeys, boolean hasEmpty) {
		// 判断是否有输入框没有填写
		hasEmpty = judgeTextEmpty(StaticVariable.choiceAllText, hasEmpty);
		if (!hasEmpty) {
			choiceList(optionKeys);
		}
		if (!hasEmpty) {
			// 清空所有输入框，以便下一次输入
			setAllTextEmpty(StaticVariable.choiceAllText);			
		}
	}
	
	public static void choiceList(List<String> optionKeys) {
		// 全部填写
		// 题目的数量加1
		StaticVariable.creQuesIndex++;
		// 获取题目
		String question = StaticVariable.choiceAllText.get("question").getText();
		// 获取答案
		String answer = StaticVariable.choiceAllText.get("answer").getText();
		// 获取所有选项的键值
		List<String> options = new ArrayList<>();
		for (String key : StaticVariable.choiceAllText.keySet()) {
			if (!key.equals("answer") && !key.equals("question")) {
				optionKeys.add(key);
			}
		}
		// 对所有键值进行排序
		Collections.sort(optionKeys);
		for (String key : optionKeys) {
			options.add(StaticVariable.choiceAllText.get(key).getText());
		}
		// 将生成的一道选择题存入StaticVariable.choiceList
		StaticVariable.choiceList.add(new ChoiceQuestion(Integer.toString(StaticVariable.creQuesIndex), question, answer, options));
		// // 测试
		// for (ChoiceQuestion cq: StaticVariable.choiceList) {
		// System.out.println(cq);
		// }
	}
	
	// 添加是非题
	public static void addToTrueOrFalseList(boolean hasEmpty) {
		hasEmpty = judgeTextEmpty(StaticVariable.trueOrFalseAllText, hasEmpty);
		if (!hasEmpty) {
			trueOrFalseList();
		}
		if (!hasEmpty) {
			setAllTextEmpty(StaticVariable.trueOrFalseAllText);			
		}
	}
	
	public static void trueOrFalseList() {
		StaticVariable.creQuesIndex++;
		String question = StaticVariable.trueOrFalseAllText.get("question").getText();
		String answer = StaticVariable.trueOrFalseAllText.get("answer").getText();
		// 将生成的一道是非题题存入StaticVariable.trueOrFalseList
		StaticVariable.trueOrFalseList.add(new TrueOrFalse(Integer.toString(StaticVariable.creQuesIndex), question, answer));
//		// 测试
//		for (TrueOrFalse tof: StaticVariable.trueOrFalseList) {
//			System.out.println(tof);
//		}
	}
	
	// 添加填空题
	public static void addToFillBlanks(boolean hasEmpty) {
		hasEmpty = judgeTextEmpty(StaticVariable.fillblanksAllText, hasEmpty);
		if (!hasEmpty) {
			fillBlanks();
			if (!hasEmpty) {
				setAllTextEmpty(StaticVariable.fillblanksAllText);				
			}
		}
	}
	
	public static void fillBlanks() {
		StaticVariable.creQuesIndex++;
		String question = StaticVariable.fillblanksAllText.get("question").getText();
		String answer = StaticVariable.fillblanksAllText.get("answer").getText();
		// 填空题答案可能有多个，通过空隔将答案进行分隔
		String[] answers = answer.split(" ");
		List<String> answerList = new ArrayList<>();
		// answers.length > 1 表示有多个答案
		if (answers.length > 1) {
			for (int i = 0; i < answers.length; i++) {
				if (!answers[i].equals("")) {
					answerList.add(answers[i]);
				}
			}					
		} else {
			answerList.add(answer);
		}
//		// 测试
//		System.out.println(Arrays.toString(answers));
//		System.out.println(answerList);
		StaticVariable.fillblanksList.add(new FillInTheBlanks(Integer.toString(StaticVariable.creQuesIndex), question, answerList));
//		// 测试
//		for (FillInTheBlanks ftb: StaticVariable.fillblanksList) {
//			System.out.println(ftb);
//		}
	}
	
	/**
	 * 判断创建题目时是否有text组件为空
	 * @param map
	 * @param hasEmpty
	 */
	private static boolean judgeTextEmpty(Map<String, Text> map, boolean hasEmpty) {
		for (Map.Entry<String, Text> inputText: map.entrySet()) {
			if (inputText.getValue().getText() == null || inputText.getValue().getText() == "" || inputText.getValue().getText().equals("若有多个空，答案请用空格分隔！")) {
				MessageBox messageBox = new MessageBox(StaticVariable.parent.getShell(), SWT.YES);
				messageBox.setText("警告");
				messageBox.setMessage("所有空行都不能为空！！！");
				messageBox.open();
				hasEmpty = true;
				break;
			}
		}
		return hasEmpty;
	}
	
	/**
	 * 清空所有的输入行
	 * @param map
	 */
	private static void setAllTextEmpty(Map<String, Text> map) {
		for (Map.Entry<String, Text> inputText: map.entrySet()) {
			inputText.getValue().setText("");
		}
	}
	
	/**
	 * 判断创建题目时是否有text组件为空
	 * @param map
	 * @param hasEmpty
	 */
	public static String judgeTextEmpty(Map<String, Text> map) {
		for (Map.Entry<String, Text> inputText: map.entrySet()) {
			if (inputText.getValue().getText() == null || inputText.getValue().getText() == "" || inputText.getValue().getText().equals("若有多个空，答案请用空格分隔！")) {
				MessageBox messageBox = new MessageBox(StaticVariable.parent.getShell(), SWT.YES | SWT.NO);
				messageBox.setText("警告");
				messageBox.setMessage("还有空行未填写！是否放弃本题？");
				if (messageBox.open() == SWT.YES) {
					return "true";
				} else {
					return "fasle";
				}
			}
		}
		return "create";
	}
	
	/**
	 * 判断text组件是否有已填写的行
	 * @param map
	 * @return
	 */
	public static String judgeTextValue(Map<String, Text> map) {
		for (Map.Entry<String, Text> inputText: map.entrySet()) {
			if (inputText.getValue().getText() != null && inputText.getValue().getText() != "") {
				MessageBox messageBox = new MessageBox(StaticVariable.parent.getShell(), SWT.YES | SWT.NO);
				messageBox.setText("警告");
				messageBox.setMessage("是否放弃本题？");
				if (messageBox.open() == SWT.YES) {
					return "true";
				} else {
					return "fasle";
				}
			}
		}
		return "true";
	}

}
