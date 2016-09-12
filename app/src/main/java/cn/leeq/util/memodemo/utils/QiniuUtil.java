package cn.leeq.util.memodemo.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.utils.UrlSafeBase64;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by LeeQ
 * Date : 2016-08-26
 * Name : MemoDemo
 * Use : 七牛云存储
 */
public class QiniuUtil {
    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";

    private static String ACCESS_KEY = "UtgyE6G73-zS2cFmRFLA0shNQsf5H7bwbVEyVhVr";
    private static String SECRET_KEY = "qyhzdHqMGYTQ5Wj6Bq0BqaNNiTF5hqluE4tpP0iA";

    String QINIU_PATH = "http://ocgl54rby.bkt.clouddn.com";

    private static String bucketname = "nicolas";


    public static void upload(String filePath,String fileName, Context context, final Handler handler) {


        JSONObject object = new JSONObject();
        long l = System.currentTimeMillis() / 1000 + 3600;
        try {
            object.put("deadline", l);
            object.put("scope", bucketname);

            String encodePut = UrlSafeBase64.encodeToString(object.toString().getBytes());
            byte[] bytes = HmacSHA1Encrypt(encodePut, SECRET_KEY);
            String encodeSign = UrlSafeBase64.encodeToString(bytes);
            String uploadTocken = ACCESS_KEY + ":" + encodeSign + ":" + encodePut;


            Configuration config = new Configuration.Builder()
                    .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认256K
                    .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认512K
                    .connectTimeout(10) // 链接超时。默认10秒
                    .responseTimeout(60) // 服务器响应超时。默认60秒
                    .zone(Zone.zone1) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。默认 Zone.zone0
                    .build();
            UploadManager uploadManager = new UploadManager(config);
            uploadManager.put(new File(filePath), fileName, uploadTocken,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            Log.e("test", "上传 " + info);
                            if (info.statusCode==200) {
                                String ip = info.ip;
                                String duration = info.duration+"";
                                long l1 = info.timeStamp*1000;
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                String format = dateFormat.format(new Date(l1));
                                String ss = "ip              : " + ip +"\n返回码     : " + info.statusCode + "\n操作时长 : " + duration + "\n创建时间 : " + format;
                                Message message = handler.obtainMessage();
                                message.what = 0;
                                message.obj = ss;
                                handler.sendMessage(message);
                            } else {
                                Message message = handler.obtainMessage();
                                message.what = 1;
                                message.obj = info.statusCode;
                                handler.sendMessage(message);
                            }
                        }
                    }, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey)
            throws Exception {
        byte[] data = encryptKey.getBytes(ENCODING);
        // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        // 生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        // 用给定密钥初始化 Mac 对象
        mac.init(secretKey);
        byte[] text = encryptText.getBytes(ENCODING);
        // 完成 Mac 操作
        return mac.doFinal(text);
    }


}
