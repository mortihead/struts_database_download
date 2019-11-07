package myApp.rest;

import myApp.model.DictionaryEntity;
import myApp.utils.HibernateUtil;
import org.hibernate.Session;
import java.io.IOException;
import java.util.List;

public class RestRequests {
    public static List getTableData(String tableName) throws IOException {
        final Session session = HibernateUtil.getHibernateSession();
        session.beginTransaction();
        List data = session.createSQLQuery("select * from TEST." + tableName)
                .addEntity(DictionaryEntity.class)
                .list();
        session.close();
        return data;
    }
}
