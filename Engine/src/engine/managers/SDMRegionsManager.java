package engine.managers;

import dto.models.RegionDTO;
import engine.file.FileManager;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SDMRegionsManager implements RegionsManager {

    private static SDMRegionsManager instance;
    private static final Object CREATION_CONTEXT_LOCK = new Object();
    private final FileManager fileManager = new FileManager();
    private final Map<String, SDMSingleRegionManager> regionNameToRegionManager = new HashMap<>();

    private SDMRegionsManager() {
    }

    public static RegionsManager getInstance() {
        if (instance == null) {
            synchronized (CREATION_CONTEXT_LOCK) {
                if (instance == null) {
                    instance = new SDMRegionsManager();
                }
            }
        }

        return instance;
    }

    @Override
    public void loadNewRegionDataFromFile(String ownerUsername, InputStream fileInputStream) throws JAXBException {
        fileManager.loadRegionDataFromFile(fileInputStream);
        checkForUniqueRegionName(fileManager.getLoadedRegionName());
        SDMSingleRegionManager newRegionManager = new SDMSingleRegionManager(fileManager.getLoadedRegionName(), ownerUsername, fileManager.getLoadedStoreIdToStore(), fileManager.getLoadedItemIdToItem());
        regionNameToRegionManager.put(newRegionManager.getRegionName(), newRegionManager);
    }

    @Override
    public Collection<RegionDTO> getAllRegions() {
        return Collections.unmodifiableCollection(regionNameToRegionManager.values()
                .stream()
                .map(SingleRegionManager::toDTO)
                .collect(Collectors.toSet()));
    }

    @Override
    public SingleRegionManager getSingleRegionManagerByRegionName(String regionName) {
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
