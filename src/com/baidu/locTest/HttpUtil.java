package com.baidu.locTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpUtil {
	public static void httpUrlConnection(String query, String content) {
		try {
//			String hosturl = "http://10.16.194.46/jiagoushi/api/report";
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
	
	/**
	 * long����ʱ���ʽ��
	 */
	public static String convertToTime(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return df.format(date);
	}
	

}
