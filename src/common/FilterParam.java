package common;

import jdk.internal.org.objectweb.asm.Type;
import jdk.nashorn.internal.objects.annotations.Getter;

/**
 * Created by no-one on 11.12.16.
 */
public class FilterParam {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setValStr(String valStr) {
        this.valStr = valStr;
    }

    public void setValInt(Integer valInt) {
        this.valInt = valInt;
    }


    public void setValFloat(Float valFloat) {
        this.valFloat = valFloat;
    }

    public Object getVal(){
        if(this.valStr != null) return this.valStr;
        if(this.valInt != null) return this.valInt;
        return this.valFloat;
    }

    private String name;
    private String sign;

    private String valStr;
    private Integer valInt;
    private Float valFloat;

    public FilterParam(){}

    public static FilterParam newF(String name, String sign, Object val){
        FilterParam f = new FilterParam();
        f.name=name;
        f.sign=sign;
        if(val instanceof String) f.valStr=(String) val;
        if(val instanceof Integer) f.valInt=(Integer) val;
        if(val instanceof Float) f.valFloat=(Float) val;
        if(f.name==null) throw new NullPointerException("Brak nazwy parametru ");
        if(f.sign==null) throw new NullPointerException("Brak znaku parametru "+name);
        if(f.valStr==null && f.valFloat==null & f.valInt==null) throw new NullPointerException("Nieprawodłowa wartość w polu "+name);
        return f;
    }

}
