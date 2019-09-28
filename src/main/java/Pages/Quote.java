package Pages;

import java.util.List;

public class Quote {

	String token;
	List<String> infoMsgs;
	public List<String> getInfoMsgs() {
		return infoMsgs;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setInfoMsgs(List<String> infoMsgs) {
		this.infoMsgs = infoMsgs;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
}
