//this class contains auxiliary functions to communicate with the database for the edit screen

package MakeTheLink.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringEscapeUtils;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import MakeTheLink.ui.Edit_row_window;


public class Edit_menu_DB_funcs {

	//list of queries to create the tables
	public static String music_artists_query = " select * from curr_music_artists order by num_links desc";
	public static String music_creations_query = " select * from curr_music_creations order by num_links desc";
	public static String cinema_actors_query = " select * from curr_cinema_actors order by num_links desc";
	public static String cinema_movies_query = " select * from curr_cinema_movies order by num_links desc";
	public static String cinema_tags_query = " select * from curr_cinema_tags ";
	public static String places_countries_query = " select * from curr_places_countries order by `GDP (billion $)` ";
	public static String places_locations_query = " select * from curr_places_locations order by num_links desc ";
	public static String nba_players_query = " select * from curr_nba_players order by links_to_player desc";
	public static String nba_teams_query = " select * from curr_nba_teams order by links_to_team desc";
	public static String israeli_soccer_players_query = " select * from curr_israeli_soccer_players order by links_to_player desc";
	public static String israeli_soccer_teams_query = " select * from curr_israeli_soccer_teams order by links_to_team desc";
	public static String world_soccer_players_query = " select * from curr_world_soccer_players order by links_to_player desc";
	public static String world_soccer_teams_query = " select * from curr_world_soccer_teams order by links_to_team desc";
	
	private static String[][] data;
	public static Map<String, String> category_links_map = new HashMap<String, String>();
	
	//map table names to linked table names
	public static void fill_map(){
		category_links_map.put("Categories", "Movies");
		category_links_map.put("Actors", "Movies");
		category_links_map.put("Movies", "Actors");
		category_links_map.put("Artists", "Creations");
		category_links_map.put("Creations", "Artists");
		category_links_map.put("Countries", "Locations");
		category_links_map.put("Locations", "Countries");
		category_links_map.put("NBA players", "NBA teams");
		category_links_map.put("NBA teams", "NBA players");
		category_links_map.put("Israeli soccer players", "Israeli soccer teams");
		category_links_map.put("Israeli soccer teams", "Israeli soccer players");
		category_links_map.put("World soccer players", "World soccer teams");
		category_links_map.put("World soccer teams", "World soccer players");
	}
	
	//in the edit row window, an actor is being edited - set the attributes accordingly.
	static public void set_actor_data(){
		
		Edit_row_window.attrs = new String[]{"actor name:", "rating:", "birth year:"};
		Edit_row_window.attr_vals = new String[]
				{Edit_row_window.selected[0].getText(1), Edit_row_window.selected[0].getText(2), 
				Edit_row_window.selected[0].getText(3)};
		
		Edit_row_window.linked_query =	" select distinct m.id, m.name, m.num_links, m.year_made " +
											" from curr_cinema_movies m, " +
											" curr_cinema_actor_movie am" +
											" where am.movie_id=m.id and am.actor_id="+Edit_row_window.id+
											" order by num_links desc ";
		
		
		Edit_row_window.not_linked_query = " select distinct m.id, m.name, m.num_links, m.year_made " +
				" from curr_cinema_movies m ";
		
		Edit_row_window.linked_attrs = new String[]{"id", "movie name", "rating", "release year"};
		Edit_row_window.label_min_parameter="min. release year:";
		Edit_row_window.linked_category_name = "MOVIES";
	}
	
	//in the edit row window, a movie is being edited - set the attributes accordingly.
	static public void set_movie_data(){

		Edit_row_window.attrs = new String[]{"movie name:", "rating:", "release year:"};
		Edit_row_window.attr_vals = new String[]
				{Edit_row_window.selected[0].getText(1), Edit_row_window.selected[0].getText(2), 
				Edit_row_window.selected[0].getText(3)};
		
		Edit_row_window.linked_query =	" select distinct a.id, a.name, a.num_links, a.year_born " +
											" from curr_cinema_actors a, " +
											" curr_cinema_actor_movie am" +
											" where am.actor_id=a.id and am.movie_id="+Edit_row_window.id+
											" order by num_links desc ";
		
		
		Edit_row_window.not_linked_query = " select distinct a.id, a.name, a.num_links, a.year_born " +
				" from curr_cinema_actors a ";
		
		Edit_row_window.linked_attrs = new String[]{"id", "actor name", "rating", "birth year"};
		Edit_row_window.label_min_parameter="min. birth year:";
		Edit_row_window.linked_category_name = "ACTORS";
	}
	
	//in the edit row window, a movie category is being edited - set the attributes accordingly.
	static public void set_category_data(){

		Edit_row_window.attrs = new String[]{"category name:"};
		Edit_row_window.attr_vals = new String[]{Edit_row_window.selected[0].getText(1)};
		
		Edit_row_window.linked_query =	" select distinct m.id, m.name, m.num_links, m.year_made " +
				" from curr_cinema_movies m, " +
				" curr_cinema_movie_tag mt" +
				" where mt.movie_id=m.id and mt.tag_id="+Edit_row_window.id+
				" order by num_links desc ";

		Edit_row_window.not_linked_query = " select distinct m.id, m.name, m.num_links, m.year_made " +
				" from curr_cinema_movies m ";
		
		Edit_row_window.linked_attrs = new String[]{"id", "movie name", "rating", "release year"};
		Edit_row_window.label_min_parameter="min. release year:";
		Edit_row_window.linked_category_name = "MOVIES";
	}
	
	//in the edit row window, a musician (artist) is being edited - set the attributes accordingly.
	static public void set_artist_data(){
		
		Edit_row_window.attrs = new String[]{"artist name:", "rating:", "birth year:"};
		Edit_row_window.attr_vals = new String[]
				{Edit_row_window.selected[0].getText(1), Edit_row_window.selected[0].getText(2), 
				Edit_row_window.selected[0].getText(3)};
		
		Edit_row_window.linked_query =	" select distinct c.id, c.name, c.num_links, c.year_made " +
											" from curr_music_creations c, " +
											" curr_music_artist_creation ac" +
											" where ac.creation_id=c.id and ac.artist_id="+Edit_row_window.id+
											" order by num_links desc ";
		
		
		Edit_row_window.not_linked_query = " select distinct c.id, c.name, c.num_links, c.year_made " +
				" from curr_music_creations c ";
		
		Edit_row_window.linked_attrs = new String[]{"id", "creation name", "rating", "release year"};
		Edit_row_window.label_min_parameter="min. release year:";
		Edit_row_window.linked_category_name = "CREATIONS";
	}
	
	//in the edit row window, a musical creation is being edited - set the attributes accordingly.
	static public void set_creation_data(){

		Edit_row_window.attrs = new String[]{"creation name:", "rating:", "release year:"};
		Edit_row_window.attr_vals = new String[]
				{Edit_row_window.selected[0].getText(1), Edit_row_window.selected[0].getText(2), 
				Edit_row_window.selected[0].getText(3)};
		
		Edit_row_window.linked_query =	" select distinct a.id, a.name, a.num_links, a.birth_year " +
											" from curr_music_artists a, " +
											" curr_music_artist_creation ac " +
											" where ac.artist_id=a.id and ac.creation_id="+Edit_row_window.id+
											" order by num_links desc ";
				
		Edit_row_window.not_linked_query = " select distinct a.id, a.name, a.num_links, a.birth_year " +
				" from curr_music_artists a ";
		
		Edit_row_window.linked_attrs = new String[]{"id", "artist name", "rating", "birth year"};
		Edit_row_window.label_min_parameter="min. birth year:";
		Edit_row_window.linked_category_name = "ARTISTS";
	}
	
	//in the edit row window, a country is being edited - set the attributes accordingly.
	static public void set_country_data(){

		Edit_row_window.attrs = new String[]{"country name:", "area (1000 km^2):", "GDP per capita (1000 $):",
				"population (million):", "capital:", "GDP (billion $):"};
		Edit_row_window.attr_vals = new String[]
				{Edit_row_window.selected[0].getText(1), Edit_row_window.selected[0].getText(2), 
				Edit_row_window.selected[0].getText(3), Edit_row_window.selected[0].getText(4), 
				Edit_row_window.selected[0].getText(5), Edit_row_window.selected[0].getText(6)};
		
		Edit_row_window.linked_query =	" select distinct l.id, l.name, l.num_links, l.population " +
											" from curr_places_locations l, " +
											" curr_places_location_country lc " +
											" where lc.location_id=l.id and lc.country_id="+Edit_row_window.id+
											" order by num_links desc ";
				
		Edit_row_window.not_linked_query = " select distinct l.id, l.name, l.num_links, l.population " +
				" from curr_places_locations l ";
		
		Edit_row_window.linked_attrs = new String[]{"id", "location name", "rating", "population"};
		Edit_row_window.label_min_parameter="min. population:";
		Edit_row_window.linked_category_name = "LOCATIONS";
	}
	
	//in the edit row window, a location is being edited - set the attributes accordingly.
	static public void set_location_data(){

		Edit_row_window.attrs = new String[]{"location name:", "rating:", "population:"};
		Edit_row_window.attr_vals = new String[]
				{Edit_row_window.selected[0].getText(1), Edit_row_window.selected[0].getText(2), 
				Edit_row_window.selected[0].getText(3)};
		
		Edit_row_window.linked_query =	
				" select distinct c.`id`, c.`name`, c.`area (1000 km^2)`, c.`GDP per capita (1000 $)`, " +
								"c.`population (million)`, c.`capital`, c.`GDP (billion $)` " +
											" from curr_places_countries c, " +
											" curr_places_location_country lc " +
											" where lc.country_id=c.id and lc.location_id="+Edit_row_window.id+
											" order by c.`GDP (billion $)` desc ";
				
		Edit_row_window.not_linked_query = " select distinct c.`id`, c.`name`, c.`area (1000 km^2)`, c.`GDP per capita (1000 $)`, " +
				" c.`population (million)`, c.`capital`, c.`GDP (billion $)` " +
				" from curr_places_countries c ";
		
		Edit_row_window.linked_attrs = new String[]{"id:", "country name:", "area (1000 km^2):", "GDP per capita (1000 $):",
				"population (million):", "capital:", "GDP (billion $):"};
		Edit_row_window.label_min_parameter="min. population:";
		Edit_row_window.linked_category_name = "COUNTRIES";
	}
	
	//in the edit row window, a sports player is being edited - set the attributes accordingly.
	static public void set_player_data(String league){
		
		Edit_row_window.attrs = new String[]{"player name:", "rating:", "birth year:"};
		Edit_row_window.attr_vals = new String[]
				{Edit_row_window.selected[0].getText(1), Edit_row_window.selected[0].getText(2), 
				Edit_row_window.selected[0].getText(3)};
		
		Edit_row_window.linked_query =	" select distinct t.id, t.name, t.links_to_team, t.creation_year " +
											" from curr_"+league+"_teams t, " +
											" curr_"+league+"_player_team pt" +
											" where pt.team_id=t.id and pt.player_id="+Edit_row_window.id+
											" order by links_to_team desc ";
		
		
		Edit_row_window.not_linked_query = " select distinct t.id, t.name, t.links_to_team, t.creation_year " +
				" from curr_"+league+"_teams t ";
		
		Edit_row_window.linked_attrs = new String[]{"id", "team name", "rating", "creation year"};
		Edit_row_window.label_min_parameter="min. creation year:";
		Edit_row_window.linked_category_name = "TEAMS";
	}
	
	//in the edit row window, a sports team is being edited - set the attributes accordingly.
	static public void set_team_data(String league){

		Edit_row_window.attrs = new String[]{"team name:", "rating:", "release year:"};
		Edit_row_window.attr_vals = new String[]
				{Edit_row_window.selected[0].getText(1), Edit_row_window.selected[0].getText(2), 
				Edit_row_window.selected[0].getText(3)};
		
		Edit_row_window.linked_query =	" select distinct p.id, p.name, p.links_to_player, p.birth_year " +
											" from curr_"+league+"_players p, " +
											" curr_"+league+"_player_team pt " +
											" where pt.player_id=p.id and pt.team_id="+Edit_row_window.id+
											" order by links_to_player desc ";
				
		Edit_row_window.not_linked_query = " select distinct p.id, p.name, p.links_to_player, p.birth_year " +
				" from curr_"+league+"_players p ";
		
		Edit_row_window.linked_attrs = new String[]{"id", "player name", "rating", "birth year"};
		Edit_row_window.label_min_parameter="min. birth year:";
		Edit_row_window.linked_category_name = "PLAYERS";
	}

	
	//search and filter a table according to the user entered parameters. if 'id' not 0 - we're viewing
	//a table that is linked to a specific entity (in another table), and should only search within the
	//items linked to it.
	public static void filter_search(String search_var, int min_rating, int min_year, int min_population, 
			String table_name, Table tbl, int id) throws SQLException{
		
		String match_links = " where ";

		if(table_name.compareTo("Actors")==0){
			
			if(id>0){
				match_links = ", curr_cinema_actor_movie am " +
								" where am.actor_id = a.id and am.movie_id="+id+" and ";
			}
			
			String query = " select distinct a.id, a.name, a.num_links, a.year_born from curr_cinema_actors a" + 
					match_links +
					" a.name like '%"+search_var+"%' " +
					" and a.num_links >="+min_rating+
					" and a.year_born >="+min_year+" ";
			
			MakeTheLink.ui.Edit_menu_gui_funcs.refresh_table(tbl, query);
		}
		
		if(table_name.compareTo("Movies")==0){
			
			//positive id means we're getting the list of movies for some specific actor
			if(id>0){
				match_links = ", curr_cinema_actor_movie am " +
								" where am.movie_id = m.id and am.actor_id="+id+" and ";
			}
			
			//negative id means we're getting the list of movies for some specific category
			if(id<0){
				match_links = ", curr_cinema_movie_tag mt " +
						" where mt.movie_id = m.id and mt.tag_id="+(0-id)+" and ";
			}
			
			String query = " select distinct m.id, m.name, m.num_links, m.year_made from curr_cinema_movies m" + 
					match_links +
					" m.name like '%"+search_var+"%' " +
					" and m.num_links >="+min_rating+
					" and m.year_made >="+min_year+" ";

			MakeTheLink.ui.Edit_menu_gui_funcs.refresh_table(tbl, query);
		}
		
		if(table_name.compareTo("Categories")==0){
			
			String query = " select distinct t.id, t.name from curr_cinema_tags t" + 
					match_links +
					" t.name like '%"+search_var+"%' ";

			MakeTheLink.ui.Edit_menu_gui_funcs.refresh_table(tbl, query);
		}
		
		if(table_name.compareTo("Artists")==0){
			
			if(id>0){
				match_links = ", curr_music_artist_creation ac " +
								" where ac.artist_id = a.id and ac.creation_id="+id+" and ";
			}
			
			String query = " select distinct a.id, a.name, a.num_links, a.birth_year from curr_music_artists a" + 
					match_links +
					" a.name like '%"+search_var+"%' " +
					" and a.num_links >="+min_rating+
					" and a.birth_year >="+min_year+" ";

			MakeTheLink.ui.Edit_menu_gui_funcs.refresh_table(tbl, query);
		}
		
		if(table_name.compareTo("Creations")==0){
			
			if(id>0){
				match_links = ", curr_music_artist_creation ac " +
								" where ac.creation_id = c.id and ac.artist_id="+id+" and ";
			}
			
			String query = " select distinct c.id, c.name, c.num_links, c.year_made from curr_music_creations c" + 
					match_links +
					" c.name like '%"+search_var+"%' " +
					" and c.num_links >="+min_rating+
					" and c.year_made >="+min_year+" ";

			MakeTheLink.ui.Edit_menu_gui_funcs.refresh_table(tbl, query);
		}
		
		if(table_name.compareTo("Countries")==0){
			
			if(id>0){
				match_links = ", curr_places_location_country lc " +
								" where lc.country_id = c.id and lc.location_id="+id+" and ";
			}
			
			String query = " select distinct c.id, c.`name`, c.`area (1000 km^2)`, c.`GDP per capita (1000 $)`, " +
								"c.`population (million)`, c.`capital`, c.`GDP (billion $)` " +
					" from curr_places_countries c " +
					match_links +
					" c.name like '%"+search_var+"%' " +
					" and (c.`population (million)` >="+(((double)min_population)/1000000)+" or c.`population (million)` is null) ";

			MakeTheLink.ui.Edit_menu_gui_funcs.refresh_table(tbl, query);
		}
		
		if(table_name.compareTo("Locations")==0){
			
			if(id>0){
				match_links = ", curr_places_location_country lc " +
								" where lc.location_id = l.id and lc.country_id="+id+" and ";
			}
			
			String query = " select distinct l.id, l.name, l.num_links, l.population from curr_places_locations l" + 
					match_links +
					" l.name like '%"+search_var+"%' " +
					" and l.num_links >="+min_rating+
					" and l.population >="+min_population+" ";

			MakeTheLink.ui.Edit_menu_gui_funcs.refresh_table(tbl, query);
		}
		
		if(table_name.compareTo("NBA players")==0 || table_name.compareTo("Israeli soccer players")==0 ||
				table_name.compareTo("World soccer players")==0){
			
			String league = table_name.compareTo("NBA players")==0 ? "nba":
				table_name.compareTo("Israeli soccer players")==0?"israeli_soccer":"world_soccer";
			
			if(id>0){
				match_links = ", curr_"+league+"_player_team pt " +
								" where pt.player_id = p.id and pt.team_id="+id+" and ";
			}
			
			String query = " select distinct p.id, p.name, p.links_to_player, "+
													"p.birth_year from curr_"+league+"_players p " + 
					match_links +
					" p.name like '%"+search_var+"%' " +
					" and p.links_to_player >="+min_rating+
					" and p.birth_year >="+min_year+" ";

			MakeTheLink.ui.Edit_menu_gui_funcs.refresh_table(tbl, query);
		}
		
		
		if(table_name.compareTo("NBA teams")==0 || table_name.compareTo("Israeli soccer teams")==0 ||
				table_name.compareTo("World soccer teams")==0){
			
			String league = table_name.compareTo("NBA teams")==0 ? "nba":
				table_name.compareTo("Israeli soccer teams")==0?"israeli_soccer":"world_soccer";
			
			if(id>0){
				match_links = ", curr_"+league+"_player_team pt " +
								" where pt.team_id = t.id and pt.player_id="+id+" and ";
			}
			
			String query = " select distinct t.id, t.name, t.links_to_team, t.creation_year from curr_"+league+"_teams t" + 
					match_links +
					" t.name like '%"+search_var+"%' " +
					" and t.links_to_team >="+min_rating+
					" and t.creation_year >="+min_year+" ";

			MakeTheLink.ui.Edit_menu_gui_funcs.refresh_table(tbl, query);
		}
	}
	
	//removes links between an entity and some of its linked items from the database
	public static void un_link_items(String source_table_name, int id, int[] indexes) throws SQLException{
		PreparedStatement pstmt=null;
		Connection conn = Connection_pooling.cpds.getConnection();
		
		if(source_table_name.compareTo("Actors")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_cinema_actor_movie where actor_id = "+id+" and movie_id = ? ");
		if(source_table_name.compareTo("Movies")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_cinema_actor_movie where movie_id = "+id+" and actor_id = ? ");
		if(source_table_name.compareTo("Categories")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_cinema_movie_tag where tag_id = "+id+" and movie_id = ? ");
		if(source_table_name.compareTo("Artists")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_music_artist_creation where artist_id = "+id+" and creation_id = ? ");
		if(source_table_name.compareTo("Creations")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_music_artist_creation where creation_id = "+id+" and artist_id = ? ");
		if(source_table_name.compareTo("Countries")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_places_location_country where country_id = "+id+" and location_id = ? ");
		if(source_table_name.compareTo("Locations")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_places_location_country where location_id = "+id+" and country_id = ? ");
		if(source_table_name.compareTo("NBA players")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_nba_player_team where player_id = "+id+" and team_id = ? ");
		if(source_table_name.compareTo("NBA teams")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_nba_player_team where team_id = "+id+" and player_id = ? ");
		if(source_table_name.compareTo("Israeli soccer players")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_israeli_soccer_player_team where player_id = "+id+" and team_id = ? ");
		if(source_table_name.compareTo("Israeli soccer teams")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_israeli_soccer_player_team where team_id = "+id+" and player_id = ? ");
		if(source_table_name.compareTo("World soccer players")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_world_soccer_player_team where player_id = "+id+" and team_id = ? ");
		if(source_table_name.compareTo("World soccer teams")==0)
			pstmt = conn.prepareStatement("DELETE FROM curr_world_soccer_player_team where team_id = "+id+" and player_id = ? ");

		conn.setAutoCommit(false);
		try{
			execute_stmt(pstmt, indexes);
		}catch(SQLException e){
			System.out.println(e);
			conn.rollback();
		}
		conn.commit();
		conn.setAutoCommit(true);
		
		pstmt.close();
		conn.close();
	}
	
	//creates links in the database between an entity and entities in its linked table
	public static void link_items(String source_table_name, int id, int[] indexes) throws SQLException{
		PreparedStatement pstmt=null;
		Connection conn = Connection_pooling.cpds.getConnection();
		
		if(source_table_name.compareTo("Actors")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_cinema_actor_movie (actor_id, movie_id) VALUES ("+id+",?) ");
		if(source_table_name.compareTo("Movies")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_cinema_actor_movie (actor_id, movie_id) VALUES (?,"+id+") ");
		if(source_table_name.compareTo("Categories")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_cinema_movie_tag (category_id, movie_id) VALUES ("+id+",?) ");
		if(source_table_name.compareTo("Artists")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_music_artist_creation (artist_id, creation_id) VALUES ("+id+",?) ");
		if(source_table_name.compareTo("Creations")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_music_artist_creation (artist_id, creation_id) VALUES (?,"+id+") ");
		if(source_table_name.compareTo("Countries")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_places_location_country (country_id, location_id) VALUES ("+id+",?) ");
		if(source_table_name.compareTo("Locations")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_places_location_country (country_id, location_id) VALUES (?,"+id+") ");
		if(source_table_name.compareTo("NBA players")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_nba_player_team (player_id, team_id) VALUES ("+id+",?) ");
		if(source_table_name.compareTo("NBA teams")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_nba_player_team (player_id, team_id) VALUES (?,"+id+") ");
		if(source_table_name.compareTo("Israeli soccer players")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_israeli_soccer_player_team (player_id, team_id) VALUES ("+id+",?) ");
		if(source_table_name.compareTo("Israeli soccer teams")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_israeli_soccer_player_team (player_id, team_id) VALUES (?,"+id+") ");
		if(source_table_name.compareTo("World soccer players")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_world_soccer_player_team (player_id, team_id) VALUES ("+id+",?) ");
		if(source_table_name.compareTo("World soccer teams")==0)
			pstmt = conn.prepareStatement("INSERT IGNORE INTO curr_world_soccer_player_team (player_id, team_id) VALUES (?,"+id+") ");

		conn.setAutoCommit(false);
		try{
			execute_stmt(pstmt, indexes);
		}catch(SQLException e){
			System.out.println(e);
			conn.rollback();
		}
		conn.commit();
		conn.setAutoCommit(true);
		
		pstmt.close();
		conn.close();
	}
	
	
	private static void execute_stmt(PreparedStatement pstmt, int[] indexes) throws SQLException{
		int cnt=0;
		for (int i=0; i<indexes.length; i++) {
		    
			pstmt.setInt(1, indexes[i]);
			pstmt.addBatch();
			cnt++;
			if(cnt>5000){
				pstmt.executeBatch();
				cnt=0;
			}
		}
		if(cnt>0)
			pstmt.executeBatch();
	}
	
	//saves to the database the textual attributes entered in the edit row window
	public static void save_attributes(String table_name, int id, Text[] attrs) throws SQLException{
		
		String name=StringEscapeUtils.escapeSql(attrs[0].getText());
		int rating;
		try{
			rating=Integer.parseInt(StringEscapeUtils.escapeSql(attrs[1].getText()));
		}catch (Exception e){
			rating=0;
		}
		int year;
		try{
			year=Integer.parseInt(StringEscapeUtils.escapeSql(attrs[2].getText()));
		}catch (Exception e){
			year=0;
		}
		String query="";
		
		if(table_name.compareTo("Actors")==0){
			query = " UPDATE IGNORE curr_cinema_actors "+
					" SET name='"+name+"', num_links="+rating+", year_born="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("Movies")==0){
			query = " UPDATE IGNORE curr_cinema_movies "+
					" SET name='"+name+"', num_links="+rating+", year_made="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("Categories")==0){
			query = " UPDATE IGNORE curr_cinema_tags "+
					" SET name='"+name+
					"' WHERE id="+id;
		}
		if(table_name.compareTo("Artists")==0){
			query = " UPDATE IGNORE curr_music_artists "+
					" SET name='"+name+"', num_links="+rating+", birth_year="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("Creations")==0){
			query = " UPDATE IGNORE curr_music_creations "+
					" SET name='"+name+"', num_links="+rating+", year_made="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("Locations")==0){
			query = " UPDATE IGNORE curr_places_locations "+
					" SET name='"+name+"', num_links="+rating+", population="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("NBA players")==0){
			query = " UPDATE IGNORE curr_nba_players "+
					" SET name='"+name+"', links_to_player="+rating+", birth_year="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("NBA teams")==0){
			query = " UPDATE IGNORE curr_nba_teams "+
					" SET name='"+name+"', links_to_team="+rating+", creation_year="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("Israeli soccer players")==0){
			query = " UPDATE IGNORE curr_israeli_soccer_players "+
					" SET name='"+name+"', links_to_player="+rating+", birth_year="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("Israeli soccer teams")==0){
			query = " UPDATE IGNORE curr_israeli_soccer_teams "+
					" SET name='"+name+"', links_to_team="+rating+", creation_year="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("World soccer players")==0){
			query = " UPDATE IGNORE curr_world_soccer_players "+
					" SET name='"+name+"', links_to_player="+rating+", birth_year="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("World soccer teams")==0){
			query = " UPDATE IGNORE curr_world_soccer_teams "+
					" SET name='"+name+"', links_to_team="+rating+", creation_year="+year+
					" WHERE id="+id;
		}
		if(table_name.compareTo("Countries")==0){
			double area;
			try{
				area = Double.parseDouble(StringEscapeUtils.escapeSql(attrs[1].getText()));
			}catch(Exception e){
				area=0.0;
			}
			double GDP_pc;
			try{
				GDP_pc = Double.parseDouble(StringEscapeUtils.escapeSql(attrs[2].getText()));
			}catch(Exception e){
				GDP_pc=0.0;
			}
			double population;
			try{
				population = Double.parseDouble(StringEscapeUtils.escapeSql(attrs[3].getText()));
			}catch(Exception e){
				population=0.0;
			}
			String capital = StringEscapeUtils.escapeSql(attrs[4].getText());
			double GDP;
			try{
				GDP = Double.parseDouble(StringEscapeUtils.escapeSql(attrs[5].getText()));
			}catch(Exception e){
				GDP=0.0;
			}
			query = " UPDATE IGNORE curr_places_countries "+
					" SET `name`='"+name+"', `area (1000 km^2)`="+area+", `GDP per capita (1000 $)`="+GDP_pc+
					", `population (million)`="+population+", `capital`='"+capital+"', `GDP (billion $)`="+GDP+
					" WHERE `id`="+id;
		}
		Connection conn = Connection_pooling.cpds.getConnection();
		Statement stmt = conn.createStatement();
		
		
		conn.setAutoCommit(false);
		stmt.executeUpdate(query);
		conn.commit();
		conn.setAutoCommit(true);
		
		stmt.close();
		conn.close();
	}
	
	//deletes from the database the items that were not selected by the user in the table that is
	//being edited
	public static void crop_rows(String table_name, int[] indexes) throws SQLException{
		
		PreparedStatement pstmt=null;
		Connection conn = Connection_pooling.cpds.getConnection();
		
		Statement stmt = conn.createStatement();
		ResultSet rst=null;
		String stmt_query="";
		String pstmt_query="";
		int[] all_indexes;
		int size=0;
		
		String query_var = "";
		
		HashSet<Integer> set = new HashSet<Integer>();
		for(int i=0; i<indexes.length; i++){
			set.add(indexes[i]);
		}
		
		if(table_name.compareTo("Actors")==0){
			query_var="cinema_actors";
		}
		if(table_name.compareTo("Movies")==0){
			query_var="cinema_movies";
		}
		if(table_name.compareTo("Categories")==0){
			query_var="cinema_tags";
		}
		if(table_name.compareTo("Artists")==0){
			query_var="music_artists";
		}
		if(table_name.compareTo("Creations")==0){
			query_var="music_creations";
		}
		if(table_name.compareTo("Countries")==0){
			query_var="places_countries";
		}
		if(table_name.compareTo("Locations")==0){
			query_var="places_locations";
		}
		if(table_name.compareTo("NBA players")==0){
			query_var="nba_players";
		}
		if(table_name.compareTo("NBA teams")==0){
			query_var="nba_teams";
		}
		if(table_name.compareTo("Israeli soccer players")==0){
			query_var="israeli_soccer_players";
		}
		if(table_name.compareTo("Israeli soccer teams")==0){
			query_var="israeli_soccer_teams";
		}
		if(table_name.compareTo("World soccer players")==0){
			query_var="world_soccer_players";
		}
		if(table_name.compareTo("World soccer teams")==0){
			query_var="world_soccer_teams";
		}
		
		stmt_query = " select id from curr_"+query_var+" ";
		pstmt_query = " delete from curr_"+query_var+" where id = ? ";
		pstmt = conn.prepareStatement(pstmt_query);
		
		conn.setAutoCommit(false);
		rst=stmt.executeQuery(stmt_query);
		
		if (rst.last()) {
			  size = rst.getRow();
			  rst.beforeFirst();
		}
		
		all_indexes = new int[size];
		for(int i=0; rst.next(); i++){
			try{
				all_indexes[i]=rst.getInt(1);
			}catch (Exception e){
				all_indexes[i]=0;
			}
		}
		
		pstmt = conn.prepareStatement(pstmt_query);
		execute_crop_stmt(pstmt, all_indexes, set);
		conn.commit();
		conn.setAutoCommit(true);
		
		
		
		stmt.close();
		pstmt.close();
		conn.close();
	}
	
	
	private static void execute_crop_stmt(PreparedStatement pstmt, int[] indexes, HashSet<Integer> set) throws SQLException{
		int cnt=0;
		for (int i=0; i<indexes.length; i++) {
		    if(!set.contains(indexes[i])){
				pstmt.setInt(1, indexes[i]);
				pstmt.addBatch();
				cnt++;
				if(cnt>5000){
					pstmt.executeBatch();
					cnt=0;
				}
		    }
		}
		if(cnt>0)
			pstmt.executeBatch();
	}
	
	//deletes from the database the items that were selected by the user
	public static void delete_rows(String table_name, int[] indexes) throws SQLException{
		
		PreparedStatement pstmt=null;
		Connection conn = Connection_pooling.cpds.getConnection();
		String pstmt_query="";
		
		String query_var = "";
		
		if(table_name.compareTo("Actors")==0){
			query_var="cinema_actors";
		}
		if(table_name.compareTo("Movies")==0){
			query_var="cinema_movies";
		}
		if(table_name.compareTo("Categories")==0){
			query_var="cinema_tags";
		}
		if(table_name.compareTo("Artists")==0){
			query_var="music_artists";
		}
		if(table_name.compareTo("Creations")==0){
			query_var="music_creations";
		}
		if(table_name.compareTo("Countries")==0){
			query_var="places_countries";
		}
		if(table_name.compareTo("Locations")==0){
			query_var="places_locations";
		}
		if(table_name.compareTo("NBA players")==0){
			query_var="nba_players";
		}
		if(table_name.compareTo("NBA teams")==0){
			query_var="nba_teams";
		}
		if(table_name.compareTo("Israeli soccer players")==0){
			query_var="israeli_soccer_players";
		}
		if(table_name.compareTo("Israeli soccer teams")==0){
			query_var="israeli_soccer_teams";
		}
		if(table_name.compareTo("World soccer players")==0){
			query_var="world_soccer_players";
		}
		if(table_name.compareTo("World soccer teams")==0){
			query_var="world_soccer_teams";
		}
		
		pstmt_query = " delete from curr_"+query_var+" where id = ? ";
		
		
		conn.setAutoCommit(false);
		pstmt = conn.prepareStatement(pstmt_query);
		execute_delete_stmt(pstmt, indexes);
		conn.commit();
		conn.setAutoCommit(true);
		
		pstmt.close();
		conn.close();
	}
	
	private static void execute_delete_stmt(PreparedStatement pstmt, int[] indexes) throws SQLException{
		int cnt=0;
		for (int i=0; i<indexes.length; i++) {

			pstmt.setInt(1, indexes[i]);
			pstmt.addBatch();
			cnt++;
			if(cnt>5000){
				pstmt.executeBatch();
				cnt=0;
		    }
		}
		if(cnt>0)
			pstmt.executeBatch();
	}
	
	//adds a row with the specified name to the database table that is being viewed.
	//other attributes get default values and can be edited later.
	public static void add_row(String table_name, String name) throws SQLException{
		
		String query="";
		
		if(table_name.compareTo("Actors")==0){
			query=" insert ignore into curr_cinema_actors (name, num_links, year_born) "+
						" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("Movies")==0){
			query=" insert ignore into curr_cinema_movies (name, num_links, year_made) "+
					" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("Categories")==0){
			query=" insert ignore into curr_cinema_movies (name) "+
					" VALUES ('"+name+"') ";
		}
		if(table_name.compareTo("Artists")==0){
			query=" insert ignore into curr_music_artists (name, num_links, birth_year) "+
					" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("Creations")==0){
			query=" insert ignore into curr_music_creations (name, num_links, year_made) "+
					" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("Locations")==0){
			query=" insert ignore into curr_places_locations (name, num_links, population) "+
					" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("NBA players")==0){
			query=" insert ignore into curr_nba_players (name, links_to_player, birth_year) "+
					" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("NBA teams")==0){
			query=" insert ignore into curr_nba_teams (name, links_to_team, creation_year) "+
					" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("Israeli soccer players")==0){
			query=" insert ignore into curr_israeli_soccer_players (name, links_to_player, birth_year) "+
					" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("Israeli soccer teams")==0){
			query=" insert ignore into curr_israeli_soccer_teams (name, links_to_team, creation_year) "+
					" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("World soccer players")==0){
			query=" insert ignore into curr_world_soccer_players (name, links_to_player, birth_year) "+
					" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("World soccer teams")==0){
			query=" insert ignore into curr_world_soccer_teams (name, links_to_team, creation_year) "+
					" VALUES ('"+name+"', 10000000, 0) ";
		}
		if(table_name.compareTo("Countries")==0){
			query=" insert ignore into curr_places_countries " + 
					" (`name`, `area (1000 km^2)`, `GDP per capita (1000 $)`, `population (million)`, " +
					" `capital`, `GDP (billion $)` ) "+
					" VALUES ('"+name+"', 0.0, 0.0, 0.0, '', 0.0) ";
		}
		Connection conn = Connection_pooling.cpds.getConnection();
		Statement stmt = conn.createStatement();
			
		conn.setAutoCommit(false);
		stmt.executeUpdate(query);
		conn.commit();
		conn.setAutoCommit(true);
		
		stmt.close();
		conn.close();
		
	}
	

	//create the data for table to be displayed
	public static String[][] create_table_data(final int col_count, final String query){
	
		final Runnable runnable = new Runnable(){
			public void run(){
				
				Connection conn;
				try {
					conn = Connection_pooling.cpds.getConnection();
					
					Statement stmt = conn.createStatement();
					int size=0;
					
					
					conn.setAutoCommit(false);
					ResultSet rst = stmt.executeQuery(query);
					conn.commit();
					conn.setAutoCommit(true);
					
					
					if (rst.last()) {
						  size = rst.getRow();
						  rst.beforeFirst();
					}
					
					data = new String[size][col_count];
					String tmp;
					for(int i=0; rst.next(); i++){
						
						for(int j=0;j<col_count; j++){
							tmp = rst.getString(j+1);
							data[i][j]= tmp==null?"":tmp;
						}
					}
					
					conn.close();
					rst.close();
					stmt.close();
				
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				
			}
		};
		Future<?> ft = MakeTheLink.core.MakeTheLinkMain.threadPool.submit(runnable);
		try {
			ft.get();
		} catch (InterruptedException e) {

			e.printStackTrace();
		} catch (ExecutionException e) {

			e.printStackTrace();
		}
		
		return data;
	}
	
	//create data to be entered to an existing table (instead of other data that was already removed from it)
	public static String[][] refresh_table_data(final int col_count, String query)throws SQLException {
		
		Connection conn = Connection_pooling.cpds.getConnection();
		Statement stmt = conn.createStatement();
		int size=0;
		
		ResultSet rst = stmt.executeQuery(query);
		
		if (rst.last()) {
			  size = rst.getRow();
			  rst.beforeFirst();
		}
		
		data = new String[size][col_count];
		String tmp;
		for(int i=0; rst.next(); i++){
			
			for(int j=0;j<col_count; j++){
				tmp = rst.getString(j+1);
				data[i][j]= tmp==null?"":tmp;
			}
		}
		conn.close();
		rst.close();
		stmt.close();
		
		return data;
	}
	
}





