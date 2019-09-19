import java.util.ArrayList;
import java.util.Iterator;

public class Population {
	
	private ArrayList<Individual> people;
	private Elite elite;
	private int ww; // worst weight

	public Population(int elitesize){
		people = new ArrayList<Individual>();
		elite = new Elite(elitesize);
		ww = 0;
	}
	
	public ArrayList<Individual> getIndividuals(){
		return people;
	}

	public int getWorstWeight() {
		return ww;
	}
	
	public int getSize() {
		return people.size();
	}

	public void add(Individual ind) {
		int weight = ind.getFenotype().getWeight();
		if(ind.getValidity() && elite.getMaxSize() > 0 && !elite.contains(ind)){
			if(!elite.isFull()) elite.insert(ind);
			else {
				if(weight < elite.getThreshold()){
					elite.deleteWorst();
					elite.insert(ind);
				}
			}
		}
		if(ww < weight) ww = weight;
		people.add(ind);
	}
	
	public Individual getBest(){
		if(people.isEmpty()) return null;
		if(elite.getSize() == 0){
			Iterator<Individual> it = people.iterator();
			Individual best = it.next();
			int bestfitness = best.getFitness();
			while(it.hasNext()){
				Individual ind = it.next();
				if(ind.getFitness() > bestfitness) best = ind;
			}
			return best;
		}
		return elite.getBest();
	}
	
	public void printPopulation(){
		if (people.isEmpty()) {
			System.out.println("There is no one is this population");
			return;
		}
		Iterator<Individual> it = people.iterator();
		int index = 0;
		while(it.hasNext()){
			System.out.println("Fenotype " + index + ": ");
			it.next().getFenotype().printFenotype();
			index++;
		}
	}

	public Elite getElite() {
		return elite;
	}

	public int getAverageFitness() {
		int avg = 0;
		for(Individual ind : people) avg = avg + ind.getFitness();
		return  (int) (avg / GeneticProblem.populationSize) + 1;
	}
	
}