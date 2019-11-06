package myApp.model;

import org.apache.commons.io.FilenameUtils;

import javax.activation.MimetypesFileTypeMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class DataPacket {
    static {
        try {
            Class.forName("org.h2.Driver");
            String url = "jdbc:h2:tcp://localhost/~/test";
            connection = DriverManager.getConnection(url, "sa", "sa");
        }
        catch (Exception ex) {
            throw new Error();
        }
    }

    private static Connection connection;
    private static final int BUFFSIZE = 10;

    public Connection getConnection() { return connection; }

    //метод проверяет все файлы в заданной папке и загружает их в бд, если их тип csv
    public static void readFiles() throws Exception {
        final File folder = new File(Configuration.getInputDirectory());

        for (final File file : Objects.requireNonNull(folder.listFiles())) {
            if (FilenameUtils.getExtension(file.getName()).equals("csv")) {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while (sendPacket(bufferedReader)) {
                    System.out.println("package read!");//need log!
                }
            } else {
                System.out.println("invalid file!!");//NEED LOG !!
            }
        }
    }

    //читывает из файлового дескриптора BUFFSIZE строк, создает sql запрос на insert и выполняет его
    private static boolean sendPacket(BufferedReader fileReader) throws Exception {
        Statement stmt;//запрос
        String buffer;
        stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);//создаю запрос
        connection.setAutoCommit(false);//вырубаю автокоммит
        //stmt.addBatch("INSERT INTO TEST.T_DICTIONARY(ID, NAME, VALUE) VALUES('?', '?', '?')");
        for (int i = 0; i < BUFFSIZE && (buffer = fileReader.readLine()) != null; i++) {
            System.out.println(buffer);
        }
        stmt.executeBatch();
        connection.commit();
        stmt.close();
        return false;
    }
}
