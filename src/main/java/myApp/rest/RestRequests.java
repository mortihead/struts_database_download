package myApp.rest;

import myApp.model.DataPacket;
import myApp.model.DictionaryEntity;
import myApp.utils.HibernateUtil;
import org.hibernate.Session;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

//class with select and delete query
public class RestRequests {
    private static boolean checkTableNotReal(String tableName) throws SQLException {
        Connection connectionDb = DataPacket.getConnectionDb();
        Statement stmt = connectionDb.createStatement();
        ResultSet names = stmt.executeQuery("SELECT TABLE_SCHEMA, TABLE_NAME " +
                "FROM INFORMATION_SCHEMA.TABLES where TABLE_TYPE = 'TABLE'");
        while (names.next()) {
            if (String.format("%s.%s", names.getString("TABLE_SCHEMA"),
                    names.getString("TABLE_NAME")).equals(tableName)) {
                stmt.close();
                return false;
            }
        }
        return true;
    }

    public static List getTableData(String tableName) throws Exception {
        if (checkTableNotReal(tableName))
            throw new Exception(String.format("table %s is not exist!", tableName));
        final Session session = HibernateUtil.getHibernateSession();
        session.beginTransaction();
        List data = session.createSQLQuery(String.format("select * from %s", tableName))
                .addEntity(DictionaryEntity.class)
                .list();
        session.close();
        return data;
    }

    public static void clearTable(String tableName) throws Exception {
        if (checkTableNotReal(tableName))
            throw new Exception(String.format("table %s is not exist!", tableName));
        Connection connectionDb = DataPacket.getConnectionDb();
        Statement stmt = connectionDb.createStatement();
        stmt.execute(String.format("truncate table %s", tableName));
        stmt.close();
    }
}
