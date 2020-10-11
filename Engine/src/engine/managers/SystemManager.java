package engine.managers;

import dto.models.RegionDTO;

import javax.xml.bind.JAXBException;
import java.util.Collection;

public interface SystemManager {

    void loadNewRegionDataFromFile(String ownerUsername, String filePath) throws JAXBException;

    Collection<RegionDTO> getAllRegions();

    RegionManager getRegionManagerByRegionName(String regionName);
}
