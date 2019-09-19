import java.util.ArrayList;
import java.util.Iterator;

public class GeneticProblem {
	
	
	//GENETIC PROBLEM PARAMETERS
	/////////////////////////////////////////////////////////////////////////////////////////
	public static int itlimit = 1000000;
	public static int punishFactor = 5;
	public static int elitesize = 5;
	public static int populationSize = 100;
	public static int cProb = 85;
	public static int mProb = 2;
	/////////////////////////////////////////////////////////////////////////////////////////

	
	private Population population;
	private FitFunction ff;
	private Dice dice;
	private IO io;

	/////////////////////////////////////////////////////////////////////////////////////////
	// Constructor
	public GeneticProblem(IO io) {
		population = new Population(elitesize);
		ff = new FitFunction(Main.problemgraph.getNumArcs() * Main.problemgraph.getWorstWeight(), punishFactor);
		dice = new Dice();
		this.io = io;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	public Population getPopulation(){
		return population;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	public int solve() {
		boolean stop = false;
		int generation = 1;
		initialize();
		evaluate(population);
		while(!stop){
			population = selection(population);
			population = crossover(population);
			population = mutation(population);
			evaluate(population);
			io.storeFitness(population.getBest().getFitness(), population.getAverageFitness());
			stop = isEnd(generation);
			generation++;
		}
		return (generation - 1);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	private boolean isEnd(int generation) {
		int weight = population.getBest().getFenotype().getWeight();
		boolean valid = population.getBest().getValidity();
		return (generation == itlimit || (valid && weight == 19));
		//return (generation == itlimit);
		//return population.getBest().getValidity();
	}

	/////////////////////////////////////////////////////////////////////////////////////////
	private Population mutation(Population p) {
		Population newpop = new Population(elitesize);
		//insert elite
		Elite e = p.getElite();
		if (e.getSize() > 0){
			for(int i = 0; i < e.getSize(); i++){
				newpop.add(e.getIndividual(i));
			}
		}
		Iterator<Individual> it = p.getIndividuals().iterator();
		while(it.hasNext() && newpop.getSize() < populationSize){
			newpop.add(new Individual(it.next(), (mProb / 100)));
		}
		return newpop;
	}

	/////////////////////////////////////////////////////////////////////////////////////////
	private Population crossover(Population p) {
		Population newpop = new Population(elitesize);
		//insert elite
		Elite e = p.getElite();
		if (e.getSize() > 0){
			for(int i = 0; i < e.getSize(); i++){
				newpop.add(e.getIndividual(i));
			}
		}
		Iterator<Individual> it = p.getIndividuals().iterator();
		int offspringSize;
		while(it.hasNext() && newpop.getSize() < populationSize){
			Individual ind1 = it.next();
			Individual ind2 = it.next();
			if (Math.random() < (cProb / 100)) {
				newpop.add(new Individual(ind1, ind2));
				newpop.add(new Individual(ind2, ind1));
			}
		}
		//immigration allowed
		offspringSize = newpop.getSize();
		for (int i = 0; i < (populationSize - offspringSize); i++){
			newpop.add(new Individual());
		}
		return newpop;
	}

	/////////////////////////////////////////////////////////////////////////////////////////
	private Population selection(Population p) { //roulette wheel
		Population newpop = new Population(elitesize);
		//insert elite
		Elite e = p.getElite();
		if (e.getSize() > 0){
			for(int i = 0; i < e.getSize(); i++){
				newpop.add(e.getIndividual(i));
			}
		}
		ArrayList<Individual> pool = p.getIndividuals();
		//obtain total fitness
		int S = 0;
		for (Individual ind : pool) S = S + ind.getFitness();
		//select (populationSize - elitesize) number of better individuals
		for (int i = 0; i < (populationSize - e.getSize()); i++){
			int random = dice.roll(0, S);
			int index = 0;
			int partialsum = pool.get(0).getFitness();
			while(partialsum < random){
				index++;
				partialsum = partialsum + pool.get(index).getFitness();
			}
			newpop.add(pool.get(index));
		}
		return newpop;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	private void evaluate(Population p) {
		int fitness;
		for (Individual ind : p.getIndividuals()){
			fitness = ff.measure(ind);
			ind.setFitness(fitness);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////
	private void initialize() {
		for (int i = 0; i < populationSize; i++){
			population.add(new Individual());
		}
	}

}
