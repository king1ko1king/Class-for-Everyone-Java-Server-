package com.wanli.swing.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wanli.utils.DBUtilsUser;
import com.wanli.utils.DbUtilsScoreTab;

/**
 * 操作userbean这张表
 * @author wanli
 *
 */
public class DBDaoUser {

	/**
	 * 往graduation_user数据库中的userbean表插入一条记录
	 * @param userInfo：用户信息数组
	 * @return
	 */
	public boolean addUser(String[] userInfo) {
	
		PreparedStatement preparedStatement = null;
		String sql = "insert into userbean(NAME, NICKNAME, PASSWORD, EMAIL) values(?, ?, ?, ?)";
		Connection connection = DBUtilsUser.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (int i = 0; i < userInfo.length - 2; i++) {
				preparedStatement.setString(i + 1, userInfo[i + 1]);
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
	 * 根据用户名和密码查询用户
	 * @param name:用户名
	 * @param password:密码
	 * @return
	 */
	public String getUserByNameAndPassword(String name, String password) {
		String nickName = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Pattern pattern = null;
		Matcher matcher = null;
		String sql;
		String regexPhone = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";
		String regexEmail = "^([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";
		pattern = Pattern.compile(regexPhone);
		matcher = pattern.matcher(name);
		if (matcher.matches()) {
			System.out.println("手机格式正确");
			sql = "select nickname from userbean where name = ? and password = ?";
		} else {
			pattern = Pattern.compile(regexEmail);
			matcher = pattern.matcher(name);
			if (matcher.matches()) {
				System.out.println("邮箱格式正确");
				sql = "select nickname from userbean where email = ? and password = ?";
			} else {
				return null;
			}
		}
		Connection connection = DBUtilsUser.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setString(2, password);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				nickName = resultSet.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nickName;
	}
	
	/**
	 * 通过手机号码或者邮箱修改帐号密码
	 * @param parameter:参数可能是电话号码，也可能是邮箱
	 * @param password:新密码
	 */
	public boolean updatePassword(String parameter, String password) {
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
		return true;
	}
	
	/**
	 * 根据手机号或者邮箱查找用户
	 * @param username
	 * @return
	 */
	public boolean getByUsername(String username) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = "";
		boolean result = username.matches("[0-9]+");
		if (result) {
			sql = "select * from userbean where name = ?";			
		} else {
			sql = "select * from userbean where email = ?";
		}
		Connection connection = DBUtilsUser.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 根据手机号码或者邮箱获取用户的昵称
	 * @param username
	 * @return
	 */
	public String getNicknameByPhoneOrEmail(String username) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = "";
		boolean result = username.matches("[0-9]+");
		if (result) {
			sql = "select nickname from userbean where name = ?";			
		} else {
			sql = "select nickname from userbean where email = ?";
		}
		Connection connection = DBUtilsUser.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString(1);
			}
		} catch (SQLException e) {
			System.out.println("数据库连接失败！！！");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查找所有的班级或者专业记录
	 * @param tableName
	 * @return
	 */
	public List<String> getAllClass() {
		List<String> allClass = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		String sql = "select * from class_or_specialty";
		connection = DBUtilsUser.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				allClass.add(resultSet.getString(2));
			}
		} catch (SQLException e) {
			System.out.println("数据库连接失败！");
			e.printStackTrace();
		}
		return allClass;
	}
	
	/**
	 * 增加班级或专业
	 * @param className
	 */
	public void addClass(String className) {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		String sql = "insert into class_or_specialty(name) values(?)";
		connection = DBUtilsUser.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, className);
			preparedStatement.execute();
		} catch (SQLException e) {
			System.out.println("数据库连接失败！");
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		DBDaoUser daoUser = new DBDaoUser();
//		String[] user = {"14433333333", "陈文豪3141904210", "123456", "RRT@qq.com"};
//		daoUser.addUser(user);
//		System.out.println(daoUser.getUserByNameAndPassword("124@qq.com", "123456"));;
//		System.out.println(daoUser.getByUsername("17759083295"));
//		System.out.println(daoUser.getNicknameByPhoneOrEmail("13344444444"));
		System.out.println(daoUser.getUserByNameAndPassword("13344444444", "123456"));
	}
	
}
