package com.wanli.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TreeItem;

import com.wanli.swing.entities.OnlineUser;

public class StaticVariable {
	
	public static Map<String, OnlineUser> users = new HashMap<>();	// ��������Socket��Map
	public static String quitSocket = "";							// ��¼�Ͽ����ӵ�socket�������Ƴ�TreeItem
	public static String onlineNumsStr = "����������";					// ��ʾ��������
	public static int onlineNumsInt = 0;							// ͳ����������
	public static Label onlining;									// ��ʾ����������Label
	public static StyledText text;									// ��Ŀ�ı���ʾ
	public static Button refresh;									// ˢ�³ɼ���ť
	public static Button scoreChartBtn;								// ��ͼ�����ʽ��ʾ��ǰ�ɼ�����
	public static Button historyCharBtn;							// ��ͼ�����ʽ��ʾ��ʷ�ɼ�����
	public static Table scoreTab;									// ��ʾ�ɼ����
	public static Table historyTab;									// ��ʾ��ʷ�ɼ����
	public static Combo historyCombo;								// ������ʷ����������
	public static String[] questions;								// ������������
	public static int index = 1;									// ��ǵڼ���
	public static ArrayList<TreeItem> rooms = new ArrayList<>();	// �洢���������н���
	public static String instruction;								// ���������ͻ��˷��͵�ָ��
	public static String tableName;									// ���������ڲ�ѯ���ݿ��еı�����
	public static String className;									// ��¼�����Ľ��ҵ�����
	public static Map<String, TreeItem> onlineUsers = new HashMap<>();// ��ʾ��������������Tree

}
