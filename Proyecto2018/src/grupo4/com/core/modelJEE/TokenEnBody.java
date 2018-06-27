package grupo4.com.core.modelJEE;

public class TokenEnBody {
	
	private String SecurityToken;

	public String getSecurityToken() {
		return SecurityToken;
	}

	public void setSecurityToken(String securityToken) {
		SecurityToken = securityToken;
	}

	public TokenEnBody(String securityToken) {
		super();
		SecurityToken = securityToken;
	}

	public TokenEnBody() {
		super();
	}
	
}
