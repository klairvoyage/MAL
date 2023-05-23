# MyAnimeList XML Configuration Tool

This Java application allows you to configure an XML file exported from MyAnimeList. You can remove entries based on specific brackets (e.g., Watching, Completed, On-Hold, Dropped, Plan to Watch) and update the importability status of the entries. The tool utilizes the Java Swing library to provide a graphical user interface (GUI) for easy interaction.

## Prerequisites

- Java Development Kit (JDK) installed on your system.

## How to Use

1. Clone or download the source code files to your local machine.
2. Open the command prompt or terminal and navigate to the project directory.
3. Compile the Java source code by executing the following command:

```bash
javac Main.java
```

4. Run the application using the following command:

```shell
java Main
```

5. The GUI window will appear with the following input options:

- **List Type**: Select the type of list (1 for Anime, 2 for Manga) you want to configure.
- **File Path**: Enter the path of the XML file exported from MyAnimeList.
- **Bracket Type**: Choose the bracket (W for Watching, C for Completed, O for On-Hold, D for Dropped, P for Plan to Watch, N for none) that you want to remove from the list.
- **Make all entries importable**: Specify if you want to update the importability status of all entries (1/0 for Yes/No).

6. After providing the required inputs, click the **Start** button to initiate the configuration process.
7. The application will perform the requested operations on the XML file.
8. Once the process is complete, a message dialog will be displayed to indicate the successful operation.
9. You can repeat the process by entering new values or close the application.

## XML File Modification

The tool modifies the XML file based on the specified options. Here's how the modifications are applied:

- **Removing Entries**: The tool iterates through the XML file and removes the entries that match the specified bracket type. The resulting file will no longer contain those entries.
- **Updating Importability Status**: If the option to update importability status is enabled, the tool sets the importability status of all entries to "1" (importable) in the XML file.

**Note**: It is recommended to make a backup of your original XML file before using this tool to avoid any data loss.

## Dependencies

The application uses the following Java libraries:

- `javax.xml.parsers` package for parsing and manipulating XML files.
- `javax.xml.transform` package for transforming the modified XML and saving it back to the file system.
- `org.w3c.dom` package for working with the XML Document Object Model.

## Known Limitations

- The tool assumes that the XML file follows the structure of the export format from MyAnimeList. Unexpected XML structures may result in errors or incorrect behavior.
- The GUI interface is basic and may not include advanced features like error handling or progress indicators.

## Contributions

Contributions to this project are welcome! If you have any suggestions, bug reports, or feature requests, please open an issue or submit a pull request.

## License

This project is licensed under the [GNU General Public License](LICENSE). Feel free to use, modify, and distribute the code for personal and commercial purposes.

---
> This [README](README.md) was mainly written by ChatGPT! :)
