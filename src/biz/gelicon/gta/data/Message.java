package biz.gelicon.gta.data;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Message {
	private Integer key = 0;
	private Integer mouse = 0;
	private Integer mouseMove = 0;
	private Date dtBegin;
	private Date dtFinish;
	private String captureFileName;
	private String teamName;

	public Message() {
	}
	
	public Message(Date dtBegin, Date dtFinish, 
			int key, int mouse, int mouseMove, String captureFileName) {
		this.key = key;
		this.mouse = mouse;
		this.mouseMove = mouseMove;
		this.captureFileName = captureFileName;
		this.dtBegin = dtBegin;
		this.dtFinish = dtFinish;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Integer getMouse() {
		return mouse;
	}

	public void setMouse(Integer mouse) {
		this.mouse = mouse;
	}

	public Integer getMouseMove() {
		return mouseMove;
	}

	public void setMouseMove(Integer mouseMove) {
		this.mouseMove = mouseMove;
	}

	public Date getDtBegin() {
		return dtBegin;
	}

	public void setDtBegin(Date dtBegin) {
		this.dtBegin = dtBegin;
	}

	public Date getDtFinish() {
		return dtFinish;
	}

	public void setDtFinish(Date dtFinish) {
		this.dtFinish = dtFinish;
	}

	public String getCaptureFileName() {
		return captureFileName;
	}

	public void setCaptureFileName(String captureFileName) {
		this.captureFileName = captureFileName;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String team) {
		this.teamName = team;
	}

}
