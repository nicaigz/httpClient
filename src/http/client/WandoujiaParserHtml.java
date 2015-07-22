package http.client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.StringFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

public class WandoujiaParserHtml {

	// ѭ���������нڵ㣬��������ؼ��ֵ�ֵ�ڵ�
	public static void extractKeyWordText(String url) {
		try {
			// ����һ����������������ҳ�� url ��Ϊ����
			Parser parser = new Parser(url);
			// ������ҳ�ı���,����ֻ��������һ�� gb2312 ������ҳ
			parser.setEncoding("UTF-8");
			String package_name = null;
			String app_name = null;
			String change_info = null;
			String desc_info = null;
			String file_size = null;
			String update_date = null;
			String version = null;
			//����class���ԡ�������ֵΪ��"detail PC"�ı�ǩ 
			NodeFilter filter_package_name = new HasAttributeFilter("class", "detail PC");
			NodeList nodeList = parser.extractAllNodesThatMatch(filter_package_name); 
			SimpleNodeIterator iterator = nodeList.elements();
			while (iterator.hasMoreNodes()) {
				Node node = iterator.nextNode();
				if (node.getText().startsWith("body data-title")) {
					String nodeStr = node.getText().toString();
					app_name = nodeStr.split("data-title=")[1]
							.split(" data-pn=")[0].replaceAll("\"", "");
					package_name = nodeStr.split("data-pn=")[1].split(" data-page=")[0].replaceAll("\"", "");
				}
			}
			//��parser�������ã��Ա����´ε�filter���� 
			parser.reset();
			NodeFilter filter_change_info = new HasAttributeFilter("class", "change-info");
			change_info = parser.extractAllNodesThatMatch(filter_change_info).asString().replaceAll("[\\t\\n\\r]", "");
			parser.reset();
			NodeFilter filter_desc_info = new HasAttributeFilter("class", "desc-info");
			desc_info = parser.extractAllNodesThatMatch(filter_desc_info).asString().replaceAll("[\\t\\n\\r]", "");
			parser.reset();
			NodeFilter filter_filesize = new HasAttributeFilter("class", "infos-list");
			NodeList nodeList_child = parser.extractAllNodesThatMatch(filter_filesize);
			if (nodeList != null && nodeList_child.size() > 0) {
				Parser child_parser = new Parser(nodeList_child.toHtml());
				NodeFilter filter_filesize2 = new TagNameFilter("dd"); 
				NodeList nodeList2 = child_parser.extractAllNodesThatMatch(filter_filesize2); 
				file_size = nodeList2.elementAt(0).toString().split("content=")[1].split("/")[0].replaceAll("\"", "").replaceAll("[\\t\\n\\r]", "");
				update_date = nodeList2.elementAt(2).toString().split("\\):")[3].split("\n")[0].replaceAll("[\\t\\n\\r]", "");
				version = nodeList2.elementAt(3).toString().split("\\):")[2].split("\n")[0].replaceAll("[\\t\\n\\r]", "");
			}  
//			System.out.println(package_name);
//			System.out.println(app_name);
//			System.out.println(change_info);
//			System.out.println(desc_info);
//			System.out.println(file_size);
//			System.out.println(update_date);
//			System.out.println(version);
			ConnectToDB connectToDB = new ConnectToDB();
			connectToDB.InsertWandoujiaData(package_name, app_name, change_info,
					desc_info, file_size,
					update_date,version);
		} catch (ParserException e) {
			e.printStackTrace();
		}
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
		WandoujiaParserHtml wandoujiaParserHtml = new WandoujiaParserHtml();
		wandoujiaParserHtml.extractKeyWordText(result);
	}
}
