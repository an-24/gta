package biz.gelicon.gta.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import biz.gelicon.gta.view.NodeView;

public class Team implements NodeView {
	private static DateTimeFormatter dtformat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
	
	private String name;
	private boolean active;
	private List<Person> persons;
	private LocalDate createDate;
	private int limit;
	

	public Team() {
	}
	
	Team(String name, List<Person> persons) {
		this.name = name;
		this.persons =persons;
	}

	Team(String name, List<Person> persons, LocalDate createDate) {
		this(name,persons);
		this.createDate = createDate;
	}
	
	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getName() {
		return name;
	}

	public LocalDate getCreateDate() {
		return createDate;
	}
	
	public static List<Team> getDemoTeams() {
		List<Team> tl = new LinkedList<Team>();
		tl.add(new Team("Dream Team", Person.getDemoPersons(), LocalDate.now()));
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
		return createDate!=null?createDate.format(dtformat):null;
	}

}
