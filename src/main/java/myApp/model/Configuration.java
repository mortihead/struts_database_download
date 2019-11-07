package myApp.model;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

//read config.xml from root directory and save params
class Configuration {
    static {
        File file = new File("config.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            inputDirectory = doc.getElementsByTagName("input_folder").item(0).getTextContent();
            outputDirectory = doc.getElementsByTagName("output_folder").item(0).getTextContent();
            userName = doc.getElementsByTagName("username").item(0).getTextContent();
            password = doc.getElementsByTagName("password").item(0).getTextContent();
            url = doc.getElementsByTagName("url").item(0).getTextContent();
        } catch (Exception ex) {
            throw new Error();
        }
    }

    private static final String inputDirectory;
    private static final String outputDirectory;
    private static final String userName;
    private static final String password;
    private static final String url;

    static String getInputDirectory() { return inputDirectory; }

    static String getOutputDirectory() { return outputDirectory; }

    static String getUserName() { return userName; }

    static String getPassword() { return password; }

    static String getUrl() { return url; }
}
