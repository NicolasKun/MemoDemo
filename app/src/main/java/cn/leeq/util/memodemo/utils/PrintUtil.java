package cn.leeq.util.memodemo.utils;


import com.zj.btsdk.BluetoothService;

/**
 * Created by LeeQ
 * Date : 2016-08-24
 * Name : CourierKdb
 * Use :
 */
public class PrintUtil {
    private BluetoothService mService;

    public PrintUtil(BluetoothService mService) {
        this.mService = mService;
    }

    public String drawControll(int height) {
        return "! 0 200 200 "+height+" 1\n";
    }

    public String drawText(int font, int size, int textX, int textY,String content) {
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

    public String drawLine(int leftX,int leftY,int rightX,int rightY) {
        return "LINE" + getSpace() + leftX + getSpace() +
                leftY + getSpace() + rightX + getSpace() + rightY+getSpace()+"1\n";
    }

    public String drawBarCode(int width,int ratio,int height,int barX,int barY,String content) {
        String line = "BARCODE 128" + getSpace() + width + getSpace() +
                ratio + getSpace() + height + getSpace() +
                barX + getSpace() + barY + getSpace() +
                content+"\n";
        return line;
    }

    public String drawBox(int leftX, int leftY, int rightX, int rightY) {
        String box = "BOX" + getSpace() + leftX + getSpace() +
                leftY + getSpace() + rightX + getSpace() +
                rightY + getSpace() + "1\n";
        return box;
    }

    public String drawPrint() {
        return "PRINT";
    }

    private String getSpace() {
        return " ";
    }

}
