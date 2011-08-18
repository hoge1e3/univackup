package jp.tonyu.univackup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.SortedMap;
import java.util.TreeMap;

import org.tmatesoft.sqljet.core.SqlJetException;

import jp.tonyu.debug.Log;
import jp.tonyu.util.MapAction;
import jp.tonyu.util.Maps;

public class Folder extends FObject {
	String name;
	public static long NO=0 ;//FileEntry.NOTSET;
	long lastupdate=NO;
	long size=0, fileCount=0, folderCount=0;
	boolean loaded=false;
	public Folder(String name) {
		this.name=name;
	}
	public String id() {
		try {
			assertLoaded();
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			writeTo(out);
			byte []b=out.toByteArray();
			return MD5.crypt(b); 
		} catch(Exception e) {
			e.printStackTrace();
			Log.die("Error - "+e);
			return null;
		}
	}

	SortedMap<String, FObject> files=new TreeMap<String, FObject>();
	public void add(String name, FObject f) {
		files.put(name, f);
		//if (created==NO || created>f.created()) created=f.created();
		if (lastupdate==NO || lastupdate<f.lastupdate()) lastupdate=f.lastupdate();
		size+=f.size();
		fileCount+=f.fileCount();
		folderCount+=f.folderCount();
		if (f instanceof Folder) {
			folderCount++;
		} else fileCount++;
			
	}
	public static final String thisId="<THIS>";
	public void writeTo(OutputStream str) throws IOException {
		if (!loaded) Log.die(this +" : "+name +" not a complete value");
		final PrintWriter w=new PrintWriter(str);
		outField(w,"name",name);
		outField(w,"lastupdate",lastupdate);
		outField(w,"size",size);
		outField(w,"fileCount",fileCount);
		outField(w,"folderCount",folderCount);
		w.println("entryFields\ttype\tname\tlastupdate\tid");
		Maps.entries(files).each(new MapAction<String, FObject>() {
			
			@Override
			public void run(String key, FObject value) {
				String type;
				if (value instanceof Folder) type="d"; else type="f";
				w.printf("%s\t%s\t%d\t%s\n", type,  key,  value.lastupdate(), value.id() );
			}
		});
		w.close();
	}

	private void outField(PrintWriter w, String name, Object value) {
		w.printf("%s\t%s\n", name, value+"");
	}
	@Override
	public long lastupdate() {
		return assertSet(lastupdate,"lastupdate");
	}
	private long assertSet(long value, String name) {
		//if (value==FileEntry.NOTSET) 
		assertLoaded();
		return value;
	}
	private void assertLoaded() {
		if (!loaded) Log.die(this +" : "+name +" not a complete value");
	}
	public void addToDB() throws SqlJetException {
		assertLoaded();
		final Repository r = Repository.cur.get();
		r.udb().addFolder(this);
		Maps.entries(files).each(new MapAction<String, FObject>() {
			
			@Override
			public void run(String name, FObject value) {
				try {
					//int type;
					//if (value instanceof Folder) type=1; else type=0;
					r.udb().addFileEntry(id(), name, value);//  type,  name,  value.lastupdate(), value.id()) ;
				} catch (SqlJetException e) {
					e.printStackTrace();
					Log.die("SQLJetException - "+e);
				}
			}
		});
	}
	@Override
	public long fileCount() {
		return assertSet(fileCount, "fileCount");
	}
	@Override
	public long folderCount() {
		return assertSet(folderCount, "folderCount");
	}
	@Override
	public long size() {
		return assertSet(size, "size");
	}
	public String getName() {
		return name;
	}

}
