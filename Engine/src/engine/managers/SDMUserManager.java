package engine.managers;

import engine.api.SystemManager;
import engine.api.UserManager;

public class SDMUserManager implements UserManager {

    private static SDMUserManager instance;
    private static final Object CREATION_CONTEXT_LOCK = new Object();

    private SDMUserManager() {
    }

    public static UserManager getInstance() {
        if (instance == null) {
            synchronized (CREATION_CONTEXT_LOCK) {
                if (instance == null) {
                    instance = new SDMUserManager();
                }
            }
        }

        return instance;
    }
}
