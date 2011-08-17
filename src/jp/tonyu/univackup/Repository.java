package jp.tonyu.univackup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
	public Repository(SFile h) {
		home=h;
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
					FObject r = FolderScanner.scan(f);
					newCiFile().text("id\t"+r.id()+"\nsrc\t"+f.fullPath()+"\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
