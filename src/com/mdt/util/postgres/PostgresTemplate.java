package com.mdt.util.postgres;

import com.mdt.listener.PropertyListener;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//import org.apache.tomcat.jdbc.pool.DataSource;
//import org.apache.tomcat.jdbc.pool.PoolProperties;


public class PostgresTemplate<E> {

	private static PostgresTemplate postgresTemplate = new PostgresTemplate();
	
	private static DataSource dataSource = null;
	
	public static PostgresTemplate getPostgresTemplate(){
		return postgresTemplate;
	}
	
	public E query(String sql,PostgresTemplateCallBack callBack){
		E retObject = null;
		
		Connection conn = getConnection();
		if(conn!=null){
			Statement st;
			try {
				st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql);
				retObject = (E)callBack.purseRs(rs);
				rs.close();
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
					try {
						if(conn!=null) conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		
		return retObject;
	}
	
	
	public synchronized  DataSource getDataSource(){
		if(this.dataSource == null){
		  //初始化
//		  PoolProperties p = new PoolProperties();
//		  p.setUrl(PropertyListener.getPropertyValue("${postgres.url}"));
//	      p.setDriverClassName(PropertyListener.getPropertyValue("${postgres.driverClassName}"));
//	      p.setUsername(PropertyListener.getPropertyValue("${postgres.username}"));
//	      p.setPassword(PropertyListener.getPropertyValue("${postgres.password}"));
//	      p.setJmxEnabled(true);
//	      p.setTestWhileIdle(false);
//	      p.setTestOnBorrow(true);
//	      p.setValidationQuery("SELECT 1");
//	      p.setTestOnReturn(false);
//	      p.setValidationInterval(30000);
//	      p.setTimeBetweenEvictionRunsMillis(30000);
//	      p.setMaxActive(100);
//	      p.setInitialSize(10);
//	      p.setMaxWait(10000);
//	      p.setRemoveAbandonedTimeout(60);
//	      p.setMinEvictableIdleTimeMillis(30000);
//	      p.setMinIdle(10);
//	      p.setLogAbandoned(true);
//	      p.setRemoveAbandoned(true);
//	      p.setJdbcInterceptors(
//	        "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
//	        "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
//	      
//	      dataSource = new DataSource();
//	      
//	      dataSource.setPoolProperties(p);
			
		  BasicDataSource ds = new BasicDataSource();
		  ds.setDriverClassName(PropertyListener.getPropertyValue("${postgres.driverClassName}"));
		  ds.setUsername(PropertyListener.getPropertyValue("${postgres.username}"));
		  ds.setPassword(PropertyListener.getPropertyValue("${postgres.password}"));
		  ds.setUrl(PropertyListener.getPropertyValue("${postgres.url}"));
		  ds.setInitialSize(5);
		  ds.setMaxTotal(50);
		  ds.setMaxIdle(30);
		  ds.setMaxWaitMillis(10000);
		  ds.setMinIdle(1);
		  ds.setTestOnCreate(false);
		  
		  this.dataSource = ds;	
		}
		
		return this.dataSource;
	}
	
	public Connection getConnection(){
		Connection conn = null;
		try {
			conn = getDataSource().getConnection();
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			conn = null;
		}
		return conn;
	}
	
	
}
