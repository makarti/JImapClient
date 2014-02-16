package code.repository;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Stack;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

public class SqliteBD {
	private static final SqliteBD INSTANCE = new SqliteBD();
	SQLiteConnection db;
	SQLiteStatement st;
	Boolean isInitBD = false;	
	// Private constructor prevents instantiation from other classes
	private SqliteBD() {
		try{
			File fclass = new File(System.getProperty("java.class.path"));
			File dir = fclass.getAbsoluteFile().getParentFile();
			File f = new File(dir.toString()+"./db/cache.ndb");
			//File f = new File("./db/cache.ndb");
    		if(!f.exists())
    		{
    			f.getParentFile().mkdirs();
    			f.createNewFile();
    			isInitBD = true;
    		}
    		//SQLite.setLibraryPath(f.getParentFile().getAbsolutePath() +"/sqlite");
			db = new SQLiteConnection(f);
			db.open(true);
			init();
		} catch (SQLiteException ex){
			System.out.println("Instantiation SQLiteException: " + ex.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Common error: " + e.getMessage());
		}
	}
	
	public static SqliteBD getInstance() {
		return INSTANCE;
	}
	
	private void init()
	{
		exec("create table if not exists Account ('Id' integer primary key, 'login' text, 'pass' text, 'server' text, 'isInclude' text);");
		exec("create table if not exists filters ('Id' integer primary key, 'value' text);");
		exec("create table if not exists setting ('Id' integer primary key, 'period' integer);");
		if(isInitBD)
		{
			exec("INSERT INTO setting VALUES(null, '5');");
			exec("INSERT INTO setting VALUES(null, '30');");
			isInitBD = false;
		}
	}
	
	public boolean exec(String str){
		try{
			st = db.prepare(str);
			st.stepThrough();
			st.dispose();
			return true;
		} catch (SQLiteException ex){
			System.out.println("Query Execution SQLiteException: " + ex.getMessage());			
			return false;
		}
	}

	public boolean prepareQuery(String str){
		try{
			st = db.prepare(str);
		} catch (SQLiteException ex){
			System.out.println("Prepare Query SQLiteException: " + ex.getMessage());
			return false;
		}
		return true;
	}

	public Enumeration<String> fetch(){
		try{
			if(!st.step()){
				st.dispose();
				return null;
			}
			else{
				if(st.hasRow()) {
					int columns = st.columnCount();
					Stack<String> stack = new Stack<String>();
					for(int column = 0 ; column < columns; column++)
						stack.push(st.columnValue(column).toString());
					return stack.elements();
				} else {
					st.dispose();
					return null;
				}
			}
		} catch (SQLiteException ex){
			db.dispose();
			System.out.println("Fetch SQLiteException: " + ex.getMessage());
		}
		db.dispose();
		return null;
	}
	
	public void close(){
		db.dispose();
	}
}
