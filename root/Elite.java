
public class Elite {
	
	private Individual[] elite;
	int maxsize;
	int size;

	public Elite(int elitesize) {
		size = 0;
		maxsize = elitesize;
		elite = new Individual[elitesize];
	}
	
	public Individual getIndividual(int i) {
		return elite[i];
	}
	
	public int getMaxSize(){
		return maxsize;
	}
	
	public int getSize() {
		return size;
	}
	
	public boolean isFull(){
		return (size == maxsize);
	}
	
	public boolean isEmpty(){
		return (size == 0);
	}
	
	public void deleteWorst(){
		size--;
	}
	
	/*
	 * precondition1: size < maxsize
	 * precondition2: !elite.contains(ind)
	 */
	public void insert(Individual ind){
		int indw = ind.getFenotype().getWeight();
		int index = 0;
		while(index < size && elite[index].getFenotype().getWeight() <= indw) index++;
		if(index == size){
			elite[index] = ind;
			size++;
		}
		else {
			shift(index);
			elite[index] = ind;
			size++;
		}
	}
	
	public boolean contains(Individual ind){
		for(int i = 0; i < size; i++){
			if(ind.equals(elite[i])) return true;
		}
		return false;
	}
	
	public Individual getBest(){
		return elite[0];
	}

	public int getThreshold(){
		return elite[size - 1].getFenotype().getWeight();
	}
	
	private void shift(int index) {
		for(int i = size; i > index; i--){
			elite[i] = elite[i-1];
		}
	}

}
