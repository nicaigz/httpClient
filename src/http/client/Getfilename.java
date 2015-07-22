package http.client;

public class Getfilename {

	/**
	 * 根据 url 和网页类型生成需要保存的网页的文件名 去除掉 url 中非文件名字符
	 */
	public String getFileNameByUrl(String url, String contentType) {
		url = url.substring(7);// remove http://
		if (contentType.indexOf("html") != -1)// text/html

		{
			url = url.replaceAll("[//?/:*|<>/]", "_") + ".html";
			return url;
		} else// 如application/pdf
		{
			return url.replaceAll("[//?/:*|<>/]", "_") + "."
					+ contentType.substring(contentType.lastIndexOf("/") + 1);
		}
	}	
}
