package MakeTheLink.ui;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import MakeTheLink.db.Load_yago_funcs;



public class ChooseUpdateUI extends AbstractScreenUI{
	
	private Label label_status;
	private Text pathText;
	
	public boolean window_busy = false;

	public ChooseUpdateUI(Shell shell) {
		super(shell, null, "choose update type");
		
	}

	@Override
	public void setLayout() {
		GridLayout layout = new GridLayout(1, false);
		shell.setLayout(layout);
	}

	@Override
	public void makeWidgets() {
		
	    Label labelPath = new Label(shell, SWT.NONE);
	    labelPath.setText("Yago files path (followed by a slash):");

	    pathText = new Text(shell, SWT.BORDER);
	    pathText.setText("/home/user7/Desktop/shared/yago2s_tsv/                                               "+
	    "                                                                                                       ");
		
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
	    
		Button fullUpdate = new Button(shell, SWT.PUSH);
		fullUpdate.setText("Perform Full Update from YAGO files");
	    	    
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		
		Button manualUpdate = new Button(shell, SWT.PUSH);
		manualUpdate.setText("Manual Update");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		Button yagoBackupUpdate = new Button(shell, SWT.PUSH);
		yagoBackupUpdate.setText("Update from YAGO backup");

		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Label status_text = new Label(shell, SWT.NONE);
		status_text.setText("STATUS:");
	    label_status = new Label(shell, SWT.NONE);
	    label_status.setText("waiting for user selection                                                     "+
	    "                                                                                                    "+
	    "                                                                                                    ");
		
	    Button back = new Button(shell, SWT.PUSH);
		back.setText("Back");
		
		yagoBackupUpdate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {	
				
				if(!window_busy){
					window_busy=true;
					
					label_status.setText("Performing update from YAGO backup...");
					
					final Runnable runnable = (new Runnable() {
						public void run() {						
							try {
								MakeTheLink.core.Load_yago.copy_from_tmp_to_curr();
								
								shell.getDisplay().asyncExec(new Runnable(){
									public void run(){
										label_status.setText("YAGO backup loaded successfully."+
										" waiting for user selection");
										
										window_busy=false;
									}
								});
							} catch (Exception e1) {
								
								shell.getDisplay().asyncExec(new Runnable(){
									public void run(){
										label_status.setText("Failed to perform update from YAGO backup!"+
												 " waiting for user selection");
										window_busy=false;
									}
								});
							}  
						}
					});
					MakeTheLink.core.MakeTheLinkMain.threadPool.execute(runnable);
					

					
					//window_busy=false;
				}
				

			}
		});
	    
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		

		

		

	    
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		

		
		fullUpdate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {	
				
				if(!window_busy){
					window_busy=true;
					
					label_status.setText("Preparing to load from YAGO files...");
					
					MakeTheLink.core.Load_yago.set_path(pathText.getText());
					
					final Runnable runnable = (new Runnable() {
						public void run() {						
							try {
								MakeTheLink.core.Load_yago.load_yago(ChooseUpdateUI.this);
							} catch (Exception e1) {
								
								shell.getDisplay().asyncExec(new Runnable(){
									public void run(){
										label_status.setText("Failed to perform update from yago files!" +
												 " waiting for user selection");
										window_busy=false;
									}
								});
							}  
						}
					});
					MakeTheLink.core.MakeTheLinkMain.threadPool.execute(runnable);
					

					
					//window_busy=false;
				}
				

			}
		});
		
		manualUpdate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {		                   
				if(!window_busy){
					window_busy=true;
					
					label_status.setText("Performing manual update... ");
					
					try {
						Edit_menu_window.open_window();
						
						label_status.setText("waiting for user selection");
						window_busy=false;
					} catch (PropertyVetoException e1) {
						e1.printStackTrace();
					}
					

				}
			}
		});
		
		back.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {	
				if(!window_busy){
					window_busy=true;
					new MainMenuScreenUI(shell);
					window_busy=false;
				}
			}
		});
	}
	
	public void update_load_progress(final String progress){
		this.shell.getDisplay().asyncExec(new Runnable(){
			public void run(){
				String loading_note = "Loading yago in progress, please do not close this window: ";
				if(progress.compareTo("Yago loaded")==0){
					label_status.setText("YAGO loaded successfully. waiting for user selection");
					window_busy=false;
				}
				else
					label_status.setText(loading_note+progress+"...");
			}
		});
	}
}
