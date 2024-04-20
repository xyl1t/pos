package pkgMisc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import pkgFile.ReaderTextFile;

/**
 * Primitive XML parser
 * Features: Nodes that can have children and (text) content, no attributes, no self-closing tags, no comments	 
 */
public class XmlParser {
	/**
	 * Is an XML node, can contain text and child nodes
	 */
	public static class Node {
		private String tagName;
		private String text;
		private List<Node> childNodes;
		private Node parent;
		
		public Node() {
			this("", "", new ArrayList<>(), null);
		}
		public Node(String tagName, String text) {
			this(tagName, text, new ArrayList<Node>(), null);
		}
		public Node(String tagName, Node parent) {
			this(tagName, "", new ArrayList<Node>(), parent);
		}
		public Node(String tagName, List<Node> childNodes) {
			this(tagName, "", childNodes, null);
		}
		public Node(String tagName, Node[] childNodes) {
			this(tagName, "", Arrays.asList(childNodes), null);
		}
		public Node(String tagName, String text, List<Node> childNodes) {
			this(tagName, text, childNodes, null);
		}
		public Node(String tagName, String text, List<Node> childNodes, Node parent) {
			this.tagName = tagName;
			this.text = text;
			this.childNodes = childNodes;
			this.parent = parent;
			if (parent != null) {
				parent.childNodes.add(this);
			}
		}
		
		public List<Node> GetNodes(String tagName) {
			List<Node> newChildNodes = new ArrayList<Node>();
			for (Node n : childNodes) {
				if (n.tagName.equals(tagName))
					newChildNodes.add(n);
			}
			return newChildNodes;
		}
		
		public String getTagName() {
			return tagName;
		}
		public String getText() {
			return text;
		}
		public Node getParent() {
			return parent;
		}
		public List<Node> getChildNodes() {
			return childNodes;
		}
		public List<Node> getChildNodesByTagName(String tagName) {
			return childNodes.stream().filter(n -> n.getTagName().equals(tagName)).collect(Collectors.toList());
		}
		public void setTagName(String tagName) {
			this.tagName = tagName;
		}
		public void setText(String text) {
			this.text = text;
		}
		public void setParent(Node parent) {
			this.parent = parent;
		}
		public void setChildNodes(List<Node> childNodes) {
			this.childNodes = childNodes;
		}
		
		
		public void addNode(Node n) {
			n.parent = this;
			childNodes.add(n);
		}
		public void removeNode(String tagName) {
			childNodes.removeIf(n -> (n.tagName.equals(tagName)));
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (childNodes == null) {
				if (other.childNodes != null)
					return false;
			} else if (!childNodes.equals(other.childNodes))
				return false;
			if (parent == null) {
				if (other.parent != null)
					return false;
			} else if (!parent.equals(other.parent))
				return false;
			if (tagName == null) {
				if (other.tagName != null)
					return false;
			} else if (!tagName.equals(other.tagName))
				return false;
			if (text == null) {
				if (other.text != null)
					return false;
			} else if (!text.equals(other.text))
				return false;
			return true;
		}
	}
	
	private Node root = new Node();
	public Node getRoot() {
		return root;
	}

	private ReaderTextFile rtf;
	// private String errorMsg;
	
	public XmlParser() {
	}
	
	/**
	 * Prases the given file
	 * Please do not read or modify the code, small alterations in the code may break everything 
	 * @throws IOException if file opening failed
	 */
	public void Parse(String file) throws IOException {
		this.rtf = new ReaderTextFile(file);	
		root = null;
		Node current = root;
		rtf.open();
		
		// parser states
		boolean betweenBrackets = false;
		boolean betweenTags = false;
		boolean endTag = false;
		
		String token = "";
		String wholeLine = rtf.readLine();
		while (wholeLine != null) {
			wholeLine = wholeLine.trim();

			if (!wholeLine.equals("<?xml version=\"1.0\"?>")) {
				for(int i = 0, n = wholeLine.length() ; i < n ; i++) { 
					char c = wholeLine.charAt(i); 
					token += c;
					
					// skip spaces if they are not between tags or brackets
					if (c == ' ' && !betweenBrackets && !betweenTags) {
						continue;
					}
					
					if (c == '<') {
						betweenBrackets = true;
						if (wholeLine.charAt(i+1) == '/') { // look if the next character is makes closing tag
							current = current.parent; 
							betweenTags = false;
							endTag = true;
							i++;
						} else {
							endTag = false;
						}
						token = "";
						continue;
					} else if (betweenBrackets && wholeLine.charAt(i+1) == '>') {
						if (!endTag) { // if not an end tag, create a new node
							current = new Node(token, current);
							if (root == null) root = current;
							betweenTags = true;
						} 
						token = "";
						i++;
						betweenBrackets = false;
						continue;
					} 
					// if text ist between tags dad them to text
					if (betweenTags && !betweenBrackets) {
						current.text += c;
					}
					
				}
			}
			
			wholeLine = rtf.readLine();
		}
		
		rtf.close();
	}
	
	public void Save(String filename) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename)));

		out.println("<?xml version=\"1.0\"?>\n");
		writeNode(root, out, "");
		out.close();
	}
	private void writeNode(Node n, PrintWriter out, String tab) throws IOException {
		if (n != null) {
			if (n.childNodes.size() > 0) { 
				out.println(tab + "<" + n.tagName + ">");
				if (!n.text.isEmpty()) {
					out.println(tab + "\t" + n.text);
				}
				for (Node child : n.childNodes) {
					writeNode(child, out, tab + "\t");
				}
				out.println(tab + "</" + n.tagName + ">");
			} else {
				out.println(tab + "<" + n.tagName + ">" + n.text + "</" + n.tagName + ">");
			}
		}
	}
	
	
	
	// public String GetErrorMsg() { return errorMsg; }
}
