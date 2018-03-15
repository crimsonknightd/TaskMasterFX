package com.daong.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBCon {
	final String DB_HOST;
	final String DB_NAME;
	final String DB_USER;
	final String DB_PASS;
	final String DB_URL;

	public DBCon() {

		DB_HOST = "localhost";
		DB_NAME = "taskmaster";
		DB_USER = "root";
		DB_PASS = "root";
		DB_URL = "jdbc:mysql://" + DB_HOST + "/" + DB_NAME;
	}

	public DBCon(String db_host, String db_name, String db_user, String db_pass, String db_url) {
		DB_HOST = db_host;
		DB_NAME = db_name;
		DB_USER = db_user;
		DB_PASS = db_pass;
		DB_URL = db_url;
	}


	List<HashMap<String, String>> getArr(String sql) throws Exception {
		List<HashMap<String, String>> rsArr = new ArrayList<HashMap<String, String>>();
		Class.forName("com.mysql.jdbc.Driver");
		try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql);) {

			ResultSetMetaData rsmd = rs.getMetaData();

			String key = null;
			String value = null;

			while (rs.next()){
				HashMap<String, String> hm = new HashMap<String, String>();

				for (int i = 1; i <= rsmd.getColumnCount(); i++){
					key = rsmd.getColumnName(i);
					value = rs.getString(i);
					hm.put(key, value);
				}

				rsArr.add(hm);
			}

		} catch (Exception e) {
			throw e;

		}

		return rsArr;
	}

	int updateDB(String sql) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		int updateCount = 0;
		try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
				Statement stmt = con.createStatement()) {

			updateCount = stmt.executeUpdate(sql);

		} catch (Exception e) {
			throw e;

		}

		return updateCount;
	}




}
