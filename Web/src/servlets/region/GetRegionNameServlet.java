package servlets.region;

import com.google.gson.Gson;
import dto.models.UserDTO;
import engine.managers.SDMUsersManager;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GetRegionNameServlet", urlPatterns = {"/region-name"})
public class GetRegionNameServlet extends HttpServlet {

    @Override
    protected synchronized void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("text/html");
            String regionName = SessionUtils.getRegionName(req);
            out.print(regionName);
            out.flush();
        }
    }
}
