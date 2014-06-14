//a window to edit an entity. opened from the edit menu window

package MakeTheLink.ui;

import java.sql.SQLException;

import org.apache.commons.lang.StringEscapeUtils;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class Edit_row_window {
	
	public static Display display;
	private int[] indexes;
	
	private int[] not_linked_loaded = new int[]{0};

	private Shell shell;
	private TabItem linked_itms_tab;
	private TabItem linkable_items_tab;
	private Button add_remove_links_btn;
	private Label lbl_search;
	private Text search_text;
	private Label lbl_rating;
	private Spinner sp_min_rating;
	private Label lbl_pop_year;
	private Spinner sp_min_pop_year;
	private Button search_button;

	private Text[] attributes;//the attributes of the edited item are entered by the user in these fields
	
	private Edit_menu_window parent_window;
	private TabFolder tabs;
	private Table source_table;//the table of the edited item
	private Table linked_table;//the table of items that are link-able to the edited item
	private String source_table_name;//the name of the table of the edited item
	
	
	public static int id;//the id of the edited item in its table
	public static TableItem[] selected;//the selected rows of the edited item's table - if there are any such rows then we'll edit the first of them.
	public static String[] attrs = null;//names of the edited item's attributes
	public static String[] attr_vals = null;//values of the edited item's attributes
	public static String[] linked_attrs;//names of the attributes of the items that are linked to the edited item
	public static String linked_query = null;//query to find what items are linked to the edited item
	public static String not_linked_query = null;//query to find all the link-able items to the edited item
	public static String label_min_parameter;//label of a search parameter
	public static String linked_category_name;//name of category of link-able items
	private String search_var = "";//search string to search names
	private int min_rating = 0;//value of a search parameter, obtained from user input
	private int min_pop_year = 0;//value of a search parameter, obtained from user input
	private boolean window_busy=false;

	public static void create(Edit_menu_window edg) {
		try {
			Edit_row_window window = new Edit_row_window();
			
			window.open(edg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void open(Edit_menu_window edg) throws SQLException {
		this.parent_window=edg;
		display = Display.getDefault();
		if(createContents()==1){
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			//shell.close();
		}
	}


	private int createContents() throws SQLException {
		shell = new Shell();

		shell.setText("SWT Application");

		shell.setSize(900, 600);

		shell.setLayout(new GridLayout(11, false));
		
		Button save_and_close_button = new Button(shell, SWT.NONE);
		save_and_close_button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		save_and_close_button.setText("save attributes and close");
		save_and_close_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				if(!window_busy){
					window_busy=true;
					try{
						
						MakeTheLink.db.Edit_menu_DB_funcs.save_attributes(source_table_name, id, attributes);
						
						MakeTheLink.db.Edit_menu_DB_funcs.filter_search("",0,0,0,source_table_name,source_table,0);
						
						Edit_row_window.this.shell.close();

					}
					catch (Exception ex){
					}
					window_busy=false;
				}
			}
		});

		
		source_table_name = ((TabFolder)(parent_window.main_folder.getSelection()[0].getControl())).getSelection()[0].getText();
		source_table=
		(Table)(((TabFolder)(parent_window.main_folder.getSelection()[0].getControl())).getSelection()[0].getControl());
		selected = source_table.getSelection();
		try{
			id = Integer.parseInt(selected[0].getText(0));
		}catch(Exception e){
			return 0;
		}
		
		if(source_table_name.compareTo("Actors")==0)
			MakeTheLink.db.Edit_menu_DB_funcs.set_actor_data();
		if(source_table_name.compareTo("Movies")==0)
			MakeTheLink.db.Edit_menu_DB_funcs.set_movie_data();
		if(source_table_name.compareTo("Categories")==0)
			MakeTheLink.db.Edit_menu_DB_funcs.set_category_data();
		if(source_table_name.compareTo("Artists")==0)
			MakeTheLink.db.Edit_menu_DB_funcs.set_artist_data();
		if(source_table_name.compareTo("Creations")==0)
			MakeTheLink.db.Edit_menu_DB_funcs.set_creation_data();
		if(source_table_name.compareTo("Countries")==0)
			MakeTheLink.db.Edit_menu_DB_funcs.set_country_data();
		if(source_table_name.compareTo("Locations")==0)
			MakeTheLink.db.Edit_menu_DB_funcs.set_location_data();
		if(source_table_name.compareTo("NBA players")==0)
			MakeTheLink.db.Edit_menu_DB_funcs.set_player_data("nba");
		if(source_table_name.compareTo("NBA teams")==0)
			MakeTheLink.db.Edit_menu_DB_funcs.set_team_data("nba");
		if(source_table_name.compareTo("Israeli soccer players")==0)
			MakeTheLink.db.Edit_menu_DB_funcs.set_player_data("israeli_soccer");
		if(source_table_name.compareTo("Israeli soccer teams")==0)
			MakeTheLink.db.Edit_menu_DB_funcs.set_team_data("israeli_soccer");
		if(source_table_name.compareTo("World soccer players")==0)
			MakeTheLink.db.Edit_menu_DB_funcs.set_player_data("world_soccer");
		if(source_table_name.compareTo("World soccer teams")==0)
			MakeTheLink.db.Edit_menu_DB_funcs.set_team_data("world_soccer");
		
		
		int j=0;
		for(; j<attrs.length; j++){
			Label lblNewLabel = new Label(shell, SWT.CENTER);
			lblNewLabel.setText(attrs[j]);
		}
		for(;j<10;j++){
			new Label(shell, SWT.NONE);
		}
		
		add_remove_links_btn = new Button(shell, SWT.NONE);
		add_remove_links_btn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		add_remove_links_btn.setText("add/remove links");
		add_remove_links_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				if(!window_busy){
					window_busy=true;
					try{
				
						final Table current_table=((Table)(tabs.getSelection()[0].getControl()));
						
		
						
						search_var = StringEscapeUtils.escapeSql(search_text.getText());						
						min_rating = sp_min_rating.getSelection();
						min_pop_year = sp_min_pop_year.getSelection();
						
						if(source_table_name.compareTo("Categories")==0 && id>0)
							id = 0 - id;
						
						final boolean bool = 
							tabs.getSelection()[0].getText().compareTo
										("LINKABLE " + linked_category_name +" LIST (TO BE LINKED)")==0;
			
						final Runnable runnable = (new Runnable() {
							public void run() {						
								try {
									
									Edit_menu_window.display.syncExec(new Runnable(){
										public void run(){
											
											final TableItem[] rows = current_table.getSelection();
											
											indexes = new int[rows.length];
											
											for(int i=0; i<rows.length; i++){
												indexes[i]=Integer.parseInt(rows[i].getText(0));
											}
										}
									});
		
									if(bool){
										MakeTheLink.db.Edit_menu_DB_funcs.link_items(source_table_name, id, indexes);
									}
									else{
										MakeTheLink.db.Edit_menu_DB_funcs.un_link_items(source_table_name, id, indexes);
									}
									MakeTheLink.db.Edit_menu_DB_funcs.filter_search(search_var, min_rating, min_pop_year, min_pop_year, 
										MakeTheLink.db.Edit_menu_DB_funcs.category_links_map.get(source_table_name), linked_table, id);
		
								
								}catch (SQLException e) {
								}
							}
						});
						MakeTheLink.core.MakeTheLinkMain.threadPool.execute(runnable);

						
 
					}
					catch (Exception ex){
					}
					window_busy=false;
				}
				
			}
		});
		
		attributes = new Text[attr_vals.length];
		for(int i=0; i<attr_vals.length; i++){
			attributes[i] = new Text(shell, SWT.BORDER);
			attributes[i].setText(attr_vals[i]);
			attributes[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		}
								
		tabs = new TabFolder(shell, SWT.BORDER);
		GridData gd_tabFolder = new GridData(SWT.FILL, SWT.FILL, true, true, 11, 8);
		gd_tabFolder.heightHint = 170;
		tabs.setLayoutData(gd_tabFolder);
		
		linked_itms_tab = new TabItem(tabs, SWT.NONE);
		linked_itms_tab.setText("LINKED " + linked_category_name +" (TO BE UN-LINKED)");
		
		linked_table = Edit_menu_gui_funcs.create_table(tabs, 
				linked_attrs,
				linked_query);
		linked_itms_tab.setControl(linked_table);
		
		linkable_items_tab = new TabItem(tabs, SWT.NONE);
		linkable_items_tab.setText("LINKABLE " + linked_category_name +" LIST (TO BE LINKED)");
		
		Edit_menu_gui_funcs.add_tab_listener
		(		tabs, 
				not_linked_loaded, 
				"LINKABLE " + linked_category_name +" LIST (TO BE LINKED)", 
				linkable_items_tab, 
				linked_attrs, 
				not_linked_query,
				null);
		
		lbl_search = new Label(shell, SWT.NONE);
		lbl_search.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lbl_search.setText("Search text:");
		
		lbl_rating = new Label(shell, SWT.NONE);
		lbl_rating.setText("min. rating:");
		
		lbl_pop_year = new Label(shell, SWT.NONE);
		lbl_pop_year.setText(label_min_parameter);
		new Label(shell, SWT.NONE);
		
		search_button = new Button(shell, SWT.NONE);
		search_button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 7, 2));
		search_button.setText("FILTER/SEARCH");
		search_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				if(!window_busy){

					window_busy=true;
					
						int tmp_var=id;
						if(tabs.getSelection()[0].getText().compareTo
													("LINKABLE " + linked_category_name +" LIST (TO BE LINKED)")==0){

							tmp_var=0;
							
						}
						final int tmp_id=tmp_var;
						search_var = StringEscapeUtils.escapeSql(search_text.getText());						
						min_rating = sp_min_rating.getSelection();
						min_pop_year = sp_min_pop_year.getSelection();
						
						if(source_table_name.compareTo("Categories")==0 && id>0)
							id = 0 - id;
						
						final Table tbl = (Table)(tabs.getSelection()[0].getControl());
						
						final Runnable runnable = (new Runnable() {
							public void run() {						
								try {
									MakeTheLink.db.Edit_menu_DB_funcs.filter_search(search_var, min_rating, min_pop_year, min_pop_year, 
											MakeTheLink.db.Edit_menu_DB_funcs.category_links_map.get(source_table_name), tbl, tmp_id);
									
								} catch (SQLException e) {
								}
							}
						});
						MakeTheLink.core.MakeTheLinkMain.threadPool.execute(runnable);
					window_busy=false;
				}
			}
		});
		
		search_text = new Text(shell, SWT.BORDER);
		search_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		sp_min_rating = new Spinner(shell, SWT.BORDER);
		sp_min_rating.setMaximum(2000000000);
		sp_min_rating.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		sp_min_pop_year = new Spinner(shell, SWT.BORDER);
		sp_min_pop_year.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		sp_min_pop_year.setMaximum(2000000000);
		new Label(shell, SWT.NONE);
		
		return 1;
	}
	
}

