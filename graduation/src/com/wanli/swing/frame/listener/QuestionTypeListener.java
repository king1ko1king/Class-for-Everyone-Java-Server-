package com.wanli.swing.frame.listener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.wanli.swing.frame.questiontype.BeginningComposite;
import com.wanli.swing.frame.questiontype.ChoiceComposite;
import com.wanli.swing.frame.questiontype.FillInTheBlanksComposite;
import com.wanli.swing.frame.questiontype.TrueOrFalseComposite;
import com.wanli.utils.AddToQuestionList;
import com.wanli.utils.StaticVariable;

/**
 * ����ѡ�������
 * @author wanli
 *
 */
public class QuestionTypeListener implements SelectionListener {

	private Composite parent;
	private Combo questionTypeCombo;	// ѡ������������
	private int index;					// ���������ѡ�е���
	
	public QuestionTypeListener(Composite parent, Combo questionTypeCombo) {
		this.parent = parent;
		this.questionTypeCombo = questionTypeCombo;
	}
	
	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		String select = "true";		// ����Ƿ��л�����
		index = questionTypeCombo.getSelectionIndex();
		switch (index) {
		case 0:
			// ������е�map
			clearAllMap();
			if (StaticVariable.nextOption != null) {
				StaticVariable.nextOption.dispose();
			}
			StaticVariable.questionCom.dispose();
			StaticVariable.questionCom = new BeginningComposite(parent, SWT.BORDER);
			// ���²���parent������Ҫ�У������޷���ʾ
			parent.layout();
			break;

		// ѡ����
		case 1:
			if (!StaticVariable.firstOpenPrepareLessonsShell) {
				// ��ǰ�Ѿ���ѡ��������
				if (StaticVariable.questionType != null && StaticVariable.questionType.equals("choice")) {
					// �жϵ�ǰ������û����д���ݣ�����ѯ���Ƿ��������
					select = AddToQuestionList.judgeTextValue(StaticVariable.choiceAllText);
				} else if (StaticVariable.questionType != null && StaticVariable.questionType.equals("true_or_false")) {
					// �л����Ƿ��⣬�жϵ�ǰ������û����д���ݣ�����ѯ���Ƿ��������
					select = AddToQuestionList.judgeTextValue(StaticVariable.trueOrFalseAllText);
				} else if (StaticVariable.questionType != null && StaticVariable.questionType.equals("fill_in_the_blanks")) {
					// �л�������⣬�жϵ�ǰ������û����д���ݣ�����ѯ���Ƿ��������
					select = AddToQuestionList.judgeTextValue(StaticVariable.fillblanksAllText);
				}
				StaticVariable.firstOpenPrepareLessonsShell = false;
			}
			if (select.equals("true")) {
				// ������е�map
				clearAllMap();
				StaticVariable.questionType = "choice";
				if (StaticVariable.nextOption != null) {
					StaticVariable.nextOption.dispose();
				}
				System.out.println("ѡ����");
				StaticVariable.questionCom.dispose();
				StaticVariable.questionCom = new ChoiceComposite(parent, SWT.BORDER);
				// ���²���parent������Ҫ�У������޷���ʾ
				parent.layout();				
			}
			break;
			
		// �Ƿ���
		case 2:
			if (!StaticVariable.firstOpenPrepareLessonsShell) {
				// ��ǰ�Ѿ����Ƿ���
				if (StaticVariable.questionType != null && StaticVariable.questionType.equals("true_or_false")) {
					// �жϵ�ǰ������û����д���ݣ�����ѯ���Ƿ��������
					select = AddToQuestionList.judgeTextValue(StaticVariable.trueOrFalseAllText);
				} else if (StaticVariable.questionType != null && StaticVariable.questionType.equals("choice")) {
					// �л���ѡ���⣬�жϵ�ǰ������û����д���ݣ�����ѯ���Ƿ��������
					select = AddToQuestionList.judgeTextValue(StaticVariable.choiceAllText);
				} else if (StaticVariable.questionType != null && StaticVariable.questionType.equals("fill_in_the_blanks")) {
					// �л�������⣬�жϵ�ǰ������û����д���ݣ�����ѯ���Ƿ��������
					select = AddToQuestionList.judgeTextValue(StaticVariable.fillblanksAllText);
				}
				StaticVariable.firstOpenPrepareLessonsShell = false;
			}
			if (select.equals("true")) {
				// ������е�map
				clearAllMap();
				StaticVariable.questionType = "true_or_false";
				if (StaticVariable.nextOption != null) {
					StaticVariable.nextOption.dispose();
				}
				System.out.println("�Ƿ���");
				StaticVariable.questionCom.dispose();
				StaticVariable.questionCom = new TrueOrFalseComposite(parent, SWT.BORDER);
				// ���²���parent������Ҫ�У������޷���ʾ
				parent.layout();				
			}
			break;
			
		// �����
		case 3:
			if (!StaticVariable.firstOpenPrepareLessonsShell) {
				// ��ǰ�Ѿ��������
				if (StaticVariable.questionType != null && StaticVariable.questionType.equals("fill_in_the_blanks")) {
					// �жϵ�ǰ������û����д���ݣ�����ѯ���Ƿ�������� 
					select = AddToQuestionList.judgeTextValue(StaticVariable.fillblanksAllText);
				} else if (StaticVariable.questionType != null && StaticVariable.questionType.equals("choice")) {
					// �л���ѡ���⣬�жϵ�ǰ������û����д���ݣ�����ѯ���Ƿ��������
					select = AddToQuestionList.judgeTextValue(StaticVariable.choiceAllText);
				} else if (StaticVariable.questionType != null && StaticVariable.questionType.equals("true_or_false")) {
					// �л����Ƿ��⣬�жϵ�ǰ������û����д���ݣ�����ѯ���Ƿ��������
					select = AddToQuestionList.judgeTextValue(StaticVariable.trueOrFalseAllText);
				}
				StaticVariable.firstOpenPrepareLessonsShell = false;
			}
			if (select.equals("true")) {
				clearAllMap();
				StaticVariable.questionType = "fill_in_the_blanks";
				if (StaticVariable.nextOption != null) {
					StaticVariable.nextOption.dispose();
				}
				System.out.println("�����");
				StaticVariable.questionCom.dispose();
				StaticVariable.questionCom = new FillInTheBlanksComposite(parent, SWT.BORDER);
				// ���²���parent������Ҫ�У������޷���ʾ
				parent.layout();				
			}
			break;
		}
	}

	/**
	 * ������е�map
	 */
	private void clearAllMap() {
		StaticVariable.choiceAllText.clear();
		StaticVariable.trueOrFalseAllText.clear();
		StaticVariable.fillblanksAllText.clear();
	}
	
}
