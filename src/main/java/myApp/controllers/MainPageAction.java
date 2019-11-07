package myApp.controllers;

import com.google.gson.Gson;
import myApp.rest.RestRequests;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class MainPageAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        List data = RestRequests.getTableData("T_DICTIONARY");
        String jsonStr = new Gson().toJson(data);
        return mapping.findForward("main");
    }
}
