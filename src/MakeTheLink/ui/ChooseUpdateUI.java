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
	
	private Label label_update_status;
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
		
		Button manualUpdate = new Button(shell, SWT.PUSH);
		manualUpdate.setText("Manual Update");
		
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		
		
	    Label labelPath = new Label(shell, SWT.NONE);
	    labelPath.setText("Yago files path (followed by a slash):");
	    pathText = new Text(shell, SWT.BORDER);
	    pathText.setText("/home/user7/Desktop/shared/yago2s_tsv/                                               "+
	    "                                                                                                       ");
		
		Button fullUpdate = new Button(shell, SWT.PUSH);
		fullUpdate.setText("Perform Full Update from YAGO files");
		
	    label_update_status = new Label(shell, SWT.NONE);
	    label_update_status.setText("                                                                       "+
	    "                                                                                                    "+
	    "                                                                                                    ");
	    
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Button back = new Button(shell, SWT.PUSH);
		back.setText("Back");
		
		fullUpdate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {	
				
				if(!window_busy){
					window_busy=true;
					
					
					MakeTheLink.core.Load_yago.set_path(pathText.getText());
					
					final Runnable runnable = (new Runnable() {
						public void run() {						
							try {
								MakeTheLink.core.Load_yago.load_yago(ChooseUpdateUI.this);
							} catch (Exception e1) {
								
								shell.getDisplay().asyncExec(new Runnable(){
									public void run(){
										label_update_status.setText("Failed to perform update from yago files!");
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
					try {
						Edit_menu_window.open_window();
					} catch (PropertyVetoException e1) {
						e1.printStackTrace();
					}
					
					window_busy=false;
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
					loading_note = "";
					window_busy=false;
				}
				
				
				label_update_status.setText(loading_note+progress);
			}
		});
	}
}
