package test2;

import java.util.*;
class TreeNode {
	int val;
	TreeNode left;
	TreeNode right;
	TreeNode(int v) {
		val = v;
	}
}
public class Solution {
	public static String tree2str(TreeNode root) {
		StringBuilder sb = new StringBuilder();
		sb.append(root.val);
		if (root.left == null && root.right != null) {
			sb.append("()(" + tree2str(root.right) + ")");
		}
        return sb.toString();
    }
	public static void main(String args[]) {
		TreeNode root = new TreeNode(5);
		root.left = new TreeNode(2);
		root.right = new TreeNode(13);
		//root.left.left = new TreeNode(1);
		root.left.right = new TreeNode(3);
		root.right.left = new TreeNode(6);
		root.right.right = new TreeNode(9);
		System.out.println(tree2str(root));
	}
}