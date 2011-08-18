package jp.tonyu.univackup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;

import jp.tonyu.debug.Log;
import jp.tonyu.univackup.db.UDB;
import jp.tonyu.util.Context;
import jp.tonyu.util.Date;
import jp.tonyu.util.SFile;

public class Repository {
	SFile home;
	UDB udb;
	public UDB udb() {
		return udb;
	}
	public static final Context<Repository> cur=new Context<Repository>();
	public Repository(SFile h) throws SqlJetException {
		home=h;
		udb=new UDB(home.rel("index.db").javaIOFile(), 1);
	}
	public SFile objectHome() {
		return home.rel("objects");
	}
	public SFile ciHome() {
		return home.rel("checkins");
	}
	public SFile newCiFile() {
		String name = new Date().toString("yyyy_MM_dd_hh_mm_ss");
		return ciHome().rel(name+".ci");
	}
	public SFile objectFile(String id) {
		String idh=id.substring(0,2);
		String idt=id.substring(2);
		return objectHome().rel(idh).rel(idt);
	}
	public void checkIn(final SFile f) {
		cur.enter(this, new Runnable() {
			@Override
			public void run() {
				try {
					udb.beginTransaction(SqlJetTransactionMode.WRITE);
					FObject r = FolderScanner.scan(f);
					SFile newCiFile = newCiFile();
					newCiFile.text("id\t"+r.id()+"\nsrc\t"+f.fullPath()+"\n");
					udb.addCiEntry(r.id(), f.fullPath(), new Date().ticks());
					Log.d(this, "Commiting..");
					udb.commit();
					Log.d(this, "Commint done");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void close() throws SqlJetException {
		udb.close();
	}
}
