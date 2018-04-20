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

	//定义保存所有Socket的Map
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
			//此行代码会阻塞，将一直等待别人的连接
			Socket s = null;
			try {
				s = ss.accept();
System.out.println(s.getInetAddress().toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (s != null) {
				System.out.println("客户端连接成功！！！");
				try {
					StaticVariable.users.put(s.getInetAddress().toString().substring(1), new OnlineUser(s));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//每当客户端连接后启动一条ServerThread线程为该客户端服务
			try {
				new Thread(new ServerThread(s)).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
