package MakeTheLink.ui;

import java.beans.PropertyVetoException;
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

public class passwordScreenUI extends AbstractScreenUI{
	//
	protected String username;
	protected String password;
	protected String adress;
	protected String port;
	//protected boolean added = false;

	public passwordScreenUI(Shell shell) {
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
		title.setText("Please input your mySql username and your mySql password into the appropriate fields.");
		GridData data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		title.setLayoutData(data);
		
		
		Label labelUser = new Label(shell, SWT.NONE);
	    labelUser.setText("MySql username:");
	    final Text usernameText = new Text(shell, SWT.BORDER);
	    data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
	    usernameText.setLayoutData(data);
	    usernameText.setText("root");
		
	    Label labelPassword = new Label(shell, SWT.NONE);
	    labelPassword.setText("MySql password:");
	    final Text passwordText = new Text(shell, SWT.BORDER);
	    data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
	    passwordText.setLayoutData(data);
	    passwordText.setText("1");
	    
	    Label labelAdress = new Label(shell, SWT.NONE);
	    labelAdress.setText("MySql adress:");
	    final Text adressText = new Text(shell, SWT.BORDER);
	    data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
	    adressText.setLayoutData(data);
	    adressText.setText("127.0.0.1");
	    
	    Label labelPort = new Label(shell, SWT.NONE);
	    labelPort.setText("MySql port:");
	    final Text portText = new Text(shell, SWT.BORDER);
	    data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
	    portText.setLayoutData(data);
	    portText.setText("3306");
	    
	    Button ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");
		
		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {		                   
				username = StringEscapeUtils.escapeSql(usernameText.getText());
				password = StringEscapeUtils.escapeSql(passwordText.getText());
				adress = StringEscapeUtils.escapeSql(adressText.getText());
				port = StringEscapeUtils.escapeSql(portText.getText());
				
				MakeTheLink.db.Connection_pooling.set_username(username);
				MakeTheLink.db.Connection_pooling.set_password(password);
				MakeTheLink.db.Connection_pooling.set_adress(adress);
				MakeTheLink.db.Connection_pooling.set_port(port);
				
				try {
					MakeTheLink.db.Connection_pooling.create_pool();
				} catch (PropertyVetoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				new MainMenuScreenUI(shell);
			}
		});
		
	}

}
