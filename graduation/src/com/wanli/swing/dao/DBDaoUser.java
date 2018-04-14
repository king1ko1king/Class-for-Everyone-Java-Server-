package com.wanli.swing.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.wanli.utils.DBUtilsUser;

/**
 * ����userbean���ű�
 * @author wanli
 *
 */
public class DBDaoUser {

	/**
	 * ��graduation_user���ݿ��е�userbean������һ����¼
	 * @param userInfo���û���Ϣ����
	 * @return
	 */
	public boolean addUser(String[] userInfo) {
	
		PreparedStatement preparedStatement = null;
		String sql = "insert into userbean(NAME, NICKNAME, PASSWORD, EMAIL) values(?, ?, ?, ?)";
		Connection connection = DBUtilsUser.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (int i = 0; i < userInfo.length; i++) {
				preparedStatement.setString(i + 1, userInfo[i]);
			}
			int back = preparedStatement.executeUpdate();
			if (back == 1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * �����û����������ѯ�û�
	 * @param name:�û���
	 * @param password:����
	 * @return
	 */
	public boolean getUserByNameAndPassword(String name, String password) {
		boolean is_get = false;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = "select * from userbean where name = ? and password = ?";
		Connection connection = DBUtilsUser.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setString(2, password);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				is_get = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return is_get;
	}
	
	/**
	 * ͨ���ֻ������޸��ʺ�����
	 * @param parameter:���������ǵ绰���룬Ҳ����������
	 * @param password:������
	 */
	public void updatePassword(String parameter, String password) {
		PreparedStatement statement = null;
		String sql = "";
		boolean result = parameter.matches("[0-9]+");
		if (result) {
			sql = "update userbean set password = ? where name = ?";			
		} else {
			sql = "update userbean set password = ? where email = ?";
		}
		Connection connection = DBUtilsUser.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, password);
			statement.setString(2, parameter);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		DBDaoUser daoUser = new DBDaoUser();
		String[] user = {"14433333333", "���ĺ�3141904210", "123456", "RRT@qq.com"};
		daoUser.addUser(user);
	}
	
}