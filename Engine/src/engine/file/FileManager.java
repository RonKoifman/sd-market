package engine.file;

import engine.file.jaxb.schema.generated.SuperDuperMarketDescriptor;
import engine.interfaces.Locationable;
import engine.models.item.MarketItem;
import engine.models.location.Location;
import engine.models.store.Store;

import javax.xml.bind.JAXBException;
import java.util.Map;

public class FileManager {

    private boolean isFileLoaded;
    private final XmlDeserializer xmlDeserializer = new XmlDeserializer();
    private final DataConverter dataConverter = new DataConverter();

    public boolean isFileLoaded() {
        return isFileLoaded;
    }

    public void loadSystemDataFromFile(String filePath) throws JAXBException {
        SuperDuperMarketDescriptor sdmDescriptor = xmlDeserializer.deserializeFile(filePath);
        dataConverter.convertJaxbObjectsToSystemModels(sdmDescriptor);
        isFileLoaded = true;
    }

    public Map<Integer, MarketItem> getConvertedItemIdToItem() {
        return dataConverter.getConvertedItemIdToItem();
    }

    public Map<Integer, Store> getConvertedStoreIdToStore() {
        return dataConverter.getConvertedStoreIdToStore();
    }

    public Map<Location, Locationable> getConvertedLocationToLocationable() {
        return dataConverter.getConvertedLocationToLocationable();
    }
}
