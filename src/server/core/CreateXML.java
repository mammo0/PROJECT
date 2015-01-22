package server.core;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import model.project.Phase;
import model.project.Project;

public class CreateXML {

	private String ProjectFile;
	private XMLEventWriter eventWriter;
	private XMLOutputFactory outputFactory;
	private XMLEventFactory eventFactory;
	private XMLEvent end;
	private Project project;

	public void setFile(String ProjectFile) throws FileNotFoundException,
			XMLStreamException {
		this.ProjectFile = ProjectFile;
		outputFactory = XMLOutputFactory.newInstance();
		eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(
				ProjectFile));
		eventFactory = XMLEventFactory.newInstance();
		end = eventFactory.createDTD("\n");
	}

	// Beginn des XML Dokuments (Project Tag)
	public void saveConfig(Project project) throws Exception,
			NullPointerException {
		// create and write Start Tag
		StartDocument startDocument = eventFactory.createStartDocument("UTF-8", "1.0");
		eventWriter.add(startDocument);
		// create project open tag
		StartElement configStartElement = eventFactory.createStartElement("",
				"", "project");
		eventWriter.add(configStartElement);
		eventWriter.add(end);
		// Write the different nodes - Main information about the project

		try {
			createNode(eventWriter, "projectName", project.getProjectName());
			createNode(eventWriter, "projectResponsible",
					project.getProjectResponsible());
			createNode(eventWriter, "description", project.getDescription());
			if(project.getStartDate()!=null){
			createNode(eventWriter, "startDate", project.getStartDate()
					.toString());
			createNode(eventWriter, "endDate", project.getEndDate().toString());
			}else{
				
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		// createNode(eventWriter, "marked", project.isFinished());
	}

	// Ende des Dokuments (Project Tag)
	public void closeXML() throws XMLStreamException, NullPointerException {
		eventWriter.add(eventFactory.createEndElement("", "", "project"));
		eventWriter.add(end);
		eventWriter.add(eventFactory.createEndDocument());
		eventWriter.close();
	}

	// Phasentag erstellen
	public void startTagPhasen() throws XMLStreamException,
			NullPointerException {
		StartElement configStartElement = eventFactory.createStartElement("",
				"", "phasen");
		eventWriter.add(configStartElement);
		eventWriter.add(end);
	}

	// Phasentag schlieﬂen
	public void endTagPhasen() throws XMLStreamException, NullPointerException {
		eventWriter.add(eventFactory.createEndElement("", "", "phasen"));
		eventWriter.add(end);
	}

	// Phase eintragen
	public void insertPhase(Project project, int i) throws XMLStreamException,
			NullPointerException {
		StartElement configStartElementphase = eventFactory.createStartElement(
				"", "", "phase");
		eventWriter.add(configStartElementphase);
		eventWriter.add(end);
		createNode(eventWriter, "phaseName", project.getPhases().get(i)
				.getPhaseName());
		createNode(eventWriter, "startDate", ""
				+ project.getPhases().get(i).getStartDate());
		createNode(eventWriter, "endDate", ""
				+ project.getPhases().get(i).getEndDate());
		createNode(eventWriter, "riskFactor", ""
				+ project.getPhases().get(i).getRiskFactor());
		if (project.getPhases().get(i).getParent() != null) {
			createNode(eventWriter, "parent", project.getPhases().get(i)
					.getParent().getPhaseName());
		}

		StartElement configStartElement = eventFactory.createStartElement("",
				"", "phaseskills");
		eventWriter.add(configStartElement);
		eventWriter.add(end);

		Phase phases = project.getPhases().get(i);
		Enumeration<Integer> enumKey = phases.getSkills().keys();
		while (enumKey.hasMoreElements()) {
			int key = enumKey.nextElement();
			createNode(eventWriter, "phaseskill", "" + key);
			createNode(eventWriter, "skillDuration", ""
					+ phases.getSkills().get(key));

		}
		eventWriter.add(eventFactory.createEndElement("", "", "phaseskills"));
		eventWriter.add(end);
		eventWriter.add(eventFactory.createEndElement("", "", "phase"));
		eventWriter.add(end);

	}

	// Skillstag erstellen
	public void startTagSkills() throws XMLStreamException,
			NullPointerException {
		StartElement configStartElement = eventFactory.createStartElement("",
				"", "skills");
		eventWriter.add(configStartElement);
		eventWriter.add(end);
	}

	// Skillstag schlieﬂen
	public void endTagSkills() throws XMLStreamException, NullPointerException {
		eventWriter.add(eventFactory.createEndElement("", "", "skills"));
		eventWriter.add(end);
	}

	// Skill eintragen
	public void insertSkill(Project project, int i) throws XMLStreamException,
			NullPointerException {
		StartElement configStartElementskill = eventFactory.createStartElement(
				"", "", "skill");
		eventWriter.add(configStartElementskill);
		eventWriter.add(end);
		createNode(eventWriter, "skillName", project.getSkills().get(i)
				.getSkillName());
		createNode(eventWriter, "skillID", ""
				+ project.getSkills().get(i).getSkillID());
		createNode(eventWriter, "dayRateInt", ""
				+ project.getSkills().get(i).getDayRateInt());
		createNode(eventWriter, "dayRateExt", ""
				+ project.getSkills().get(i).getDayRateExt());

		eventWriter.add(eventFactory.createEndElement("", "", "skill"));
		eventWriter.add(end);
	}

	// Resourcenstag erstellen
	public void startTagResource() throws XMLStreamException,
			NullPointerException {
		StartElement configStartElement = eventFactory.createStartElement("",
				"", "resources");
		eventWriter.add(configStartElement);
		eventWriter.add(end);
	}

	// Resourcetag schlieﬂen
	public void endTagResource() throws XMLStreamException,
			NullPointerException {
		eventWriter.add(eventFactory.createEndElement("", "", "resources"));
		eventWriter.add(end);
	}

	// Skill eintragen
	public void insertResource(Project project, int i)
			throws XMLStreamException, NullPointerException {
		StartElement configStartElementsResource = eventFactory
				.createStartElement("", "", "resource");
		eventWriter.add(configStartElementsResource);
		eventWriter.add(end);
		createNode(eventWriter, "resourcename", project.getResources().get(i)
				.getResourceName());
		createNode(eventWriter, "resourceID", ""
				+ project.getResources().get(i).getResourceID());
		createNode(eventWriter, "skillID", ""
				+ project.getResources().get(i).getSkillID());
		createNode(eventWriter, "availability", ""
				+ project.getResources().get(i).getAvailability());
		createNode(eventWriter, "skillamount",
				"" + project.getResources().get(i).getSkillAmount());
		createNode(eventWriter, "intern", ""
				+ project.getResources().get(i).isIntern());

		eventWriter.add(eventFactory.createEndElement("", "", "resource"));
		eventWriter.add(end);
	}

	private void createNode(XMLEventWriter eventWriter, String name,
			String value) throws XMLStreamException, NullPointerException {

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