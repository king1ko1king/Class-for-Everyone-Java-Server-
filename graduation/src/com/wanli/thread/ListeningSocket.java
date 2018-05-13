package com.wanli.thread;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;

import com.wanli.classforereryone.server.ServerThread;
import com.wanli.utils.StaticVariable;

public class ListeningSocket implements Runnable  {

	private int socketNum = 0;	// 已经连上的客户端的数量
	
	public ListeningSocket() {
		// 获取当前已经连上的客户端的数量
		socketNum = StaticVariable.users.size();
	}
	
	@Override
	public void run() {
		while (true) {
			// StaticVariable.users.size() > socketNum表示有新的客户端连上
			if (StaticVariable.rooms.size() > 0 && StaticVariable.users.size() > socketNum) {
				if (ServerThread.getIpAddress() != "") {
					// 更新连上的客户端的数量
					socketNum = StaticVariable.users.size();
					// 更新显示在线人数的数量
					StaticVariable.onlineNumsInt++;
					// 在显示列表中添加一个显示刚连上的客户端信息
					Display.getDefault().syncExec(new Runnable(){
						public void run() {
//							TreeItem treeItem = new TreeItem(StaticVariable.rooms.get(0), SWT.NONE);
//							treeItem.setText(ServerThread.getUserName());
//							StaticVariable.onlineUsers.put(ServerThread.getIpAddress(), treeItem);
							StaticVariable.onlining.setText(StaticVariable.onlineNumsStr + StaticVariable.onlineNumsInt);
						}
						
					});
				}
			} else {
				// 没有客户端连接上，则有两种情况，一种是客户端断开连接，一种是保持原样，没有断开也没有连接
				Display.getDefault().syncExec(new Runnable(){
					public void run() {
						if (StaticVariable.onlineUsers != null && StaticVariable.onlineUsers.size() > 0) {
							// 判断是否有客户端断开连接
							if (StaticVariable.quitSocket != "") {
								// 更新显示在线人数的数量
								StaticVariable.onlineNumsInt--;
								// 更新当前已经连接上的客户端的数量
								socketNum = StaticVariable.users.size();
								// 将已经断开的客户端从列表中移除
								StaticVariable.onlineUsers.get(StaticVariable.quitSocket).dispose();
								// 重置标记，将判断是否有客户端退出的标记置为空
								StaticVariable.quitSocket = "";
								StaticVariable.onlining.setText(StaticVariable.onlineNumsStr + StaticVariable.onlineNumsInt);
							}
						}
					}
				});
			}
			
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
		
	}

}
