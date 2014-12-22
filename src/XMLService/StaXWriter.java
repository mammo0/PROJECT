package XMLService;
import java.io.FileOutputStream;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class StaXWriter {
  private String ProjectFile;

  public void setFile(String ProjectFile) {
    this.ProjectFile = ProjectFile;
  }
  
  private Benennung naming = new Benennung();


  public void saveConfig() throws Exception {
	  naming.BenoetigteSkillsPhasefuellen();
	  naming.BenoetigtePTproSkillPhasefuellen();
	  naming.datumgesamtprojektsetzen();
	  naming.ArrayMitHauptphasenfuellen();
    // create an XMLOutputFactory
    XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
    // create XMLEventWriter
    XMLEventWriter eventWriter = outputFactory
        .createXMLEventWriter(new FileOutputStream(ProjectFile));
    // create an EventFactory
    XMLEventFactory eventFactory = XMLEventFactory.newInstance();
    XMLEvent end = eventFactory.createDTD("\n");
    // create and write Start Tag
    StartDocument startDocument = eventFactory.createStartDocument();
    eventWriter.add(startDocument);

    // create project open tag
    StartElement configStartElement = eventFactory.createStartElement("", "", "projekt");
    eventWriter.add(configStartElement);
    eventWriter.add(end);
    // Write the different nodes - Main information about the project
    createNode(eventWriter, "projektname", naming.getProjektname());
    createNode(eventWriter, "projektstart", ""+naming.getStartdatum().getTimeInMillis());
    createNode(eventWriter, "projektende", ""+naming.getEnddatum().getTimeInMillis());
    //Information about the main phases of the project
    for (int p = 0; p<naming.getHauptphasen().size(); p++){
    StartElement configHauptphaseElement = eventFactory.createStartElement("","", "hauptphase");
        eventWriter.add(configHauptphaseElement);
        eventWriter.add(end);
        createNode(eventWriter, "Hauptphasenname", naming.getHauptphasen().get(p).toString());
        createNode(eventWriter, "Startdatum", naming.getHauptphasenname());
        createNode(eventWriter, "Enddatum", naming.getHauptphasenname());
        createNode(eventWriter, "Risikozuschlag", naming.getHauptphasenname());

        //resources needed in main phase
        StartElement configHauptphaseRessourceElement = eventFactory.createStartElement("",
                "", "RessourcenProHauptphase");
            eventWriter.add(configHauptphaseRessourceElement);
            eventWriter.add(end);
        for(int i=0; i<naming.getBenoetigteSkillsPhase().size(); i++){
        createNode(eventWriter, "Benötigter Skill", ""+naming.getBenoetigteSkillsPhase().get(i));
        createNode(eventWriter, "Benötigter Anzahl PT", ""+naming.getBenoetigtePTproSkillPhase().get(i));
        }
        eventWriter.add(eventFactory.createEndElement("", "", "RessourcenProHauptphase"));
        //information about subphase
        StartElement configSubphaseElement = eventFactory.createStartElement("",
                "", "Subphase");
        eventWriter.add(configSubphaseElement);
        eventWriter.add(end);
        createNode(eventWriter, "Subphasenname", naming.getHauptphasenname());
        createNode(eventWriter, "Startdatum", naming.getHauptphasenname());
        createNode(eventWriter, "Enddatum", naming.getHauptphasenname());
        
      //resources needed in sub phase
        StartElement configSubphaseRessourceElement = eventFactory.createStartElement("",
                "", "RessourcenProSubphase");
            eventWriter.add(configSubphaseRessourceElement);
            eventWriter.add(end);
        for(int i=0; i<naming.getBenoetigteSkillsPhase().size(); i++){
        createNode(eventWriter, "Benötigter Skill", ""+naming.getBenoetigteSkillsPhase().get(i));
        createNode(eventWriter, "Benötigter Anzahl PT", ""+naming.getBenoetigtePTproSkillPhase().get(i));
        }
        eventWriter.add(eventFactory.createEndElement("", "", "RessourcenProSubphase"));
      //close sub phase node
        eventWriter.add(eventFactory.createEndElement("", "", "Subphase"));
        
        //close main phase node
        eventWriter.add(eventFactory.createEndElement("", "", "hauptphase"));
    }
    
    StartElement configVerfuegbareRessourcenElement = eventFactory.createStartElement("",
            "", "VerfuegbareRessourcen");
        eventWriter.add(configVerfuegbareRessourcenElement);
        eventWriter.add(end);
        createNode(eventWriter, "VerfuegbarerSkill", "Java Entwickler");
        createNode(eventWriter, "VerfuegbarerSkillinTagen", "12");
        
    eventWriter.add(eventFactory.createEndElement("", "", "projekt"));
    eventWriter.add(end);
    eventWriter.add(eventFactory.createEndDocument());
    eventWriter.close();
  }

  private void createNode(XMLEventWriter eventWriter, String name,
      String value) throws XMLStreamException {

    XMLEventFactory eventFactory = XMLEventFactory.newInstance();
    XMLEvent end = eventFactory.createDTD("\n");
    XMLEvent tab = eventFactory.createDTD("\t");
    // create Start node
    StartElement sElement = eventFactory.createStartElement("", "", name);
    eventWriter.add(tab);
    eventWriter.add(sElement);
    // create Content
    Characters characters = eventFactory.createCharacters(value);
    eventWriter.add(characters);
    // create End node
    EndElement eElement = eventFactory.createEndElement("", "", name);
    eventWriter.add(eElement);
    eventWriter.add(end);

  }

} 