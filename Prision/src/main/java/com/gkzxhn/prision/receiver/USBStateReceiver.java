package com.gkzxhn.prision.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.gkzxhn.prision.service.ScreenRecordService;
import com.gkzxhn.prision.utils.Utils;

/**
 * Created by Raleigh.Luo on 17/5/18.
 * 检测USB和TF检测挂载和异常
 */

public class USBStateReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
            //USB设备移除，更新UI
            Log.e("USB:ACTION_MEDIA_EJECT","raleigh_test" );
            String rootPath = Utils.getTFPath();
            if (rootPath == null) {//停止录像
                Intent service = new Intent(context, ScreenRecordService.class);
                context.stopService(service);
            }
        } else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
            //USB设备挂载，更新UI
            Log.d("USB:ACTION_MEDIA_MOUNTED","raleigh_test");
        }
    }
}