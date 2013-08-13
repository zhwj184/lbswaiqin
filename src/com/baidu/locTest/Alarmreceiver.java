package com.baidu.locTest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Alarmreceiver extends BroadcastReceiver {  
    @Override  
    public void onReceive(Context context, Intent intent) {  
  
        if (intent.getAction().equals("arui.alarm.action")) {  
            Intent i = new Intent();  
            i.setClass(context, LocationReportService.class);  
            // 启动service   
            // 多次调用startService并不会启动多个service 而是会多次调用onStart  
            context.startService(i);  
        	Toast.makeText(context, "移动定位导航启动", Toast.LENGTH_LONG).show();
        }  
    }  
}
