package com.wanli.thread;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;

import com.wanli.classforereryone.server.ServerThread;
import com.wanli.utils.StaticVariable;

public class ListeningSocket implements Runnable  {

	private int socketNum = 0;	// �Ѿ����ϵĿͻ��˵�����
	
	public ListeningSocket() {
		// ��ȡ��ǰ�Ѿ����ϵĿͻ��˵�����
		socketNum = StaticVariable.users.size();
	}
	
	@Override
	public void run() {
		while (true) {
			// StaticVariable.users.size() > socketNum��ʾ���µĿͻ�������
			if (StaticVariable.rooms.size() > 0 && StaticVariable.users.size() > socketNum) {
				if (ServerThread.getIpAddress() != "") {
					// �������ϵĿͻ��˵�����
					socketNum = StaticVariable.users.size();
					// ������ʾ��������������
					StaticVariable.onlineNumsInt++;
					// ����ʾ�б������һ����ʾ�����ϵĿͻ�����Ϣ
					Display.getDefault().syncExec(new Runnable(){
						public void run() {
							TreeItem treeItem = new TreeItem(StaticVariable.rooms.get(0), SWT.NONE);
							treeItem.setText(ServerThread.getIpAddress());
							StaticVariable.onlineUsers.put(ServerThread.getIpAddress(), treeItem);
							StaticVariable.onlining.setText(StaticVariable.onlineNumsStr + StaticVariable.onlineNumsInt);
						}
						
					});
				}
			} else {
				// û�пͻ��������ϣ��������������һ���ǿͻ��˶Ͽ����ӣ�һ���Ǳ���ԭ����û�жϿ�Ҳû������
				Display.getDefault().syncExec(new Runnable(){
					public void run() {
						if (StaticVariable.onlineUsers != null && StaticVariable.onlineUsers.size() > 0) {
							// �ж��Ƿ��пͻ��˶Ͽ�����
							if (StaticVariable.quitSocket != "") {
								// ������ʾ��������������
								StaticVariable.onlineNumsInt--;
								// ���µ�ǰ�Ѿ������ϵĿͻ��˵�����
								socketNum = StaticVariable.users.size();
								// ���Ѿ��Ͽ��Ŀͻ��˴��б����Ƴ�
								StaticVariable.onlineUsers.get(StaticVariable.quitSocket).dispose();
								// ���ñ�ǣ����ж��Ƿ��пͻ����˳��ı����Ϊ��
								StaticVariable.quitSocket = "";
								StaticVariable.onlining.setText(StaticVariable.onlineNumsStr + StaticVariable.onlineNumsInt);
							}
						}
					}
				});
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
