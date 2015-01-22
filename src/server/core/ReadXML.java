package server.core;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import model.project.Phase;
import model.project.Project;
import model.project.Resource;
import model.project.Skill;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

public class ReadXML {

	Project project = new Project();
	Document doc = null;
	File file;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	DateTimeFormatter formattertimestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
	private ICoreServer core;

	public ReadXML(String name){
		this.core = Core.getInstance(Core.class);
		
		//Pfad anpassen
		file = new File(core.getProjectDirectory()+name+".xml");
		
	}
	public Project read() {

		SAXBuilder saxbuilder = new SAXBuilder();

		try {
			doc = saxbuilder.build(file);
			//XMLOutputter xmlout = new XMLOutputter();
			Element projectelement = doc.getRootElement();
			List<Element> phasenelement = projectelement.getChildren("phasen");
			List<Element> skillselement = projectelement.getChildren("skills");
			List<Element> resourceselement = projectelement.getChildren("resources");

			// Fill the first elements of project object
			// System.out.println(element.getChildText("projectName"));
			project.setProjectName(projectelement.getChildText("projectName"));
			// System.out.println(element.getChildText("projectResponsible"));
			project.setProjectResponsible(projectelement
					.getChildText("projectResponsible"));
			// System.out.println(element.getChildText("description"));
			project.setDescription(projectelement.getChildText("description"));
			// System.out.println(element.getChildText("startDate"));
			if(projectelement.getChildText("finished").equals("true")){
				project.setFinished(true);
				LocalDateTime _timestamp = LocalDateTime.parse(
						projectelement.getChildText("timestamp"), formattertimestamp);
				project.setTimestamp(_timestamp);
			}
			else{
				project.setFinished(false);
			}
		
			if(projectelement.getChildText("startDate")==null || projectelement.getChildText("startDate")==""){
				
			}else{
			LocalDate startDate = LocalDate.parse(
					projectelement.getChildText("startDate"), formatter);
			project.setStartDate(startDate);
			// System.out.println(element.getChildText("endDate"));
			LocalDate endDate = LocalDate.parse(
					projectelement.getChildText("endDate"), formatter);
			project.setEndDate(endDate);
			}

			// fill the phase object

			
			for (Element _phaseelement : phasenelement) {
				List<Element> phaseelement = _phaseelement.getChildren();
				for (Element _phaseinhalt : phaseelement) {

					Phase phase = new Phase();
					phase.setPhaseName(_phaseinhalt.getChildText("phaseName"));

					
					if(_phaseinhalt.getChildText("phaseName")!= "") {
						LocalDate startDatephase = LocalDate.parse(
						_phaseinhalt.getChildText("startDate"), formatter);
				phase.setStartDate(startDatephase);
				LocalDate endDatephase = LocalDate.parse(
						_phaseinhalt.getChildText("endDate"), formatter);
				phase.setEndDate(endDatephase);
					}
					else {
						
					}
				int riskFactor = Integer.valueOf(_phaseinhalt
						.getChildText("riskFactor"));
				phase.setRiskFactor(riskFactor);
				
					
					
					
					if(_phaseinhalt.getChildText("parent") == null){
			
					}else{
						
						Phase parent = new Phase();
						parent.setPhaseName((_phaseinhalt.getChildText("parent")));
						phase.setParent(parent);
					}

					//System.out.println(phase.getPhaseName());

					// fill the Skill Hashtablet in the phases object
					List<Element> phasenskills = _phaseinhalt
							.getChildren("phaseskills");
					for (Element _phasenskills : phasenskills) {
						List<Element> skillID = _phasenskills
								.getChildren("phaseskill");
						List<Element> duration = _phasenskills
								.getChildren("skillDuration");
						for (int i = 0; i < skillID.size(); i++) {
							int _skillID = Integer.valueOf(skillID.get(i)
									.getText());
							int _amount = Integer.valueOf(duration.get(i)
									.getText());
							phase.addSkill(_skillID, _amount);
//							System.out.println(skillID.get(i).getText());
//							System.out.println(duration.get(i).getText());
						}
					}
					project.addPhase(phase);
				}

			}
			

			// fill the skill object
			
			for (Element _skillselement : skillselement) {
				List<Element> _skillelement = _skillselement.getChildren();
				for (Element _skillinhalt : _skillelement) {
					List<Element> skillName = _skillinhalt
							.getChildren("skillName");
					List<Element> skillID = _skillinhalt.getChildren("skillID");
					List<Element> dayRateInt = _skillinhalt
							.getChildren("dayRateInt");
					List<Element> dayRateExt = _skillinhalt
							.getChildren("dayRateExt");
					for (int i = 0; i < skillName.size(); i++) {
						Skill skill = new Skill();
						skill.setSkillName(skillName.get(i).getText());
						int _skillID = Integer
								.valueOf(skillID.get(i).getText());
						skill.setSkillID(_skillID);
						float _dayRateInt = Float.parseFloat(dayRateInt.get(i)
								.getText());
						skill.setDayRateInt(_dayRateInt);
						float _dayRateExt = Float.parseFloat(dayRateExt.get(i)
								.getText());
						skill.setDayRateExt(_dayRateExt);
						project.addSkill(skill);
					}

				}

			}
			
			// fill the resource object
			
			for (Element _resourceselement : resourceselement) {
				List<Element> _resourceelement = _resourceselement.getChildren();
				for (Element _resourceinhalt : _resourceelement) {
					List<Element> resourcename = _resourceinhalt
							.getChildren("resourcename");
					List<Element> resourceID = _resourceinhalt.getChildren("resourceID");
					List<Element> skillID = _resourceinhalt
							.getChildren("skillID");
					List<Element> availability = _resourceinhalt
							.getChildren("availability");
					List<Element> skillamount = _resourceinhalt
							.getChildren("skillamount");
					List<Element> intern = _resourceinhalt
							.getChildren("intern");
					for (int i = 0; i < resourcename.size(); i++) {
						Resource resource = new Resource();
						resource.setResourceName(resourcename.get(i).getText());
						int _skillID = Integer
								.valueOf(skillID.get(i).getText());
						resource.setSkill(_skillID);
						int _availability = Integer
								.valueOf(availability.get(i).getText());
						resource.setAvailability(_availability);
						int _skillamount = Integer
								.valueOf(skillamount.get(i).getText());
						resource.setSkillAmount(_skillamount);
						if(intern.get(i).getText().equals("true")){
							resource.setIntern(true);
						}
						else{
							resource.setIntern(false);
						}
						project.addResource(resource);
					}
					
				}
				
			}

		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return project;

	}
}
