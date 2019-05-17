package br.edu.ufrgs.inf.bpm.wrapper;

import br.edu.ufrgs.inf.bpm.util.Paths;
import org.apache.commons.io.IOUtils;
import org.omg.spec.bpmn._20100524.model.ObjectFactory;
import org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.omg.spec.bpmn._20100524.model.TSequenceFlow;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

public class JaxbWrapper {

    public static <T> T convertXMLToObject(String bpmnString) {
        T object = null;
        try {
            JAXBContext context = JAXBContext.newInstance(Paths.PackageBpmnPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            JAXBElement<T> element = (JAXBElement<T>) unmarshaller.unmarshal(IOUtils.toInputStream(bpmnString, "UTF-8"));
            object = element.getValue();
        } catch (JAXBException | IOException e) {
            System.err.println(String.format("Exception while unmarshalling: %s", e.getMessage()));
        }
        return object;
    }

    public static <T> String convertObjectToXML(T object) {
        try {
            StringWriter stringWriter = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(new JAXBElement<T>(new QName("uri", "local"), (Class<T>) object.getClass(), object), stringWriter);
            return stringWriter.toString();
        } catch (JAXBException e) {
            System.err.println(String.format("Exception while marshalling: %s", e.getMessage()));
        }
        return null;
    }

    public static <T> String convertToXML(TDefinitions definition) {
        StringWriter stringWriter = new StringWriter();
        try {
            JAXBContext context = JAXBContext.newInstance(Paths.PackageBpmnPath);
            Marshaller marshaller = context.createMarshaller();
            JAXBElement<TDefinitions> element = new ObjectFactory().createDefinitions(definition);
            // file = File.createTempFile("temp", ".bpmn");
            marshaller.marshal(element, stringWriter);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

    public static <T> Element convertObjectToElement(T object) {
        Document doc = null;
        try {
            if (object instanceof TSequenceFlow) {
                TSequenceFlow tSequenceFlow = (TSequenceFlow) object;
            }

            String xml = JaxbWrapper.convertObjectToXML(object);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return doc != null ? doc.getDocumentElement() : null;
    }

    public static <T> String convertObjectToXMLS(T object) {
        try {
            StringWriter stringWriter = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(object, stringWriter);
            return stringWriter.toString();
        } catch (JAXBException e) {
            System.err.println(String.format("Exception while marshalling: %s", e.getMessage()));
        }
        return null;
    }

}
