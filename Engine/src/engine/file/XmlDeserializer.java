package engine.file;

import engine.file.jaxb.schema.generated.SuperDuperMarketDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

class XmlDeserializer {

    private static final String XML_SUFFIX = ".xml";

    public SuperDuperMarketDescriptor deserializeFile(String filePath) throws JAXBException {
        if (!filePath.endsWith(XML_SUFFIX)) {
            throw new IllegalArgumentException(String.format("XML file type must end with the suffix '%s'.", XML_SUFFIX));
        }

        File loadedFile = new File(filePath);
        JAXBContext jaxbContext = JAXBContext.newInstance(SuperDuperMarketDescriptor.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        return (SuperDuperMarketDescriptor)jaxbUnmarshaller.unmarshal(loadedFile);
    }
}
