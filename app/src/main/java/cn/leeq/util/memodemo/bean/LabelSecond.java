package cn.leeq.util.memodemo.bean;

/**
 * Created by LeeQ on 2016-7-4.
 */
public class LabelSecond {
    private int lsCode;
    private String lsName;

    public LabelSecond(int lsCode, String lsName) {
        this.lsCode = lsCode;
        this.lsName = lsName;
    }

    public int getLsCode() {
        return lsCode;
    }

    public void setLsCode(int lsCode) {
        this.lsCode = lsCode;
    }

    public String getLsName() {
        return lsName;
    }

    public void setLsName(String lsName) {
        this.lsName = lsName;
    }
}
