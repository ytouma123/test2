package hps;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.CipherHelper;
import com.MySQLConnector;
import com.StringUtil;

public class sample {
	public static void main(String[] args) throws IOException{
		/**************************
		 * ログ出力設定 start
		 **************************/
			// ロガーを取得してログレベルをINFOに設定
		Logger logger = Logger.getLogger(sample.class.getName());
		logger.setLevel(Level.INFO);
		// ハンドラーを作成してロガーに登録
		Handler handler = new FileHandler("sample.log");
		logger.addHandler(handler);

		// フォーマッターを作成してハンドラーに登録
		SimpleFormatter formatter =  new SimpleFormatter();
		handler.setFormatter(formatter);
		/**************************
		 * ログ出力設定 end
		 **************************/

		logger.log(Level.INFO, "sample start");

		/**
		 * 一度に更新するSQLの件数
		 */
		int EXEC_CNT = 500;

		//更新レコード取得用SQL
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ");
		sbf.append(" FROM ");

		//作成したキー情報UPDATE用SQL
		StringBuffer sbfUpd = new StringBuffer();
		sbfUpd.append("UPDATE xxxxx");
		sbfUpd.append(" SET ");
		sbfUpd.append("xxx=? ");
		sbfUpd.append("WHERE ");
		sbfUpd.append("xxx=? ");

		String sql = sbf.toString();
        MySQLConnector mysqlcon = new MySQLConnector();
        StringUtil util = new StringUtil();

        String updSQL = null;		//更新実行SQL情報(LOG用)

		try{
			/**
			 * 更新レコード取得
			 */
			PreparedStatement stm = mysqlcon.connect().prepareStatement(sql);
	        ResultSet rs = stm.executeQuery(sql);

	        /**
	         * 更新用stm
	         */
	        PreparedStatement stmUpd = mysqlcon.connect().prepareStatement(sbfUpd.toString());

	        int execCnt = 0;

	        while(rs.next()){
	        	//取得データを退避
	        	xxx = rs.getString("xxx");

	        	stmUpd.setString(1, "");

	        	//test用
//	        	stmUpd.setString(6, "1");

	        	stmUpd.addBatch();
				execCnt++;

				//executeは特定件数おき又は最終行
				if(execCnt >= EXEC_CNT || rs.isLast()) {
					updSQL= stmUpd.toString();
					int updcnt[] = stmUpd.executeBatch();

					//コミット時にupdateした内容を出力
					logger.log(Level.INFO, "sample updSQL=" + updSQL);
					mysqlcon.commit();

					execCnt=0;
				}
			}
	        rs.close();
	        stm.close();
//			mysqlcon.commit();

			logger.log(Level.INFO, "sample end");

//		}catch(SQLException e) {
		}catch(Exception e) {
			e.printStackTrace();

			logger.log(Level.SEVERE, "sample updSQL=" + updSQL);

			//rollbackする
			mysqlcon.rollback();
		}finally {
			//connect close
	        mysqlcon.disconnect();
		}
	}

}
