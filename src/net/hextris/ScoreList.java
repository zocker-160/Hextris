package net.hextris;

import lombok.Getter;

import java.io.*;
//import java.io.BufferedWriter;
//import java.io.FileWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;


/**
 * maintains a highscore table and offers functions for uploading and reading highscores from the server 
 * 
 * @author fränk
 * 
 */
public class ScoreList {

	@Getter
	private static int maxSize = 500;

	private static URL baseURL;
	private static ArrayList<Score> scoreList = null;//new ScoreList();


	public static ArrayList<Score> getScoreList() {
		if (scoreList == null) {

			scoreList = new ArrayList<>(maxSize);
			Properties prop = new Properties();


			try (InputStream inS = ScoreList.class.getResourceAsStream("hextris.properties")) {
				prop.load(inS);
				String url = prop.getProperty("hextris.ServerScriptUrl");

				baseURL = URI.create(url).toURL();
	            read();

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			
		}
		return scoreList;
	}
	
	/**
	 * Adds the score s into the highscore list. If the score is too low it
	 * won't be added.
	 * 
	 * @param s
	 * @return
	 */
//	public boolean add(Score s) {
//		int c;
//		for (c = 0; c < size() && s.getScore() <= ((Score) get(c)).getScore(); c++) {
//		}
//		if (c < maxSize) {
//			add(c, s);
//		}
//		return true;
//	}

	/**
	 * upload a new score to the server
	 */
	public static void upload(Score s) 
	{
		OutputStreamWriter wr = null;
		try {
	        // post data
	        String data = URLEncoder.encode("name", "ISO-8859-1") + "=" + URLEncoder.encode(s.name(), "ISO-8859-1");
	        data += "&" + URLEncoder.encode("score", "ISO-8859-1") + "=" + URLEncoder.encode(new Integer(s.score()).toString(), "ISO-8859-1");
	        // post senden
	        HttpURLConnection conn = (HttpURLConnection)getBaseURL().openConnection();
	        conn.setDoOutput(true);
	        wr = new OutputStreamWriter(conn.getOutputStream());
	        wr.write(data);
	        wr.flush();
	        wr.close();

			read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean read() {
		ArrayList<Score> scoreList = ScoreList.getScoreList();
		scoreList.clear();

		int count = 0;

		try (var br = new BufferedReader(new InputStreamReader(baseURL.openStream(), StandardCharsets.ISO_8859_1))) {
			String line;

			while ((line = br.readLine()) != null && ++count <= maxSize) {
				if (line.trim().isEmpty()) continue;

				String[] values = line.split(";");
				if (values.length != 2) continue;

				scoreList.add(new Score(values[1], Integer.parseInt(values[0])));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	/**
	 * returns the least score in the scorelist
	 * @return
	 */
	public static int getMinScore() {
		int s = scoreList.size();
		if (s >= maxSize) {
			return ((Score) scoreList.get(scoreList.size() - 1)).score();
		} else {
			return 0;
		}
	}

	/**
	 * the url of the scorelist
	 * @return
	 */
	private static URL getBaseURL() {
		return baseURL;
	}

	/**
	 * @param url
	 */
	private static void setBaseURL(URL url) {
		baseURL = url;
	}

	/**
	 * returns the place in the scorelist for the given score
	 * @param score
	 * @return
	 */
	public static int getPlace(int score)
	{
		int res = scoreList.size()+1;
		for (int i = 0; i<scoreList.size(); i++) {
			if (score>scoreList.get(i).score())
			{
				res = i+1;
				break;
			}
		}
		return res;
	}
}
