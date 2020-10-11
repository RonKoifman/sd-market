package engine.file;

import engine.file.jaxb.schema.generated.SuperDuperMarketDescriptor;
import engine.models.item.RegionItem;
import engine.models.store.Store;

import javax.xml.bind.JAXBException;
import java.util.Map;

public class FileManager {

    private final XmlDeserializer xmlDeserializer = new XmlDeserializer();
    private final DataConverter dataConverter = new DataConverter();

    public void loadRegionDataFromFile(String filePath) throws JAXBException {
        SuperDuperMarketDescriptor sdmDescriptor = xmlDeserializer.deserializeFile(filePath);
        dataConverter.convertJaxbObjectsToSystemModels(sdmDescriptor);
    }

    public Map<Integer, RegionItem> getLoadedItemIdToItem() {
        return dataConverter.getConvertedItemIdToItem();
    }

    public Map<Integer, Store> getLoadedStoreIdToStore() {
        return dataConverter.getConvertedStoreIdToStore();
    }

    public String getLoadedRegionName() {
        return dataConverter.getConvertedRegionName();
    }
}
