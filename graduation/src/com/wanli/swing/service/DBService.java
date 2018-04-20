package com.wanli.swing.service;

import java.util.List;
import java.util.Map;

import com.wanli.swing.dao.DBDao;

public class DBService {

	private DBDao dbDao = null;
	
	public DBService() {
		dbDao = new DBDao();
	}
	
	/**
	 * ������
	 * @param num��ָ������
	 * @param tableName��ָ������
	 */
	public void createTable(int num, String tableName) {
		dbDao.createTable(num, tableName);
	}
	
	/**
	 * ��ȡ�ɼ�����
	 * @param tableName������
	 * @return ���ػ�ȡ�ĳɼ�����
	 */
	public List<String[]> getScoreData(String tableName) {
		return dbDao.getScoreData(tableName);
	}
	
	/**
	 * ��ȡ���������
	 * @param tableName������
	 * @return ����
	 */
	public int getTableColumn(String tableName) {
		return dbDao.getTableColumn(tableName);
	}
	
	/**
	 * ��ȡgraduation_scoretab���ݿ�����б���
	 * @return:���ر�����list
	 */
	public List<String> getTableList() {
		return dbDao.getTableList();
	}
	
	/**
	 * ������������
	 * @param userName���û���
	 * @param tableName������
	 * @param answers��ĳһ���û����еĻش�
	 * @param columnNum���ڼ���
	 */
	public void addRecord(String tableName, Map<String, String> answers, int columnNum) {
		dbDao.addRecord(tableName, answers, columnNum);
	}
	
	/**
	 * ��ȡ���е����һ�е�ͳ������
	 * @param tableName
	 * @return
	 */
	public List<String> getStatisticalData(String tableName) {
		return dbDao.getStatisticalData(tableName); 
	}
}
