package jp.tonyu.univackup.db;

import java.io.File;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import jp.tonyu.db.SqlJetHelper;
import jp.tonyu.db.SqlJetTableHelper;

public class UDB extends SqlJetHelper {
	private static final String FOLDER = "folder";
	private static final String CHEKIN = "chceckin";
	private static final String FENTRY = "fileentry";
	public UDB(File f,int version) throws SqlJetException {
		super(f,version);
	}
	@Override
	protected void onCreate(SqlJetDb db) throws SqlJetException {
		super.onCreate(db);
		
		//createBackRefTable(db);
		createObjectTable(db);
		createCheckinTable(db);
		createFolderTable(db);
	}
	/*private void createBackRefTable(SqlJetDb db) throws SqlJetException {
		// id  referer referee
		table(BACKREF)
		.a("id", "INTEGER PRIMARY KEY")
		.a("referer", "TEXT")
		.a("referee", "TEXT")
		.create()
		.createIndex("referee");
	}*/
	private void createCheckinTable(SqlJetDb db) throws SqlJetException {
		//  id     oid   parentOid   path  checkinDate  
		table(CHEKIN)
		.a("id", "INTEGER PRIMARY KEY")
		.a("oid", "TEXT")
		.a("path", "TEXT")
		.a("checkinDate", "INTEGER")
		.create()
		.createIndex("oid")
		.createIndex("checkinDate")
		;
	}
	private void createObjectTable(SqlJetDb db) throws SqlJetException {
		// id oid   name  revname lastupdate  
		table(FENTRY)
		.a("id", "INTEGER PRIMARY KEY")
		.a("parentOid", "TEXT")
		.a("type", "INTEGER")
		.a("name", "TEXT")
		.a("revname", "TEXT")
		.a("lastupadte", "INTEGER")
		.a("oid", "TEXT")
		.create()
		.createIndex("oid") 
		.createIndex("name")
		.createIndex("revname")
		.createIndex("lastupdate")
		.createIndex("oid,parentOid") 
		;
	}
	public void addFileEntry(String parentId, int type, String name, long lastupdate, String id) throws SqlJetException {
		SqlJetTableHelper fot = table(FENTRY);
		ISqlJetCursor cur = fot.lookup("oid,parentOid", id,parentId);
		if (cur.eof()) {
			fot.insert(null, parentId, type, name, rev(name), lastupdate, id);			
		}
		cur.close();
	}
	private static  Object rev(String name) {
		StringBuilder b=new StringBuilder();
		for (int i=name.length()-1 ; i>=0 ; i--) {
			b.append(name.charAt(i));
		}
		return b.toString();
	}
}
