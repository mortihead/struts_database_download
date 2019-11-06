package myApp.model;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

//считывает xml файл из корня проекта и сохраняет в поля параметры конфига
public class Configuration {
    static {
        File file = new File("config.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            inputDirectory = doc.getElementsByTagName("input_folder").item(0).getTextContent();
            outputDirectory = doc.getElementsByTagName("output_folder").item(0).getTextContent();
        } catch (Exception ex) {
            throw new Error();
        }
    }

    private static final String inputDirectory;
    private static final String outputDirectory;

    public static String getInputDirectory() { return inputDirectory; }

    public static String getOutputDirectory() { return outputDirectory; }
}
