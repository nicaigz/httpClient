package http.client;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

public class DomainParserHtml {
	// ѭ���������нڵ㣬��������ؼ��ֵ�ֵ�ڵ�
	public static void StartHtmlParser(String url) {
		try {
			// ����һ����������������ҳ�� url ��Ϊ����
			Parser parser = new Parser(url);
			System.out.println(url);
			// ������ҳ�ı���,����ֻ��������һ�� gb2312 ������ҳ
			parser.setEncoding("UTF-8");
			//����class���ԡ�������ֵΪ��"detail PC"�ı�ǩ 
			NodeFilter filter_package_name = new HasAttributeFilter("class", "whoisinfo");
//			System.out.println(filter_package);
			NodeList nodeList = parser.extractAllNodesThatMatch(filter_package_name); 
//			System.out.println(nodeList.size());
			SimpleNodeIterator iterator = nodeList.elements();
			while (iterator.hasMoreNodes()) {
				Node node = iterator.nextNode();
//				System.out.println(node.getChildren());
				NodeList nodeList2 = node.getChildren();
				System.out.println(nodeList2.size());
			}
			//��parser�������ã��Ա����´ε�filter���� 
			parser.reset();
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}
}
