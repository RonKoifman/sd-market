package engine.file;

import engine.file.jaxb.schema.generated.SuperDuperMarketDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

class XmlDeserializer {

    public SuperDuperMarketDescriptor deserializeFile(InputStream fileInputStream) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(SuperDuperMarketDescriptor.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        return (SuperDuperMarketDescriptor)jaxbUnmarshaller.unmarshal(fileInputStream);
    }
}
