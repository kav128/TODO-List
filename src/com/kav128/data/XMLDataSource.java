/*
 * Created by kav128
 * ITMO University, St Petersburg, Russia
 * 2018
 */

package com.kav128.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class XMLDataSource extends DataSource
{
    private DocumentBuilder builder;
    private Document document;
    private Document writeDocument;
    private NodeList root;
    private int documentPosition;
    private final String documentRootName;
    private Element rootWriteElement;

    public XMLDataSource(String fileName, String documentRootName)
    {
        super(fileName);
        this.documentRootName = documentRootName;

        try
        {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void openResource()
    {
        try
        {
            document = builder.parse(connectionString);
            opened = true;
            root = document.getDocumentElement().getChildNodes();
            documentPosition = 0;
        }
        catch (SAXException | IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void closeResource()
    {
        document = null;
        opened = false;
    }

    @Override
    protected DataRecord getNextRecord()
    {
        Node curNode;
        do
        {
            curNode = root.item(documentPosition++);
            if (curNode == null)
                return null;
        }
        while (curNode.getNodeType() == Node.TEXT_NODE && documentPosition < root.getLength());
        if (curNode.getNodeType() == Node.TEXT_NODE)
            curNode = null;

        if (curNode != null)
        {
            DataRecord record = new DataRecord();
            NodeList properties = curNode.getChildNodes();
            for (int i = 0; i < properties.getLength(); i++)
            {
                Node propertyNode = properties.item(i);
                if (propertyNode.getNodeType() != Node.TEXT_NODE)
                    record.setValue(propertyNode.getNodeName(), propertyNode.getTextContent());
            }
            return record;
        }
        return null;
    }

    @Override
    protected void beginWrite()
    {
        writeDocument = builder.newDocument();
        rootWriteElement = writeDocument.createElement(documentRootName);
        writeDocument.appendChild(rootWriteElement);
    }

    @Override
    protected void endWrite()
    {
        try
        {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(writeDocument);
            FileOutputStream stream = new FileOutputStream(connectionString);
            StreamResult result = new StreamResult(stream);
            transformer.transform(source, result);
        }
        catch (FileNotFoundException | TransformerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void writeRecord(DataRecord record)
    {
        String[] fields = record.getFieldArray();
        Element task = writeDocument.createElement("task");
        for (String field : fields)
        {
            Element element = writeDocument.createElement(field);
            element.setTextContent(record.getValue(field));
            task.appendChild(element);
        }
        rootWriteElement.appendChild(task);
    }
}
