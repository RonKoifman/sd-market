package servlets.chat;

import com.google.gson.Gson;
import dto.models.ChatEntryDTO;
import engine.managers.SDMChatManager;
import utils.SessionUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet(name = "GetUnseenChatEntriesServlet", urlPatterns = {"/chat"})
public class GetUnseenChatEntriesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        processRequest(req, res);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter out = res.getWriter()) {
            res.setContentType("application/json");
            Collection<ChatEntryDTO> unseenChatEntries;
            int serverChatVersion;
            int clientChatVersion = SessionUtils.getChatVersion(req);

            synchronized (getServletContext()) {
                serverChatVersion = SDMChatManager.getInstance().getChatEntriesVersion();
                unseenChatEntries = SDMChatManager.getInstance().getChatEntriesByVersion(clientChatVersion);
            }

            if (serverChatVersion != clientChatVersion) {
                SessionUtils.setChatVersion(req, serverChatVersion);
                Gson gson = new Gson();
                String json = gson.toJson(unseenChatEntries);
                out.print(json);
            }

            out.flush();
        }
    }
}
