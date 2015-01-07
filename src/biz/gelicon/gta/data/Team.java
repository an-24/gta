package biz.gelicon.gta.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import biz.gelicon.gta.view.NodeView;

@XmlRootElement
public class Team implements NodeView {
	private static SimpleDateFormat dtformat = new SimpleDateFormat("dd.MM.yyyy");
	
	private Integer id;
	private String name;
	private boolean active;
	private List<Person> persons;
	private Date createDate;
	private Integer limit;
	private Integer workedOfDay;
	private Integer workedOfWeek;
	private Integer workedOfMonth;
	private Integer workedOfBeginProject;
	

	public Team() {
	}
	
	Team(String name, List<Person> persons) {
		this.name = name;
		this.persons =persons;
	}

	Team(String name, List<Person> persons, Date createDate) {
		this(name,persons);
		this.createDate = createDate;
	}
	
	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	@XmlTransient
	public Boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getName() {
		return name;
	}

	public Date getCreateDate() {
		return createDate;
	}
	
	public static List<Team> getDemoTeams() {
		List<Team> tl = new LinkedList<Team>();
		tl.add(new Team("Dream Team", Person.getDemoPersons(), new Date()));
		tl.add(new Team("Clever Team", Person.getDemoPersons()));
		tl.add(new Team("Grind Team", Person.getDemoPersons()));
		//if(tl.size()>0)	tl.get(new Random().nextInt(tl.size())).setActive(true);
		return tl;
	}

	@Override
	public String getText() {
		return name;
	}

	public String getCreateDateAsText() {
		return createDate!=null?dtformat.format(createDate):null;
	}

	public Integer getLimit() {
		return limit;
	}

	public Integer getWorkedOfDay() {
		return workedOfDay;
	}

	public Integer getWorkedOfWeek() {
		return workedOfWeek;
	}

	public Integer getWorkedOfMonth() {
		return workedOfMonth;
	}

	public Integer getWorkedOfBeginProject() {
		return workedOfBeginProject;
	}

	public Integer getId() {
		return id;
	}

}
