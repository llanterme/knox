package za.co.knox.restservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class OtpGateWayRequest{

	@JsonProperty("to")
	private String to;

	@JsonProperty("body")
	private String body;

	public void setTo(String to){
		this.to = to;
	}

	public String getTo(){
		return to;
	}

	public void setBody(String body){
		this.body = body;
	}

	public String getBody(){
		return body;
	}

	@Override
 	public String toString(){
		return 
			"OtpGateWayRequest{" + 
			"to = '" + to + '\'' + 
			",body = '" + body + '\'' + 
			"}";
		}

	public OtpGateWayRequest(String to, String body) {
		this.to = to;
		this.body = body;
	}
}