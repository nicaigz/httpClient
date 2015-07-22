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

	// 循环访问所有节点，输出包含关键字的值节点
	public static void extractKeyWordText(String url, String keyword) {
		try {
			// 生成一个解析器对象，用网页的 url 作为参数
			Parser parser = new Parser(url);
			// 设置网页的编码,这里只是请求了一个 gb2312 编码网页
			parser.setEncoding("UTF-8");
			// 迭代所有节点, null 表示不使用 NodeFilter
			NodeFilter filter = new TagNameFilter(keyword);
			NodeList list = parser.extractAllNodesThatMatch(filter);
			// 从初始的节点列表跌倒所有的节点
			processNodeList(list, keyword);
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	public static void processNodeList(NodeList list, String keyword) {
		// 迭代开始
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

			// 得到该节点的子节点列表
			NodeList childList = node.getChildren();
			// System.out.println(childList.asString());
			// 孩子节点为空，说明是值节点
			if (null == childList) {
				// 得到值节点的值
				String result = node.toPlainTextString();
				// 若包含关键字，则简单打印出来文本
				if (result.indexOf(keyword) != -1)
					System.out.println(result);
			} // end if
				// 孩子节点不为空，继续迭代该孩子节点
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
