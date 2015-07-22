package http.client;

public class Getfilename {

	/**
	 * ���� url ����ҳ����������Ҫ�������ҳ���ļ��� ȥ���� url �з��ļ����ַ�
	 */
	public String getFileNameByUrl(String url, String contentType) {
		url = url.substring(7);// remove http://
		if (contentType.indexOf("html") != -1)// text/html

		{
			url = url.replaceAll("[//?/:*|<>/]", "_") + ".html";
			return url;
		} else// ��application/pdf
		{
			return url.replaceAll("[//?/:*|<>/]", "_") + "."
					+ contentType.substring(contentType.lastIndexOf("/") + 1);
		}
	}	
}
