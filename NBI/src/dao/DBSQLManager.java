package dao;

import java.sql.*;

public class DBSQLManager{
	protected Connection conn = null;	//Connection����
	protected Statement stmt = null;	//Statement����
	protected ResultSet rs = null;		//��¼�����
	protected String sqlStr;			//SQL���
	
	public DBSQLManager() {
		try
		{
			sqlStr = "";
			DBConnectionManager dcm = new DBConnectionManager();
			conn = dcm.getConnection();
			stmt = conn.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE,java.sql.ResultSet.CONCUR_UPDATABLE);
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
	}

	public Statement getStmt() {
		return stmt;
	}

	public Connection getConn() {
		return conn;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setSqlStr(String newSqlStr) {
		this.sqlStr=newSqlStr;
	}


	public String getSqlStr() {
		return sqlStr;
	}

	public void executeQuery() throws Exception  {
		rs = stmt.executeQuery(sqlStr);
	}

	
	public void executeUpdate() throws Exception  {
		stmt.executeUpdate(sqlStr);
	}
	
	public void close() throws SQLException {
		if ( stmt != null ) {
			stmt.close();
			stmt = null;
		}
		conn.close();
		conn = null;
	}

};
