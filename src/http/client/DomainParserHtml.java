package http.client;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

public class DomainParserHtml {
	// 循环访问所有节点，输出包含关键字的值节点
	public static void StartHtmlParser(String url) {
		try {
			// 生成一个解析器对象，用网页的 url 作为参数
			Parser parser = new Parser(url);
			System.out.println(url);
			// 设置网页的编码,这里只是请求了一个 gb2312 编码网页
			parser.setEncoding("UTF-8");
			//包含class属性、且属性值为："detail PC"的标签 
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
			//对parser进行重置，以便于下次的filter操作 
			parser.reset();
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}
}
