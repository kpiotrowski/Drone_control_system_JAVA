package service;

import common.FilterParam;
import dataModels.DataModel;
import dataModels.Punkt_na_trasie;
import dataModels.Trasa;
import databaseController.MySQLController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by no-one on 18.11.16.
 */
public class PunktNaTrasieService extends Service implements ServiceInterface {

    private static final String table = "Punkt_na_trasie";
    private static final String insertType = "numer_kolejny,Trasa_nazwa,Trasa_Uzytkownik_id,Punkt_Kontrolny_id,Polozenie_id";
    private static final String selectPoints = "p.numer_kolejny";
    private static final String selectPointsTable =
            "SELECT p.numer_kolejny, pp.wspX, pp.wspY, pp.wspZ, p.Punkt_kontrolny_id, p.Polozenie_id  FROM Punkt_na_trasie p INNER JOIN Punkt_kontrolny pp ON p.Punkt_kontrolny_id = pp.id " +
                    "WHERE p.Trasa_Uzytkownik_id = ? AND p.Trasa_Nazwa = ? " +
                    "UNION " +
                    "SELECT p.numer_kolejny, pp.wspX, pp.wspY, pp.wspZ, p.Punkt_kontrolny_id, p.Polozenie_id  FROM Punkt_na_trasie p INNER JOIN Polozenie pp ON p.Polozenie_id = pp.id " +
                    "WHERE p.Trasa_Uzytkownik_id = ? AND p.Trasa_Nazwa = ?";


    public PunktNaTrasieService(MySQLController con){
        super(con);
    }

    @Override
    public Error insert(DataModel data) {return new Error("not to use");}

    @Override
    public Error update(DataModel data) {return new Error("You can't update route point");}

    @Override
    public Error delete(Integer id) {return new Error("Not tu use!");}

    @Override
    public Error validate(DataModel data) {
        Punkt_na_trasie p = (Punkt_na_trasie)data;
        if(p.getNumer_kolejny()==null || p.getNumer_kolejny()<0) return new Error("Numer kolejny jest niepoprawny");
        if(p.getWspX()==null) return new Error("Wsp X jest niepoprawny");
        if(p.getWspY()==null) return new Error("Wsp Y jest niepoprawny");
        if(p.getWspZ()==null) return new Error("Wsp Z jest niepoprawny");
        if(p.getTrasa_nazwa()==null) return new Error("Nazwa trasy w punkcie nie może być pusta");
        if(p.getTrasa_uzytkownik_id()==null) return new Error("Id uzytkownika w punkcie nie może być puste");
        return null;
    }

    public List<DataModel> findPointsForGivenRoute(Trasa t) throws SQLException {
        try(PreparedStatement pstmt = mysql.getCon().prepareStatement(selectPointsTable);){
            pstmt.setInt(1,t.getUzytkownik_id());
            pstmt.setString(2,t.getNazwa());
            pstmt.setInt(3,t.getUzytkownik_id());
            pstmt.setString(4,t.getNazwa());
            ResultSet rs = pstmt.executeQuery();
            return  this.parseToModel(rs);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<DataModel> parseToModel(ResultSet res) throws SQLException {
        ArrayList<DataModel> list = new ArrayList<>();
        while(res.next()){
            Punkt_na_trasie tr = new Punkt_na_trasie();
            tr.setNumer_kolejny(res.getInt(1));
            tr.setWspX(res.getFloat(2));
            tr.setWspY(res.getFloat(3));
            tr.setWspZ(res.getFloat(4));
            tr.setPunkt_kontrolny_id((Integer)res.getObject(5));
            tr.setPolozenie_id((Integer) res.getObject(6));
            list.add(tr);
        }
        res.close();
        return list;
    }

    Error insertRoutePoints(List<Punkt_na_trasie> list){
        String values="";
        int loop = 0;
        for (Punkt_na_trasie p :list) {
            if (loop > 0) values += ",";
            values += "(?,?,?,?,?)";
            ++loop;
        }
        String sql = String.format("INSERT INTO %s (%s) VALUES %s",table,insertType,values);
        try(PreparedStatement pstmt = mysql.getCon().prepareStatement(sql)){
            loop=0;
            for (Punkt_na_trasie p :list){
                    pstmt.setInt(loop*5+1,p.getNumer_kolejny());
                    pstmt.setString(loop*5+2,p.getTrasa_nazwa());
                    pstmt.setInt(loop*5+3,p.getTrasa_uzytkownik_id());
                    if(p.getPunkt_kontrolny_id()!=null) {
                        pstmt.setInt(loop * 5 + 4, p.getPunkt_kontrolny_id());
                        pstmt.setNull(loop*5+5, Types.INTEGER);
                    } else {
                        pstmt.setNull(loop * 5 + 4,Types.INTEGER);
                        pstmt.setInt(loop*5+5,p.getPolozenie_id());
                    }
                    ++loop;
            }
            if(loop==0) return null;
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return new Error(e.getMessage());
        }
        return null;
    }
}
