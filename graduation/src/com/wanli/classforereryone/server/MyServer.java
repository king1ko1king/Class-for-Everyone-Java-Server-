package com.wanli.classforereryone.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wanli.swing.entities.OnlineUser;
import com.wanli.utils.StaticVariable;

public class MyServer implements Runnable {

	//���屣������Socket��Map
//	public static Map<String, OnlineUser> users = new HashMap<>();
	private ServerSocket ss = null;
	
	public MyServer() {
		try {
			ss = new ServerSocket(30000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			//���д������������һֱ�ȴ����˵�����
			Socket s = null;
			try {
				s = ss.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (s != null) {
				System.out.println("�ͻ������ӳɹ�������");
				try {
					StaticVariable.users.put(s.getInetAddress().toString().substring(1), new OnlineUser(s));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//ÿ���ͻ������Ӻ�����һ��ServerThread�߳�Ϊ�ÿͻ��˷���
			try {
				new Thread(new ServerThread(s)).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
