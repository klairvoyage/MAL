package processor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class XMLProcessor {

    public void removeStatus(String listType, String filePath, String statusType) {
        Document doc = parseDocument(filePath);
        if (doc != null) {
            NodeList nodeList = doc.getElementsByTagName(listType);
            processRemoveStatus(nodeList, statusType);
            writeDocument(doc, filePath);
        }
    }

    public void updateOnImport(String listType, String filePath) {
        Document doc = parseDocument(filePath);
        if (doc != null) {
            NodeList nodeList = doc.getElementsByTagName(listType);
            processUpdateOnImport(nodeList);
            writeDocument(doc, filePath);
        }
    }

    private Document parseDocument(String filePath) {
        try {
            File file = new File(filePath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            return db.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void processRemoveStatus(NodeList nodeList, String statusType) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            NodeList myStatusList = element.getElementsByTagName("my_status");
            if (myStatusList.getLength() > 0) {
                Element myStatus = (Element) myStatusList.item(0);
                if (myStatus.getTextContent().equals(statusType)) {
                    element.getParentNode().removeChild(element);
                    i--;
                }
            }
        }
    }

    private void processUpdateOnImport(NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element anime = (Element) nodeList.item(i);
            NodeList updateOnImportList = anime.getElementsByTagName("update_on_import");
            if (updateOnImportList.getLength() > 0) {
                Element updateOnImport = (Element) updateOnImportList.item(0);
                updateOnImport.setTextContent("1");
            }
        }
    }

    private void writeDocument(Document doc, String filePath) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}

