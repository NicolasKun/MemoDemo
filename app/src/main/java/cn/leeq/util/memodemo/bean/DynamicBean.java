package cn.leeq.util.memodemo.bean;

/**
 * Created by LeeQ on 2016-10-22.
 * Use :
 */

public class DynamicBean {
    String content;
    int position;

    public DynamicBean() {
    }

    public DynamicBean(String content, int position) {
        this.content = content;
        this.position = position;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
