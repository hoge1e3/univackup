package jp.tonyu.univackup;


public class FileEntry extends FObject {
	String id; long lastupdate;
	@Override
	public String id() {
		return id;
	}

	public FileEntry(String id, long lastupdate) {
		super();
		this.id = id;
		//this.created = created;
		this.lastupdate = lastupdate;
	}

	/*@Override
	public long created() {
		return created;
	}*/

	@Override
	public long lastupdate() {
		return lastupdate;
	}



}
