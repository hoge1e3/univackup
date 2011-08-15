package jp.tonyu.univackup.db;

import java.io.File;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import jp.tonyu.db.SqlJetHelper;

public class UDB extends SqlJetHelper {
	private static final String BACKREF = "backref";
	private static final String BACKREF_REFEREE = "backref_referee";
	public UDB(File f,int version) throws SqlJetException {
		super(f,version);
	}
	@Override
	protected void onCreate(SqlJetDb db) throws SqlJetException {
		super.onCreate(db);
		
		createBackRefTable(db);
		createObjectTable(db);
		createCheckinTable(db);
	}
	private void createBackRefTable(SqlJetDb db) throws SqlJetException {
		// id  referer referee
		db.createTable("CRATE TABLE "+BACKREF+"(\n"+
				"id INTEGER PRIMARY KEY, \n"+
				"referer TEXT, \n"+
				"referee TEXT  \n)");
		db.createIndex("CREATE INDEX "+BACKREF_REFEREE+" on "+BACKREF+"(referee)");
	}
	private void createCheckinTable(SqlJetDb db) {
		//  id     oid   parentOid   path  checkinDate  
		db.createTable("CREATE TABLE "+CHECKIN+"(\n");
	}
	private void createObjectTable(SqlJetDb db) {
		// id   name  revname lastupdate  
		
	}
}
