package com.emc.xsdviewer.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XsdTreeObjectHandler {
    private static final Logger LOGGER = Logger.getLogger(XsdTreeObjectHandler.class.getName());

    protected XsdTreeObjectHandler() {
    }

    public static XsdNode createXSDNode(String fileName) {
        XsdNode element = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(fileName));
            element = new XsdNode(document.getDocumentElement(), "");
        } catch (final IOException | SAXException | ParserConfigurationException e) {
            LOGGER.log(Level.ALL, "EXCEPTION in createXSDNode-method", e);
        }

        return element;
    }

    public static XsdNode createXSDNode(InputStream inputStream) {
        XsdNode element = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            element = new XsdNode(document.getDocumentElement(), "");
        } catch (final IOException | SAXException | ParserConfigurationException e) {
            LOGGER.log(Level.ALL, "EXCEPTION in createXSDNode-method", e);
        }

        return element;
    }
}
