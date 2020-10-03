package engine.models;

import engine.enums.DiscountOfferType;
import engine.enums.PurchaseForm;
import engine.exceptions.IdenticalLocationsException;
import engine.exceptions.LocationOutOfRangeException;
import engine.interfaces.Locationable;
import engine.jaxb.schema.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

class FileHandler {

    private boolean isFileLoaded;
    private Map<Integer, Store> tempStoreIdToStore;
    private Map<Integer, MarketItem> tempItemIdToItem;
    private Map<Location, Locationable> tempLocationToLocationable;

    public Map<Integer, Store> getTempStoreIdToStore() {
        return tempStoreIdToStore;
    }

    public Map<Integer, MarketItem> getTempItemIdToItem() {
        return tempItemIdToItem;
    }

    public Map<Location, Locationable> getTempLocationToLocationable() {
        return tempLocationToLocationable;
    }

    public boolean isFileLoaded() {
        return isFileLoaded;
    }

    public void saveOrdersHistoryDataToFile(String filePath, Map<Integer, GeneralOrder> orderIdToOrder) throws IOException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(String.valueOf(filePath)))) {
            out.writeObject(orderIdToOrder);
            out.flush();
        }
    }

    public Map<Integer, GeneralOrder> loadSavedOrdersHistoryDataFromFile(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(String.valueOf(filePath)))) {

            return (Map<Integer, GeneralOrder>)in.readObject();
        }
    }

    public void loadSystemDataFromFile(String filePath) throws JAXBException {
        if (!filePath.endsWith(".xml")) {
            throw new IllegalArgumentException("XML file type must end with the suffix '.xml'.");
        }

        SuperDuperMarketDescriptor sdmDescriptor = unmarshalFile(filePath);
        setTempItems(sdmDescriptor.getSDMItems().getSDMItem());
        setTempStores(sdmDescriptor.getSDMStores().getSDMStore());
        setTempLocationables(new HashSet<>(tempStoreIdToStore.values()));
        isFileLoaded = true;
    }

    private SuperDuperMarketDescriptor unmarshalFile(String filePath) throws JAXBException {
        File loadedFile = new File(filePath);
        JAXBContext jaxbContext = JAXBContext.newInstance(SuperDuperMarketDescriptor.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        return (SuperDuperMarketDescriptor)jaxbUnmarshaller.unmarshal(loadedFile);
    }

    private void setTempLocationables(Collection<Locationable> locationables) {
        tempLocationToLocationable = new HashMap<>();

        for (Locationable locationable : locationables) {
            if (tempLocationToLocationable.containsKey(locationable.getLocation())) {
                throw new IdenticalLocationsException("The location (" + locationable.getLocation().x + ", " +
                        locationable.getLocation().y + ") exists more than once in the market.");
            }

            tempLocationToLocationable.put(locationable.getLocation(), locationable);
        }
    }

    private void checkEachItemSoldByAtLeastOneStore(Collection<SDMStore> generatedStores) {
        Collection<Integer> storesItemsId = generatedStores.stream()
                .map(SDMStore::getSDMPrices)
                .map(SDMPrices::getSDMSell)
                .flatMap(List::stream)
                .map(SDMSell::getItemId)
                .collect(Collectors.toSet());

        for (MarketItem item : tempItemIdToItem.values()) {
            if (!storesItemsId.contains(item.getId())) {
                throw new IllegalStateException("An item with the id '" + item.getId() + "' is not sold by any store in the market.");
            }
        }
    }

    private void checkForExistingItemsInStoresSells(Collection<SDMStore> stores) {
        for (SDMStore store : stores) {
            for (SDMSell sell : store.getSDMPrices().getSDMSell()) {
                if (!tempItemIdToItem.containsKey(sell.getItemId())) {
                    throw new IllegalStateException("An item with the id '" + sell.getItemId() + "', which sold by '" +
                            store.getName() + "' store, does not exist in the market.");
                }
            }
        }
    }

    private void setStoreItemsPrices(Store store, Collection<SDMSell> itemsPrices) {
        for (SDMSell sell : itemsPrices) {
            if (store.isItemSold(sell.getItemId())) {
                throw new IllegalStateException("An item with the id '" + sell.getItemId() + "' exists more than once in '" +
                        store.getName() + "' store.");
            }

            MarketItem itemToAdd = tempItemIdToItem.get(sell.getItemId());
            store.addNewItem(new StoreItem(itemToAdd.getId(), itemToAdd.getName(), itemToAdd.getPurchaseForm(), store, sell.getPrice()));
        }
    }

    private void setStoreDiscounts(Store store, SDMDiscounts sdmDiscounts) {
        if (sdmDiscounts != null) {
            for (SDMDiscount discount : sdmDiscounts.getSDMDiscount()) {
                if (!store.isItemSold(discount.getIfYouBuy().getItemId())) {
                    throw new IllegalStateException("A discount offer '" + discount.getName() + "' contains an item with the id '" +
                            discount.getIfYouBuy().getItemId() + "' which is not sold by '" + store.getName() + "' store.");
                }

                for (SDMOffer offer : discount.getThenYouGet().getSDMOffer()) {
                    if (!store.isItemSold(offer.getItemId())) {
                        throw new IllegalStateException("A discount offer '" + discount.getName() + "' contains an item with the id '" +
                                offer.getItemId() + "', which is not sold by '" + store.getName() + "' store.");
                    }
                }

                DiscountInformation newDiscount = new DiscountInformation(discount.getName().trim(),
                        new DiscountTrigger(store.getItemById(discount.getIfYouBuy().getItemId()), (float)discount.getIfYouBuy().getQuantity()),
                        DiscountOfferType.valueOf(String.join("_", discount.getThenYouGet().getOperator().split("-"))));

                discount.getThenYouGet().getSDMOffer().forEach(sdmOffer -> newDiscount.addNewOffer(
                        new DiscountOffer(store.getItemById(sdmOffer.getItemId()), (float)sdmOffer.getQuantity(), sdmOffer.getForAdditional())));

                store.addNewDiscount(newDiscount);
            }
        }
    }

    private void setTempStores(Collection<SDMStore> generatedStores) {
        tempStoreIdToStore = new HashMap<>();

        checkForExistingItemsInStoresSells(generatedStores);
        checkEachItemSoldByAtLeastOneStore(generatedStores);

        for (SDMStore sdmStore : generatedStores) {
            if (tempStoreIdToStore.containsKey(sdmStore.getId())) {
                throw new IllegalStateException("A store with the id '" + sdmStore.getId() + "' exists more than once in the market.");
            }

            try {
                Store newStore = new Store(sdmStore.getId(), sdmStore.getName().trim(), sdmStore.getDeliveryPpk(), new Location(sdmStore.getLocation().getX(), sdmStore.getLocation().getY()));
                tempStoreIdToStore.put(sdmStore.getId(), newStore);
                setStoreItemsPrices(newStore, sdmStore.getSDMPrices().getSDMSell());
                setStoreDiscounts(newStore, sdmStore.getSDMDiscounts());
            } catch (LocationOutOfRangeException e) {
                throw new IllegalArgumentException("The store '" + sdmStore.getName() + "' has an invalid location." +
                        System.lineSeparator() + e.getMessage());
            }
        }
    }

    private void setTempItems(Collection<SDMItem> generatedItems) {
        tempItemIdToItem = new HashMap<>();

        for (SDMItem sdmItem : generatedItems) {
            if (tempItemIdToItem.containsKey(sdmItem.getId())) {
                throw new IllegalStateException("An item with the id '" + sdmItem.getId() + "' exists more than once in the market.");
            }

            tempItemIdToItem.put(sdmItem.getId(), new MarketItem(sdmItem.getId(), sdmItem.getName().trim(), PurchaseForm.valueOf(sdmItem.getPurchaseCategory().toUpperCase())));
        }
    }

    @Override
    public String toString() {
        return "FileHandler{" +
                "isFileLoaded=" + isFileLoaded +
                ", tempStoreIdToStore=" + tempStoreIdToStore +
                ", tempItemIdToItem=" + tempItemIdToItem +
                ", tempLocationToLocationable=" + tempLocationToLocationable +
                '}';
    }
}
