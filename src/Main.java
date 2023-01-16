import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main {
    public static void main(String[] args) { //ACHTUNG: changes are made on the very same file
        Scanner scanner = new Scanner(System.in);

        int yo;
        do {
            System.out.println("Enter the list type (1 for anime, 2 for manga):");
            yo = scanner.nextInt();
        } while (yo!=1 && yo!=2);
        String listType;
        if (yo==1) listType = "anime";
        else listType = "manga";
        scanner.nextLine();
        System.out.println("Enter the file path:");
        String filePath = scanner.nextLine();

        String bracketType;
        do {
            System.out.println("Enter what bracket of the list you want to remove (W for Watching, C for Completed, " +
                    "O for On-Hold, D for Dropped, P for Plan to Watch, N for none):");
            bracketType = scanner.nextLine();
        } while (bracketType.charAt(0)!='W' && bracketType.charAt(0)!='C' && bracketType.charAt(0)!='O'
                && bracketType.charAt(0)!='D' && bracketType.charAt(0)!='P' && bracketType.charAt(0)!='N');

        int importable;
        do {
            System.out.println("Enter if you want to make all the entries of the new file importable (1/0 for Y/N):");
            importable = scanner.nextInt();
        } while (importable != 1 && importable!=0);

        switch (bracketType.charAt(0)) {
                case 'W' -> bracketType = "Watching";
                case 'C' -> bracketType = "Completed";
                case 'O' -> bracketType = "On-Hold";
                case 'D' -> bracketType = "Dropped";
                case 'P' -> bracketType = "Plan to Watch";
        }
        if (bracketType.charAt(0)!='N') removeBracket(listType, filePath, bracketType);
        if (importable==1) updateOnImport(listType, filePath);
        System.out.println("Process successfully finished!");
    }

    public static void removeBracket(String listType, String filePath, String bracketType) {
        try {
            File file = new File(filePath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName(listType);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                NodeList myStatusList = element.getElementsByTagName("my_status");
                if(myStatusList.getLength() > 0) {
                    Element myStatus = (Element) myStatusList.item(0);
                    if(myStatus.getTextContent().equals(bracketType)) {
                        element.getParentNode().removeChild(element);
                        i--;
                    } else {
                        System.out.println(element.getTextContent());
                    }
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void updateOnImport(String listType, String filePath) {
        try {
            File file = new File(filePath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName(listType);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element anime = (Element) nodeList.item(i);
                NodeList updateOnImportList = anime.getElementsByTagName("update_on_import");
                if(updateOnImportList.getLength() > 0) {
                    Element updateOnImport = (Element) updateOnImportList.item(0);
                    updateOnImport.setTextContent("1");
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace();
        }
    }
}