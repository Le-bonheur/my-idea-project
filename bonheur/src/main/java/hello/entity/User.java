package hello.entity;

import net.sf.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String nickName;

    private String openId;

    private Long times;

    private String avatarUrl;

	private Double money;

    public User(){}

    public User(JSONObject json){
    	this.nickName = json.getString("nickName");
    	this.openId = json.getString("openId");
    	this.times = json.getLong("times");
    	this.avatarUrl = json.getString("avatarUrl");
	}

	public User(String nickName, String openId, Long times, String avatarUrl,Double money) {
		this.nickName = nickName;
		this.openId = openId;
		this.times = times;
		this.avatarUrl = avatarUrl;
		this.money = money;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", nickName='" + nickName + '\'' +
				", openId='" + openId + '\'' +
				", avatarUrl='" + avatarUrl + '\'' +
				'}';
	}

	public Long getTimes() {
		return times;
	}

	public void setTimes(Long times) {
		this.times = times;
	}
}

