package engine.managers;

import dto.models.ChatEntryDTO;

import java.util.Collection;

public interface ChatManager {

    void addNewChatEntry(String username, String message);

    Collection<ChatEntryDTO> getChatEntriesByVersion(int version);

    int getChatEntriesVersion();
}
