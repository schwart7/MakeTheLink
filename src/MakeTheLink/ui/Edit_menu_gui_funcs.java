//this class contains auxiliary GUI functions for the edit screen

package MakeTheLink.ui;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TabFolder;


public class Edit_menu_gui_funcs {
	
	private static int aux_var;
	
	//create the music tab and its tables
	public static void create_music_menu(Edit_menu_window menu) throws SQLException{
		TabItem music_item = new TabItem(menu.main_folder, SWT.NONE);
		music_item.setText("Music");
		
		TabFolder music_folder = new TabFolder(menu.main_folder, SWT.NONE);
		music_item.setControl(music_folder);
		
		TabItem artists_item = new TabItem(music_folder, SWT.NONE);
		artists_item.setText("Artists");
		
		// Add an event listener to the music tab to create the artists table when first pressed		
		add_tab_listener(menu.main_folder, menu.artists_loaded, 
				"Music", artists_item, new String[]{"id", "name", "rating", "birth year"},
							MakeTheLink.db.Edit_menu_DB_funcs.music_artists_query,
							music_folder);
		
		TabItem creations_item = new TabItem(music_folder, SWT.NONE);
		creations_item.setText("Creations");
		
	    // Add an event listener to the creations tab to create the creations table when first pressed
		add_tab_listener(music_folder, menu.creations_loaded, 
				"Creations", creations_item, new String[]{"id", "name", "rating", "release year"},
				MakeTheLink.db.Edit_menu_DB_funcs.music_creations_query, null);
	}
	//create the cinema tab and its tables
	public static void create_cinema_menu(Edit_menu_window menu) throws SQLException{
		TabItem cinema_item = new TabItem(menu.main_folder, SWT.NONE);
		cinema_item.setText("Cinema");
		
		TabFolder cinema_folder = new TabFolder(menu.main_folder, SWT.NONE);
		cinema_item.setControl(cinema_folder);
		
		TabItem actors_item = new TabItem(cinema_folder, SWT.NONE);
		actors_item.setText("Actors");
		
		
		//create the actors table when the window opens - it is the first table the user sees.
		Table actors_table = create_table(cinema_folder, 
						new String[]{"id","name","rating","birth year"}, 
						MakeTheLink.db.Edit_menu_DB_funcs.cinema_actors_query);
		
		actors_item.setControl(actors_table);
		
		TabItem movies_item = new TabItem(cinema_folder, SWT.NONE);
		movies_item.setText("Movies");
		
	    // Add an event listener to the movies tab to create the movies table when first pressed		
		add_tab_listener(cinema_folder, menu.movies_loaded, 
							"Movies", movies_item, new String[]{"id", "name", "rating", "release year"},
							MakeTheLink.db.Edit_menu_DB_funcs.cinema_movies_query, null);
		
		TabItem categories_item = new TabItem(cinema_folder, SWT.NONE);
		categories_item.setText("Categories");
		
	    // Add an event listener to the categories tab to create the movies table when first pressed
		add_tab_listener(cinema_folder, menu.categories_loaded, 
				"Categories", categories_item, new String[]{"id", "name"},
				MakeTheLink.db.Edit_menu_DB_funcs.cinema_tags_query, null);
	}
	
	//create the places tab and its tables
	public static void create_places_menu(Edit_menu_window menu) throws SQLException{
		TabItem places_item = new TabItem(menu.main_folder, SWT.NONE);
		places_item.setText("Places");
		
		TabFolder places_folder = new TabFolder(menu.main_folder, SWT.NONE);
		places_item.setControl(places_folder);
		
		TabItem countries_item = new TabItem(places_folder, SWT.NONE);
		countries_item.setText("Countries");
		
	    // Add an event listener to the places tab to create the countries table when first pressed		
		add_tab_listener(menu.main_folder, menu.countries_loaded, 
				"Places", countries_item, new String[]{
				"id", "name", "area (1000 km^2)", "gdp per capita (1000 $)", "population (million)", "capital", 
				"gdp (billion $)"},
				MakeTheLink.db.Edit_menu_DB_funcs.places_countries_query, places_folder);
		
		TabItem locations_item = new TabItem(places_folder, SWT.NONE);
		locations_item.setText("Locations");
		
	    // Add an event listener to the locations tab to create the locations table when first pressed
		add_tab_listener(places_folder, menu.locations_loaded, 
				"Locations", locations_item, new String[]{"id", "name", "rating", "population"},
				MakeTheLink.db.Edit_menu_DB_funcs.places_locations_query, null);
	}
	
	//create the sports tab and its tables
	public static void create_sports_menu(Edit_menu_window menu) throws SQLException{
		TabItem sports_item = new TabItem(menu.main_folder, SWT.NONE);
		sports_item.setText("Sports");
		
		TabFolder sports_folder = new TabFolder(menu.main_folder, SWT.NONE);
		sports_item.setControl(sports_folder);
		
		TabItem nba_players_item = new TabItem(sports_folder, SWT.NONE);
		nba_players_item.setText("NBA players");
		
		// Add an event listener to the sports tab to create the nba_players table when first pressed
		add_tab_listener(menu.main_folder, menu.nba_players_loaded, 
				"Sports", nba_players_item, new String[]{"id", "name", "rating", "birth year"},
				MakeTheLink.db.Edit_menu_DB_funcs.nba_players_query,
							sports_folder);
		
		TabItem nba_teams_item = new TabItem(sports_folder, SWT.NONE);
		nba_teams_item.setText("NBA teams");
		
	    // Add an event listener to the nba_teams tab to create the nba_teams table when first pressed
		add_tab_listener(sports_folder, menu.nba_teams_loaded, 
				"NBA teams", nba_teams_item, new String[]{"id", "name", "rating", "creation year"},
				MakeTheLink.db.Edit_menu_DB_funcs.nba_teams_query, null);
		
		TabItem israeli_soccer_players_item = new TabItem(sports_folder, SWT.NONE);
		israeli_soccer_players_item.setText("Israeli soccer players");
		
		// Add an event listener to the israeli_soccer_players tab to create the israeli_soccer_players table when first pressed
		add_tab_listener(sports_folder, menu.israeli_soccer_players_loaded, 
				"Israeli soccer players", israeli_soccer_players_item, new String[]{"id", "name", "rating", "birth year"},
				MakeTheLink.db.Edit_menu_DB_funcs.israeli_soccer_players_query,
							null);
		
		TabItem israeli_soccer_teams_item = new TabItem(sports_folder, SWT.NONE);
		israeli_soccer_teams_item.setText("Israeli soccer teams");
		
	    // Add an event listener to the israeli_soccer_teams tab to create the israeli_soccer_teams table when first pressed
		add_tab_listener(sports_folder, menu.israeli_soccer_teams_loaded, 
				"Israeli soccer teams", israeli_soccer_teams_item, new String[]{"id", "name", "rating", "creation year"},
				MakeTheLink.db.Edit_menu_DB_funcs.israeli_soccer_teams_query, null);

		
		TabItem world_soccer_players_item = new TabItem(sports_folder, SWT.NONE);
		world_soccer_players_item.setText("World soccer players");
		
		// Add an event listener to the world_soccer_players tab to create the world_soccer_players table when first pressed
		add_tab_listener(sports_folder, menu.world_soccer_players_loaded, 
				"World soccer players", world_soccer_players_item, new String[]{"id", "name", "rating", "birth year"},
				MakeTheLink.db.Edit_menu_DB_funcs.world_soccer_players_query,
							null);
		
		TabItem world_soccer_teams_item = new TabItem(sports_folder, SWT.NONE);
		world_soccer_teams_item.setText("World soccer teams");
		
	    // Add an event listener to the world_soccer_teams tab to create the world_soccer_teams table when first pressed
		add_tab_listener(sports_folder, menu.world_soccer_teams_loaded, 
				"World soccer teams", world_soccer_teams_item, new String[]{"id", "name", "rating", "creation year"},
				MakeTheLink.db.Edit_menu_DB_funcs.world_soccer_teams_query, null);
	}



	//this function doesn't create the table immediately, but rather only when the user presses on its tab -
	//so that the main menu doesn't have to wait for all the tables to load.
	public static void add_tab_listener
	
	(		final TabFolder primary_folder, 
			final int[] load_indicator, //if the value in load_indicator[0] is 1 - the table was already loaded.
			final String table_name, 
			final TabItem tab_item, 
			final String[] col_names, 
			final String query,
			final TabFolder secondary_folder
			){
		
	    // Add an event listener to the specified tab to create the relevant table when first pressed
		primary_folder.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(org.eclipse.swt.events.SelectionEvent event) {
	    	 
	        if(load_indicator[0]!=1 && primary_folder.getSelection()[0].getText().compareTo(table_name)==0){
	        	
	        	try {
	        		tab_item.setControl(
	        				create_table(primary_folder,
									col_names, 
									query));
					
	        		load_indicator[0]=1;
				}
	        	catch (IllegalArgumentException e) {

        			tab_item.setControl(
        					create_table(secondary_folder,
									col_names, 
									query));
					
	        		load_indicator[0]=1;

					
	        		load_indicator[0]=1;
				}
	        }
	      }
	    });
	}
	

	//create the table to be displayed
	public static Table create_table(TabFolder tabFolder, 
						final String[] col_names, final String query){
		

		
		final int col_count = col_names.length;
	
		String[][] data = MakeTheLink.db.Edit_menu_DB_funcs.create_table_data(col_count, query);
		
		final Table table = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.VIRTUAL);
		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		final TableColumn id_column0 = new TableColumn(table, SWT.CENTER);
		id_column0.setWidth(0);
		id_column0.setText(col_names[0]);
		id_column0.setResizable(false);
		
		for(int i=1; i<col_count; i++){
			final TableColumn id_column = new TableColumn(table, SWT.CENTER);
			
			if(i==1)
				id_column.setWidth(250);
			else
				id_column.setWidth(100);
			id_column.setText(col_names[i]);
		}
		
		table.setItemCount(data.length);
		
		add_sort_column(table, data);
		
		return table;
	}
	
	//refresh the data in the table - following a search, add, delete, crop or edit
	public static void refresh_table(final Table table, String query)throws SQLException {
		
		Edit_menu_window.display.syncExec(new Runnable(){
			public void run(){
				aux_var = table.getColumnCount();
			}
		});
		int col_count = aux_var;
		
		final String[][] data = MakeTheLink.db.Edit_menu_DB_funcs.refresh_table_data(col_count, query);
		
		Edit_menu_window.display.syncExec(new Runnable(){
			public void run(){
				table.removeAll();

				table.setItemCount(data.length);
				
				add_sort_column(table, data);
			}
		});
	}
	
	
	//add sorting function to the table's columns
	private static void add_sort_column(final Table tb, final String[][] data){
		
		final TableColumn[] clmns = tb.getColumns();
		
		while(tb.getListeners(SWT.SetData).length>0){
			tb.removeListener(SWT.SetData, tb.getListeners(SWT.SetData)[0]);
		}
		
		tb.addListener(SWT.SetData, new Listener() {
			@Override
			public void handleEvent(Event e) {
				TableItem item = (TableItem) e.item;
				int index = tb.indexOf(item);
				
				String[] datum = data[index];
				item.setText(datum);
			}

		});		

		// Add sort indicator and sort data when column selected
		Listener sortListener = new Listener() {
			@Override
			public void handleEvent(Event e) {
				// determine new sort column and direction
				TableColumn sortColumn = tb.getSortColumn();
				TableColumn currentColumn = (TableColumn) e.widget;
				int dir = tb.getSortDirection();
				if (sortColumn == currentColumn) {
					dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
				} else {
					tb.setSortColumn(currentColumn);
					dir = SWT.UP;
				}
				// sort the data based on column and direction
				
				int tmp=0;
				for(int i=0; i<clmns.length; i++){
					if(clmns[i]==currentColumn){
						tmp = i;
					}
				}
				
				final int index = tmp;
				
				final int direction = dir;
				Arrays.sort(data, new Comparator<String[]>() {
					@Override
					public int compare(String[] a, String[] b) {
						try{
							if (a[index].compareTo(b[index]) == 0) return 0;
							if (direction == SWT.UP) {
								return Double.parseDouble(a[index]) < Double.parseDouble(b[index]) ? -1 : 1;
							}
							return Double.parseDouble(a[index]) < Double.parseDouble(b[index]) ? 1 : -1;
						}
						catch (Exception e){
							if (a[index].compareTo(b[index]) == 0) return 0;
							if (direction == SWT.UP) {
								return a[index].compareTo(b[index])<0 ? -1 : 1;
							}
							return a[index].compareTo(b[index])<0 ? 1 : -1;
						}
					}
				});
				
				// update data displayed in table
				tb.setSortDirection(dir);
				tb.clearAll();
			}
		};
		
		for(int i=0; i<clmns.length; i++){
			while(clmns[i].getListeners(SWT.Selection).length>0){
				clmns[i].removeListener(SWT.Selection, clmns[i].getListeners(SWT.Selection)[0]);
			}
			
			clmns[i].addListener(SWT.Selection, sortListener);
		}
	}

}