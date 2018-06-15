package com;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLConnector {
//	private String DATABASE_DRIVER = null;
	private String DATABASE_URL = null;
	private String USERNAME = null;
	private String PASSWORD = null;
	private String MAX_POOL = null;

	// init connection object
	private Connection connection;
	// init properties object
	private Properties properties;

	public MySQLConnector() {
		//Propertiesファイルの設定を読み込み値を設定する

		Properties props = new Properties();

		try {
			FileInputStream in = new FileInputStream("db.properties");
			props.load(in);
			in.close();

//			String driver = props.getProperty("jdbc.driver");
//			if (driver != null) {
//			    Class.forName(driver) ;
//			}
			//プロパティファイルの内容をセットする
			this.DATABASE_URL = props.getProperty("jdbc.url");
			this.USERNAME = props.getProperty("jdbc.username");
			this.PASSWORD = props.getProperty("jdbc.password");
			this.MAX_POOL = props.getProperty("jdbc.max_pool");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * create properties
	 * @return
	 */
	private Properties getProperties() {
	    if (properties == null) {
	        properties = new Properties();
	        properties.setProperty("user", USERNAME);
	        properties.setProperty("password", PASSWORD);
	        properties.setProperty("MaxPooledStatements", MAX_POOL);
	    }
	    return properties;
	}

	/**
	 * connectする
	 * @return Connection
	 */
    public Connection connect() {
        if (connection == null) {
            try {
//                Class.forName(DATABASE_DRIVER);
                connection = DriverManager.getConnection(DATABASE_URL, getProperties());

                //autoコミットをoffにして、updateエラーが発生したときなどのロールバックを可能とする
                connection.setAutoCommit(false);
//            } catch (ClassNotFoundException | SQLException e) {
            } catch (SQLException e) {
            	System.out.println("MySQLに接続できませんでした。");
                e.printStackTrace();
            }
        }
        return connection;
    }
    /**
     * commit
     */
    public void commit() {
    	if(connection != null) {
        	try {
           		connection.commit();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
    	}
    }
    /**
     * rollback
     */
    public void rollback() {
    	if(connection != null) {
        	try {
           		connection.rollback();
        	}catch(Exception e){
        		System.out.println("rollbackに失敗しました。");
        		e.printStackTrace();
        	}
    	}
    }

    /**
     * connectをcloseする
     */
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
            	System.out.println("MySQLのクローズに失敗しました。");
                e.printStackTrace();
            }
        }
    }

}
