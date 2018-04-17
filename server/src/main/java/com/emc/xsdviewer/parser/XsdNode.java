package com.emc.xsdviewer.parser;

import java.util.List;

import org.w3c.dom.Node;

public class XsdNode {

    private String name;
    private String details;
    private List<XsdNode> children;
    private Node node;
    private boolean visible;
    private String xPath;

    public XsdNode() {
    }

    public XsdNode(Node node, String xPath) {
        this.name = XsdNodeHandler.createName(node);
        this.details = XsdNodeHandler.createDetails(node);
        this.children = XsdNodeHandler.createNextNodeList(node,
                node.getNodeName().equals("xs:element") ? xPath + "/" + this.name : xPath);
        this.node = node;
        this.visible = true;
        this.xPath = node.getNodeName().equals("xs:element") ? xPath + "/" + this.name : xPath;
    }

    public String getName() {
        return name;
    }

    public void setName(final String elementName) {
        this.name = elementName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<XsdNode> getChildren() {
        return children;
    }

    public void setNextNodeList(final List<XsdNode> nextNodeList) {
        this.children = nextNodeList;
    }

    protected Node getNode() {
        return node;
    }

    public void setNode(final Node node) {
        this.node = node;
    }

    protected boolean isVisible() {
        return visible;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    public String getXPath() {
        return xPath;
    }

    public void setXPath(final String newxPath) {
        this.xPath = newxPath;
    }

}
