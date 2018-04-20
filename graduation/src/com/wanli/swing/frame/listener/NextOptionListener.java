package com.wanli.swing.frame.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import com.wanli.swing.entities.ChoiceQuestion;
import com.wanli.swing.entities.FillInTheBlanks;
import com.wanli.swing.entities.TrueOrFalse;
import com.wanli.utils.AddToQuestionList;
import com.wanli.utils.StaticVariable;

public class NextOptionListener extends SelectionAdapter {

	private List<String> optionKeys = new ArrayList<>();// �洢StaticVariable.choiceAllText����ѡ���keyֵ
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		// �����optionKeys�������Ӱ����һ��ѡ�����ѡ��
		optionKeys.clear();
		// �ж��Ƿ��������������д��Ĭ��Ϊfalse
		boolean hasEmpty = false;
		switch (StaticVariable.questionType) {
		// ѡ����
		case "choice":
			AddToQuestionList.addToChoiceList(optionKeys, hasEmpty);
			break;
		
		// �Ƿ���
		case "true_or_false":
			AddToQuestionList.addToTrueOrFalseList(hasEmpty);
			break;
			
		case "fill_in_the_blanks":
			AddToQuestionList.addToFillBlanks(hasEmpty);
			break;
			
		}
	}
	
}
