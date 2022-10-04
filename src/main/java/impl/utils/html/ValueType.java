package impl.utils.html;

public enum ValueType {
    TEXT(0),
    NUMBER(1);


    int type;
    private ValueType(int type){this.type = type;}
    public int getScriptType() {return type;}

}
