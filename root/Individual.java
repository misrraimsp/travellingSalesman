public class Individual {
	private Grafo fenotype;
	private int numGenes;
	private int fitness;
	private boolean valid;
	private Dice dice = new Dice();

	/////////////////////////////////////////////////////////////////////////////////////////
	//Constructor for a random individual
	public Individual() {
		Grafo prefenotype = new Grafo();
		Grafo pool = new Grafo(Main.problemgraph);
		//Build fenotype: obtain genes
		numGenes = dice.roll(1, Main.problemgraph.getNumArcs());//number of individual's genes random generation
		for (int i = 0; i < numGenes; i++){
			int index = dice.roll(0, (pool.getNumArcs() - 1)); //select a random arc 0-based index
			prefenotype.addArc(pool.getArcFromIndex(index));
			pool.deleteArc(index);
		}
		//Build fenotype: sort genes
		fenotype = sortFenotype(prefenotype);
		if (fenotype == null){
			valid = false;
			fenotype = prefenotype;
		}
		else valid = true;
	}
 
	/////////////////////////////////////////////////////////////////////////////////////////
	//Constructor for a child individual
	public Individual(Individual ind1, Individual ind2) {
		fenotype = new Grafo();
		Grafo prefenotype = new Grafo();
		//Build fenotype: obtain genes
		Grafo fenotype1 = ind1.getFenotype();
		Grafo fenotype2 = ind2.getFenotype();
		int numGenesFrom1 = ind1.getNumOfGenes() / 2;
		int numGenesFrom2 = ind2.getNumOfGenes() / 2;
		for (int i = 0; i < numGenesFrom1; i++){
			prefenotype.addArc(fenotype1.getArcFromIndex(i));
		}
		for (int i = numGenesFrom2; i < ind2.getNumOfGenes(); i++){
			prefenotype.addArc(fenotype2.getArcFromIndex(i));
		}
		numGenes = numGenesFrom1 + numGenesFrom2;
		if(ind2.getNumOfGenes() % 2 == 1) numGenes++;
		//Build fenotype: sort genes
		fenotype = sortFenotype(prefenotype);
		if (fenotype == null){
			valid = false;
			fenotype = prefenotype;
		}
		else valid = true;
	}

	/////////////////////////////////////////////////////////////////////////////////////////
	//Constructor for a mutant individual
	public Individual(Individual mutant, double mProb) {
		Grafo prefenotype = new Grafo();
		//Build fenotype: obtain genes
		numGenes = mutant.getNumOfGenes();
		for (int i = 0; i < numGenes; i++){
			if (Math.random() < mProb) {
				int index = dice.roll(0, (Main.problemgraph.getNumArcs() - 1)); //select a random arc 0-based index
				prefenotype.addArc(Main.problemgraph.getArcFromIndex(index));
			}
			prefenotype.addArc(mutant.getFenotype().getArcFromIndex(i));
		}
		//Build fenotype: sort genes
		fenotype = sortFenotype(prefenotype);
		if (fenotype == null){
			valid = false;
			fenotype = prefenotype;
		}
		else valid = true;
	}

	public int getNumOfGenes(){
		return numGenes;
	}
	
	public int getFitness(){
		return fitness;
	}

	public boolean getValidity() {
		return valid;
	}
	
	public Grafo getFenotype(){
		return fenotype;
	}

	public void setFitness(int f) {
		fitness = f;
	}

	/*
	 * This method tries to convert a set of Arc elements
	 * into a proper solution, that is, a sequence of Arc
	 * elements that stars at 'startNode', ends at 'endNode'
	 * and the all remain elements are connected consecutively
	 * 
	 * If the method success, return the ordered fenotype.
	 * Otherwise return null.
	 */
	private Grafo sortFenotype(Grafo f) {
		if (f.isEmpty()) return null;
		Arc arc;
		//special case: 1-arc solution
		if(f.getNumArcs() == 1){
			arc = f.getArcFromIndex(0);
			if(arc.getNode1() == Main.startNode && arc.getNode2() == Main.endNode) return f;
			if(arc.getNode2() == Main.startNode && arc.getNode1() == Main.endNode) return f;
			return null;
		}
		//general case
		Grafo unsorted = new Grafo(f);//copy the graph
		Grafo sorted = new Grafo();
		int node = Main.startNode;
		int index;
		do{
			index = unsorted.findIndex(node);
			if (index == -1) return null;
			arc = unsorted.getArcFromIndex(index);
			//get next node
			if (arc.getNode1() == node) node = arc.getNode2();
			else node = arc.getNode1();
			if (sorted.contains(arc)) return null;
			sorted.addArc(arc);
			unsorted.deleteArc(index);
		} while(!unsorted.isEmpty());	
		//check if last arc contains the endNode
		if (arc.getNode1() == Main.endNode || arc.getNode2() == Main.endNode){
			//check if arc before last arc contains the endNode
			index = sorted.getNumArcs() - 2;
			arc = sorted.getArcFromIndex(index);
			if(arc.getNode1() != Main.endNode && arc.getNode2() != Main.endNode) return sorted;
		}
		return null;
	}
	
	public boolean equals(Individual ind){
		return ind.getFenotype().equals(fenotype);
	}

}
