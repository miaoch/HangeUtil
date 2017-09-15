package org.jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {
	public static String prefix = "http://www.suqian.gov.cn";
	public static String url = "http://www.suqian.gov.cn/sofpro/xxgk/xxgkList.jsp";
	public static void main(String args[]) throws IOException {
        int currentPage = 1;
        List<String> hrefs = new ArrayList<String>();
        while (true) {
        	 //获取请求连接
            Connection con = Jsoup.connect(url).timeout(5000);
        	con.data("currentPage", (currentPage++) + "");
        	con.data("channel_id", "778669b95cfd4b8fb94b6cf95d88b80b");
        	con.data("webSiteId", "d97a02e21046404d906cb2f541e60d1e");
            Document doc = con.post();
    		Elements links = doc.select("a.trlist_1");
    		if (links.isEmpty()) {
    			break;
    		}
    		for (Element link : links) {
    			if (link.text().indexOf("病残鉴定公示") > -1) {
    				String linkHref = link.attr("href");
    				hrefs.add(linkHref);
    				System.out.println(link.text());
    			}
    		}
        }
        
		for (int i=0;i<hrefs.size();i++) {
			String href = hrefs.get(i);
			System.out.println(prefix + href);
			try {
				Document doc1 = Jsoup.connect(prefix + href).get();
				//页面中是第3个table
				Elements trs = doc1.select("table").get(2).select("tr");
				for (int j=1,len=trs.size();j<len;j++) {
					Elements tds = trs.get(j).select("td");
					//new cjr()
					for (Element td:tds) {
						String value = td.select("p").first().select("span").first().text();
						System.out.print(value + " ");
						//TODO 你可以直接拿这个Value去组装一个bean
						//cjr.setxx...
					}
					// save(cjr)
					System.out.println();
				}
			} catch (Exception e) {
				System.out.println("第i个页面不符合要求，url为：" + prefix + href);
			}
		}
	}
}
