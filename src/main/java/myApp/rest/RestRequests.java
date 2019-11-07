package myApp.rest;

import myApp.model.DataPacket;
import myApp.model.DictionaryEntity;
import myApp.utils.HibernateUtil;
import org.hibernate.Session;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class RestRequests {
    public static List getTableData(String tableName) throws IOException {
        final Session session = HibernateUtil.getHibernateSession();
        session.beginTransaction();
        List data = session.createSQLQuery("select * from " + tableName)
                .addEntity(DictionaryEntity.class)
                .list();
        session.close();
        return data;
    }

    public static void clearTable(String tableName) throws SQLException {
        Connection connectionDb = DataPacket.getConnectionDb();
        Statement stmt = connectionDb.createStatement();
        stmt.execute("delete from " + tableName + " where true");
        stmt.close();
    }
}
