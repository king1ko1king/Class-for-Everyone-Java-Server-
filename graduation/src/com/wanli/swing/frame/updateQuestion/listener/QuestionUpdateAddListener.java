package com.wanli.swing.frame.updateQuestion.listener;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import com.wanli.swing.frame.PrepareLessons;
import com.wanli.swing.frame.QuestionManagerShell;
import com.wanli.utils.StaticVariable;

public class QuestionUpdateAddListener implements SelectionListener {

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		new PrepareLessons(StaticVariable.parent, "×·¼ÓÊÔÌâ", "×·     ¼Ó", true);
	}

}
