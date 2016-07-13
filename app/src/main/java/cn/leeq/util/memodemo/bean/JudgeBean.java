package cn.leeq.util.memodemo.bean;

import java.util.List;

/**
 * Created by LeeQ
 * Date : 2016-7-13
 * Name : MemoDemo
 * Use :
 */
public class JudgeBean {

    /**
     * id : 1
     * name : 王二
     * content : 不是不是我
     * date : 2016-07-01 11:13:27 798
     */

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private int id;
        private String name;
        private String content;
        private String date;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
