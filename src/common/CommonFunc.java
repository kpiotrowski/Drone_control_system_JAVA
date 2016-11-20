package common;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
            byte[] hash = messageDigest.digest(pass.getBytes(StandardCharsets.UTF_8));
            return new String(HexBin.encode(hash));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean comparePass(String pass, String hash) {
        pass = hashPass(pass);
        if(pass.equals(hash)) return true;
        return false;
    }

    public static java.sql.Date parseDateToSQL(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date parsed = format.parse(date);
        java.sql.Date sql = new java.sql.Date(parsed.getTime());
        return sql;
    }

    public static String sqlDateToString(java.sql.Date date){
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(date);
    }

    public static String emptyNullStr(String str){
        if(str.length()==0) return null;
        return str;
    }
}
