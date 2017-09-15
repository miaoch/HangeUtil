package org.other.huffman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 哈弗曼树
 * @author miao
 *
 * @param <T>
 */
public class HuffmanTree<T extends Number> {
	
	public HuffmanTree<T> left;
	public HuffmanTree<T> right;
	private Entry<T> node;
	
	public HuffmanTree() {}
	
	public HuffmanTree(HuffmanTree<T> left, HuffmanTree<T> right, Entry<T> node) {
		this.left = left;
		this.right = right;
		this.node = node;
	}
	
	@Deprecated
	public void add(T value) throws Exception  {
		put(value.toString(), value);
	}
	
	public void put(String key, T value) throws Exception {
		if (value.doubleValue() <= 0) {
			throw new Exception("数字必须为非负数！");
		}
		Map<String, T> KVMap = new HashMap<String, T>();
		setKVMap(KVMap);
		if (KVMap.containsKey(key)) {
			KVMap.put(key, value);
			setSelf(getHuffmanTree(KVMap));
			return;
		}
		//此为新节点
		Entry<T> entry = new Entry<T>(value);
		entry.setKey(key);
		HuffmanTree<T> newTree = new HuffmanTree<T>(null, null, entry);
		//如果本数为空树，则令该节点为自身
		if (this.getValue() == null) {
			setSelf(newTree);
			return;
		} else if (value.doubleValue() >= this.getValue().doubleValue()) {
			//如果value大于本树的value 新增树 将新节点设为新树的右子树，本树设为左子树
			Entry<T> newEntry = new Entry(value.doubleValue() + this.getValue().doubleValue());
			HuffmanTree<T> cloneTree = clone(); //获得原树的复制
			HuffmanTree<T> rootTree = new HuffmanTree<T>(cloneTree, newTree, newEntry);
			setSelf(rootTree);
		} else if (this.right == null || value.doubleValue() >= this.right.getValue().doubleValue()) {
			//如果value大于本树右节点的value 新增树 将新节点设为新树的左子树，本树设为右子树
			Entry<T> newEntry = new Entry(value.doubleValue() + this.getValue().doubleValue());
			HuffmanTree<T> cloneTree = clone(); //获得原树的复制
			HuffmanTree<T> rootTree = new HuffmanTree<T>(newTree, cloneTree, newEntry);
			setSelf(rootTree);
		} else if (this.left == null || value.doubleValue() >= this.left.getValue().doubleValue()) {
			//如果value大于本树左节点的value 将新节点设为左节点的右节点 并指向新树 再将新树设为this的节点(再判断)
			Entry<T> newEntry = new Entry(value.doubleValue() + this.left.getValue().doubleValue());
			HuffmanTree<T> rootTree = new HuffmanTree<T>(this.left, newTree, newEntry);
			if (rootTree.getValue().doubleValue() > this.right.getValue().doubleValue()) {
				this.left = this.right;
				this.right = rootTree;
			} else {
				this.left = rootTree;
			}
		} else {
			setSelf(getHuffmanTree(key, value));
		}
	}

	private void setSelf(HuffmanTree<T> newTree) {
		this.left = null;
		this.right = null;
		this.node = null;
		this.left = newTree.left;
		this.right = newTree.right;
		this.node = newTree.node;
	}
	
	private HuffmanTree<T> clone(HuffmanTree<T> newTree) {
		return new HuffmanTree<T>(newTree.left, newTree.right, newTree.node);
	}
	
	protected HuffmanTree<T> clone() {
		return clone(this);
	}
	
	private T getValue() {
		if (this.node == null) {
			return null;
		}
		return (T) this.node.value;
	}
	
	public Entry getNode() {
		return this.node;
	}
	
	@Override
	public String toString() {
		if (node == null) {
			return "";
		}
		if (this.right != null) {
			return "(" + left.toString() + "," + right.toString() + ")";
		} else {
			return getValue().toString();
		}
	}
	
	private HuffmanTree<T> getHuffmanTree(String key, T value) {
		Map<String, T> KVMap = new HashMap<String, T>();
		setKVMap(KVMap);
		KVMap.put(key, value);
		return getHuffmanTree(KVMap);
	}
	
	private HuffmanTree<T> getHuffmanTree(Map<String, T> KVMap) {
		Iterator<String> iterator = KVMap.keySet().iterator();
		List<String> keys = new ArrayList<>();
		List<T> values = new ArrayList<>();
		while (iterator.hasNext()) {
			String key1 = iterator.next();
			keys.add(key1);
			values.add(KVMap.get(key1));
		}
		return getHuffmanTree(keys, values);
	}
	private void setKVMap(Map<String, T> KVMap) {
		//如果有孩子 跳过这步骤 只输出叶子节点
		if ((this.left == null || this.right == null)) {
			if (getNode() != null) {
				KVMap.put(getNode().getKey(), getValue());
			}
		} else {
			this.left.setKVMap(KVMap);
			this.right.setKVMap(KVMap);
		}
	}
	/**
	 * 根据list获取哈弗曼树
	 * @param input
	 * @return
	 */
	private static HuffmanTree getRealHuffmanTree(List<HuffmanTree> nodes) {
		// 只要nodes数组中还有2个以上的节点  
        while (nodes.size() > 1) {  
            Collections.sort(nodes, new Comparator<HuffmanTree>() {
    			@Override
    			public int compare(HuffmanTree o1, HuffmanTree o2) {
    				double com = o1.getValue().doubleValue() - o2.getValue().doubleValue();
    				return com > 0?-1:com==0?0:1;
    			}
    		});
            //获取权值最小的两个节点  
            HuffmanTree left = nodes.get(nodes.size()-1);  
            HuffmanTree right = nodes.get(nodes.size()-2);  
            //生成新节点，新节点的权值为两个子节点的权值之和  
            Entry newEntry = new Entry(left.getValue().doubleValue() + right.getValue().doubleValue());
            HuffmanTree parent = new HuffmanTree(left, right, newEntry);  
            //删除权值最小的两个节点  
            nodes.remove(nodes.size()-1);  
            nodes.remove(nodes.size()-1);  
            //将新节点加入到集合中  
            nodes.add(parent);  
        }  
        return nodes.get(0);
	}
	
	public static HuffmanTree getHuffmanTree(int[] values) {
		return getHuffmanTree(null, values);
	}
	
	public static HuffmanTree getHuffmanTree(String[] keys, int[] values) {
		List<HuffmanTree> nodes = new ArrayList<>(); 
		for (int i=0; i<values.length; i++) {
			Entry data = new Entry(values[i]);
			if (keys != null) {
				data.setKey(keys[i]);
			}
			nodes.add(new HuffmanTree<>(null, null, data));
		}
		return getRealHuffmanTree(nodes);
	}
	
	public static HuffmanTree getHuffmanTree(double[] values) {
		return getHuffmanTree(null, values);
	}
	public static HuffmanTree getHuffmanTree(String[] keys, double[] values) {
		List<HuffmanTree> nodes = new ArrayList<>(); 
		for (int i=0; i<values.length; i++) {
			Entry data = new Entry<>(values[i]);
			if (keys != null) {
				data.setKey(keys[i]);
			}
			nodes.add(new HuffmanTree<>(null, null, data));
		}
		return getRealHuffmanTree(nodes);
	}
	
	public static HuffmanTree getHuffmanTree(List<String> keys, List values) {
		List<HuffmanTree> nodes = new ArrayList<>(); 
		for (int i=0; i<values.size(); i++) {
			Entry data = new Entry<>(values.get(i).toString());
			if (keys != null) {
				data.setKey(keys.get(i));
			}
			nodes.add(new HuffmanTree<>(null, null, data));
		}
		return getRealHuffmanTree(nodes);
	}
	
	public static HuffmanTree getHuffmanTree(List values) {
		return getHuffmanTree(null, values);
	}
	
	public static HuffmanTree getHuffmanTree(String input) {
		Map<String, Integer> map = HuffmanCode.getCodeCountMap(input);
		List<Integer> valueList = new ArrayList<>();
		List<String> keyList = new ArrayList<>();
		Iterator<String> keys = map.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			valueList.add(map.get(key));
			keyList.add(key);
		}
		return HuffmanTree.getHuffmanTree(keyList, valueList);
	}
	
	static class Entry<T extends Number> {
		public T value;
		private String key;
		public Entry(Integer value) {
			this.value = (T) new Integer(value);
		}
		public Entry(Long value) {
			this.value = (T) new Long(value);
		}
		public Entry(Double value) {
			if ((value+"").endsWith(".0")) {
				this.value = this.value = (T) new Integer((value+"").substring(0,(value+"").length()-2));
			} else {
				this.value = (T) new Double(value);
			}
		}
		public Entry(Float value) {
			if ((value+"").endsWith(".0")) {
				this.value = this.value = (T) new Integer((value+"").substring(0,(value+"").length()-2));
			} else {
				this.value = (T) new Float(value);
			}
		}
		
		public Entry(T value) {
			this.value = value;
		}
		
		public String getKey(){
			return key;
		}
		
		public void setKey(String key){
			this.key = key;
		}
		
		public Entry(String value) {
			if (value.indexOf("\\.") < 0) {
				this.value = (T) new Long(value);
			} else if (value.endsWith(".0")) {
				this.value = (T) new Integer(value.substring(0,value.length()-2));
			} else {
				this.value = (T) new Double(value);
			}
		}
		
	}
	
}

