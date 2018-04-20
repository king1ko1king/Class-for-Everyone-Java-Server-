package com.wanli.swing.frame.questiontype;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import com.wanli.swing.frame.listener.NextOptionListener;
import com.wanli.utils.StaticVariable;

public class ChoiceComposite extends Composite {

	private Composite parent;
	private int ascoll = 69; 						// ������ĸ��ascoll�룬Ĭ��Ϊ��ĸE��ascoll��
	private List<Label> labels = new ArrayList<>(); // ��������ѡ��ʱ��Label���
	private List<Text> texts = new ArrayList<>(); 	// ��������ѡ��ʱ��Text���
	private String key = "";						// ������һ��ѡ���key

	public ChoiceComposite(Composite parent, int parameters) {
		super(parent, parameters);
		this.parent = parent;
		addComposite();
	}

	public void addComposite() {
		ChoiceComposite choiceCom = this;
		choiceCom.setLayout(new GridLayout(2, false));
		GridData choiceGrid = new GridData(GridData.FILL_BOTH);
		choiceGrid.horizontalSpan = 2;
		choiceCom.setLayoutData(choiceGrid);

		// ��Ŀ
		Label questionLabel = new Label(choiceCom, SWT.NONE);
		questionLabel.setText("��Ŀ��");
		Text questionText = new Text(choiceCom, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		// ����questionTextΪ����ʽ���֣���ˮƽռ����
		GridData questionGrid = new GridData(GridData.FILL_BOTH);
		questionGrid.horizontalSpan = 2;
		questionText.setLayoutData(questionGrid);
		
		// ����ѡ��𰸵Ĳ���
		GridData optionGrid = new GridData(GridData.FILL_HORIZONTAL);

		// ��
		Label answerLabel = new Label(choiceCom, SWT.NONE);
		answerLabel.setText("�𰸣�");
		Text answerText = new Text(choiceCom, SWT.BORDER);
		answerText.setLayoutData(optionGrid);

		// ����ѡ��
		Button addOption = new Button(choiceCom, SWT.NONE);
		addOption.setText("����ѡ��");
		addOption.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Label option = new Label(choiceCom, SWT.NONE);
				option.setText("ѡ��" + (char) ascoll + "��");
				Text optionText = new Text(choiceCom, SWT.BORDER);
				optionText.setLayoutData(optionGrid);
				choiceCom.layout();
				labels.add(option);
				texts.add(optionText);
				key = "option" + (char) ascoll;
				StaticVariable.choiceAllText.put(key, optionText);
				ascoll++;
			}
		});
		// ɾ�������ѡ��
		Button delOption = new Button(choiceCom, SWT.NONE);
		delOption.setText("ɾ��ѡ��");
		delOption.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (labels.size() > 0) {
					ascoll--;
					key = "option" + (char) ascoll;
					StaticVariable.choiceAllText.remove(key);
					labels.get(labels.size() - 1).dispose();
					texts.get(texts.size() - 1).dispose();
					labels.remove(labels.size() - 1);
					texts.remove(texts.size() - 1);
					choiceCom.layout();
//					ascoll--;
				} else {
					MessageBox messageBox = new MessageBox(parent.getShell(), SWT.YES);
					messageBox.setText("��ʾ");
					messageBox.setMessage("������ɾ��ѡ���ˣ�");
					messageBox.open();
				}
			}
		});

		// Ĭ���ĸ�ѡ��
		Label optionA = new Label(choiceCom, SWT.NONE);
		optionA.setText("ѡ��A��");
		Text optionTextA = new Text(choiceCom, SWT.BORDER);
		optionTextA.setLayoutData(optionGrid);
		Label optionB = new Label(choiceCom, SWT.NONE);
		optionB.setText("ѡ��B��");
		Text optionTextB = new Text(choiceCom, SWT.BORDER);
		optionTextB.setLayoutData(optionGrid);
		Label optionC = new Label(choiceCom, SWT.NONE);
		optionC.setText("ѡ��C��");
		Text optionTextC = new Text(choiceCom, SWT.BORDER);
		optionTextC.setLayoutData(optionGrid);
		Label optionD = new Label(choiceCom, SWT.NONE);
		optionD.setText("ѡ��D��");
		Text optionTextD = new Text(choiceCom, SWT.BORDER);
		optionTextD.setLayoutData(optionGrid);
		// ��ChoiceComposite����е�����Text�������
		StaticVariable.choiceAllText.put("question", questionText);
		StaticVariable.choiceAllText.put("answer", answerText);
		StaticVariable.choiceAllText.put("optionA", optionTextA);
		StaticVariable.choiceAllText.put("optionB", optionTextB);
		StaticVariable.choiceAllText.put("optionC", optionTextC);
		StaticVariable.choiceAllText.put("optionD", optionTextD);

		// ��һ��
		Button nextOption = new Button(parent, SWT.NONE);
		nextOption.setText("��һ��");
		GridData nextGrid = new GridData(GridData.FILL_HORIZONTAL);
		nextGrid.horizontalSpan = 2;
		nextOption.setLayoutData(nextGrid);
		StaticVariable.nextOption = nextOption;
		nextOption.addSelectionListener(new NextOptionListener());
	}
}
