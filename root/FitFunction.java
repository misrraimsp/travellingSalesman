public class FitFunction {
	
	private int punish;
	private int bottom;

	public FitFunction(int bottom, int pfactor){
		punish = pfactor;
		this.bottom = bottom;
	}
	
	public int measure(Individual ind) {
		int score = bottom - ind.getFenotype().getWeight();
		if (!ind.getValidity()){
			score = (int) (score / punish);
		}
		return score;
	}

}