package common;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import com.sun.org.apache.xerces.internal.impl.dv.xs.FloatDV;
import javafx.scene.control.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
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
            return HexBin.encode(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean comparePass(String pass, String hash) {
        pass = hashPass(pass);
        return pass.equals(hash);
    }

    public static java.sql.Date parseDateToSQL(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date parsed = format.parse(date);
        return new java.sql.Date(parsed.getTime());
    }
    public static java.sql.Timestamp parseDateTimeToSQL(String datetime) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date parsed = format.parse(datetime);
        return new java.sql.Timestamp(parsed.getTime());
    }

    public static String sqlDateToString(java.sql.Date date){
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        if(date == null) return "";
        return df.format(date);
    }
    public static String sqlTimestampToString(Timestamp timestamp){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        if(timestamp==null) return "";
        return format.format(timestamp);
    }

    public static String emptyNullStr(String str){
        if(str.length()==0) return null;
        return str;
    }

    public static Long strToLong(String str){
        try{
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static Integer strToInteger(String str){
        try{
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static Float strToFloat(String str){
        try {
            return Float.parseFloat(str);
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
    public static void statSetVarPar(PreparedStatement pstmt, int index, Timestamp val) throws SQLException {
        if(val==null) pstmt.setNull(index, Types.FLOAT);
        else pstmt.setTimestamp(index, val);
    }

    public static void clearForm(Control[] list){
        for (Control c: list) {
            if(c instanceof Label) ((Label)c).setText("");
            if(c instanceof TextField) ((TextField)c).setText("");
            if(c instanceof ChoiceBox) ((ChoiceBox<?>)c).setValue(null);
            if(c instanceof TextArea) ((TextArea)c).setText("");
            if(c instanceof CheckBox) ((CheckBox)c).setSelected(false);
            if(c instanceof Button) ((Button)c).setDisable(true);
        }
    }


}
