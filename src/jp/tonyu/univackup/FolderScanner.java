package jp.tonyu.univackup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.tmatesoft.sqljet.core.SqlJetException;

import jp.tonyu.util.SFile;

public class FolderScanner {
	SFile src;
	Folder dst;
	public FolderScanner(SFile sFile) {
		src=sFile;
	}
	public static FObject scan(File f) throws IOException, SqlJetException {
		SFile sFile = new SFile(f);
		return scan(sFile);
	}
	public static FObject scan(SFile sFile) throws IOException, SqlJetException {
		if (sFile.isDir()) {
			return new FolderScanner(sFile).scan();
		} else {
			return new FileScanner(sFile).scan();
		}
	}
	public Folder scan() throws IOException, SqlJetException {
		Repository r = Repository.cur.get();
		Folder res=new Folder(src.name());
		for (SFile f:src) {
			FObject scan = scan(f);
			res.add(f.name(), scan);	
		}
		res.loaded=true;
		res.addToDB();
		SFile outf=r.objectFile(res.id());
		if (!outf.exists()) {
			res.writeTo(outf.outputStream());
		}
		return res;
	}
}
