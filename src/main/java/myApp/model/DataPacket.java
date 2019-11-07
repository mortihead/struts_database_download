package myApp.model;

import myApp.controllers.MainPageAction;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(DataPacket.class);

    public static Connection getConnectionDb() { return connectionDb; }

    //download all .csv files from input directory to database
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
                    logger.debug("to next package!");
                }
                logger.debug("file read!");
                bufferedReader.close();
                Files.move(Paths.get(Configuration.getInputDirectory() + "/" + file.getName()),
                           Paths.get(Configuration.getOutputDirectory() + "/" + file.getName()),
                           StandardCopyOption.REPLACE_EXISTING);
            } else {
                logger.debug("Invalid file " + file.getName());
            }
        }
        return f;
    }

    //read BUFFSIZE lines from FD, create insert sql query and executes him
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
