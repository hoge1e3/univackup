package jp.tonyu.univackup;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author http://www.syboos.jp
 * http://www.syboos.jp/java/doc/md5-algorithm.html
 * ※以下コードを無保証で自由にご利用できます。
 */
public class MD5 { 
    private static final String EMPTY = "d41d8cd98f00b204e9800998ecf8427e";

	/**
     * MD5アルゴリズム
     * 文字列を暗号化する
     * 
     * @param str 暗号化される文字列
     * @return 暗号化結果
     * @throws NoSuchAlgorithmException 
     */
     public static String crypt(String str) throws NoSuchAlgorithmException
     {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }
                        
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        byte[] hash = md.digest();

        return hashByte2MD5(hash);
    }
    
    /**
     * MD5アルゴリズム
     * byte[]配列を暗号化する
     * 
     * @param bytes 暗号化されるbyte[]配列
     * @return 暗号化結果
     * @throws NoSuchAlgorithmException 
     */
    public static String crypt(byte [] bytes) throws NoSuchAlgorithmException {
        if (bytes == null ) {
            throw new IllegalArgumentException("bytes to encript cannot be null");
        }
        if ( bytes.length == 0) return EMPTY;
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] hash = digest.digest(bytes);
        
        return hashByte2MD5(hash);
    }
    
    /**
     * MD5アルゴリズム
     * InputStreamを暗号化する（ファイルなどのチェックサムを求む）
     * 
     * @param in 暗号化されるInputStreamオブジェクト
     * @return 暗号化結果
     * @throws NoSuchAlgorithmException 
     */
    public static String crypt(InputStream in) throws NoSuchAlgorithmException, IOException {
        if (in == null ) {
            throw new IllegalArgumentException("InputStream can't be null");
        }
        if (in.available() == 0) {
        	return EMPTY;
        }
        
        MessageDigest digest = MessageDigest.getInstance("MD5");
        try {
            byte[] buff = new byte[4096];
            int len = 0;
            while ((len = in.read(buff, 0, buff.length)) >= 0) {
                digest.update(buff, 0, len);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        byte[] hash = digest.digest();
        
        return hashByte2MD5(hash);
    }
    
    //MD5　ハッシュ関数
    private static String hashByte2MD5(byte []hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        
        return hexString.toString();
    }
    
    //テスト
    public static void main(String [] args) {
        try {
            System.out.println(crypt("112233445566"));
            System.out.println(crypt("112233445566".getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}