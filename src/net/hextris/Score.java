package net.hextris;

/**
 * simply contains a highscore entry
 * name and score
 *
 * @author frank
 */
public record Score(String name, int score) {

	public String toString() {
		return "      " + score + " " + name.trim();
	}

}