package com.enjoyf.webapps.tools.weblogic.privilege;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class MenuTree implements Serializable{
	public final static String TREE_ROOT_ID = "-1";
	private static String TREE_ROOT_ICON_URL = "/static/images/tree/root.gif";

	private ArrayList nodes = new ArrayList();

	private String baseTarget = "_blank";

	private String jsTreeName = "js_tree";

	private MenuTree() {
	}

	public static MenuTree getInstance() {
		return new MenuTree();
	}

	public String getBaseTarget() {
		return baseTarget;
	}

	public void setBaseTarget(String baseTarget) {
		this.baseTarget = baseTarget;
	}

	public void addNode(MenuTreeNode node) {
		nodes.add(node);
	}

	public void removeNode(MenuTreeNode node) {
		nodes.remove(node);
	}

	public String treeToString() {
		StringBuffer tree = new StringBuffer();
		tree.append("<script language=\"JavaScript\">\n");
		tree.append("var " + this.jsTreeName + "=" + "new TreeView('"
				+ this.jsTreeName + "','" + this.baseTarget + "');\n");
		Iterator it = nodes.iterator();
		MenuTreeNode node = null;
		while (it.hasNext()) {
			node = (MenuTreeNode) it.next();
			toStringNode(tree, node);
		}
		tree.append("document.write(" + this.jsTreeName + ");");
		tree.append("</script>\n");
		return tree.toString();
	}
	
	public String treeToRootMenuString(){
		StringBuffer tree = new StringBuffer();
		tree.append("<script language=\"JavaScript\">\n");
		tree.append("var " + this.jsTreeName + "=" + "new TreeView('"
				+ this.jsTreeName + "','" + this.baseTarget + "');\n");
		Iterator it = nodes.iterator();
		MenuTreeNode node = null;
		while (it.hasNext()) {
			node = (MenuTreeNode) it.next();
			toStringNode(tree, node);
		}
		tree.append("document.write(" + this.jsTreeName + ".toRootMenuString());");
		tree.append("</script>\n");
		return tree.toString();
	}
	
	private void toStringNode(StringBuffer tree, final MenuTreeNode node) {
		tree.append(this.jsTreeName + ".add('" + node.getId() + "','"
				+ node.getParentId() + "','" + node.getName() + "','"
				+ node.getUrl() + "','" + node.getDescription() + "','"
				+ node.getTarget() + "','" + node.getIconUrl() + "');\n");
	}

	public boolean checkTree() {
		if(nodes.size() == 0) {
			return false;
		}
		Iterator it = nodes.iterator();
		MenuTreeNode node= null;
		boolean checkResult = false;
		while(it.hasNext()) {
			node = (MenuTreeNode)it.next();
			if(node.getParentId().equals(TREE_ROOT_ID)) {
				node.setParentId(TREE_ROOT_ID);
				node.setIconUrl(TREE_ROOT_ICON_URL);
				checkResult = true;
			}
		}
		
		return checkResult;
	}
	
	public int getNodeNum() {
		return nodes.size();
	}

	public String getJsTreeName() {
		return jsTreeName;
	}

	public void setJsTreeName(String jsTreeName) {
		this.jsTreeName = jsTreeName;
	}
}

