package cn.edu.sdu.online.entity;

import java.io.Serializable;
import java.util.Date;

public class Content implements Serializable{

	private String from;
	private String to;
	private String content;
	private Date datetime;
	private boolean checked;

	public Content() {
	}

	public Content(String from, String to, String content, Date datetime,
			boolean checked) {
		super();
		this.from = from;
		this.to = to;
		this.content = content;
		this.datetime = datetime;
		this.checked = checked;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
