package biz.gelicon.gta.data;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import biz.gelicon.gta.view.NodeView;

@XmlRootElement
public class Person  implements NodeView {
	private Integer id;
	private String nic;
	private String post;
	private Boolean active;
	private Integer limit;
	private Team team;
	private Boolean internal;
	private Boolean manager;
	
	public Boolean getManager() {
		return manager;
	}

	public void setManager(Boolean manager) {
		this.manager = manager;
	}

	public Boolean getInternal() {
		return internal;
	}

	public void setInternal(Boolean internal) {
		this.internal = internal;
	}

	public Person() {
	}

	protected Person(String nic, String post) {
		this.nic = nic;
		this.post = post;
	}
	
	public  Boolean isActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public String getNic() {
		return nic;
	}
	public String getPost() {
		return post;
	}
	
	public static List<Person> getDemoPersons() {
		List<Person> tp = new LinkedList<Person>();
		if(new Random().nextInt(2)>0) tp.add(new Person("John", "PM"));
		if(new Random().nextInt(2)>0) tp.add(new Person("Mary", "QA"));
		if(new Random().nextInt(2)>0) tp.add(new Person("Liza","Developer"));
		
		if(tp.size()>0)
			tp.get(new Random().nextInt(tp.size())).setActive(true);
		
		return tp;
	}

	@Override
	public String getText() {
		return nic+"#"+post;
	}

	public Integer getId() {
		return id;
	}

	@XmlTransient
	public Team getTeam() {
		return team;
	}

	public Integer getLimit() {
		return limit;
	}

}
