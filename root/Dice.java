
public class Dice {

	public int roll(int lowBound, int highBound){
		int range = highBound - lowBound;
		double random = Math.random();
		return (int) (lowBound + (random * range) + 0.5);
	}
	
}
