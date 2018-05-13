package com.wanli.swing.service;

import java.util.List;
import java.util.Map;

import com.wanli.swing.dao.DBDao;
import com.wanli.swing.entities.QuestionType;

public class DBService {

	private DBDao dbDao = null;
	
	public DBService() {
		dbDao = new DBDao();
	}
	
	/**
	 * 创建表
	 * @param allQuestionList：存储所有问题的list
	 * @param tableName：指定表名
	 */
	public void createTable(List<QuestionType> allQuestionList, String tableName) {
		dbDao.createTable(allQuestionList, tableName);
	}
	
	/**
	 * 获取成绩数据
	 * @param tableName：表名
	 * @return 返回获取的成绩数据
	 */
	public List<String[]> getScoreData(String tableName) {
		return dbDao.getScoreData(tableName);
	}
	
	/**
	 * 获取表的总列数
	 * @param tableName：表名
	 * @return 列数
	 */
	public int getTableColumn(String tableName) {
		return dbDao.getTableColumn(tableName);
	}
	
	/**
	 * 获取graduation_scoretab数据库的所有表名
	 * @return:返回表名的list
	 */
	public List<String> getTableList() {
		return dbDao.getTableList();
	}
	
	/**
	 * 向表中添加数据
	 * @param userName：用户名
	 * @param tableName：表名
	 * @param answers：某一题用户所有的回答
	 * @param columnNum：第几题
	 */
	public void addRecord(String tableName, Map<String, String> answers, int columnNum) {
		dbDao.addRecord(tableName, answers, columnNum);
	}
	
	/**
	 * 获取表中的最后一行的统计数据
	 * @param tableName
	 * @return
	 */
	public List<String> getStatisticalData(String tableName) {
		return dbDao.getStatisticalData(tableName); 
	}
	
	/**
	 * 查询所有题目的类型
	 * @param tableName
	 * @return
	 */
	public List<String> getAllQuestionType(String tableName) {
		return dbDao.getAllQuestionType(tableName);
	}
}
