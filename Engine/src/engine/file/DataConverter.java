package engine.file;

import engine.enums.DiscountOfferType;
import engine.enums.PurchaseForm;
import engine.exceptions.LocationOutOfRangeException;
import engine.file.jaxb.schema.generated.*;
import engine.models.location.Location;
import engine.models.discount.DiscountInformation;
import engine.models.discount.DiscountOffer;
import engine.models.discount.DiscountTrigger;
import engine.models.item.RegionItem;
import engine.models.item.StoreItem;
import engine.models.store.Store;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

class DataConverter {

    private Map<Integer, Store> convertedStoreIdToStore;
    private Map<Integer, RegionItem> convertedItemIdToItem;
    private String convertedRegionName;

    public Map<Integer, Store> getConvertedStoreIdToStore() {
        return convertedStoreIdToStore;
    }

    public Map<Integer, RegionItem> getConvertedItemIdToItem() {
        return convertedItemIdToItem;
    }

    public String getConvertedRegionName() {
        return convertedRegionName;
    }

    public void convertJaxbObjectsToSystemModels(SuperDuperMarketDescriptor sdmDescriptor) {
        convertItems(sdmDescriptor.getSDMItems().getSDMItem());
        convertStores(sdmDescriptor.getSDMStores().getSDMStore());
        convertRegionName(sdmDescriptor.getSDMZone().getName());
    }

    private void checkEachItemSoldByAtLeastOneStore(Collection<SDMStore> generatedStores) {
        Collection<Integer> storesItemsId = generatedStores.stream()
                .map(SDMStore::getSDMPrices)
                .map(SDMPrices::getSDMSell)
                .flatMap(List::stream)
                .map(SDMSell::getItemId)
                .collect(Collectors.toSet());

        for (RegionItem item : convertedItemIdToItem.values()) {
            if (!storesItemsId.contains(item.getId())) {
                throw new IllegalStateException("An item with the id '" + item.getId() + "' is not sold by any store in the region.");
            }
        }
    }

    private void checkForExistingItemsInStoresSells(Collection<SDMStore> stores) {
        for (SDMStore store : stores) {
            for (SDMSell sell : store.getSDMPrices().getSDMSell()) {
                if (!convertedItemIdToItem.containsKey(sell.getItemId())) {
                    throw new IllegalStateException("An item with the id '" + sell.getItemId() + "', which sold by '" +
                            store.getName() + "' store, does not exist in region's items.");
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

            RegionItem itemToAdd = convertedItemIdToItem.get(sell.getItemId());
            store.addNewItem(new StoreItem(itemToAdd.getId(), itemToAdd.getName(), itemToAdd.getPurchaseForm(), sell.getPrice(), store.getId()));
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
                        DiscountOfferType.valueOf(discount.getThenYouGet().getOperator().replace("-", "_")));

                discount.getThenYouGet().getSDMOffer().forEach(sdmOffer -> newDiscount.addNewOffer(
                        new DiscountOffer(store.getItemById(sdmOffer.getItemId()), (float)sdmOffer.getQuantity(), sdmOffer.getForAdditional())));

                store.addNewDiscount(newDiscount);
            }
        }
    }

    private void convertRegionName(String regionName) {
        this.convertedRegionName = regionName.trim();
    }

    private void convertStores(Collection<SDMStore> generatedStores) {
        convertedStoreIdToStore = new HashMap<>();

        checkForExistingItemsInStoresSells(generatedStores);
        checkEachItemSoldByAtLeastOneStore(generatedStores);
        for (SDMStore sdmStore : generatedStores) {
            if (convertedStoreIdToStore.containsKey(sdmStore.getId())) {
                throw new IllegalStateException("A store with the id '" + sdmStore.getId() + "' exists more than once in the region.");
            }

            try {
                Store newStore = new Store(sdmStore.getId(), sdmStore.getName().trim(), sdmStore.getDeliveryPpk(), new Location(sdmStore.getLocation().getX(), sdmStore.getLocation().getY()));
                convertedStoreIdToStore.put(sdmStore.getId(), newStore);
                setStoreItemsPrices(newStore, sdmStore.getSDMPrices().getSDMSell());
                setStoreDiscounts(newStore, sdmStore.getSDMDiscounts());
            } catch (LocationOutOfRangeException e) {
                throw new IllegalArgumentException("The store '" + sdmStore.getName() + "' has an invalid location." +
                        System.lineSeparator() + e.getMessage());
            }
        }
    }

    private void convertItems(Collection<SDMItem> generatedItems) {
        convertedItemIdToItem = new HashMap<>();

        for (SDMItem sdmItem : generatedItems) {
            if (convertedItemIdToItem.containsKey(sdmItem.getId())) {
                throw new IllegalStateException("An item with the id '" + sdmItem.getId() + "' exists more than once in the region.");
            }

            convertedItemIdToItem.put(sdmItem.getId(), new RegionItem(sdmItem.getId(), sdmItem.getName().trim(), PurchaseForm.valueOf(sdmItem.getPurchaseCategory().toUpperCase())));
        }
    }

    @Override
    public String toString() {
        return "DataConverter{" +
                "convertedStoreIdToStore=" + convertedStoreIdToStore +
                ", convertedItemIdToItem=" + convertedItemIdToItem +
                ", convertedRegionName='" + convertedRegionName + '\'' +
                '}';
    }
}