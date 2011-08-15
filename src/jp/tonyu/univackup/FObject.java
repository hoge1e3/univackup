package jp.tonyu.univackup;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import jp.tonyu.debug.Log;

public abstract class FObject {
	//String id;
	public abstract String id();
	//public abstract void writeTo(OutputStream str);
	//public abstract long created();
	public abstract long lastupdate();
}
