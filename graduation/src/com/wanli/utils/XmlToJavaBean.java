package com.wanli.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.swt.widgets.Display;

import com.wanli.swing.entities.ChoiceQuestion;
import com.wanli.swing.entities.FillInTheBlanks;
import com.wanli.swing.entities.Question;
import com.wanli.swing.entities.TrueOrFalse;

public class XmlToJavaBean {

	private File file;										// 读取的xml文件
	private StringBuffer str = new StringBuffer();			// 存储单个问题
	
	public XmlToJavaBean(File file) {
		this.file = file;
		xmlToJava();
	}
	
	private void xmlToJava() {
		// 每次重新选择文件时，都要先清空下拉框的内容
		StaticVariable.questionSelect.removeAll();
		// 清空存储所有类型题目的list
		StaticVariable.allQuestionList.clear();
		// 清空存储所有转换成String的题目的list
		StaticVariable.questionsList.clear();
		JAXBContext jaxbc;
		try {
			// xml要转化为javabean的类
			jaxbc = JAXBContext.newInstance(Question.class);
			Unmarshaller unmarshaller = jaxbc.createUnmarshaller();
			Question q = (Question) unmarshaller.unmarshal(file);
			// 获取所有的选择题
			List<ChoiceQuestion> choiceQs = q.getChoiceList();
			if (choiceQs != null) {
				StaticVariable.choiceList = choiceQs;				
			}
			// 获取所有的是非题
			List<TrueOrFalse> tofQs = q.getTrueOrFalseList();
			if (tofQs != null) {
				StaticVariable.trueOrFalseList = tofQs;				
			}
			// 获取所有的填空题
			List<FillInTheBlanks> fbQs = q.getFillBlanksList();
			if (fbQs != null) {
				StaticVariable.fillblanksList = fbQs;				
			}
			// 遍历所有的选择题
			// 选择题存储类型：编号,类型,问题,答案,选项A,选项B,.... 
			if (choiceQs != null && choiceQs.size() > 0) {
				// 遍历所有的选择题
				for (ChoiceQuestion choice: choiceQs) {
					StaticVariable.allQuestionList.add(choice);
//					str.append(choice.getNo());
					str.append(choice.getType());
					str.append("#^" + choice.getQuestion());
					str.append("#^" + choice.getAnswer());
					for (int i = 0; i < choice.getOptions().size(); i++) {
						str.append("#^" + choice.getOptions().get(i));
					}
//					StaticVariable.questionsMap.put(choice.getNo(), str.toString());
					StaticVariable.questionsList.add(str.toString());
//					System.out.println(str.toString());
					// 清空StringBuffer
					str.setLength(0);
				}
			}
			// 遍历所有的是非题
			// 是非题存储类型：编号,类型,问题,答案
			if (tofQs != null && tofQs.size() > 0) {
				// 遍历所有的是非题
				for (TrueOrFalse tof: tofQs) {
					StaticVariable.allQuestionList.add(tof);
//					str.append(tof.getNo());
					str.append(tof.getType());
					str.append("#^" + tof.getQuestion());
					str.append("#^" + tof.getAnswer());
//					StaticVariable.questionsMap.put(tof.getNo(), str.toString());
					StaticVariable.questionsList.add(str.toString());
					// 清空StringBuffer
					str.setLength(0);
				}
			}
			// 遍历所有的填空题
			// 填空题存储类型：编号,类型,问题,答案1,答案2....
			if (fbQs != null && fbQs.size() > 0) {
				// 遍历所有的填空题
				for (FillInTheBlanks fb: fbQs) {
					StaticVariable.allQuestionList.add(fb);
//					str.append(fb.getNo());
					str.append(fb.getType());
					str.append("#^" + fb.getQuestion());
					for (int i = 0; i < fb.getAnswer().size(); i++) {
						str.append("#^" + fb.getAnswer().get(i));
					}
//					StaticVariable.questionsMap.put(fb.getNo(), str.toString());
					StaticVariable.questionsList.add(str.toString());
					// 清空StringBuffer
					str.setLength(0);
				}				
			}
			
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					// 向问题选择下拉框添加项
					StaticVariable.questionSelect.removeAll();;
					StaticVariable.questionSelect.add("请选择题目");
					StaticVariable.questionSelect.select(0);
				}
			});
			// 遍历所有的问题
//			for (int i = 0; i < StaticVariable.questionsMap.size(); i++) {
//				String[] strs = StaticVariable.questionsMap.get(Integer.toString(i + 1)).split(",");
			for (int i = 0; i < StaticVariable.questionsList.size(); i++) {
				String[] strs = StaticVariable.questionsList.get(i).split("#\\^");
				final int index = i + 1;
				System.out.println(strs[0]);
				switch (strs[0]) {
				// 选择题
				case "choice":
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							// 向问题选择下拉框添加项
							StaticVariable.questionSelect.add(index + "-选择题");
//							StaticVariable.questionSelect.add(strs[0] + "-选择题");
						}
					});
					break;
				// 是非题
				case "true_or_false":
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							// 向问题选择下拉框添加项
							StaticVariable.questionSelect.add(index + "-是非题");
//							StaticVariable.questionSelect.add(strs[0] + "-是非题");
						}
					});
					break;
				// 填空题
				case "fill_in_the_blanks":
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							// 向问题选择下拉框添加项
							StaticVariable.questionSelect.add(index + "-填空题");
//							StaticVariable.questionSelect.add(strs[0] + "-填空题");
						}
					});
					break;
				}
			}
		} catch (JAXBException e) {
			System.out.println("xml转java失败");
			e.printStackTrace();
		}
	}
	
}
