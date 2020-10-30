package servlets.feedbacks;

import com.google.gson.Gson;
import dto.models.FeedbackDTO;
import engine.managers.SDMUsersManager;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet(name = "GetStoreOwnerStoresFeedbacksServlet", urlPatterns = {"/owner-stores-feedbacks"})
public class GetStoreOwnerStoresFeedbacksServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("application/json");
            String username = SessionUtils.getUsername(req);
            String regionName = SessionUtils.getRegionName(req);
            Collection<FeedbackDTO> storesFeedbacks;

            synchronized (getServletContext()) {
                storesFeedbacks = SDMUsersManager.getInstance().getStoreOwnerOwnedStoresFeedbacksByRegionName(username, regionName);
            }

            Gson gson = new Gson();
            String json = gson.toJson(storesFeedbacks);
            out.print(json);
            out.flush();
        }
    }
}
