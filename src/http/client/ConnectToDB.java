package http.client;

import java.sql.*;

public class ConnectToDB {

	public static Connection getConnection() throws SQLException,
			java.lang.ClassNotFoundException {
		String url = "jdbc:mysql://localhost:3306/app?useUnicode=true&characterEncoding=utf8";
		Class.forName("com.mysql.jdbc.Driver");
		String userName = "root";
		String password = "1234";
		Connection con = DriverManager.getConnection(url, userName, password);
		return con;
	}

	public static void InsertData(String data_name, String data_icon,
			String data_size, String data_versionname, String data_updatetime,
			String data_download_url) {
		try {
			Connection con = getConnection();
			Statement sql = con.createStatement();
			// String query =
			// "insert into baidu_app_rank(data_name,data_icon,data_size,data_versionname,data_updatetime) values ('"
			// + data_name +"', '" + data_icon + "', "+ data_size +", '" +
			// data_versionname + "', '" + data_updatetime +"');";
			// 由于排名rank是从0开始计数的，所以把所有的rank+1
			String query = "insert into baidu_app_rank(data_name,data_icon,data_size,data_versionname,data_updatetime,data_download_url) values ('"
					+ data_name
					+ "', '"
					+ data_icon
					+ "', "
					+ data_size
					+ ", '"
					+ data_versionname
					+ "', '"
					+ data_updatetime
					+ "', '" + data_download_url + "');";
			System.out.println(query);
			sql.execute(query);
			sql.close();
			con.close();
		} catch (java.lang.ClassNotFoundException e) {
			System.err.println("ClassNotFoundException:" + e.getMessage());
		} catch (SQLException ex) {
			System.err.println("SQLException:" + ex.getMessage());
		}
	}

	public static void InsertWandoujiaData(String package_name,
			String app_name, String change_info, String desc_info,
			String file_size, String update_date, String version) {
		try {
			Connection con = getConnection();
			Statement sql = con.createStatement();
			// String query =
			// "insert into baidu_app_rank(data_name,data_icon,data_size,data_versionname,data_updatetime) values ('"
			// + data_name +"', '" + data_icon + "', "+ data_size +", '" +
			// data_versionname + "', '" + data_updatetime +"');";
			// 由于排名rank是从0开始计数的，所以把所有的rank+1
			String query = "insert into wandoujia_app(package_name,app_name,change_info,desc_info,file_size,update_date,version) values ('"
					+ package_name
					+ "', '"
					+ app_name
					+ "', '"
					+ change_info
					+ "', '"
					+ desc_info
					+ "', '"
					+ file_size
					+ "', '"
					+ update_date + "','" + version + "');";
			System.out.println(query);
			sql.execute(query);
			sql.close();
			con.close();
		} catch (java.lang.ClassNotFoundException e) {
			System.err.println("ClassNotFoundException:" + e.getMessage());
		} catch (SQLException ex) {
			System.err.println("SQLException:" + ex.getMessage());
		}
	}

	public static void SelectDomainDaa() {
		try {
			Connection con = getConnection();
			Statement sql = con.createStatement();
			String query = "select domain FROM domain_url where domain not in (select domain_name from url_fenlei) group by domain;";
			// System.out.println(query);
			ResultSet rs = sql.executeQuery(query);
			while (rs.next()) {
				String domain = rs.getString("domain");
				// String url = "http://whois.chinaz.com/" + domain;
				 String url = "http://whois.alexa.cn/index.php?url=" + domain;
				// String url = "http://whois.aizhan.com/" + domain;
//				String url = "http://www.cha127.com/whois/" + domain + ".html";
				// System.out.println(url);
				// DomainProxyClient domainProxyClient = new
				// DomainProxyClient();
				// domainProxyClient.downloadFile(url);
				DomainParserHtml domainParserHtml = new DomainParserHtml();
				domainParserHtml.StartHtmlParser(url);
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// sleep 10000；
			}
		} catch (java.lang.ClassNotFoundException e) {
			System.err.println("ClassNotFoundException:" + e.getMessage());
		} catch (SQLException ex) {
			System.err.println("SQLException:" + ex.getMessage());
		}
	}
}