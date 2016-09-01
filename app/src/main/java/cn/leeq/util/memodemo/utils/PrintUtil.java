package cn.leeq.util.memodemo.utils;



import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


/**
 * Created by LeeQ
 * Date : 2016-08-24
 * Name : CourierKdb
 * Use :
 */
public class PrintUtil {
    public Canvas canvas = null;
    public Paint paint = null;

    public Bitmap bm = null;
    public int width;
    public float length = 0.0F;

    public byte[] bitbuf = null;

    /**
     * 将构造函数私有化
     */
    private PrintUtil() {
    };

    private static PrintUtil instance = new PrintUtil();

    /**
     * 获取PrintPic的实例，保证只有一个PrintPic实例存在
     *
     * @return
     */
    public static PrintUtil getInstance() {
        return instance;
    }

    public int getLength() {
        return (int) this.length + 20;
    }

    /**
     * 初始化画布
     *
     * @param w
     *            宽度
     */
    public void initCanvas(int w) {
        int h = 10 * w;

        this.bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        this.canvas = new Canvas(this.bm);

        this.canvas.drawColor(-1);
        this.width = w;
        this.bitbuf = new byte[this.width / 8];
    }

    /**
     * 初始化画笔
     */
    public void initPaint() {
        this.paint = new Paint();// 新建一个画笔

        this.paint.setAntiAlias(true);//

        this.paint.setColor(Color.parseColor("#000000"));

        this.paint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 画出位图
     *
     * @param x
     *            The position of the left side of the bitmap being drawn
     * @param y
     *            The position of the top side of the bitmap being drawn
     * @param btm
     *            Bitmap
     */
    public void drawImage(float x, float y, Bitmap btm) {
        try {
            // Bitmap btm = BitmapFactory.decodeFile(path);
            this.canvas.drawBitmap(btm, x, y, null);
            if (this.length < y + btm.getHeight())
                this.length = (y + btm.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用光栅位图打印
     *
     * [url=home.php?mod=space&uid=7300]@return[/url] 字节
     */
    public byte[] printDraw() {
        Bitmap nbm = Bitmap
                .createBitmap(this.bm, 0, 0, this.width, getLength());

        byte[] imgbuf = new byte[this.width / 8 * getLength() + 8];

        int s = 0;

        // 打印光栅位图的指令
        imgbuf[0] = 29;// 十六进制0x1D
        imgbuf[1] = 118;// 十六进制0x76
        imgbuf[2] = 48;// 30
        imgbuf[3] = 0;// 位图模式 0,1,2,3
        // 表示水平方向位图字节数（xL+xH × 256）
        imgbuf[4] = (byte) (this.width / 8);
        imgbuf[5] = 0;
        // 表示垂直方向位图点数（ yL+ yH × 256）
        imgbuf[6] = (byte) (getLength() % 256);//
        imgbuf[7] = (byte) (getLength() / 256);

        s = 7;
        for (int i = 0; i < getLength(); i++) {// 循环位图的高度
            for (int k = 0; k < this.width / 8; k++) {// 循环位图的宽度
                int c0 = nbm.getPixel(k * 8 + 0, i);// 返回指定坐标的颜色
                int p0;
                if (c0 == -1)// 判断颜色是不是白色
                    p0 = 0;// 0,不打印该点
                else {
                    p0 = 1;// 1,打印该点
                }
                int c1 = nbm.getPixel(k * 8 + 1, i);
                int p1;
                if (c1 == -1)
                    p1 = 0;
                else {
                    p1 = 1;
                }
                int c2 = nbm.getPixel(k * 8 + 2, i);
                int p2;
                if (c2 == -1)
                    p2 = 0;
                else {
                    p2 = 1;
                }
                int c3 = nbm.getPixel(k * 8 + 3, i);
                int p3;
                if (c3 == -1)
                    p3 = 0;
                else {
                    p3 = 1;
                }
                int c4 = nbm.getPixel(k * 8 + 4, i);
                int p4;
                if (c4 == -1)
                    p4 = 0;
                else {
                    p4 = 1;
                }
                int c5 = nbm.getPixel(k * 8 + 5, i);
                int p5;
                if (c5 == -1)
                    p5 = 0;
                else {
                    p5 = 1;
                }
                int c6 = nbm.getPixel(k * 8 + 6, i);
                int p6;
                if (c6 == -1)
                    p6 = 0;
                else {
                    p6 = 1;
                }
                int c7 = nbm.getPixel(k * 8 + 7, i);
                int p7;
                if (c7 == -1)
                    p7 = 0;
                else {
                    p7 = 1;
                }
                int value = p0 * 128 + p1 * 64 + p2 * 32 + p3 * 16 + p4 * 8
                        + p5 * 4 + p6 * 2 + p7;
                this.bitbuf[k] = (byte) value;
            }

            for (int t = 0; t < this.width / 8; t++) {
                s++;
                imgbuf[s] = this.bitbuf[t];
            }
        }
        return imgbuf;
    }
}
