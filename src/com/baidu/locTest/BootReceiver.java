package com.baidu.locTest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class BootReceiver extends BroadcastReceiver {
	

//	@Override
//	public void onReceive(Context arg0, Intent arg1) {
//
//		Intent mBootIntent = new Intent(arg0, LocationReportService.class);
//		arg0.startService(mBootIntent);
//	}
	
    @Override  
    public void onReceive(Context context, Intent mintent) {  
  
        if (Intent.ACTION_BOOT_COMPLETED.equals(mintent.getAction())) {  
            // �������  
            Intent intent = new Intent(context, Alarmreceiver.class);  
            intent.setAction("arui.alarm.action");  
            PendingIntent sender = PendingIntent.getBroadcast(context, 0,  
                    intent, 0);  
            long firstime = SystemClock.elapsedRealtime();  
            AlarmManager am = (AlarmManager) context  
                    .getSystemService(Context.ALARM_SERVICE);  
  
            // 10��һ�����ڣ���ͣ�ķ��͹㲥  
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime,  
                    2 * 60 * 1000, sender);  
        }  
  
    }
}