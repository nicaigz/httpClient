package http.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class DomainProxyClient {

	/**
	 * 抓取网页的方法
	 * 
	 * @param url
	 * @return
	 */
	public String downloadFile(String url) {
		String filePath = null;
		HttpClient httpclient = new HttpClient();
		Getfilename getfilename= new Getfilename();
		Savefile savefile=new Savefile();

		 // 设置HTTP代理IP和端口
//		 httpclient.getHostConfiguration().setProxy("10.4.200.21", 18765);
//		 // 代理认证
//		 UsernamePasswordCredentials creds = new
//		 UsernamePasswordCredentials("eb_pangx@bmccisa.com", "pl,.054");
//		 httpclient.getParams().setAuthenticationPreemptive(true);
//		 httpclient.getState().setProxyCredentials(AuthScope.ANY, creds);
//		
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
//				 System.out.println(statusCode +"......................................");
//				 System.out.println(new String(method.getResponseBody(),"UTF-8"));
				BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String str = "";
				while((str = reader.readLine())!=null){
					stringBuffer.append(str);
				}
				String ts = stringBuffer.toString();
				byte[] responseBody = stringBuffer.toString().getBytes();
//				System.out.println(responseBody.toString());
//				byte[] responseBody = method.getResponseBody();
				// 根据网页 url 生成保存时的文件名
				filePath = "e://savelocal//"
						+ getfilename.getFileNameByUrl(url,
								method.getResponseHeader("Content-Type")
										.getValue());
				System.out.println(filePath);
				savefile.saveToLocal(responseBody, filePath);
//				BaiduParserHtml htmlParserHtml=new BaiduParserHtml();
//				htmlParserHtml.StartHtmlParser(filePath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return filePath;
	}

	// main 方法
	public static void main(String[] args) {
//		BaiduProxyClient proxyClient = new BaiduProxyClient();
		ConnectToDB connectToDB = new ConnectToDB();
		connectToDB.SelectDomainDaa();
//		String url = 
//		for (int i = 1; i <= 10; i++) {
//			String url = "http://beian.links.cn/beian.asp?beiantype=domain&keywords="
//					+ i;
//			proxyClient.downloadFile(url);
//		}
	}
}
