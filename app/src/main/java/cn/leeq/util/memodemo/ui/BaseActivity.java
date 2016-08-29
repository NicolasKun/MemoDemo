package cn.leeq.util.memodemo.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by LeeQ on 2016-6-29.
 */
public class BaseActivity extends AppCompatActivity {

    public void loadByXUtils(RequestParams params, final Handler handler, final int what) {
        x.http().get(params, new Callback.CommonCallback<File>() {

            @Override
            public void onSuccess(File result) {
                Message msg = handler.obtainMessage();
                msg.what = what;
                msg.obj = result;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("test", "拉取数据失败 " + ex.getMessage());
                handler.sendEmptyMessage(99);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
