package jp.tonyu.univackup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import jp.tonyu.util.SFile;

public class FolderScanner {
	SFile src;
	Folder dst;
	public FolderScanner(SFile sFile) {
		src=sFile;
	}
	public static FObject scan(File f) throws IOException {
		SFile sFile = new SFile(f);
		return scan(sFile);
	}
	public static FObject scan(SFile sFile) throws IOException {
		if (sFile.isDir()) {
			return new FolderScanner(sFile).scan();
		} else {
			return new FileScanner(sFile).scan();
		}
	}
	public Folder scan() throws IOException {
		Folder res=new Folder(src.name());
		for (SFile f:src) {
			res.add(f.name(), scan(f));
		}
		Repository r = Repository.cur.get();
		SFile outf=r.objectFile(res.id());
		if (!outf.exists()) {
			res.writeTo(outf.outputStream());
		}
		return res;
	}
}
