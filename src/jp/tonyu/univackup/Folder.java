package jp.tonyu.univackup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.SortedMap;
import java.util.TreeMap;

import jp.tonyu.debug.Log;
import jp.tonyu.util.MapAction;
import jp.tonyu.util.Maps;

public class Folder extends FObject {
	String name;
	public static long NO=0;
	long lastupdate=NO;
	public Folder(String name) {
		this.name=name;
	}
	public String id() {
		try {
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
		
	}
	public static final String thisId="<THIS>";
	public void writeTo(OutputStream str) throws IOException {
		final PrintWriter w=new PrintWriter(str);
		w.printf("%s\t%d\t%s\n", name, lastupdate, thisId);
		Maps.entries(files).each(new MapAction<String, FObject>() {
			
			@Override
			public void run(String key, FObject value) {
				w.printf("%s\t%d\t%s\n", key,  value.lastupdate(), value.id() );
			}
		});
		w.close();
	}

	@Override
	public long lastupdate() {
		return lastupdate;
	}

}
