package gui;

import processor.XMLProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExportManagerGUI extends JFrame implements ActionListener {
    private JTextField filePathTextField;
    private JComboBox<String> listTypeComboBox, statusTypeComboBox, importableComboBox;
    private JButton startButton;
    private final XMLProcessor xmlProcessor;

    public ExportManagerGUI() {
        setTitle("Anime/Manga List Processing");
        setSize(650, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        xmlProcessor = new XMLProcessor();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 2, 10, 10));
        initializeComponents(mainPanel);
        add(mainPanel);
        setVisible(true);
    }

    private void initializeComponents(JPanel mainPanel) {
        JLabel listTypeLabel = new JLabel("Choose the list type (anime/manga):");
        mainPanel.add(listTypeLabel);

        listTypeComboBox = new JComboBox<>();
        listTypeComboBox.addItem("anime");
        listTypeComboBox.addItem("manga");
        mainPanel.add(listTypeComboBox);

        JLabel filePathLabel = new JLabel("Enter the file path:");
        mainPanel.add(filePathLabel);

        filePathTextField = new JTextField();
        mainPanel.add(filePathTextField);

        JLabel statusTypeLabel = new JLabel("Choose the show status type to be removed:");
        mainPanel.add(statusTypeLabel);

        statusTypeComboBox = new JComboBox<>();
        statusTypeComboBox.addItem("None");
        statusTypeComboBox.addItem("Watching");
        statusTypeComboBox.addItem("Completed");
        statusTypeComboBox.addItem("On-Hold");
        statusTypeComboBox.addItem("Dropped");
        statusTypeComboBox.addItem("Plan to Watch");
        mainPanel.add(statusTypeComboBox);

        JLabel importableLabel = new JLabel("Make all entries importable?");
        mainPanel.add(importableLabel);

        importableComboBox = new JComboBox<>();
        importableComboBox.addItem("No");
        importableComboBox.addItem("Yes");
        mainPanel.add(importableComboBox);

        startButton = new JButton("Start");
        startButton.addActionListener(this);
        mainPanel.add(startButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            String listType = listTypeComboBox.getSelectedItem().toString();
            String filePath = filePathTextField.getText();
            String statusType = statusTypeComboBox.getSelectedItem().toString();
            String importable = importableComboBox.getSelectedItem().toString();

            if (!"None".equals(statusType)) xmlProcessor.removeStatus(listType, filePath, statusType);
            if ("Yes".equals(importable)) xmlProcessor.updateOnImport(listType, filePath);

            JOptionPane.showMessageDialog(this, "Process successfully finished!");
        }
    }
}

