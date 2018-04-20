package com.wanli.swing.frame.text.menu.listener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;

import com.wanli.utils.StaticVariable;

public class SetTextColor implements SelectionListener {

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		// ������ɫѡ��Ի���
		ColorDialog dlg = new ColorDialog(StaticVariable.parent.getShell());
		// �򿪶Ի���
		RGB rgb = dlg.open();
		if (rgb != null) {
			// ���� color ����
			StaticVariable.color = new Color(StaticVariable.parent.getShell().getDisplay(), rgb);
			// ���� point ���󣬻�ȡѡ��Χ��
			Point point = StaticVariable.text.getSelectionRange();
			for (int i = point.x; i < point.x + point.y; i++) {
				// ���ѡ�е�������ʽ�ͷ�Χ
				StaticVariable.range = StaticVariable.text.getStyleRangeAtOffset(i);
				// �������������������ʽ( ��Ӵ֡�б�塢���»���)
				if (StaticVariable.range != null) {
					/**
					 * ����һ����ԭ�� StyleRange ��ֵ��ͬ�� StyleRange
					 */
					StaticVariable.style = (StyleRange) StaticVariable.range.clone();
					StaticVariable.style.start = i;
					StaticVariable.style.length = 1;
					// ����ǰ����ɫ
					StaticVariable.style.foreground = StaticVariable.color;
				} else {

					StaticVariable.style = new StyleRange(i, 1, StaticVariable.color, null, SWT.NORMAL);
				}
				StaticVariable.text.setStyleRange(StaticVariable.style);
			}
		}
	}

}
