package jp.tonyu.univackup.db;

import java.io.File;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import jp.tonyu.db.SqlJetHelper;
import jp.tonyu.db.SqlJetTableHelper;
import jp.tonyu.univackup.FObject;
import jp.tonyu.univackup.Folder;

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
	private void createFolderTable(SqlJetDb db) throws SqlJetException {
		table(FOLDER)
		.a("id", "INTEGER PRIMARY KEY")
		.a("oid", "TEXT")
		.a("name", "TEXT")
		.a("lastupdate", "INTEGER")
		.a("size", "INTEGER")
		.a("fileCount", "INTEGER")
		.a("folderCount", "INTEGER")
		.create()
		.createIndex("oid")
		.createIndex("name")
		.createIndex("lastupdate")
		;
		//outField(w,"name",name);
		//outField(w,"lastupdate",lastupdate);
		//outField(w,"size",size);
		//outField(w,"fileCount",fileCount);
		//outField(w,"folderCount",folderCount);
		
	}
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
		.a("lastupdate", "INTEGER")
		.a("oid", "TEXT")
		.create()
		.createIndex("oid") 
		.createIndex("name")
		.createIndex("revname")
		.createIndex("lastupdate")
		.createIndex("oid,parentOid") 
		;
	}
	public void addFileEntry(String parentId, String name, FObject value) throws SqlJetException {
		SqlJetTableHelper fot = table(FENTRY);
		ISqlJetCursor cur = fot.lookup("oid,parentOid", value.id(),parentId);
		if (cur.eof()) {
			int type=(value instanceof Folder?1:0);
			fot.insert(null, parentId, type, name, rev(name), value.lastupdate(), value.id());			
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
	public void addFolder(Folder folder) throws SqlJetException {
		/*
		 * .a("name", "TEXT")
		.a("lastupdate", "INTEGER")
		.a("size", "INTEGER")
		.a("fileCount", "INTEGER")
		.a("folderCount", "INTEGER")
		 */
		SqlJetTableHelper ft = table(FOLDER);
		ISqlJetCursor cur = ft.lookup("oid", folder.id());
		if (cur.eof()) {		
			ft.insert(null, folder.id(), folder.getName(), folder.lastupdate(), folder.size(), folder.fileCount(), folder.folderCount());
		}
		cur.close();
	}
	public void addCiEntry(String id, String fullPath, long ticks) throws SqlJetException {
		/*
		 * .a("id", "INTEGER PRIMARY KEY")
		.a("oid", "TEXT")
		.a("path", "TEXT")
		.a("checkinDate", "INTEGER")
		 */
		SqlJetTableHelper ft = table(CHEKIN);
		ISqlJetCursor cur = ft.lookup("oid", id);
		if (cur.eof()) {		
			ft.insert(null, id, fullPath, ticks);
		}
		cur.close();
	}
}
