package jp.tonyu.univackup;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

import jp.tonyu.debug.Log;
import jp.tonyu.util.SFile;

public class FileScanner {
	SFile src;
	public FileScanner(SFile sFile) {
		src=sFile;
	}

	public FObject scan() throws IOException {
		try {
			String id = MD5.crypt(src.inputStream());
			FileEntry res = new FileEntry(id , src.lastModified());
			System.out.print("Checkin "+src+"...");
			System.out.println(save(src, id));			
			
			return res;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			Log.die("Error ");
			return null;
		}
		
		
	}

	private static  boolean save(SFile src2, String id) throws IOException{
		Repository r = Repository.cur.get();
		SFile outf = r.objectFile(id);
		if (outf.exists()) return false;
		InputStream fi =src2.inputStream();
		OutputStream fo = outf.outputStream();
		byte [] b=new byte[1024];
		while (true) {
			int len=fi.read(b);
			if (len<=0) break;
			fo.write(b,0,len);
		}
		fi.close();
		fo.close();
		return true;
	}

}
