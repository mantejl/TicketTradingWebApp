package a4.util;

import java.net.HttpURLConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import a4.models.Favorite;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GetEventDetails {

	private static String eventURL = "https://us-west2-csci201-376723.cloudfunctions.net/explore-events/eventDetail/";

	public static Favorite getFavoriteEvent(String entityID) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Favorite event = null;
		try {
			URL url = new URL(eventURL + entityID);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			// System.out.println("GET Response Code :: " + responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				event = gson.fromJson(response.toString(), Favorite.class);
			}
		} catch (Exception error) {
			// TODO Auto-generated catch block
			// error.printStackTrace();
		}
		return event;
	}
}
