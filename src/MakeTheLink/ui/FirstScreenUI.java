package MakeTheLink.ui;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.*;

import MakeTheLink.core.MakeTheLinkMain;

public class FirstScreenUI extends AbstractScreenUI{
	//
	protected String username;
	protected String password;
	protected String adress;
	protected String port;
	protected Label notify;
	
	static protected boolean window_busy = false;
	//protected boolean added = false;

	public FirstScreenUI(Shell shell) {
		super(shell, null, "User Name and Password");
//		if(added == true){
//			
//		}
		
	}


	public void setLayout() {
		GridLayout layout = new GridLayout(1, false);
		shell.setLayout(layout);
		
	}


	public void makeWidgets() {
		
		Label title = new Label(shell, SWT.NONE);
		title.setText("Please input the appropriate details into the appropriate fields.");
		GridData data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		title.setLayoutData(data);
		
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		
		
		Label labelUser = new Label(shell, SWT.NONE);
	    labelUser.setText("MySql username:");
	    final Text usernameText = new Text(shell, SWT.BORDER);
	    data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
	    usernameText.setLayoutData(data);
	    usernameText.setText("root");
	    
	    new Label(shell, SWT.NONE);
		
	    Label labelPassword = new Label(shell, SWT.NONE);
	    labelPassword.setText("MySql password:");
	    final Text passwordText = new Text(shell, SWT.BORDER);
	    data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
	    passwordText.setLayoutData(data);
	    passwordText.setText("1");
	    
	    new Label(shell, SWT.NONE);
	    
	    Label labelAdress = new Label(shell, SWT.NONE);
	    labelAdress.setText("MySql adress:");
	    final Text adressText = new Text(shell, SWT.BORDER);
	    data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
	    adressText.setLayoutData(data);
	    adressText.setText("127.0.0.1");
	    
	    new Label(shell, SWT.NONE);
	    
	    Label labelPort = new Label(shell, SWT.NONE);
	    labelPort.setText("MySql port:");
	    final Text portText = new Text(shell, SWT.BORDER);
	    data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
	    portText.setLayoutData(data);
	    portText.setText("3306");
	    
	    new Label(shell, SWT.NONE);
	    new Label(shell, SWT.NONE);
	    new Label(shell, SWT.NONE);
	    new Label(shell, SWT.NONE);
	    
	    
	    Button ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");
	    
	    notify = new Label(shell, SWT.NONE);
	    notify.setText("                                                                                    ");
	    

		
		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {	
				
				if(!window_busy){
					window_busy = true;
				
					username = StringEscapeUtils.escapeJava(usernameText.getText());
					password = StringEscapeUtils.escapeJava(passwordText.getText());
					adress = StringEscapeUtils.escapeJava(adressText.getText());
					port = StringEscapeUtils.escapeJava(portText.getText());
					
					MakeTheLink.db.Connection_pooling.set_username(username);
					MakeTheLink.db.Connection_pooling.set_password(password);
					MakeTheLink.db.Connection_pooling.set_adress(adress);
					MakeTheLink.db.Connection_pooling.set_port(port);
	
					notify.setText("Trying to connect...");
					
					final Runnable runnable = (new Runnable() {
						public void run() {						
							try {
								
								
								MakeTheLinkMain.init_conn_and_schema();
								
								shell.getDisplay().asyncExec(new Runnable(){
									public void run(){
										new MainMenuScreenUI(shell);
									}
								});				
							
							
							} catch (Exception e1) {
								
								shell.getDisplay().asyncExec(new Runnable(){
									public void run(){
										notify.setText("Failed to connect!");
									}
								});
							}
							
							window_busy=false;
						}
					});
					MakeTheLink.core.MakeTheLinkMain.threadPool.execute(runnable);
					
					
					
				}
				
			}
		});
		
	}

}
