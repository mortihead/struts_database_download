package myApp.controllers;

import myApp.model.DictionaryEntity;
import myApp.utils.HibernateUtil;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class MainPageAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
    {
        final Session session = HibernateUtil.getHibernateSession();
        session.beginTransaction();
        List tt = session.createSQLQuery("select * from TEST.T_DICTIONARY limit 1")
                .addEntity(DictionaryEntity.class)
                .list();
        System.out.println(tt);
        session.close();
        return mapping.findForward("main");
    }
}
