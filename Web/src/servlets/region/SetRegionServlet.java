package servlets.region;

import utils.Constants;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SetRegionServlet", urlPatterns = {"/set-region"})
public class SetRegionServlet extends HttpServlet {

    @Override
    protected synchronized void doGet(HttpServletRequest req, HttpServletResponse res) {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) {
        String regionNameFromParameter = req.getParameter(Constants.REGION_NAME);
        SessionUtils.setRegionName(req, regionNameFromParameter);
    }
}
