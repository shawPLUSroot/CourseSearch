package com.example.coursesearch;
import java.util.HashSet;

/**
 * Skeleton code for Red Black Tree
 *
 * @author dongwookim
 * @author Bernardo Pereira Nunes fixed small bug
 * @author Xueting Ren filled TODO part
 * @param <T> data type
 */

public class RBTree<T extends Comparable<T>> {

	Node<T> root; // The root node of the tree

	/**
	 * Initialize empty RBTree
	 */
	public RBTree() {
		root = null;
	}

	/**
	 * Add a new node into the tree with {@code root} node.
	 *
	 * @param root Node<T> The root node of the tree where x is being inserted.
	 * @param x    Node<T> New node being inserted.
	 */
	private void insertRecurse(Node<T> root, Node<T> x) {
		int cmp = root.value.compareTo(x.value);

		if (cmp > 0) {
			if (root.left.value == null) {
				root.left = x;
				x.parent = root;
			} else {
				insertRecurse(root.left, x);
			}
		} else if (cmp < 0) {
			if (root.right.value == null) {
				root.right = x;
				x.parent = root;
			} else {
				insertRecurse(root.right, x);
			}
		}
		// Do nothing if the tree already has a node with the same value.
	}

	/**
	 * Insert node into RBTree.
	 *
	 * @param x Node<T> The new node being inserted into the tree.
	 */
	private void insert(Node<T> x) {
		// TODO: Complete this function
		// You need to finish cases 1, 2 and 3; the rest has been done for you

		// Insert node into tree
		if (root == null) {
			root = x;
		} else {
			if(search(x.value) != null) return;
			insertRecurse(root, x);
		}

		// Fix tree
		while (x.value != root.value && x.parent.colour == Colour.RED) {
			boolean left  = x.parent == x.parent.parent.left; // Is parent a left node
			Node<T> uncle = left ? x.parent.parent.right : x.parent.parent.left; // Get opposite "uncle" node to parent

			if (uncle.colour == Colour.RED) {
				x.parent.colour = Colour.BLACK;
				uncle.colour = Colour.BLACK;
				x.parent.parent.colour = Colour.RED;
				x = x.parent.parent;
			} else {
				if (x.value == (left ? x.parent.right.value : x.parent.left.value)) {
					// Case 2: Left Rotation, uncle is right node, x is on the right / Right Rotation, uncle is left node, x is on the left
					x = x.parent;
					if (left) {
						// Perform left rotation
						if (x.value == root.value)
							root = x.right; // Update root
						rotateLeft(x);
					} else {
						if (x.value == root.value)
							root = x.left; // Update root
						rotateRight(x);
					}
				}
				// Adjust colours to ensure correctness after rotation
				x.parent.colour = Colour.BLACK;
				x.parent.parent.colour = Colour.RED;

				// Case 3 : Right Rotation, uncle is right node, x is on the left / Left Rotation, uncle is left node, x is on the right
				// TODO: Complete this part
				if (left) {
					// Perform right rotation
					rotateRight(x.parent.parent);
				} else {
					// This is part of the "then" clause where left and right are swapped
					// Perform left rotation
					rotateLeft(x.parent.parent);
				}
			}
		}

		// Ensure property 2 (root and leaves are black) holds
		root.colour = Colour.BLACK;
	}

	/** Rotate the node so it becomes the child of its right branch
	 /*
	 e.g.
	 [x]                    b
	 /   \                 /   \
	 a       b     == >   [x]     f
	 / \     / \           /  \
	 c  d    e   f         a    e
	 / \
	 c   d
	 */
	void rotateLeft(Node node) {
		if (node.parent != null) {
			if (node == node.parent.left) {
				node.parent.left = node.right;
			} else {
				node.parent.right = node.right;
			}
			node.right.parent = node.parent;
			node.parent = node.right;
			if (node.right.left != null) {
				node.right.left.parent = node;
			}
			node.right = node.right.left;
			node.parent.left = node;
		} else {//Need to rotate root
			Node right = root.right;
			root.right = right.left;
			right.left.parent = root;
			root.parent = right;
			right.left = root;
			right.parent = null;
			root = right;
		}
	}

	public void rotateRight(Node node) {
		if (node.parent != null) {
			if (node == node.parent.left) {
				node.parent.left = node.left;
			} else {
				node.parent.right = node.left;
			}

			node.left.parent = node.parent;
			node.parent = node.left;
			if (node.left.right != null) {
				node.left.right.parent = node;
			}
			node.left = node.left.right;
			node.parent.right = node;
		} else {//Need to rotate root
			Node left = root.left;
			root.left = root.left.right;
			left.right.parent = root;
			root.parent = left;
			left.right = root;
			left.parent = null;
			root = left;
		}
	}

	/**
	 * Demo functions (Safely) insert a value into the tree
	 *
	 * @param value T The value of the new node being inserted.
	 */
	public void insert(T value) {
		Node<T> node = new Node<T>(value);
		if (node != null)
			insert(node);
	}

	/**
	 * Return the result of a pre-order traversal of the tree
	 *
	 * @param tree Tree we want to pre-order traverse
	 * @return pre-order traversed tree
	 */
	private void preOrder(Node<T> tree) {
		if (tree != null && tree.value != null) {
			preOrder(tree.left);
			preOrder(tree.right);
		}
	}

	public void preOrder() {
		preOrder(root);
	}




	/**
	 * Return the corresponding node of a value, if it exists in the tree
	 *
	 * @param x Node<T> The root node of the tree we search for the value {@code v}
	 * @param v Node<T> The node that we are looking for
	 * @return
	 */
	private Node<T> find(Node<T> x, T v) {

		if (x == null || x.value == null)
			return null;

		int cmp = v.compareTo(x.value);
		if (cmp < 0)
			return find(x.left, v);
		else if (cmp > 0)
			return find(x.right, v);
		else
			return x;
	}

	/**
	 * Returns a node if the value of the node is {@code key}.
	 *
	 * @param key T The value we are looking for
	 * @return
	 */
	public Node<T> search(T key) {
		return find(root, key);
	}
	public HashSet<String> searchLower(int num) {

		return findLower((Node<Pair<Integer>>)root, num);
	}
	public HashSet<String> searchGreater(int num) {
		return findGreater((Node<Pair<Integer>>)root, num);
	}
	private HashSet<String> findLower(Node<Pair<Integer>> x, int num) {

		if (x != null && x.value != null) {
			if(num>x.value.val){
				HashSet<String> oneNode =x.value.codes;
				for (String s : oneNode) {
					Lower.add(s);
				}
			}
			findLower(x.left,num);
			findLower(x.right,num);
		}return Lower;

	}
	private HashSet<String> findGreater(Node<Pair<Integer>> x, int num) {

			if (x != null && x.value != null) {
				if(num<x.value.val){
					HashSet<String> oneNode =x.value.codes;
					for (String s : oneNode) {
						Greater.add(s);
					}
				}
				findGreater(x.left,num);
				findGreater(x.right,num);
			}return Greater;

	}
    HashSet<String> Greater =new HashSet<>();
	HashSet<String> Lower =new HashSet<>();


	}
