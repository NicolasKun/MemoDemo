package cn.leeq.util.memodemo.bean;

import java.util.List;

/**
 * Created by LeeQ
 * Date : 2016-8-1
 * Name : MemoDemo
 * Use : 一些定位信息
 */
public class NearbyLocation {

    /**
     * bgntime : 7-26 10:8-10:8
     * ts : 2016-07-27 10:16:00 658
     * posy : 39.84706
     * subject : å°æç´
     * posx : 116.33403
     * opid : 29
     * teacherid : 15
     * addr : åäº¬åäº¬å¸ä¸ååº å å°æ¶é
     * label : ä¸å¯¹ä¸æå­¦,éæ¶é,åæä¼,æ°å´è½»æ¾,èå¸åè¼,
     * avatar : http://fengss.com:8085/uploads/13412345678/1469532209063_1698016561.jpg
     * coverpic : http://fengss.com:8085/uploads/18330226823/1466133985007_1156509720.png
     * id : 153
     * distance : 2
     * title : é¢ç´æå¡
     * price : 124
     * memname : å±å±å¤§å
     * timelen : null
     * note : æ²¡æè¡¥å
     * dr : 0
     */

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String bgntime;
        private String ts;
        private double posy;
        private String subject;
        private double posx;
        private int opid;
        private int teacherid;
        private String addr;
        private String avatar;
        private String coverpic;
        private int id;
        private int distance;
        private String title;
        private int price;
        private String memname;

        public String getBgntime() {
            return bgntime;
        }

        public void setBgntime(String bgntime) {
            this.bgntime = bgntime;
        }

        public String getTs() {
            return ts;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }

        public double getPosy() {
            return posy;
        }

        public void setPosy(double posy) {
            this.posy = posy;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public double getPosx() {
            return posx;
        }

        public void setPosx(double posx) {
            this.posx = posx;
        }

        public int getOpid() {
            return opid;
        }

        public void setOpid(int opid) {
            this.opid = opid;
        }

        public int getTeacherid() {
            return teacherid;
        }

        public void setTeacherid(int teacherid) {
            this.teacherid = teacherid;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCoverpic() {
            return coverpic;
        }

        public void setCoverpic(String coverpic) {
            this.coverpic = coverpic;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getMemname() {
            return memname;
        }

        public void setMemname(String memname) {
            this.memname = memname;
        }
    }
}
