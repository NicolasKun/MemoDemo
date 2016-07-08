package cn.leeq.util.memodemo.bean;

import java.util.List;

/**
 * Created by LeeQ on 2016-7-4.
 */
public class LabelFirst {
    private int lfCode;
    private String lfName;
    private List<LabelSecond> lfSecondList;

    public LabelFirst(int lfCode, String lfName, List<LabelSecond> lfSecondList) {
        this.lfCode = lfCode;
        this.lfName = lfName;
        this.lfSecondList = lfSecondList;
    }

    public int getLfCode() {
        return lfCode;
    }

    public void setLfCode(int lfCode) {
        this.lfCode = lfCode;
    }

    public String getLfName() {
        return lfName;
    }

    public void setLfName(String lfName) {
        this.lfName = lfName;
    }

    public List<LabelSecond> getLfSecondList() {
        return lfSecondList;
    }

    public void setLfSecondList(List<LabelSecond> lfSecondList) {
        this.lfSecondList = lfSecondList;
    }
}
