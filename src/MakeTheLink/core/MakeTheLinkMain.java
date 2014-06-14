package MakeTheLink.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.eclipse.swt.widgets.Shell;

import MakeTheLink.ui.MainMenuScreenUI;
import MakeTheLink.ui.ShellUtil;
import MakeTheLink.ui.passwordScreenUI;

public class MakeTheLinkMain {
	
	
	public static ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors
			.newFixedThreadPool(10);
	
	
	public static int isDisposed = 1;
//
	public static void main(String[] args) {
		Shell shell = ShellUtil.getShell();
		new passwordScreenUI(shell);
		ShellUtil.openShell(shell);
		threadPool.shutdown();
		if(MakeTheLink.db.Connection_pooling.cpds!=null)
			MakeTheLink.db.Connection_pooling.cpds.close();
	}
}
