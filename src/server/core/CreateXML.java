package server.core;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

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
	public void saveConfig(Project project) throws Exception {
		// create and write Start Tag
		StartDocument startDocument = eventFactory.createStartDocument();
		eventWriter.add(startDocument);
		// create project open tag
		StartElement configStartElement = eventFactory.createStartElement("",
				"", "project");
		eventWriter.add(configStartElement);
		eventWriter.add(end);
		// Write the different nodes - Main information about the project
		createNode(eventWriter, "projectName", project.getProjectName());
		createNode(eventWriter, "projectResponsible",
				project.getProjectResponsible());
		createNode(eventWriter, "description", project.getDescription());
		createNode(eventWriter, "startDate", project.getStartDate().toString());
		createNode(eventWriter, "endDate", project.getEndDate().toString());
	}

	// Ende des Dokuments (Project Tag)
	public void closeXML() throws XMLStreamException {
		eventWriter.add(eventFactory.createEndElement("", "", "project"));
		eventWriter.add(end);
		eventWriter.add(eventFactory.createEndDocument());
		eventWriter.close();
	}

	// Phasentag erstellen
	public void startTagPhasen() throws XMLStreamException {
		StartElement configStartElement = eventFactory.createStartElement("",
				"", "phasen");
		eventWriter.add(configStartElement);
		eventWriter.add(end);
	}

	// Phase eintragen
	public void insertPhase(String phaseName, String parent, Date startDate,
			Date endDate, int riskfactor, ArrayList skills,
			ArrayList durationPerSkill) throws XMLStreamException {
		createNode(eventWriter, "phaseName", phaseName);
		createNode(eventWriter, "startDate", "" + startDate);
		createNode(eventWriter, "endDate", "" + endDate);
		if (parent != null) {
			createNode(eventWriter, "parent", parent);
		}

		if (skills.size() == durationPerSkill.size()) {

			StartElement configStartElement = eventFactory.createStartElement(
					"", "", "skills");
			eventWriter.add(configStartElement);
			eventWriter.add(end);

			for (int i = 0; i < skills.size(); i++) {
				createNode(eventWriter, "skill", "" + skills.get(i));
				createNode(eventWriter, "skillDuration",
						"" + durationPerSkill.get(i));

			}

			eventWriter.add(eventFactory.createEndElement("", "", "skills"));
			eventWriter.add(end);
		}

		else {
			System.out.println("Arraygrößen stimmen nicht überein");
		}

	}

	// Phasentag schließen
	public void endTagPhasen() throws XMLStreamException {
		eventWriter.add(eventFactory.createEndElement("", "", "phasen"));
		eventWriter.add(end);
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