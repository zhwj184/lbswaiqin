package com.baidu.locTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.baidu.location.*;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.os.Process;
import android.os.Vibrator;

public class Location extends Application {

	public LocationClient mLocationClient = null;
//	public LocationClient locationClient = null;
//	public LocationClient LocationClient = null;
	private String mData;  
	public MyLocationListenner myListener = new MyLocationListenner();
//	public MyLocationListenner listener = new MyLocationListenner();
//	public MyLocationListenner locListener = new MyLocationListenner();
	public TextView mTv;
	public NotifyLister mNotifyer=null;
	public Vibrator mVibrator01;
	public static String TAG = "LocTestDemo";
	
	@Override
	public void onCreate() {
		mLocationClient = new LocationClient( this );
//		locationClient = new LocationClient( this );
//		LocationClient = new LocationClient( this );
		mLocationClient.registerLocationListener( myListener );
//		locationClient.registerLocationListener( listener );
//		LocationClient.registerLocationListener( locListener );
		//λ��������ش���
//		mNotifyer = new NotifyLister();
//		mNotifyer.SetNotifyLocation(40.047883,116.312564,3000,"gps");//4����������Ҫλ�����ѵĵ�����꣬���庬������Ϊ��γ�ȣ����ȣ����뷶Χ������ϵ����(gcj02,gps,bd09,bd09ll)
//		mLocationClient.registerNotify(mNotifyer);
		
		super.onCreate(); 
		Log.d(TAG, "... Application onCreate... pid=" + Process.myPid());
	}
	
	/**
	 * ��ʾ�ַ���
	 * @param str
	 */
	public void logMsg(String str) {
		try {
			mData = str;
			if ( mTv != null )
				mTv.setText(mData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��������������λ�õ�ʱ�򣬸�ʽ�����ַ������������Ļ��
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;
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
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				location.setAddrStr("");
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
//				sb.append("\nʡ��");
//				sb.append(location.getProvince());
//				sb.append("\n�У�");
//				sb.append(location.getCity());
//				sb.append("\n��/�أ�");
//				sb.append(location.getDistrict());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
			sb.append("\nsdk version : ");
			sb.append(mLocationClient.getVersion());
			sb.append("\nisCellChangeFlag : ");
			sb.append(location.isCellChangeFlag());
			logMsg(sb.toString());
			Log.i(TAG, sb.toString());
			
//			while (true) {
				try {
					TelephonyManager tm = (TelephonyManager) Location.this.getSystemService(Context.TELEPHONY_SERVICE);
					httpUrlConnection("lat=" + location.getLatitude() + "&lng=" + location.getLongitude() + "&phone="+ tm.getDeviceId() + "&date=" + URLEncoder.encode(HttpUtil.convertToTime(System.currentTimeMillis()), "GBK"),sb.toString());
//					Thread.sleep(10000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch(Exception e){
					e.printStackTrace();
				}
//			}
		}
		
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null){
				return ; 
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
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			} else{
				poiLocation.setAddrStr("");
			}
			if(poiLocation.hasPoi()){
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			}else{				
				sb.append("noPoi information");
			}
			logMsg(sb.toString());
			
//			while (true) {
				try {
					TelephonyManager tm = (TelephonyManager) Location.this.getSystemService(Context.TELEPHONY_SERVICE);
					httpUrlConnection("lat=" + poiLocation.getLatitude() + "&lng=" + poiLocation.getLongitude() + "&phone="+ tm.getDeviceId() + "&date=" + URLEncoder.encode(poiLocation.getTime(), "GBK"),sb.toString());
//					Thread.sleep(10000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch(Exception e){
					e.printStackTrace();
				}
//			}
		}
	}
	
	/**
	 * long����ʱ���ʽ��
	 */
	public static String convertToTime(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return df.format(date);
	}
	
	public class NotifyLister extends BDNotifyListener{
		public void onNotify(BDLocation mlocation, float distance){
			mVibrator01.vibrate(1000);
		}
	}
	
	public static void httpUrlConnection(String query, String content) {
		try {
//			String hosturl = "http://192.168.1.5/jiagoushi/api/report";
			String hosturl = "http://www.javaarch.net/jiagoushi/api/report";
			String pathUrl = hosturl + "?"
					+ query + "&content=" + URLEncoder.encode(content, "GBK");
			// ��������
			URL url = new URL(pathUrl);
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();

			// //������������
			httpConn.setDoOutput(true);// ʹ�� URL ���ӽ������
			httpConn.setDoInput(true);// ʹ�� URL ���ӽ�������
			httpConn.setUseCaches(false);// ���Ի���
			httpConn.setRequestMethod("GET");// ����URL���󷽷�
			String requestString = "";

			// ������������
			// ��������ֽ����ݣ������������ı��룬���������������˴����������ı���һ��
			byte[] requestStringBytes = requestString.getBytes("GBK");
			httpConn.setRequestProperty("Content-length", ""
					+ requestStringBytes.length);
			httpConn.setRequestProperty("Content-Type", "text/html");
			// httpConn.setRequestProperty("Connection", "Keep-Alive");// ά�ֳ�����
			httpConn.setRequestProperty("Charset", "GBK");
			//

			// �������������д������
			OutputStream outputStream = httpConn.getOutputStream();
			outputStream.write(requestStringBytes);
			outputStream.close();
			// �����Ӧ״̬
			int responseCode = httpConn.getResponseCode();
			if (HttpURLConnection.HTTP_OK == responseCode) {// ���ӳɹ�

				// ����ȷ��Ӧʱ��������
				StringBuffer sb = new StringBuffer();
				String readLine;
				BufferedReader responseReader;
				// ������Ӧ�����������������Ӧ������ı���һ��
				responseReader = new BufferedReader(new InputStreamReader(
						httpConn.getInputStream(), "GBK"));
				while ((readLine = responseReader.readLine()) != null) {
					sb.append(readLine).append("\n");
				}
				responseReader.close();

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}