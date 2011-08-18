import org.tmatesoft.sqljet.core.SqlJetException;

import jp.tonyu.univackup.Repository;
import jp.tonyu.util.SFile;


public class Test {
	public static void main(String[] args) throws SqlJetException {
		Repository r = new Repository(new SFile("c:\\Archives\\repository"));
		r.checkIn(new SFile("C:\\bin\\media\\FD"));
		r.close();
	}
}
