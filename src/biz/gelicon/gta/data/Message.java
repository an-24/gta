package biz.gelicon.gta.data;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Message {
	private int key = 0;
	private int mouse = 0;
	private int mouseMove = 0;
	private Date dtBegin;
	private Date dtFinish;
	private String captureFileName;
	private String team;

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

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getMouse() {
		return mouse;
	}

	public void setMouse(int mouse) {
		this.mouse = mouse;
	}

	public int getMouseMove() {
		return mouseMove;
	}

	public void setMouseMove(int mouseMove) {
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

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

}
