package processor;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLProcessor {
    private Document parseDocument(String filePath) {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(filePath));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeStatus(String listType, String filePath, String statusType) {
        Document doc = parseDocument(filePath);
        if (doc != null) {
            processRemoveStatus(doc.getElementsByTagName(listType), statusType);
            writeDocument(doc, filePath);
        }
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

    public void updateOnImport(String listType, String filePath, String bool) {
        Document doc = parseDocument(filePath);
        if (doc != null) {
            processUpdateOnImport(doc.getElementsByTagName(listType), bool);
            writeDocument(doc, filePath);
        }
    }

    private void processUpdateOnImport(NodeList nodeList, String bool) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element anime = (Element) nodeList.item(i);
            NodeList updateOnImportList = anime.getElementsByTagName("update_on_import");
            if (updateOnImportList.getLength() > 0) {
                Element updateOnImport = (Element) updateOnImportList.item(0);
                updateOnImport.setTextContent(bool);
            }
        }
    }

    private void writeDocument(Document doc, String filePath) {
        try {
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(new File(filePath)));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
