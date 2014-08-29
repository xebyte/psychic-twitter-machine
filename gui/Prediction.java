package gui;

public class Prediction implements Comparable<Prediction> {
	private String term;
	private double probability;
	
	public Prediction(String term, double prob) {
		this.term = term;
		this.probability = prob;
	}
	
	public String getTerm() {
		return term;
	}
	
	public double getProbability() {
		return probability;
	}

	@Override
	public int compareTo(Prediction o) {
		
		if((probability - o.getProbability()) > 0) return -1;
		else if((probability - o.getProbability()) < 0) return 1;
		else return 0;
	}
	
	public String toString() {
		return term + " " + String.format("%.10f", probability);
	}
}
