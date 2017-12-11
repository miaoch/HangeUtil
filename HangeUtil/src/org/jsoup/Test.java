package org.jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {
	public static void main(String args[]) throws IOException {
	    
	    Connection.Response res = Jsoup.connect("http://220.191.216.139:8080/webapps/login")
	    		.data("name", "admin", "password", "123456")
	    		.method(Connection.Method.POST)
	    		.execute();
	    		 
		String sessionId = res.cookie("JSESSIONID");
		System.out.println(Jsoup.connect("http://220.191.216.139:8080/webapps/attendance")
				.cookie("JSESSIONID", sessionId).get().body());
		
	}
}
