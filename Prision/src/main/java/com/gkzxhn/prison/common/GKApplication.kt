package com.gkzxhn.prison.common

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap

import com.gkzxhn.prison.R
import com.gkzxhn.prison.activity.LoginActivity
import com.gkzxhn.prison.service.EReportService
import com.gkzxhn.prison.utils.CrashHandler
import com.gkzxhn.prison.utils.NimInitUtil
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.auth.AuthService
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import com.nostra13.universalimageloader.core.download.BaseImageDownloader

import java.io.File

/**
 * Created by Raleigh.Luo on 17/4/10.
 */

class GKApplication : Application() {
    private val imageLoader = ImageLoader.getInstance()
    private var config: ImageLoaderConfiguration? = null
    /**获取文件的缓存工具类
     * 通过url地址获取本地图片文件，通过文件就可以得到文件的路径 imageLoadCache.get(imageUri)
     * @return
     */
    private val options = DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.ic_imageloading)//默认加载的图片
            .showImageForEmptyUri(R.mipmap.ic_imageload_failed)//下载地址不存在

            .showImageOnFail(R.mipmap.ic_imageload_failed).cacheInMemory(false).cacheOnDisk(true)//加载失败的图
            //	.displayer(new RoundedBitmapDisplayer(0))  设置圆角，设置后不能使用loadimage方法，项目并不需要圆角
            .bitmapConfig(Bitmap.Config.RGB_565)    //设置图片的质量
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)    //设置图片的缩放类型，该方法可以有效减少内存的占用
            .build()
    val terminalAccount: String
        get() {
            val sharedPreferences = getSharedPreferences(Constants.USER_TABLE, Context.MODE_PRIVATE)
            return sharedPreferences.getString(Constants.TERMINAL_ACCOUNT, "")
        }
    val terminalRate: Int
        get() {
            val sharedPreferences = getSharedPreferences(Constants.USER_TABLE, Context.MODE_PRIVATE)
            return sharedPreferences.getInt(Constants.TERMINAL_RATE, 512)
        }
    val terminalPassword: String
        get() {
            val sharedPreferences = getSharedPreferences(Constants.USER_TABLE, Context.MODE_PRIVATE)
            return sharedPreferences.getString(Constants.TERMINAL_PASSWORD, "")
        }

    override fun onCreate() {
        super.onCreate()
        instance = this
        NimInitUtil().initNim()// 云信SDK相关初始化及后续操作
        initImageLoader()
        //收集崩溃日志
        CrashHandler.instance.init(this)
    }

    private fun initImageLoader() {
        config = ImageLoaderConfiguration.Builder(applicationContext)
                .memoryCacheExtraOptions(600, 800)// max width, max height，即保存的每个缓存文件的最大长宽
                // max width, max height
                // .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75) //
                // Can slow ImageLoader, use it carefully (Better don't use it)
                .threadPoolSize(3).////线程池内加载的数量
                threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                ////任务线程的执行方式  后进先出法
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // You can pass your own memory cache implementation
                //不用使用缓存路径
                .diskCacheFileNameGenerator(HashCodeFileNameGenerator())
                .imageDownloader(BaseImageDownloader(applicationContext, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                //		  .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密 ,.new HashCodeFileNameGenerator()//使用HASHCODE对UIL进行加密命名
                // .imageDownloader(new Httpclie(5 * 1000, 20 * 1000)) //
                // connectTimeout (5 s), readTimeout (20 s)
                .defaultDisplayImageOptions(options).build()
        imageLoader.init(config!!)
        //	清除所有图片imageLoader.clearDiskCache();

        //下载图片ImageLoader.getInstance().displayImage(loadUri,imageView);
        //		如果经常出现OOM
        //	   ①减少配置之中线程池的大小，(.threadPoolSize).推荐1-5；
        //	   ②使用.bitmapConfig(Bitmap.config.RGB_565)代替ARGB_8888;
        //	   ③使用.imageScaleType(ImageScaleType.IN_SAMPLE_INT)或者        try.imageScaleType(ImageScaleType.EXACTLY)；
        //	   ④避免使用RoundedBitmapDisplayer.他会创建新的ARGB_8888格式的Bitmap对象；
        //	   ⑤使用.memoryCache(new WeakMemoryCache())，不要使用.cacheInMemory();
    }


    fun loginOff() {
        //停止zijing服务
        stopService(Intent(this, EReportService::class.java))
        //退出云信
        NIMClient.getService(AuthService::class.java).logout()
        //清除数据
        val sharedPreferences = getSharedPreferences(Constants.USER_TABLE, Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.remove(Constants.USER_ACCOUNT)
        edit.remove(Constants.USER_PASSWORD)
        edit.remove(Constants.TERMINAL_ACCOUNT)
        edit.apply()
        //调整到登录界面
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        //关闭其他页面
        sendBroadcast(Intent(Constants.NIM_KIT_OUT))
    }

    companion object {
        lateinit var instance: GKApplication
    }

}
