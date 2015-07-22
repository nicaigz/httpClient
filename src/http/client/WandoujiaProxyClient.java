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
	 * 抓取网页的方法
	 * 
	 * @param url
	 * @return
	 */
	public String downloadFile(String url) {
		String filePath = null;
		HttpClient httpclient = new HttpClient();
		Savefile savefile = new Savefile();
		Getfilename getfilename = new Getfilename();

		// 设置HTTP代理IP和端口
//		httpclient.getHostConfiguration().setProxy("10.4.200.21", 18765);
//		// 代理认证
//		UsernamePasswordCredentials creds = new UsernamePasswordCredentials("eb_pangx@bmccisa.com", "pl,.054");
//		httpclient.getParams().setAuthenticationPreemptive(true);
//		httpclient.getState().setProxyCredentials(AuthScope.ANY, creds);		
		
		// get调用
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
				// 根据网页 url 生成保存时的文件名
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
			// System.out.println("最后的查询结果为：");
			while (rs.next()) // 判断是否还有下一个数据
			{
				// 根据字段名获取相应的值
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

	// main 方法
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
