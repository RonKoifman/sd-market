package engine.managers;

import dto.models.RegionDTO;
import engine.file.FileManager;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SDMSystemManager implements SystemManager {

    private static SDMSystemManager instance;
    private static final Object CREATION_CONTEXT_LOCK = new Object();
    private final FileManager fileManager = new FileManager();
    private final Map<String, SDMRegionManager> regionNameToRegionManager = new HashMap<>();

    private SDMSystemManager() {
    }

    public static SystemManager getInstance() {
        if (instance == null) {
            synchronized (CREATION_CONTEXT_LOCK) {
                if (instance == null) {
                    instance = new SDMSystemManager();
                }
            }
        }

        return instance;
    }

    @Override
    public synchronized void loadNewRegionDataFromFile(String ownerUsername, InputStream fileInputStream) throws JAXBException {
        fileManager.loadRegionDataFromFile(fileInputStream);
        checkForUniqueRegionName(fileManager.getLoadedRegionName());
        SDMRegionManager newRegionManager = new SDMRegionManager(fileManager.getLoadedRegionName(), ownerUsername, fileManager.getLoadedStoreIdToStore(), fileManager.getLoadedItemIdToItem());
        regionNameToRegionManager.put(newRegionManager.getRegionDTO().getName(), newRegionManager);
    }

    @Override
    public synchronized Collection<RegionDTO> getAllRegions() {
        return regionNameToRegionManager.values()
                .stream()
                .map(RegionManager::getRegionDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public synchronized RegionManager getRegionManagerByRegionName(String regionName) {
        return regionNameToRegionManager.get(regionName);
    }

    private void checkForUniqueRegionName(String newRegionName) {
        boolean isNewRegionNameExists = regionNameToRegionManager.keySet()
                .stream()
                .anyMatch(regionName -> regionName.toLowerCase().equals(newRegionName.toLowerCase()));

        if (isNewRegionNameExists) {
            throw new IllegalStateException("The region '" + newRegionName + "' is already exists.");
        }
    }
}
