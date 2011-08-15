import jp.tonyu.univackup.Repository;
import jp.tonyu.util.SFile;


public class Test {
	public static void main(String[] args) {
		Repository r = new Repository(new SFile("repository"));
		r.checkIn(new SFile("C:\\bin\\media\\FD"));
	}
}
