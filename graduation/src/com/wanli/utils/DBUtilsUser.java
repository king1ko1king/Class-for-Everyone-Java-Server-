package com.wanli.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtilsUser {

	public static Connection getConnection() {
		//����Connection����
		Connection conn;
		//������������
		String driverClass = "com.mysql.jdbc.Driver";
		//URLָ��Ҫ���ʵ����ݿ���graduation_design
		String url = "jdbc:mysql:///graduation_user";
		//Mysql����ʱ���û���
		String user = "root";
		//Mysql����ʱ������
		String password = "123456";
		
		try {
			//������������
			Class.forName(driverClass);
			//��ȡ���ݿ�����
			conn = DriverManager.getConnection(url, user, password);
			return conn;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = "select * from userbean where name = ? and password = ?";
		Connection connection = DbUtilsScoreTab.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, "1");
			statement.setString(2, "1");
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				System.out.println("��ֵ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}