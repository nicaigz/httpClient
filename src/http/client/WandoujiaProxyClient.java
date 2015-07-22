package http.client;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class WandoujiaProxyClient {

	/**
	 * ץȡ��ҳ�ķ���
	 * 
	 * @param url
	 * @return
	 */
	public String downloadFile(String url) {
		String filePath = null;
		HttpClient httpclient = new HttpClient();
		Savefile savefile = new Savefile();
		Getfilename getfilename = new Getfilename();

		// ����HTTP����IP�Ͷ˿�
//		httpclient.getHostConfiguration().setProxy("10.4.200.21", 18765);
//		// ������֤
//		UsernamePasswordCredentials creds = new UsernamePasswordCredentials("eb_pangx@bmccisa.com", "pl,.054");
//		httpclient.getParams().setAuthenticationPreemptive(true);
//		httpclient.getState().setProxyCredentials(AuthScope.ANY, creds);		
		
		// get����
		GetMethod method = new GetMethod(url);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));
		try {
			int statusCode = httpclient.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				System.out.println(statusCode
						+ "......................................");
				System.out.println(statusCode + ": " + method.getStatusLine());
			} else {
				// System.out.println(statusCode
				// +"......................................");
				// System.out.println(new
				// String(method.getResponseBody(),"UTF-8"));
				byte[] responseBody = method.getResponseBody();
				// ������ҳ url ���ɱ���ʱ���ļ���
				filePath = "e://savelocal//"
						+ getfilename.getFileNameByUrl(url, method
								.getResponseHeader("Content-Type").getValue());
				// System.out.println(filePath);
				savefile.saveToLocal(responseBody, filePath);
				WandoujiaParserHtml wandoujiaParserHtml = new WandoujiaParserHtml();
				wandoujiaParserHtml.StartHtmlParser(filePath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return filePath;
	}

	public static void GetAppPackage() {
		String app_package_name = null;
		try {
			ConnectToDB connectToDB = new ConnectToDB();
			Connection con = connectToDB.getConnection();
			Statement sql = con.createStatement();
			String query = "select * from app_package;";
			// System.out.println(query);
			ResultSet rs = sql.executeQuery(query);
			// System.out.println("���Ĳ�ѯ���Ϊ��");
			while (rs.next()) // �ж��Ƿ�����һ������
			{
				// �����ֶ�����ȡ��Ӧ��ֵ
				app_package_name = rs.getString("app_package_name");
				if (!app_package_name.equals("")) {
//					System.out.println(app_package_name);
					String url = "http://www.wandoujia.com/apps/"
							+ app_package_name;
					WandoujiaProxyClient wandoujia = new WandoujiaProxyClient();
					wandoujia.downloadFile(url);
				}
			}
			// sql.execute(query);
			sql.close();
			con.close();
		} catch (java.lang.ClassNotFoundException e) {
			System.err.println("ClassNotFoundException:" + e.getMessage());
		} catch (SQLException ex) {
			System.err.println("SQLException:" + ex.getMessage());
		}
	}

	// main ����
	public static void main(String[] args) {
		WandoujiaProxyClient wandoujia = new WandoujiaProxyClient();
		wandoujia.GetAppPackage();
		// System.out.println(app_package_name);
		// for (int i = 1; i <= 10; i++) {
		// String url = "http://www.wandoujia.com/apps/"
		// + i;
		// proxyClient.downloadFile(url);
		// }
		// }
	}
}
