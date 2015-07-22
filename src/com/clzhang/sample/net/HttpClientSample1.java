package com.clzhang.sample.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class HttpClientSample1 {
    
    // 获取一个HTML页面的内容，一个简单的get应用
    public void grabPageHTML() throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("http://www.baidu.com/");
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String html = EntityUtils.toString(entity, "GBK");
        
        // releaseConnection等同于reset，作用是重置request状态位，为下次使用做好准备。
        // 其实就是用一个HttpGet获取多个页面的情况下有效果；否则可以忽略此方法。
        httpget.releaseConnection();
        System.out.println(html);
    }
    /*
     *
     *
    // 下载一个文件到本地（本示范中为一个验证码图片）
    public void downloadFile() throws Exception {
        String url = "http://www.lashou.com/account/captcha";
        String destfilename = "D:\\TDDOWNLOAD\\yz.png";
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        File file = new File(destfilename);
        if (file.exists()) {
            file.delete();
        }
        
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();
        try {
            FileOutputStream fout = new FileOutputStream(file);
            int l = -1;
            byte[] tmp = new byte[2048]; 
            while ((l = in.read(tmp)) != -1) {
                fout.write(tmp);
            } 
            fout.close();
        } finally {
            // 在用InputStream处理HttpEntity时，切记要关闭低层流。
            in.close();
        }
        
        httpget.releaseConnection();
    }
    * 
    */
    // Post方法，模拟表单提交参数登录到网站。
    // 结合了上面两个方法：grabPageHTML/downloadFile，同时增加了Post的代码。
    /*
     * 
     
    public void login2Lashou() throws Exception {
        // 第一步：先下载验证码到本地
        String url = "http://www.lashou.com/account/captcha";
        String destfilename = "D:\\TDDOWNLOAD\\yz.png";
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        File file = new File(destfilename);
        if (file.exists()) {
            file.delete();
        }
        
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();
        try {
            FileOutputStream fout = new FileOutputStream(file);
            int l = -1;
            byte[] tmp = new byte[2048]; 
            while ((l = in.read(tmp)) != -1) {
                fout.write(tmp);
            } 
            fout.close();
        } finally {
            in.close();
        }
        httpget.releaseConnection();
        
        // 第二步：用Post方法带若干参数尝试登录，需要手工输入下载验证码中显示的字母、数字
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("请输入下载下来的验证码中显示的数字...");
        String yan = br.readLine();

        HttpPost httppost = new HttpPost("http://www.lashou.com/account/login/");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_id", "testuser007"));
        params.add(new BasicNameValuePair("pwd", "asdfg123"));
        params.add(new BasicNameValuePair("yan", yan));
        params.add(new BasicNameValuePair("save_user", "on"));
        params.add(new BasicNameValuePair("save_pwd", "on"));
        params.add(new BasicNameValuePair("sub", "登录"));
        httppost.setEntity(new UrlEncodedFormEntity(params));
        
        response = httpclient.execute(httppost);
        entity = response.getEntity();
        // 在这里可以用Jsoup之类的工具对返回结果进行分析，以判断登录是否成功
        String postResult = EntityUtils.toString(entity, "GBK"); 
        // 我们这里只是简单的打印出当前Cookie值以判断登录是否成功。
        List<Cookie> cookies = ((AbstractHttpClient)httpclient).getCookieStore().getCookies();
        for(Cookie cookie: cookies)
            System.out.println(cookie);
        httppost.releaseConnection();
        
        // 第三步：打开会员页面以判断登录成功（未登录用户是打不开会员页面的）
        String memberpage = "http://www.lashou.com/account/orders/";
        httpget = new HttpGet(memberpage);
        response = httpclient.execute(httpget); // 必须是同一个HttpClient！
        entity = response.getEntity();
        String html = EntityUtils.toString(entity, "GBK");
        httpget.releaseConnection();

        System.out.println(html);
    }
    */
    // 设置代理服务器
    public void testProxy() throws Exception {
        HttpHost proxy = new HttpHost("10.4.200.21", 18765);
        
        // 方式一
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        
        // 方式二
        HttpParams params = new BasicHttpParams(); 
        params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        HttpClient httpclient1 = new DefaultHttpClient(params);
    }
    
    // 几种常用HTTP头的设置
    public void testBasicHeader() throws Exception {
        HttpParams params = new BasicHttpParams(); 
        Collection<BasicHeader> collection = new ArrayList<BasicHeader>();
        collection.add(new BasicHeader("Accept", "text/html, application/xhtml+xml, */*"));
        collection.add(new BasicHeader("Referer", "http://www.sina.com/"));
        collection.add(new BasicHeader("Accept-Language", "zh-CN"));
        collection.add(new BasicHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)"));
        collection.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
        params.setParameter(ClientPNames.DEFAULT_HEADERS, collection);
        
        HttpClient httpclient = new DefaultHttpClient(params);
        
        // 下面内容略
    }
    
    // 多线程编程下的线程池设置（这点在需要登录且用一个HttpClient对象抓取多个页面的情况下特别有用）
    public void testConnectionManager() throws Exception {
        // 连接池设置
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

        PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
        cm.setMaxTotal(200); // 连接池里的最大连接数
        cm.setDefaultMaxPerRoute(20); // 每个路由的默认最大连接数
        HttpHost localhost = new HttpHost("locahost", 80); // 可以针对某特定网站指定最大连接数
        cm.setMaxPerRoute(new HttpRoute(localhost), 30);

        // 其它设置
        HttpParams params = new BasicHttpParams(); 
        params.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        HttpClient httpclient = new DefaultHttpClient(cm, params);
        
        // 下面内容略
    }
    
    // 测试HTTP上下文对象（HttpContext）
    public void testContext() throws Exception {
        // 请求一个页面，然后解析各上下文对象
        DefaultHttpClient httpclient = new DefaultHttpClient();

        HttpContext localContext = new BasicHttpContext();
        HttpGet httpget = new HttpGet("http://www.baidu.com/");

        HttpResponse response = httpclient.execute(httpget, localContext);
        
        // the actual connection to the target server.
        HttpConnection conn = (HttpConnection) localContext.getAttribute(
            ExecutionContext.HTTP_CONNECTION);  
        System.out.println("Socket timeout: " + conn.getSocketTimeout());  

        // the connection target
        HttpHost target = (HttpHost) localContext.getAttribute(
            ExecutionContext.HTTP_TARGET_HOST);
        System.out.println("Final target: " + target);
        
        // the connection proxy, if used 
        HttpHost proxy = (HttpHost) localContext
                .getAttribute(ExecutionContext.HTTP_PROXY_HOST);
        if (proxy != null)
            System.out.println("Proxy host/port: " + proxy.getHostName() + "/"
                    + proxy.getPort());

        // the actual HTTP request
        HttpRequest request = (HttpRequest) localContext
                .getAttribute(ExecutionContext.HTTP_REQUEST);
        System.out.println("HTTP version: " + request.getProtocolVersion());
        Header[] headers = request.getAllHeaders();
        System.out.println("HTTP Headers: ");
        for (Header header : headers) {
            System.out.println("\t" + header.getName() + ": " + header.getValue());
        }
        System.out.println("HTTP URI: " + request.getRequestLine().getUri());

        // the actual HTTP response
        response = (HttpResponse) localContext
                .getAttribute(ExecutionContext.HTTP_RESPONSE);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            System.out.println("Content Encoding：" + entity.getContentEncoding());
            System.out.println("Content Type：" + entity.getContentType());
        }
        
        // the flag indicating whether the actual request has been fully transmitted to the connection target. 
        System.out.println("Sent flag: " + localContext.getAttribute(ExecutionContext.HTTP_REQ_SENT));
        
        // 如果没有用到返回entity中的内容，那么要把它消费掉，以保证底层的资源得以释放。
        entity = response.getEntity();
        EntityUtils.consume(entity);
    }
    
    public static void main(String[] args) throws Exception {
        HttpClientSample1 ins = new HttpClientSample1();
        
//        ins.grabPageHTML();
//        ins.downloadFile();
//        ins.login2Lashou();
//        ins.testContext();
    }
}