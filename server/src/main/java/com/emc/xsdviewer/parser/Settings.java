package com.emc.xsdviewer.parser;

import java.util.HashSet;
import java.util.Set;

public class Settings {
    private Set<String> xsdNodesNames;

    public Settings() {
        Set<String> set = new HashSet<>();

        set.add("xs:element");
        set.add("xs:schema");
        set.add("xs:complexType");
        set.add("xs:simpleType");
        set.add("xs:sequence");
        set.add("xs:restriction");
        set.add("xs:minInclusive");
        set.add("xs:maxInclusive");
        set.add("xs:totalDigits");
        set.add("xs:minLength");
        set.add("xs:maxLength");
        xsdNodesNames = set;
    }

    public Set<String> getXsdNodesNames() {
        return xsdNodesNames;
    }

    public void setXsdNodesNames(final Set<String> xSDNodesNames) {
        xsdNodesNames = xSDNodesNames;
    }

    public void addNodeName(final String nodeName) {
        xsdNodesNames.add(nodeName);
    }

    public void deleteNodeName(final Set<String> nodeNames) {
        xsdNodesNames.removeAll(nodeNames);
    }
}
