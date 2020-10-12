package engine.file;

import engine.file.jaxb.schema.generated.SuperDuperMarketDescriptor;
import engine.models.item.RegionItem;
import engine.models.store.Store;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.Map;

public class FileManager {

    private final XmlDeserializer xmlDeserializer = new XmlDeserializer();
    private final DataConverter dataConverter = new DataConverter();

    public void loadRegionDataFromFile(InputStream fileInputStream) throws JAXBException {
        SuperDuperMarketDescriptor sdmDescriptor = xmlDeserializer.deserializeFile(fileInputStream);
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
