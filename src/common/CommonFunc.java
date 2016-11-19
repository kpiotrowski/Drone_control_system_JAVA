package common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by no-one on 19.11.16.
 */
public final class CommonFunc {

    public static String hashPass(String pass) {
        if(pass.length()==0) return "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(pass.getBytes());
            String encryptedString = new String(messageDigest.digest());
            return encryptedString;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean comparePass(String pass, String hash) {
        pass = hashPass(pass);
        if(pass==hash) return true;
        return false;
    }

    public static java.sql.Date parseDateToSQL(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date parsed = format.parse(date);
        java.sql.Date sql = new java.sql.Date(parsed.getTime());
        return sql;
    }

    public static String emptyNullStr(String str){
        if(str.length()==0) return null;
        return str;
    }
}
