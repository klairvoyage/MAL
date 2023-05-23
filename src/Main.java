import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
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

public class Main extends JFrame implements ActionListener {
    private JLabel listTypeLabel, filePathLabel, bracketTypeLabel, importableLabel;
    private JTextField filePathTextField;
    private JComboBox<String> listTypeComboBox, bracketTypeComboBox, importableComboBox;
    private JButton startButton;
    public Main() {
        setTitle("Anime/Manga List Processing");
        setSize(650, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 2, 10, 10));

        listTypeLabel = new JLabel("Choose the list type:");
        mainPanel.add(listTypeLabel);

        listTypeComboBox = new JComboBox<>();
        listTypeComboBox.addItem("anime");
        listTypeComboBox.addItem("manga");
        mainPanel.add(listTypeComboBox);

        filePathLabel = new JLabel("Enter the file path:");
        mainPanel.add(filePathLabel);

        filePathTextField = new JTextField();
        mainPanel.add(filePathTextField);

        bracketTypeLabel = new JLabel("Choose the bracket type to be removed:");
        mainPanel.add(bracketTypeLabel);

        bracketTypeComboBox = new JComboBox<>();
        bracketTypeComboBox.addItem("Watching");
        bracketTypeComboBox.addItem("Completed");
        bracketTypeComboBox.addItem("On-Hold");
        bracketTypeComboBox.addItem("Dropped");
        bracketTypeComboBox.addItem("Plan to Watch");
        bracketTypeComboBox.addItem("None");
        mainPanel.add(bracketTypeComboBox);

        importableLabel = new JLabel("Make all entries importable?");
        mainPanel.add(importableLabel);

        importableComboBox = new JComboBox<>();
        importableComboBox.addItem("No");
        importableComboBox.addItem("Yes");
        mainPanel.add(importableComboBox);

        startButton = new JButton("Start");
        startButton.addActionListener(this);
        mainPanel.add(startButton);

        add(mainPanel);
        setVisible(true);
    }
    public static void main(String[] args) {
        new Main();
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            String listType = listTypeComboBox.getSelectedItem().toString();
            String filePath = filePathTextField.getText();
            String bracketType = bracketTypeComboBox.getSelectedItem().toString();
            String importable = importableComboBox.getSelectedItem().toString();

            if (bracketType.charAt(0)!='N') removeBracket(listType, filePath, bracketType);
            if (importable.compareTo("Yes")==0) updateOnImport(listType, filePath);

            JOptionPane.showMessageDialog(this, "Process successfully finished!");

            //listTypeComboBox.setSelectedIndex(0);
            //filePathTextField.setText("");
            //bracketTypeComboBox.setSelectedIndex(0);
            //importableComboBox.setSelectedIndex(0);
        }
    }
}