package engine.managers;

import dto.models.ChatEntryDTO;
import engine.models.chat.ChatEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SDMChatManager implements ChatManager {

    private static SDMChatManager instance;
    private static final Object CREATION_CONTEXT_LOCK = new Object();
    private final List<ChatEntry> chatEntries = new ArrayList<>();

    private SDMChatManager() {
    }

    public static ChatManager getInstance() {
        if (instance == null) {
            synchronized (CREATION_CONTEXT_LOCK) {
                if (instance == null) {
                    instance = new SDMChatManager();
                }
            }
        }

        return instance;
    }

    @Override
    public void addNewChatEntry(String username, String message) {
        ChatEntry newChatEntry = new ChatEntry(username, message);
        chatEntries.add(newChatEntry);
    }

    @Override
    public Collection<ChatEntryDTO> getChatEntriesByVersion(int version) {
        return Collections.unmodifiableCollection(chatEntries.stream()
                .map(ChatEntry::toDTO)
                .skip(version)
                .collect(Collectors.toList()));
    }

    @Override
    public int getChatEntriesVersion() {
        return chatEntries.size();
    }

    @Override
    public String toString() {
        return "SDMChatManager{" +
                "chatEntries=" + chatEntries +
                '}';
    }
}
