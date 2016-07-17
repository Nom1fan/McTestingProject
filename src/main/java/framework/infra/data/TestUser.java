package framework.infra.data;

import framework.infra.annotations.RandomString;

public class TestUser {

	@RandomString(size = 10)
	private String _id;

	@RandomString(size = 45)
	private String _token;

	public TestUser() {

	}

	public TestUser(String id, String token) {

		_id = id;
		_token = token;
	}

	public String getToken() {
		return _token;
	}
	
	public String getId() {
		return _id;
	}
	
}
