package engine.managers;

import dto.models.RegionDTO;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.Collection;

public interface RegionsManager {

    void loadNewRegionDataFromFile(String ownerUsername, InputStream fileInputStream) throws JAXBException;

    Collection<RegionDTO> getAllRegions();

    SingleRegionManager getSingleRegionManagerByRegionName(String regionName);
}
