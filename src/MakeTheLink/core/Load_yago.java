//the main course of loading yago files to schema is managed here

package MakeTheLink.core;

import java.sql.*;
import java.util.Map;
import java.io.IOException;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;

import MakeTheLink.db.Load_yago_funcs;
import MakeTheLink.db.Connection_pooling;
import MakeTheLink.db.Copy_tmp_to_curr;
import MakeTheLink.db.Create_schema;
import MakeTheLink.db.Populate_tmp_schema;
import MakeTheLink.ui.Edit_menu_window;
import MakeTheLink.ui.ChooseUpdateUI;

public class Load_yago {
	
	private static String yago_file_path = "";
	
	public static void set_path(String str){
		yago_file_path=str.trim();
	}
	//the main course of loading yago files to schema is managed here
	public static void load_yago(ChooseUpdateUI ui)
			 throws ClassNotFoundException, SQLException, IOException{
		
		Load_yago_funcs.conn = Connection_pooling.cpds.getConnection();
		
		Load_yago_funcs.clean_aux();
		Populate_tmp_schema.clean_aux();
		
		ui.update_load_progress("Loading facts (0% completed)");
		Load_yago_funcs.load_facts(yago_file_path);
		ui.update_load_progress("Loading literal facts (20% completed)");
		Load_yago_funcs.load_literal_facts(yago_file_path);
		ui.update_load_progress("Loading types (30% completed)");
		Load_yago_funcs.load_types(yago_file_path);
		ui.update_load_progress("Loading wikipedia info (50% completed)");
		Load_yago_funcs.load_wiki(yago_file_path);
		
		Create_schema.delete("tmp");
		
		ui.update_load_progress("Populating music (75% completed)");
		Populate_tmp_schema.populate_music(Load_yago_funcs.conn);

		ui.update_load_progress("Populating cinema (80% completed)");
		Populate_tmp_schema.populate_cinema(Load_yago_funcs.conn);
		
		ui.update_load_progress("Populating places (82% completed)");
		Populate_tmp_schema.populate_places(Load_yago_funcs.conn);

		
		ui.update_load_progress("Populating sports (95% completed)");
		Populate_tmp_schema.populate_sports(Load_yago_funcs.conn);
		
		Load_yago_funcs.clean_aux();
		Populate_tmp_schema.clean_aux();

		ui.update_load_progress("Copying data from temporary tables (99% completed)");
		
		Load_yago_funcs.conn.close();
		
		//copy yago data from tmp to curr
		copy_from_tmp_to_curr();
		
		
		ui.update_load_progress("Yago loaded");
	}
	
	public static void clean_tmp() throws SQLException{
		Load_yago_funcs.clean_aux();
		Populate_tmp_schema.clean_aux();
	}
	
	public static void copy_from_tmp_to_curr() throws SQLException, ClassNotFoundException, IOException{
		Connection conn = Connection_pooling.cpds.getConnection();
		conn.setAutoCommit(false);
		Copy_tmp_to_curr.copy(conn);
		conn.commit();
		conn.setAutoCommit(true);
		conn.close();
	}
}
