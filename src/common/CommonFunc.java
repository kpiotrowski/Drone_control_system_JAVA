package common;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import com.sun.org.apache.xerces.internal.impl.dv.xs.FloatDV;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by no-one on 19.11.16.
 */
public abstract class CommonFunc {

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
        if(date == null) return "";
        return df.format(date);
    }

    public static String emptyNullStr(String str){
        if(str.length()==0) return null;
        return str;
    }

    public static Long strToLong(String str){
        try{
            Long l = Long.parseLong(str);
            return l;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static Integer strToInteger(String str){
        try{
            Integer l = Integer.parseInt(str);
            return l;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static Float strToFloat(String str){
        try {
            Float f = Float.parseFloat(str);
            return f;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static void statSetVarPar(PreparedStatement pstmt, int index, Float val) throws SQLException {
        if(val==null) pstmt.setNull(index, Types.FLOAT);
        else pstmt.setFloat(index, val);
    }
    public static void statSetVarPar(PreparedStatement pstmt, int index, Integer val) throws SQLException {
        if(val==null) pstmt.setNull(index, Types.INTEGER);
        else pstmt.setInt(index, val);
    }
    public static void statSetVarPar(PreparedStatement pstmt, int index, String val) throws SQLException {
        if(val==null) pstmt.setNull(index, Types.VARCHAR);
        else pstmt.setString(index, val);
    }
    public static void statSetVarPar(PreparedStatement pstmt, int index, java.sql.Date val) throws SQLException {
        if(val==null) pstmt.setNull(index, Types.DATE);
        else pstmt.setDate(index, val);
    }


}
