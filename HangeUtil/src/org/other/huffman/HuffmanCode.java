package org.other.huffman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HuffmanCode {
	private static final String DEFAULT_PATH = "D:\\hange_file\\";
	private static final String DEFAULT_FILENAME = "编码表.txt";
	
	public static Map<String, String> getCodeMap(String input) {
		HuffmanTree rootTree = HuffmanTree.getHuffmanTree(input);
		Map<String, String> codeMap = new HashMap<String, String>();
		setCodeMap(rootTree, codeMap, "");
		return codeMap;
	}
	
	public static String encode(String input) throws Exception {
		HuffmanTree rootTree = HuffmanTree.getHuffmanTree(input);
		return encode(rootTree, input);
	}
	
	@Deprecated
	public static String encode(HuffmanTree rootTree, String input) throws Exception {
		Map<String, String> codeMap = new HashMap<String, String>();
		setCodeMap(rootTree, codeMap, "");
		writeCodeMap(codeMap);
		List<String> keyList = getKeyList(codeMap);
		StringBuilder result = new StringBuilder();
		int length = keyList.size();
		for (int i=0; i<input.length(); i++) {
			String c = input.charAt(i) + "";
			for (int j=0; j<length ; j++) {
				if (c.equals(keyList.get(j))) {
					result.append(codeMap.get(keyList.get(j)));
					break;
				}
			}
		}
		return result.toString();
	}
	
	public static String decode(String output) {
		return decode(output, readCodeMap());
	}
	
	public static String decode(String output, String filepath) {
		return decode(output, readCodeMap(filepath));
	}
	
	public static String decode(String output, final Map<String, String> codeMap) {
		String nowStr = "";
		StringBuilder result = new StringBuilder();
		List<String> keyList = getKeyList(codeMap);
		int length = keyList.size();
		for (int i=0; i<output.length(); i++) {
			nowStr += output.charAt(i);
			for (int j=0; j<length ; j++) {
				if (nowStr.equals(codeMap.get(keyList.get(j)))) {
					nowStr = "";
					result.append(keyList.get(j));
					break;
				} else if (codeMap.get(keyList.get(j)).length() > nowStr.length()) {
					break;
				}
			}
		}
		return result.toString();
	}
	
	public static void showCodeMap(String input) {
		HuffmanTree rootTree = HuffmanTree.getHuffmanTree(input);
		showCodeMap(rootTree);
	}
	
	public static void showCodeMap(HuffmanTree rootTree) {
		Map<String, String> codeMap = new HashMap<String, String>();
		setCodeMap(rootTree, codeMap, "");
		Iterator<String> keys = codeMap.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			if (key.equals("\n")) {
				System.out.println("'\\n':'" + codeMap.get(key) + "'");
			} else {
				System.out.println("'" + key + "':'" + codeMap.get(key) + "'");
			}
		}
	}
	
	/**
	 * 获得字符出现次数统计map
	 * @param input
	 * @return miaoch
	 */
	protected static Map<String, Integer> getCodeCountMap(String input) {
		Map<String, Integer> resultMap = new HashMap<String, Integer>();
		for (int i=0; i<input.length(); i++) {
			String c = input.charAt(i) + "";
			if (resultMap.containsKey(c)) {
				resultMap.put(c, resultMap.get(c) + 1);
			} else {
				resultMap.put(c, 1);
			}
		}
		return resultMap;
	}
	
	private static void setCodeMap(HuffmanTree rootTree, Map<String, String> codeMap, String prefix) {
		if (rootTree == null) {
			return;
		}
		prefix += "0";
		setCodeMap(rootTree.left, codeMap, prefix);
		prefix = prefix.substring(0, prefix.length() - 1);
		//如果有孩子 跳过这步骤 只输出叶子节点
		if (rootTree.left == null) {
			codeMap.put(rootTree.getNode().getKey(), prefix);
		}
		prefix += "1";
		setCodeMap(rootTree.right, codeMap, prefix);
		prefix = prefix.substring(prefix.length() - 1);
	}
	
	private static List<String> getKeyList(final Map<String, String> codeMap) {
		List<String> keyList = new ArrayList<>();
		Iterator<String> keys = codeMap.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			keyList.add(key);
		}
		Collections.sort(keyList, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return codeMap.get(o1).length() - codeMap.get(o2).length();
			}
		});
		return keyList;
	}
	
	public static void writeCodeMap(String input) throws Exception {
		HuffmanTree rootTree = HuffmanTree.getHuffmanTree(input);
		Map<String, String> codeMap = new HashMap<String, String>();
		setCodeMap(rootTree, codeMap, "");
		writeCodeMap(codeMap, DEFAULT_PATH);
	}
	
	public static void writeCodeMap(String input, String filePath) throws Exception {
		HuffmanTree rootTree = HuffmanTree.getHuffmanTree(input);
		Map<String, String> codeMap = new HashMap<String, String>();
		setCodeMap(rootTree, codeMap, "");
		writeCodeMap(codeMap, filePath);
	}
	
	private static void writeCodeMap(final Map<String, String> codeMap) throws Exception {
		writeCodeMap(codeMap, DEFAULT_PATH + "\\" + DEFAULT_FILENAME);
	}
	
	private static void writeCodeMap(final Map<String, String> codeMap, String filePath) throws Exception {
		File file = new File(filePath);
		FileWriter fw = null;
        PrintWriter out = null;
		try {  
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile(); // 创建文件
			}
			fw = new FileWriter(file);
			out = new PrintWriter(fw);
			Iterator<String> keys = codeMap.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				String value = codeMap.get(key);
				if (key.equals("\n")) {
					out.println("\\n");
				} else {
					out.println(key);
				}
				out.println(value);
			}
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {
        	out.close();
            try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	private static Map<String, String> readCodeMap() {
		File file = new File(DEFAULT_PATH, DEFAULT_FILENAME);
		return readCodeMap(file);
	}
	
	private static Map<String, String> readCodeMap(String filePath) {
		if (!filePath.contains(":")) {
			filePath = DEFAULT_PATH + "\\" + filePath;
		}
		File file = new File(filePath);
		return readCodeMap(file);
	}
	
	private static Map<String, String> readCodeMap(File file) {
		Map<String, String> codeMap = new HashMap<>();
		BufferedReader br= null;
		try {  
			if (!file.exists()) {
				return null;
			}
			br = new BufferedReader(new FileReader(file));
			String key = br.readLine();
			String value = br.readLine();
			while (key != null && value != null) {
				if (key.equals("\\n")) {
					codeMap.put("\n", value);
				} else {
					codeMap.put(key, value);
				}
				key = br.readLine();
				value = br.readLine();
			}
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return codeMap;
		
	}
}
