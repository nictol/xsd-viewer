package com.emc.xsdviewer.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XsdTreeObject {
    private XsdNode rootElement;

    public XsdTreeObject(String fileName) {
        this.rootElement = XsdTreeObjectHandler.createXSDNode(fileName);
    }

    public XsdTreeObject(InputStream inputStream) {
        this.rootElement = XsdTreeObjectHandler.createXSDNode(inputStream);
    }

    public XsdNode getRootElement() {
        return rootElement;
    }

    public void setRootElement(XsdNode rootElement) {
        this.rootElement = rootElement;
    }

    private void changeTree(XsdNode parent, XsdNode child, Settings settings) {
        List<XsdNode> pointer = child.getChildren();

        if (pointer != null) {
            for (int i = pointer.size() - 1; i >= 0; --i) {
                changeTree(child, pointer.get(i), settings);
            }
        }
        if (settings.getXsdNodesNames().contains(child.getName())) {
            List<XsdNode> mem = child.getChildren();

            parent.getChildren().remove(child);
            if (parent.getChildren().isEmpty()) {
                parent.setNextNodeList(mem);
            }
        }
    }

    public XsdNode getTree(Settings settings) {
        XsdTreeObject copyTree = this;
        List<XsdNode> root = new ArrayList<XsdNode>();

        root.add(copyTree.getRootElement());
        XsdNode aboveRoot = new XsdNode();
        aboveRoot.setNextNodeList(root);
        aboveRoot.setName("root");
        copyTree.setRootElement(aboveRoot);
        changeTree(copyTree.getRootElement(), copyTree.getRootElement().getChildren().get(0), settings);

        return copyTree.getRootElement();
    }
}
