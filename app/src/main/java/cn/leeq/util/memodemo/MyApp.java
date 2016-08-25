package cn.leeq.util.memodemo;

import android.app.Application;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;

/**
 * Created by LeeQ on 2016-6-29.
 */
public class MyApp extends Application {
    public static MyApp app;
    public DbManager.DaoConfig config;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        x.Ext.init(this);
        initUIL();
    }

    public static MyApp getInstance() {
        return app;
    }

    public DbManager.DaoConfig getDaoconfig(){
        File file = new File(Environment.getExternalStorageDirectory().getPath());
        if (config == null) {
            config = new DbManager.DaoConfig()
                    .setDbName("memo.db")
                    .setDbDir(file)
                    .setDbVersion(1)
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                        }
                    });
        }
        return config;
    }

    private void initUIL() {
        DisplayImageOptions build = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)    //内存缓存
                .cacheOnDisk(true)     //磁盘缓存
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)    //图片压缩格式 二次采样
                .build();

        File cacheDirectory = StorageUtils.getOwnCacheDirectory(this, "Electron/cache/image"); //缓存地址
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new LruMemoryCache(10 << 20))
                .defaultDisplayImageOptions(build)    //使用
                .memoryCacheSize(10 << 20)     //定义内存缓冲区大小
                .diskCache(new UnlimitedDiskCache(cacheDirectory))   //设置磁盘缓冲区路径
                .diskCacheSize(50 * 1024 * 1024)  //定义磁盘缓冲区大小
                .imageDownloader(new BaseImageDownloader(this, 20 * 60 * 60, 30 * 60 * 60))
                .diskCacheFileCount(100)    //设置缓存文件数量
                .threadPoolSize(4)
                .denyCacheImageMultipleSizesInMemory()
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }
}
