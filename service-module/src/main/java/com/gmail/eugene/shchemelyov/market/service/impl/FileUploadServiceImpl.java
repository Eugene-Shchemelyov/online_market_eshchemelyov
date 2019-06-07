package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.exception.ExpectedException;
import com.gmail.eugene.shchemelyov.market.service.FileUploadService;
import com.gmail.eugene.shchemelyov.market.service.ItemService;
import com.gmail.eugene.shchemelyov.market.service.exception.ServiceException;
import com.gmail.eugene.shchemelyov.market.service.model.ItemCatalogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;

import static com.gmail.eugene.shchemelyov.market.service.constant.FileUploadConstant.ITEM_UPLOAD_XSD;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);
    private final ItemService itemService;

    @Autowired
    public FileUploadServiceImpl(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    @Transactional
    public void uploadItems(MultipartFile file) {
        validateXML(file);
        try {
            JAXBContext context = JAXBContext.newInstance(ItemCatalogDTO.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ItemCatalogDTO items = (ItemCatalogDTO) unmarshaller.unmarshal(file.getInputStream());
            items.getItems().forEach(itemService::add);
        } catch (JAXBException | IOException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format("Parse error file: %s.",
                    file.getOriginalFilename()), e);
        }
    }

    private void validateXML(MultipartFile fileXML) {
        try (InputStream inputStream = fileXML.getInputStream()) {
            Source xmlFile = new StreamSource(inputStream);
            SchemaFactory schemaFactory = SchemaFactory
                    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new ClassPathResource(ITEM_UPLOAD_XSD).getFile());
            Validator validator = schema.newValidator();
            validator.validate(xmlFile);
        } catch (SAXException e) {
            logger.error(e.getMessage(), e);
            throw new ExpectedException(String.format("%s: %s.",
                    "File not valid", fileXML.getName()));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format("%s: %s.",
                    "When validate file", fileXML.getName()), e);
        }
    }
}
