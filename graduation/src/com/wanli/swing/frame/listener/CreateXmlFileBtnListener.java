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
import org.eclipse.swt.widgets.Shell;

import com.wanli.swing.entities.Question;
import com.wanli.utils.AddToQuestionList;
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
	
	public CreateXmlFileBtnListener(Shell shell) {
		this.shell = shell;
	}
	
	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		// 判断最后一题的题型
		if (StaticVariable.questionType.equals("choice")) {
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
		if (create.equals("create") || create.equals("true")) {
			questions = new Question();
			questions.setChoiceList(StaticVariable.choiceList);
			questions.setTrueOrFalseList(StaticVariable.trueOrFalseList);
			questions.setFillBlanksList(StaticVariable.fillblanksList);
			// 生成映射字符串
			String str = beanToXml(questions, Question.class);
			// 写入到xml文件中
			boolean save = saveXmlFile(str);
			StaticVariable.choiceList.clear();
			StaticVariable.trueOrFalseList.clear();
			StaticVariable.fillblanksList.clear();
			StaticVariable.creQuesIndex = 0;
			if (save) {
				shell.dispose();				
			}
		}
	}
	
	/**
     * java对象转换为xml文件
     * @param xmlPath: xml文件路径
     * @param load:    java对象.Class
     * @return:        xml文件的String
     * @throws JAXBException    
     */
    private String beanToXml(Object obj,Class<?> load) {
        JAXBContext context = null;
        StringWriter writer = null;
		try {
			context = JAXBContext.newInstance(load);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "GBK");
			writer = new StringWriter();
			marshaller.marshal(obj,writer);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
        return writer.toString();
    }

    private boolean saveXmlFile(String str) {
    	boolean save = false;
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
    	return save;
    }
    
}
