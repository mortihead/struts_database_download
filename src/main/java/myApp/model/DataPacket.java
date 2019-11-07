package myApp.model;

import org.apache.commons.io.FilenameUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.Objects;

public class DataPacket {
    static {
        try {
            Class.forName("org.h2.Driver");
            connectionDb = DriverManager.getConnection(Configuration.getUrl(),
                    Configuration.getUserName(), Configuration.getPassword());
        }
        catch (Exception ex) {
            throw new Error();
        }
    }

    private static Connection connectionDb;
    private static final int buffSize = 10;

    public static Connection getConnectionDb() { return connectionDb; }

    //метод проверяет все файлы в заданной папке и загружает их в бд, если их тип csv
    public static boolean readFiles() throws Exception {
        final File folder = new File(Configuration.getInputDirectory());
        boolean f = false;
        for (final File file : Objects.requireNonNull(folder.listFiles())) {
            if (FilenameUtils.getExtension(file.getName()).equals("csv")) {
                f = true;
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                bufferedReader.readLine();
                while (sendPacket(bufferedReader)) {
                    System.out.println("package read!");//need log!
                }
                bufferedReader.close();
                Files.move(Paths.get(Configuration.getInputDirectory() + "/" + file.getName()),
                           Paths.get(Configuration.getOutputDirectory() + "/" + file.getName()),
                           StandardCopyOption.REPLACE_EXISTING);
            } else {
                System.out.println("invalid file!!");//NEED LOG !!
            }
        }
        return f;
    }

    //читывает из файлового дескриптора BUFFSIZE строк, создает sql запрос на insert и выполняет его
    private static boolean sendPacket(BufferedReader fileReader) throws Exception {
        Statement stmt;
        String buffer;
        int i = 0;
        stmt = connectionDb.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        connectionDb.setAutoCommit(false);
        while (i++ < buffSize && (buffer = fileReader.readLine()) != null) {
            String[] line = buffer.split(",");
            stmt.addBatch("INSERT INTO TEST.T_DICTIONARY(ID, NAME, VALUE) VALUES(\'"
                    + line[0] + "\', \'" + line[1] + "\', " + line[2] + ")");
        }
        stmt.executeBatch();
        connectionDb.commit();
        stmt.close();
        return i == buffSize;
    }
}
