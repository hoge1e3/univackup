package jp.tonyu.univackup;

import jp.tonyu.debug.Log;
import jp.tonyu.util.SFile;


public class FileEntry extends FObject {
	public static final long NOTSET = -1;
	String id; long lastupdate;
	long size=NOTSET;
	@Override
	public String id() {
		return id;
	}

	public FileEntry(String id, SFile src) {
		super();
		this.id = id;
		//this.created = created;
		this.lastupdate = src.lastModified();
		this.size= src.size();
	}

	/*@Override
	public long created() {
		return created;
	}*/

	@Override
	public long lastupdate() {
		return lastupdate;
	}
	@Override
	public long fileCount() {
		return 0; // do not include myself
	}
	@Override
	public long folderCount() {
		return 0;
	}
	@Override
	public long size() {
		if (size==NOTSET) Log.die(this+": size not yet set");
		return size;
	}
	public void size(long s) {
		size=s;
	}
}
