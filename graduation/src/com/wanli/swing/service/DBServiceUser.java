package com.wanli.swing.service;

import java.util.List;

import com.wanli.swing.dao.DBDaoUser;

/**
 * 操作DBDaoUser的业务
 * @author wanli
 *
 */
public class DBServiceUser {

	private DBDaoUser daoUser = null;
	
	public DBServiceUser() {
		daoUser = new DBDaoUser();
	}
	
	/**
	 * 往graduation_user数据库中的userbean表插入一条记录
	 * @param userInfo：用户信息数组
	 * @return
	 */
	public boolean addUser(String[] userInfo) {
		return daoUser.addUser(userInfo);
	}
	
	/**
	 * 根据用户名和密码查询用户
	 * @param name:用户名
	 * @param password:密码
	 * @return
	 */
	public String getUserByNameAndPassword(String name, String password) {
		return daoUser.getUserByNameAndPassword(name, password);
	}
	
	/**
	 * 通过手机号码修改帐号密码
	 * @param parameter:参数可能是电话号码，也可能是邮箱
	 * @param password:新密码
	 */
	public void updatePassword(String parameter, String password) {
		daoUser.updatePassword(parameter, password);
	}
	
	/**
	 * 根据手机号或者邮箱查找用户
	 * @param username
	 * @return
	 */
	public boolean getByUsername(String username) {
		return daoUser.getByUsername(username);
	}
	
	/**
	 * 根据手机号码或者邮箱获取用户的昵称
	 * @param username
	 * @return
	 */
	public String getNicknameByPhoneOrEmail(String username) {
		return daoUser.getNicknameByPhoneOrEmail(username);
	}
	
	/**
	 * 获取所有的班级和专业信息
	 * @return
	 */
	public List<String> getAllClass() {
		return daoUser.getAllClass();
	}
	
	/**
	 * 增加班级或专业
	 * @param className
	 */
	public void addClass(String className) {
		daoUser.addClass(className);
	}
}
