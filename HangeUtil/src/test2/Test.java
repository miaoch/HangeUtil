package test2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Test{
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "1");
		String value = map.get("1");
		value = new String("2");
		System.out.println(map.get("1"));
		Map<String, String> m = null;
		{
			m = new HashMap<String, String>(); 
			m.put("1", value);
			m.put("2", value);
			m.put("3", value);
			m.put("4", value);
			m.put("5", value);
		}
	}


}
