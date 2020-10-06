package engine.interfaces;

import javax.xml.bind.JAXBException;

public interface FileManager {

    boolean isFileLoaded();

    void loadDataFromFile(String filePath) throws JAXBException;
}
