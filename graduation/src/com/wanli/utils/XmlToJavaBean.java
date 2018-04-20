package com.wanli.utils;

import java.io.File;
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

	private File file;										// ��ȡ��xml�ļ�
	private StringBuffer str = new StringBuffer();			// �洢��������
	
	public XmlToJavaBean(File file) {
		this.file = file;
		xmlToJava();
	}
	
	private void xmlToJava() {
		JAXBContext jaxbc;
		try {
			// xmlҪת��Ϊjavabean����
			jaxbc = JAXBContext.newInstance(Question.class);
			Unmarshaller unmarshaller = jaxbc.createUnmarshaller();
			Question q = (Question) unmarshaller.unmarshal(file);
			// ��ȡ���е�ѡ����
			List<ChoiceQuestion> choiceQs = q.getChoiceList();
			// ��ȡ���е��Ƿ���
			List<TrueOrFalse> tofQs = q.getTrueOrFalseList();
			// ��ȡ���е������
			List<FillInTheBlanks> fbQs = q.getFillBlanksList();
			// �������е�ѡ����
			// ѡ����洢���ͣ����,����,����,��,ѡ��A,ѡ��B,.... 
			if (choiceQs != null && choiceQs.size() > 0) {
				// �������е�ѡ����
				for (ChoiceQuestion choice: choiceQs) {
					str.append(choice.getNo());
					str.append("," + choice.getType());
					str.append("," + choice.getQuestion());
					str.append("," + choice.getAnswer());
					for (int i = 0; i < choice.getOptions().size(); i++) {
						str.append("," + choice.getOptions().get(i));
					}
					StaticVariable.questionsMap.put(choice.getNo(), str.toString());
					// ���StringBuffer
					str.setLength(0);
				}
			}
			// �������е��Ƿ���
			// �Ƿ���洢���ͣ����,����,����,��
			if (tofQs != null && tofQs.size() > 0) {
				// �������е��Ƿ���
				for (TrueOrFalse tof: tofQs) {
					str.append(tof.getNo());
					str.append("," + tof.getType());
					str.append("," + tof.getQuestion());
					str.append("," + tof.getAnswer());
					StaticVariable.questionsMap.put(tof.getNo(), str.toString());
					// ���StringBuffer
					str.setLength(0);
				}
			}
			// �������е������
			// �����洢���ͣ����,����,����,��1,��2....
			if (fbQs != null && fbQs.size() > 0) {
				// �������е������
				for (FillInTheBlanks fb: fbQs) {
					str.append(fb.getNo());
					str.append("," + fb.getType());
					str.append("," + fb.getQuestion());
					for (int i = 0; i < fb.getAnswer().size(); i++) {
						str.append("," + fb.getAnswer().get(i));
					}
					StaticVariable.questionsMap.put(fb.getNo(), str.toString());
					// ���StringBuffer
					str.setLength(0);
				}				
			}
			
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					// ������ѡ�������������
					StaticVariable.questionSelect.removeAll();;
					StaticVariable.questionSelect.add("��ѡ����Ŀ");
					StaticVariable.questionSelect.select(0);
				}
			});
			// �������е�����
			for (int i = 0; i < StaticVariable.questionsMap.size(); i++) {
				String[] strs = StaticVariable.questionsMap.get(Integer.toString(i + 1)).split(",");
				switch (strs[1]) {
				// ѡ����
				case "choice":
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							// ������ѡ�������������
							StaticVariable.questionSelect.add(strs[0] + "-ѡ����");
						}
					});
					break;
				// �Ƿ���
				case "true_or_false":
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							// ������ѡ�������������
							StaticVariable.questionSelect.add(strs[0] + "-�Ƿ���");
						}
					});
					break;
				// �����
				case "fill_in_the_blanks":
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							// ������ѡ�������������
							StaticVariable.questionSelect.add(strs[0] + "-�����");
						}
					});
					break;
				}
			}
		} catch (JAXBException e) {
			System.out.println("xmlתjavaʧ��");
			e.printStackTrace();
		}
	}
	
}
