package com.baidu.locTest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class LocationReportService extends Service {

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener;

	@Override
	public void onCreate() {
		super.onCreate();
		
		init();
	}

	private void init() {
		TelephonyManager tm = (TelephonyManager) LocationReportService.this
				.getSystemService(Context.TELEPHONY_SERVICE);
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		myListener = new MyLocationListener(tm, this);
		mLocationClient.registerLocationListener(myListener); // 注册监听函数

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		// option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(2 * 60000);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		option.setPoiNumber(10); // 最多返回POI个数
		option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(false); // 是否需要POI的电话和地址等详细信息
		option.setPriority(LocationClientOption.GpsFirst);// 设置GPS优先
		mLocationClient.setLocOption(option);
		mLocationClient.start();

		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.requestLocation();
		else
			Log.d("LocSDK3", "locClient is null or not started");
	}

	@Override
	public void onStart(Intent intent, int startId) {

		super.onStart(intent, startId);
		
//		this.openGPSSettings();

		init();
	}

	public IBinder onBind(Intent intent) {
		Log.d("BBBBBBBBBBBBBBBBBBB", "BBBBBBBBBBBBBBBBBBBBBBB");
		Toast.makeText(getApplicationContext(), "不默认Toast样式", Toast.LENGTH_LONG)
				.show();

		return null;
	}

	public void openGPSSettings() {
		// 获取GPS现在的状态（打开或是关闭状态）
		boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled(
				getContentResolver(), LocationManager.GPS_PROVIDER);

		if (!gpsEnabled) {
			// 打开GPS
			Settings.Secure.setLocationProviderEnabled(getContentResolver(),
					LocationManager.GPS_PROVIDER, true);

		}
	}
	public void closeGPSSettings() {
		// 获取GPS现在的状态（打开或是关闭状态）
		boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled(
				getContentResolver(), LocationManager.GPS_PROVIDER);

		if (gpsEnabled) {
			// 关闭GPS
			Settings.Secure.setLocationProviderEnabled(getContentResolver(),
					LocationManager.GPS_PROVIDER, false);
		} 
	}
}

class MyLocationListener implements BDLocationListener {

	private TelephonyManager tm;
	private LocationReportService service;

	public MyLocationListener(TelephonyManager tm,
			LocationReportService service) {
		this.tm = tm;
		this.service = service;
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		if (location == null)
			return;
		StringBuffer sb = new StringBuffer(256);
		sb.append("time : ");
		sb.append(location.getTime());
		sb.append("\nerror code : ");
		sb.append(location.getLocType());
		sb.append("\nlatitude : ");
		sb.append(location.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(location.getLongitude());
		sb.append("\nradius : ");
		sb.append(location.getRadius());
		if (location.getLocType() == BDLocation.TypeGpsLocation) {
			sb.append("\nspeed : ");
			sb.append(location.getSpeed());
			sb.append("\nsatellite : ");
			sb.append(location.getSatelliteNumber());
			location.setAddrStr("");
		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
		}
		// while (true) {
		try {
			HttpUtil.httpUrlConnection(
					"lat="
							+ location.getLatitude()
							+ "&lng="
							+ location.getLongitude()
							+ "&phone="
							+ tm.getDeviceId()
							+ "&date="
							+ URLEncoder.encode(HttpUtil.convertToTime(System
									.currentTimeMillis()), "GBK") + "&type="
							+ location.getLocType() + "&radius="
							+ location.getRadius(), sb.toString());
			// Thread.sleep(10000);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// }
//		this.service.mLocationClient.stop();
//		this.service.closeGPSSettings();
	}

	public void onReceivePoi(BDLocation poiLocation) {
		if (poiLocation == null) {
			return;
		}
		StringBuffer sb = new StringBuffer(256);
		sb.append("Poi time : ");
		sb.append(poiLocation.getTime());
		sb.append("\nerror code : ");
		sb.append(poiLocation.getLocType());
		sb.append("\nlatitude : ");
		sb.append(poiLocation.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(poiLocation.getLongitude());
		sb.append("\nradius : ");
		sb.append(poiLocation.getRadius());
		if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
			sb.append("\naddr : ");
			sb.append(poiLocation.getAddrStr());
		}
		if (poiLocation.hasPoi()) {
			sb.append("\nPoi:");
			sb.append(poiLocation.getPoi());
		} else {
			sb.append("noPoi information");
		}
	}

}
