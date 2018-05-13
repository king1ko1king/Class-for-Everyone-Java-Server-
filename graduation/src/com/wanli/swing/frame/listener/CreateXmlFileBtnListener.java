package com.wanli.swing.frame.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.wanli.swing.entities.Question;
import com.wanli.utils.AddToQuestionList;
import com.wanli.utils.JavaBeanToXml;
import com.wanli.utils.StaticVariable;

/**
 * 创建Xml文件
 * @author wanli
 *
 */
public class CreateXmlFileBtnListener implements SelectionListener {

	private Question questions;							// Question类，映射成xml文件
	private String create;								// 标记是否创建xml文件
	private List<String> optionKeys = new ArrayList<>();// 存储StaticVariable.choiceAllText所有选项的key值
	private Shell shell;
	private boolean isAppend = false;
	
	public CreateXmlFileBtnListener(Shell shell, boolean isAppend) {
		this.shell = shell;
		this.isAppend = isAppend;
	}
	
	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		if (StaticVariable.questionType == null) {
			MessageBox messageBox = new MessageBox(shell, SWT.YES);
			messageBox.setText("警告");
			messageBox.setMessage("还没有添加任何的题目，无法创建！");
			messageBox.open();
		} else if (StaticVariable.questionType.equals("choice")) {
			// 判断最后一题的题型
			// 判断是否有空行没有填写，是则询问是否放弃本题
			create = AddToQuestionList.judgeTextEmpty(StaticVariable.choiceAllText);
			if (create.equals("create")) {
				AddToQuestionList.choiceList(optionKeys);
			}
		} else if (StaticVariable.questionType.equals("true_or_false")) {
			create = AddToQuestionList.judgeTextEmpty(StaticVariable.trueOrFalseAllText);
			if (create.equals("create")) {
				AddToQuestionList.trueOrFalseList();
			}
		} else {
			create = AddToQuestionList.judgeTextEmpty(StaticVariable.fillblanksAllText);
			if (create.equals("create")) {
				AddToQuestionList.fillBlanks();
			}
		}
		if (create != null) {
			if (create.equals("create") || create.equals("true")) {
				if (StaticVariable.choiceList.size() > 0 || StaticVariable.trueOrFalseList.size() > 0 || StaticVariable.fillblanksList.size() > 0) {
					questions = new Question();
					questions.setChoiceList(StaticVariable.choiceList);
					questions.setTrueOrFalseList(StaticVariable.trueOrFalseList);
					questions.setFillBlanksList(StaticVariable.fillblanksList);
					// 生成映射字符串
					String str = JavaBeanToXml.beanToXml(questions, Question.class);
					// 写入到xml文件中
					boolean save = saveXmlFile(str);
					StaticVariable.choiceList.clear();
					StaticVariable.trueOrFalseList.clear();
					StaticVariable.fillblanksList.clear();
					StaticVariable.creQuesIndex = 0;
					if (save) {
						shell.dispose();
						if (isAppend) {
							MessageBox messageBox = new MessageBox(StaticVariable.parent.getShell(), SWT.YES);
							messageBox.setText("提示");
							messageBox.setMessage("添加成功，请刷新表格！");
							messageBox.open();
						}
					}
				} else {
					MessageBox messageBox = new MessageBox(shell, SWT.YES);
					messageBox.setText("警告");
					messageBox.setMessage("还没有添加任何的题目，无法创建！");
					messageBox.open();
				}	
			}			
		}
	}

    /**
     * 保存成xml文件
     * @param str
     * @return
     */
    private boolean saveXmlFile(String str) {
    	boolean save = false;
    	if (!isAppend) {
    		// 保存文件对话框
    		FileDialog dialog = new FileDialog(StaticVariable.parent.getShell(), SWT.SAVE);
    		dialog.setText("保存");
    		// 设置对话框保存的限定类型,保存成xml文件
    		dialog.setFilterExtensions(new String[] {"*.xml"});
    		// 打开对话框，并返回保存文件的路径
    		String saveFile = dialog.open();
    		if (saveFile != null && saveFile != "") {
    			save = true;
    			File file = new File(saveFile);
    			try {
    				FileWriter writer = new FileWriter(file);
    				StringBuffer fileString = new StringBuffer();
    				fileString.append(str);
    				writer.write(fileString.toString());
    				writer.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}    		
    		}
    	} else {
    		save = true;
    		File file = new File(StaticVariable.questionManagerFileName);
			try {
				FileWriter writer = new FileWriter(file);
				StringBuffer fileString = new StringBuffer();
				fileString.append(str);
				writer.write(fileString.toString());
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	return save;
    }
    
}
