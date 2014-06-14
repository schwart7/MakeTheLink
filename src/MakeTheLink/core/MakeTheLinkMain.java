package MakeTheLink.core;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.eclipse.swt.widgets.Shell;

import MakeTheLink.ui.MainMenuScreenUI;
import MakeTheLink.ui.ShellUtil;
import MakeTheLink.ui.FirstScreenUI;

public class MakeTheLinkMain {
	
	
	public static ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors
			.newFixedThreadPool(10);
	
	
	public static int isDisposed = 1;
//
	public static void main(String[] args) {
		Shell shell = ShellUtil.getShell();
		new FirstScreenUI(shell);
		ShellUtil.openShell(shell);
		threadPool.shutdown();
		if(MakeTheLink.db.Connection_pooling.cpds!=null){
			//if the window was closed while yago update was in progress,
			//clean up any temporary tables that were used during the update.
			try {
				MakeTheLink.core.Load_yago.clean_tmp();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			MakeTheLink.db.Connection_pooling.cpds.close();
		}
	}
	
	//called from the first screen, once the user entered all the required details.
	public static void init_conn_and_schema() throws SQLException, ClassNotFoundException, IOException, PropertyVetoException{
		
		//create the connection pool to the database.
		MakeTheLink.db.Connection_pooling.create_pool();

		//if the main schema does not exist - create it.
		MakeTheLink.db.Create_schema.init_schema();

		
	}
}
