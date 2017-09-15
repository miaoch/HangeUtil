package org.other.game.library.db;

import java.sql.*;  
import java.util.*;  

public class DBHelper {  
	private static String driver_class = "com.mysql.jdbc.Driver";  
	private static String driver_url = "jdbc:mysql://127.0.0.1:3306/library";  
	private static String database_user = "root";  
	private static String database_password = "123456";
    
	private Connection con;  
    public void setCon(Connection con) {  
	    this.con = con;  
	}  
  
    public DBHelper(){  
    	setCon(getConnection()); 
    }  
      
    public static Connection getConnection() {   
        Connection con = null;
        try {  
            Class.forName(driver_class);  
            con = DriverManager.getConnection(driver_url, database_user, database_password);  
        } catch (ClassNotFoundException e) {  
            e.printStackTrace();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }
        return con;  
    }  
  
    public static void closeAll(Connection con, PreparedStatement pst, ResultSet rst) {  
        if(rst!=null){  
            try {  
                rst.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
        if(pst!=null){  
            try {  
                pst.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
        if(con!=null){  
            try {  
                con.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
      
    
    /** 
     * 更新操作
     * @return 
     */  
    public static int executeUpdate(String sql, List<Object> sqlValues) {  
        int result = -1;  
        PreparedStatement pst = null;
        Connection con = null;
        try {  
        	con = getConnection();
            pst = con.prepareStatement(sql);  
            if(sqlValues != null && sqlValues.size() > 0){ 
                setSqlValues(pst, sqlValues);  
            }  
            result = pst.executeUpdate();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        } finally {  
            closeAll(con, pst, null);  
        }  
        return result;  
    }  
      
    /** 
     * 设置参数
     * @param pst 
     * @param sqlValues 
     */  
    private static void setSqlValues(PreparedStatement pst, List<Object> sqlValues){  
        for(int i=0; i<sqlValues.size(); i++){  
            try {  
                pst.setObject(i+1, sqlValues.get(i));  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    
    /**
     * 查询操作
     * @param sql
     * @return
     */
    public static List<Map<String, Object>> executeQuery(String sql, List<Object> sqlValues) {
    	List<Map<String, Object>> result = null;  
        ResultSet rst = null;  
        PreparedStatement pst = null;  
        Connection con = null;
        try {  
        	con = getConnection();
            pst = con.prepareStatement(sql);  
            if(sqlValues != null && sqlValues.size() > 0){ 
                setSqlValues(pst, sqlValues);  
            } 
            rst = pst.executeQuery(); 
            result = resultToList(rst);
        } catch (SQLException e) {  
            e.printStackTrace();  
        } finally {  
            closeAll(con, pst, rst);  
        }  
        return result;  
    }
    
    private static List<Map<String, Object>> resultToList(ResultSet rst) throws SQLException {
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();   
    	if (rst == null)   
            return list;
        ResultSetMetaData md = rst.getMetaData();
        int columnCount = md.getColumnCount();
        Map<String, Object> rowData = new HashMap<String, Object>();   
        while (rst.next()) {   
         	rowData = new HashMap<String, Object>(columnCount);   
         	for (int i = 1; i <= columnCount; i++) {   
         		rowData.put(md.getColumnName(i), rst.getObject(i));   
         	}   
         	list.add(rowData);
        }   
        return list; 
    }
    
} 