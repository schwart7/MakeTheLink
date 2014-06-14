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
		Populate_tmp_schema.clean_aux(Load_yago_funcs.conn);
		
		Create_schema.destroy(Load_yago_funcs.conn, "tmp");
		Create_schema.create(Load_yago_funcs.conn, "tmp");
		
		ui.update_load_progress("Loading facts (0% completed)");
		Load_yago_funcs.load_facts(yago_file_path);
		ui.update_load_progress("Loading literal facts (20% completed)");
		Load_yago_funcs.load_literal_facts(yago_file_path);
		ui.update_load_progress("Loading types (30% completed)");
		Load_yago_funcs.load_types(yago_file_path);
		ui.update_load_progress("Loading wikipedia info (50% completed)");
		Load_yago_funcs.load_wiki(yago_file_path);
		
		ui.update_load_progress("Populating music (70% completed)");
		Populate_tmp_schema.populate_music(Load_yago_funcs.conn);

		ui.update_load_progress("Populating cinema (75% completed)");
		Populate_tmp_schema.populate_cinema(Load_yago_funcs.conn);
		
		ui.update_load_progress("Populating places (80% completed)");
		Populate_tmp_schema.populate_places(Load_yago_funcs.conn);

		
		ui.update_load_progress("Populating sports (95% completed)");
		Populate_tmp_schema.populate_sports(Load_yago_funcs.conn);
		
		Load_yago_funcs.clean_aux();
		Populate_tmp_schema.clean_aux(Load_yago_funcs.conn);

		ui.update_load_progress("Copying data from temporary tables (99% completed)");
		
		//copy yago data from tmp to curr
		Load_yago_funcs.conn.setAutoCommit(false);
		Copy_tmp_to_curr.copy(Load_yago_funcs.conn);
		Load_yago_funcs.conn.commit();
		Load_yago_funcs.conn.setAutoCommit(true);

		Create_schema.destroy(Load_yago_funcs.conn, "tmp");

		Load_yago_funcs.conn.close();
		
		ui.update_load_progress("Yago loaded");
	}
	
	public static void clean_tmp() throws SQLException{
		Load_yago_funcs.clean_aux();
		Populate_tmp_schema.clean_aux(Load_yago_funcs.conn);
		Create_schema.destroy(Load_yago_funcs.conn, "tmp");
	}
}
