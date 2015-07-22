package com.httpclient;

import org.apache.commons.httpclient.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;

public class ClientWithResponseHandler {

    public final static void main(String[] args) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet("http://www.baidu.com");
            
            // Create a custom response handler

            
            CloseableHttpResponse execute = httpclient.execute(httpget);
            System.out.println("----------------------------------------");
            
            StatusLine statusLine = execute.getStatusLine();
            System.out.println(statusLine);
            
            if(statusLine.getStatusCode() != 200){
            	return ;
            }
            HttpEntity entity = execute.getEntity();
            
            String str = EntityUtils.toString(entity);
            Parser parser = new Parser(str);
            NodeFilter filter_title = new TagNameFilter("title");//
            NodeList nodelist1 = parser.extractAllNodesThatMatch(filter_title);//
            
            
            System.out.println(nodelist1.asString());
        } finally {
            httpclient.close();
        }
    }

}