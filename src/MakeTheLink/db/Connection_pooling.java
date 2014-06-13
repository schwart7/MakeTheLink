package MakeTheLink.db;

import java.beans.PropertyVetoException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class Connection_pooling {
	
	public static ComboPooledDataSource cpds;
	
	static private String sql_username; //root
	static private String sql_password; //1
	static private String sql_adress;   //127.0.0.1
	static private String sql_port;	    //3306
	
	public static void create_pool() 
					throws PropertyVetoException 
			{
		
		cpds = new ComboPooledDataSource();
		cpds.setDriverClass( "com.mysql.jdbc.Driver" );
		cpds.setJdbcUrl( 
			"jdbc:mysql://"+sql_adress+":"+sql_port+"/DbMysql02?rewriteBatchedStatements=true&allowMultiQueries=true" );
		cpds.setUser(sql_username);
		cpds.setPassword(sql_password);
	}	
	
	public static void set_username(String str){
		sql_username=str;
	}
	public static void set_password(String str){
		sql_password=str;
	}
	public static void set_adress(String str){
		sql_adress=str;
	}
	public static void set_port(String str){
		sql_port=str;
	}
}
