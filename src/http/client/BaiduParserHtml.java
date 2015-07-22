package http.client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

public class BaiduParserHtml {

	// ѭ���������нڵ㣬��������ؼ��ֵ�ֵ�ڵ�
	public static void extractKeyWordText(String url, String keyword) {
		try {
			// ����һ����������������ҳ�� url ��Ϊ����
			Parser parser = new Parser(url);
			// ������ҳ�ı���,����ֻ��������һ�� gb2312 ������ҳ
			parser.setEncoding("UTF-8");
			// �������нڵ�, null ��ʾ��ʹ�� NodeFilter
			NodeFilter filter = new TagNameFilter(keyword);
			NodeList list = parser.extractAllNodesThatMatch(filter);
			// �ӳ�ʼ�Ľڵ��б�������еĽڵ�
			processNodeList(list, keyword);
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	public static void processNodeList(NodeList list, String keyword) {
		// ������ʼ
		SimpleNodeIterator iterator = list.elements();
		while (iterator.hasMoreNodes()) {
			Node node = iterator.nextNode();
			if (node.getText().startsWith("a href=")) {
				if (node.getText().contains("data-sname=")) {
					// System.out.println("-----------------------------------------");
					String nodeStr = node.getText().toString();
					String data_name = nodeStr.split("data-sname=")[1]
							.split(" data-icon=")[0].replaceAll("\"", "");
					String data_icon = nodeStr.split("data-icon=")[1]
							.split(" data-type=")[0].replaceAll("\"", "");
					String data_size = nodeStr.split("data-size=")[1]
							.split(" data-versionname=")[0]
							.replaceAll("\"", "");
					String data_versionname = nodeStr
							.split("data-versionname=")[1]
							.split(" data-versioncode=")[0]
							.replaceAll("\"", "");
					String data_updatetime = nodeStr.split("data-updatetime=")[1]
							.split(" class=")[0].replaceAll("\"", "");
					String data_download_url = nodeStr
							.split("data-download_url=")[1]
							.split(" data-updatetime")[0].replaceAll("\"", "");
					String data_package = nodeStr.split("data-package=")[1]
							.split(" data-download_url")[0]
							.replaceAll("\"", "");
					// System.out.println(data_package);
					// System.out.println("data_download_url="
					// +data_download_url);
					// System.out.println("data_name=" + data_name);
					// System.out.println("data_icon=" + data_icon);
					// System.out.println("data_size=" + data_size);
					// System.out.println("data_versionname=" +
					// data_versionname);
					// System.out.println("data_updatetime=" + data_updatetime);
					ConnectToDB connectToDB = new ConnectToDB();
					connectToDB.InsertData(data_name, data_icon, data_size,
							data_versionname, data_updatetime,
							data_download_url);
				}
			}

			// �õ��ýڵ���ӽڵ��б�
			NodeList childList = node.getChildren();
			// System.out.println(childList.asString());
			// ���ӽڵ�Ϊ�գ�˵����ֵ�ڵ�
			if (null == childList) {
				// �õ�ֵ�ڵ��ֵ
				String result = node.toPlainTextString();
				// �������ؼ��֣���򵥴�ӡ�����ı�
				if (result.indexOf(keyword) != -1)
					System.out.println(result);
			} // end if
				// ���ӽڵ㲻Ϊ�գ����������ú��ӽڵ�
			else {
				processNodeList(childList, keyword);
			}// end else
		}// end wile
	}

	public static void StartHtmlParser(String path) throws IOException {
		// System.out.println(path);
		StringBuffer abstr = new StringBuffer();
		InputStreamReader isr = new InputStreamReader(
				new FileInputStream(path), "UTF-8");
		BufferedReader reader = new BufferedReader(isr);
		String temp = "";
		while ((temp = reader.readLine()) != null) {
			abstr.append(temp);
			abstr.append("\r\n");
		}
		String result = abstr.toString();
		// System.out.print(result);
		BaiduParserHtml httpparserhtml = new BaiduParserHtml();
		httpparserhtml.extractKeyWordText(result, "li");
	}
}
