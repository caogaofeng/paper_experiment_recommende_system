package dao;
import java.sql.*;

public class DBConnectionManager {
	private String driverName = "com.microsoft.jdbc.sqlserver.SQLServerDriver"; 
	private String url = "jdbc:sqlserver://localhost:1433;databaseName=MovieLens";
	private String user = "sa";
	private String password = "123456";

	public void setDriverName(String newDriverName) {
        	this.driverName = newDriverName;
    		}
    public String getDriverName() {
        	return driverName;
    		}
	
	public void setUrl(String newUrl) {
	        this.url = newUrl;
	    	}
   	
	public String getUrl() {
	        return url;
	    	}
	public void setUser(String newUser) {
	        this.user = newUser;
		}
	public String getUser() {
	        return user;
		}
	public void setPassword(String newPassword) {
	        this.password = newPassword;
	    	}
	public String getPassword() {
	        return password;
	    	}
	
	public Connection getConnection() {
	     try 
{
	         Class.forName(driverName);
	         return DriverManager.getConnection(url, user, password);
	     }
	        catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        	}
	    	}
	public static void main(String[] args) {
		System.out.print(new DBConnectionManager().getConnection().toString());
	}
	
}
