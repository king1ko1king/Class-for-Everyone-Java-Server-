package com.wanli.swing.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wanli.utils.DbUtilsScoreTab;
import com.wanli.utils.StaticVariable;

/**
 * �������ݿ��Dao����
 * @author wanli
 *
 */
public class DBDao {

	/**
	 * ������
	 * @param num��numֵָ�������ı��ж�����
	 */
	public void createTable(int num, String tableName) {
		List<String> titles = new ArrayList<>();
		String title = "title";
		String sql = "create table " + tableName + "(username char(30), ";
		PreparedStatement statement = null;
		Connection connection = DbUtilsScoreTab.getConnection();
		for (int i = 1; i <= num; i++) {
			titles.add(title + i);
			if (i == num) {
				sql = sql + titles.get(i - 1) + " char(30))";
				break;
			}
			sql = sql + titles.get(i - 1) + " char(30),";
			
		}
		try {
			statement = connection.prepareStatement(sql);
			statement.execute();
			StaticVariable.firstInsert = true;
		} catch (SQLException e) {
//			e.printStackTrace();
			System.out.println("������ʧ�ܣ����Ѿ����ڣ����޸��ļ������Ա�֤����ɹ���");
			StaticVariable.firstInsert = false;
		}
	}
	
	/**
	 * ��ȡָ���������
	 * @param tableName������
	 */
	public List<String[]> getScoreData(String tableName) {
		List<String[]> list = new ArrayList<>();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = "select * from " + tableName;
//		System.out.println(sql);
		Connection connection = DbUtilsScoreTab.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();
			int colCount = metaData.getColumnCount();
			resultSet.beforeFirst();
			while (resultSet.next()) {
				String[] titles = new String[colCount];
				for (int i = 1; i <= colCount; i++) {
					titles[i - 1] = resultSet.getString(i);
				}
				list.add(titles);
			}
		} catch (SQLException e) {
			System.out.println("���ݿ�����ʧ�ܣ�");
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * ��ȡ���������
	 * @param tableName
	 * @return
	 */
	public int getTableColumn(String tableName) {
		int colCount =  0;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = "select * from " + tableName;
		Connection connection = DbUtilsScoreTab.getConnection();
		
		try {
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();
			colCount = metaData.getColumnCount();
		} catch (SQLException e) {
			System.out.println("���ݿ�����ʧ�ܣ�");
			e.printStackTrace();
		}
		return colCount;
	}
	
	/**
	 * ��ȡgraduation_scoretab���ݿ�����б���
	 * @return:���ر�����list
	 */
	public List<String> getTableList() {
		List<String> tables = new ArrayList<>();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = "show tables";
		Connection connection = DbUtilsScoreTab.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			int count = 0;
			while(resultSet.next()) {
				count++;
				tables.add(resultSet.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("���ݿ�����ʧ�ܣ�");
			e.printStackTrace();
		}
		return tables;
	}
	
	/**
	 * ��ȡһ�ű����������
	 * @param tableName
	 * @return
	 */
	public List<String> getTableColumnName(String tableName) {
		List<String> columns = new ArrayList<>();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ResultSetMetaData metaData = null;
		String sql = "select * from " + tableName;
		Connection connection = DbUtilsScoreTab.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			metaData = resultSet.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				columns.add(metaData.getColumnName(i));
			}
		} catch (SQLException e) {
			System.out.println("���ݿ�����ʧ�ܣ�");
			e.printStackTrace();
		}
		return columns;
	}
	
	/**
	 * ��ȡ���е����һ�е�ͳ������
	 * @param tableName
	 * @return
	 */
	public List<String> getStatisticalData(String tableName) {
		List<String> columns = new ArrayList<>();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ResultSetMetaData metaData = null;
		String sql = "select * from " + tableName + " where username = 'ͳ��'";
		Connection connection = DbUtilsScoreTab.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			metaData = resultSet.getMetaData();
			while (resultSet.next()) {
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					columns.add(resultSet.getString(i));
				}
			}
		} catch (SQLException e) {
			System.out.println("���ݿ�����ʧ�ܣ�");
			e.printStackTrace();
		}
		return columns;
	}
	
	/**
	 * �������Ӽ�¼
	 * @param userName���û���
	 * @param tableName������
	 * @param answers��ĳһ������лش�
	 * @param columnNum���ڼ���
	 */
	public void addRecord(String tableName, Map<String, String> answers, int columnNum) {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		List<String> tableColumn = getTableColumnName(tableName);
		String sql = "";
		connection = DbUtilsScoreTab.getConnection();
		if (StaticVariable.firstInsert) {
			sql = "insert into " + tableName + "(username," + tableColumn.get(columnNum) + ")"
					+ " values(?, ?)";
			for (Map.Entry<String, String> answer: answers.entrySet()) {
				try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, answer.getKey());
					preparedStatement.setString(2, answer.getValue());
					preparedStatement.execute();
				} catch (SQLException e) {
					System.out.println("���ݿ�����ʧ�ܣ�");
					e.printStackTrace();
				}
				
			}
		} else {
			sql = "update " + tableName + " set " + tableColumn.get(columnNum) + "= ? where username = ?" ;
			for (Map.Entry<String, String> answer: answers.entrySet()) {
				try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, answer.getValue());
					preparedStatement.setString(2, answer.getKey());
					preparedStatement.execute();
				} catch (SQLException e) {
					System.out.println("���ݿ�����ʧ�ܣ�");
					e.printStackTrace();
				}
				
			}		
		}
		StaticVariable.firstInsert = false;
//		String sql = "insert into " + tableName + " values(";
//		for (int i = 0; i < tableColumn; i++) {
//			if (i == tableColumn - 1) {
//				sql += "?)";				
//			} else {
//				sql += "?, ";				
//			}
//		}
//		connection = DbUtilsScoreTab.getConnection();
//		try {
//			preparedStatement = connection.prepareStatement(sql);
//			for (int i = 0; i < tableColumn; i++) {
//				if (i == 0) {
//					preparedStatement.setString(i + 1, userName);
//				} else {
//					preparedStatement.setString(i + 1, answers[i - 1]);
//				}
//			}
//			preparedStatement.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}
	
	public static void main(String[] args) {
		DBDao dao = new DBDao();
//		dao.addRecord("11", "ttt");
//		List<String> tables = dao.getTableList();
//		for (String table: tables) {
//			System.out.println(table);
//		}
//		dao.createTable(4, "table1");
//		List<String[]> list = dao.getScoreData("table1");
//		for (String[] record: list) {
//			for (int i = 0; i < record.length; i++) {
//				System.out.print(record[i]);
//				if (i == record.length - 1) {
//					System.out.println();
//				}
//			}
//		}
//		System.out.println(dao.getTableColumn("table1"));
//		System.out.println(dao.getTableColumnName("question"));;
//		StaticVariable.firstInsert = false;
//		Map<String, String> s = new HashMap<>();
//		s.put("wan1", "a b");
//		s.put("wan2", "c");
//		dao.addRecord("question", s, 5);
		System.out.println(dao.getStatisticalData("question"));
	}
	
}
