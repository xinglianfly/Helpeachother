package cn.edu.sdu.online.util;

public class ImageAndText {  
     
    public ImageAndText(String userName, String imageUrl, String content,
			String award, String deadLine) {
		super();
		this.userName = userName;
		this.imageUrl = imageUrl;
		this.content = content;
		this.award = award;
		this.deadLine = deadLine;
	}
	private String userName;
    private String imageUrl;
    private String content;
    private String award;
    private String deadLine;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAward() {
		return award;
	}
	public void setAward(String award) {
		this.award = award;
	}
	public String getDeadLine() {
		return deadLine;
	}
	public void setDeadLine(String deadLine) {
		this.deadLine = deadLine;
	}
   

   
}  
