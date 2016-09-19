package cn.leeq.util.memodemo.utils;

/**
 * Created by LeeQ
 * Date : 2016-08-24
 * Name : CourierKdb
 * Use :
 */
public class PrintUtil {

    public PrintUtil() {}

    public String drawControll(int height) {
        return "! 0 200 200 "+height+" 1\n";
    }

    public String drawPostFeed() {
        return "PREFEED 70\n";
    }

    public String drawText(int font, int size, int textX, int textY, String content) {
        String line = "TEXT" + getSpace() + font + getSpace() +
                size + getSpace() + textX + getSpace() +
                textY+getSpace()+ content+"\n";
        return line;
    }

    public String drawVText(int font, int size, int textX, int textY, String content) {
        String vText = "TEXT270" + getSpace() + font + getSpace() +
                size + getSpace() + textX + getSpace() +
                textY + getSpace() + content + "\n";
        return vText;
    }

    public String drawLine(int leftX, int leftY, int rightX, int rightY) {
        return "LINE" + getSpace() + leftX + getSpace() +
                leftY + getSpace() + rightX + getSpace() + rightY+getSpace()+"2\n";
    }

    public String drawBarCode(int width, int ratio, int height, int barX, int barY, String content) {
        StringBuffer sb = new StringBuffer();
        String bt = "BARCODE-TEXT" + getSpace() + "7" + getSpace() + "0" + getSpace() + "4\n";
        String line = "BARCODE 128" + getSpace() + width + getSpace() +
                ratio + getSpace() + height + getSpace() +
                barX + getSpace() + barY + getSpace() +
                content+"\n";
        String bt1 = "BARCODE-TEXT OFF\n";
        sb.append(bt);
        sb.append(line);
        sb.append(bt1);
        return sb.toString();
    }

    public String drawBox(int leftX, int leftY, int rightX, int rightY) {
        String box = "BOX" + getSpace() + leftX + getSpace() +
                leftY + getSpace() + rightX + getSpace() +
                rightY + getSpace() + "2\n";
        return box;
    }

    public String drawPrint() {
        return "PRINT\n";
    }

    private String getSpace() {
        return " ";
    }

    /**
     * 第一联 865
     */
    public String printSendStub(String awb,String sender_name_mobile,String sender_addr,
                                 String rec_name_mobile,String rec_addr) {
        StringBuffer sb = new StringBuffer();
        sb.append(this.drawBox(0, 0, 570, 745));    //最外框
        sb.append(this.drawLine(500, 0, 500, 745)); //右侧说明框
        sb.append(this.drawLine(0, 87, 500, 87));   //最上
        sb.append(this.drawLine(0, 219, 500, 219));  //最上-1  条码
        sb.append(this.drawLine(0, 460, 500, 460));  //最上-2  收寄人信息
        sb.append(this.drawLine(0, 340, 340, 340));
        sb.append(this.drawLine(340, 219, 340, 460));
        sb.append(this.drawLine(0, 559, 500, 559));   //订单详情
        sb.append(this.drawLine(0, 608, 500, 608));   //签名
        sb.append(this.drawLine(250, 559, 250, 608));
        sb.append(this.drawBarCode(1, 1, 70, 120, 100, awb));
        sb.append(this.drawText(0, 1, 3, 225, "寄件方:"));
        sb.append(this.drawText(0, 1, 8, 260, " "+sender_name_mobile));
        sb.append(this.drawText(0, 1, 8, 295, " "+sender_addr));
        sb.append(this.drawText(0, 1, 3, 345, "收件方:"));
        sb.append(this.drawText(0, 1, 3, 380, " "+rec_name_mobile));
        sb.append(this.drawText(0, 1, 3, 415, " "+rec_addr));
        sb.append(this.drawText(0, 1, 343, 225, "目的地"));

        sb.append(this.drawText(0 , 1, 3  , 570, "收方签名:"));
        sb.append(this.drawText(0 , 1, 253, 570, "收件时间:"));
        sb.append(this.drawVText(0, 2, 530, 600, "派件存根联"));
        return sb.toString();
    }

    /**
     * 第二联355
     */
    public String printRecStub(String awb,String sender_name_mobile,String sender_addr,
                                String rec_name_mobile,String rec_addr) {
        StringBuffer sb = new StringBuffer();
        sb.append(this.drawBox(0, 0, 570, 335));
        sb.append(this.drawBox(0, 0, 500, 70));
        sb.append(this.drawBox(0, 70, 330, 170));
        sb.append(this.drawBox(0, 170, 330, 270));
        sb.append(this.drawLine(500, 0, 500, 335));
        sb.append(this.drawLine(330, 270, 330, 335));
        sb.append(this.drawLine(330, 214, 500, 214));
        sb.append(this.drawText(0, 1, 3, 73, "寄件方:"));
        sb.append(this.drawText(0, 1, 8, 108, " " + sender_name_mobile));
        sb.append(this.drawText(0, 1, 8, 143, " " + sender_addr));
        sb.append(this.drawText(0, 1, 3, 175, "收件方:"));
        sb.append(this.drawText(0, 1, 13, 210, " " + rec_name_mobile));
        sb.append(this.drawText(0, 1, 13, 245, " " + rec_addr));
//        sb.append(this.drawBarCode(1, 1, 50, 170, 10, "8100101"));
        sb.append(this.drawVText(0, 2, 530, 180, "收件人存根联"));
        return sb.toString();
    }

    /**
     * 第三联   85  269
     * 517
     */
    public String printSenderStub(String awb,String sender_name_mobile,String sender_addr,
                                   String rec_name_mobile,String rec_addr) {
        StringBuffer sb = new StringBuffer();
        sb.append(this.drawBox(0, 0, 570, 490));    //最外框
        sb.append(this.drawLine(500, 0, 500, 490)); //右侧说明框
        sb.append(this.drawLine(0, 96, 500, 96));    //条码
        sb.append(this.drawLine(0, 192, 500, 192));  //寄件方信息
        sb.append(this.drawLine(0, 288, 500, 288)); //收件方信息
        sb.append(this.drawLine(0, 368, 500, 368)); //订单详情

        sb.append(this.drawBarCode(1, 1, 60, 120, 8, awb));
        sb.append(this.drawText(0, 1, 3, 101, "寄件方:"));
        sb.append(this.drawText(0, 1, 8, 131, " " + sender_name_mobile));
        sb.append(this.drawText(0, 1, 8, 161, " " + sender_addr));
        sb.append(this.drawText(0, 1, 3, 200, "收件方:"));
        sb.append(this.drawText(0, 1, 8, 230, " " + rec_name_mobile));
        sb.append(this.drawText(0, 1, 8, 260, " " + rec_addr));

        sb.append(this.drawVText(0, 2, 530, 340, "寄件人存根联"));
        return sb.toString();
    }
    
}
