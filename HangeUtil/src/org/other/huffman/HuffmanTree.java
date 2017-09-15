package org.other.huffman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ��������
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
			throw new Exception("���ֱ���Ϊ�Ǹ�����");
		}
		Map<String, T> KVMap = new HashMap<String, T>();
		setKVMap(KVMap);
		if (KVMap.containsKey(key)) {
			KVMap.put(key, value);
			setSelf(getHuffmanTree(KVMap));
			return;
		}
		//��Ϊ�½ڵ�
		Entry<T> entry = new Entry<T>(value);
		entry.setKey(key);
		HuffmanTree<T> newTree = new HuffmanTree<T>(null, null, entry);
		//�������Ϊ����������ýڵ�Ϊ����
		if (this.getValue() == null) {
			setSelf(newTree);
			return;
		} else if (value.doubleValue() >= this.getValue().doubleValue()) {
			//���value���ڱ�����value ������ ���½ڵ���Ϊ��������������������Ϊ������
			Entry<T> newEntry = new Entry(value.doubleValue() + this.getValue().doubleValue());
			HuffmanTree<T> cloneTree = clone(); //���ԭ���ĸ���
			HuffmanTree<T> rootTree = new HuffmanTree<T>(cloneTree, newTree, newEntry);
			setSelf(rootTree);
		} else if (this.right == null || value.doubleValue() >= this.right.getValue().doubleValue()) {
			//���value���ڱ����ҽڵ��value ������ ���½ڵ���Ϊ��������������������Ϊ������
			Entry<T> newEntry = new Entry(value.doubleValue() + this.getValue().doubleValue());
			HuffmanTree<T> cloneTree = clone(); //���ԭ���ĸ���
			HuffmanTree<T> rootTree = new HuffmanTree<T>(newTree, cloneTree, newEntry);
			setSelf(rootTree);
		} else if (this.left == null || value.doubleValue() >= this.left.getValue().doubleValue()) {
			//���value���ڱ�����ڵ��value ���½ڵ���Ϊ��ڵ���ҽڵ� ��ָ������ �ٽ�������Ϊthis�Ľڵ�(���ж�)
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
		//����к��� �����ⲽ�� ֻ���Ҷ�ӽڵ�
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
	 * ����list��ȡ��������
	 * @param input
	 * @return
	 */
	private static HuffmanTree getRealHuffmanTree(List<HuffmanTree> nodes) {
		// ֻҪnodes�����л���2�����ϵĽڵ�  
        while (nodes.size() > 1) {  
            Collections.sort(nodes, new Comparator<HuffmanTree>() {
    			@Override
    			public int compare(HuffmanTree o1, HuffmanTree o2) {
    				double com = o1.getValue().doubleValue() - o2.getValue().doubleValue();
    				return com > 0?-1:com==0?0:1;
    			}
    		});
            //��ȡȨֵ��С�������ڵ�  
            HuffmanTree left = nodes.get(nodes.size()-1);  
            HuffmanTree right = nodes.get(nodes.size()-2);  
            //�����½ڵ㣬�½ڵ��ȨֵΪ�����ӽڵ��Ȩֵ֮��  
            Entry newEntry = new Entry(left.getValue().doubleValue() + right.getValue().doubleValue());
            HuffmanTree parent = new HuffmanTree(left, right, newEntry);  
            //ɾ��Ȩֵ��С�������ڵ�  
            nodes.remove(nodes.size()-1);  
            nodes.remove(nodes.size()-1);  
            //���½ڵ���뵽������  
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

