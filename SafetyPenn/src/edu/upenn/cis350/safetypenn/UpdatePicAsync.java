package edu.upenn.cis350.safetypenn;

import org.json.JSONObject;

public class UpdatePicAsync extends NetworkAsync {
	private String filePath;

	public UpdatePicAsync(String email, String filePath) {
		super(email);
		this.filePath = filePath;
	}

	@Override
	protected JSONObject doInBackground(String... args) {
		System.out.println("Starting background thread");
		JSONObject json = jsonParser.getJSONMultiPart(URL, email, filePath);
		return json;
	}

}
