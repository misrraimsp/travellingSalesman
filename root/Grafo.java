import java.util.ArrayList;
import java.util.Iterator;

public class Grafo {
	private ArrayList<Arc> arcs;
	private int weight;//sum of weights
	
	public Grafo(){
		arcs = new ArrayList<Arc>();
		weight = 0;
	}
	
	//Constructor by copying
	public Grafo(Grafo g) {
		arcs = new ArrayList<Arc>();
		weight = g.getWeight();
		Iterator<Arc> it = g.getArcs().iterator();
		while(it.hasNext()){
			arcs.add(it.next());
		}
	}

	public void addArc(Arc arc){
		arcs.add(arc);
		weight = weight + arc.getWeight();
	}
	
	public Arc getArcFromIndex(int index){
		return arcs.get(index);
	}
	
	public Arc getArcFromId(int id){
		Arc arc = null;
		Iterator<Arc> it = arcs.iterator();
		int aux = -1;
		while(aux != id && it.hasNext()){
			arc = it.next();
			aux = arc.getId();
		}
		return arc;
	}
	
	public int getNumArcs(){
		return arcs.size();
	}
	
	public ArrayList<Arc> getArcs(){
		return arcs;
	}
	
	public int getWeight() {
		return weight;
	}

	public boolean isEmpty() {
		return arcs.isEmpty();
	}

	/*
	 * Find the index in the graph ArrayList that
	 * reference an Arc element in which there exist
	 * a node with value 'node'.
	 * 
	 * If found, return index. Otherwise return -1
	 */
	public int findIndex(int node) {
		Iterator<Arc> it = arcs.iterator();
		int index = 0;
		while(it.hasNext()){
			Arc arc = it.next();
			if (arc.getNode1() == node || arc.getNode2() == node) return index;
			index++;
		}
		return -1;
	}

	public void deleteArc(int index) {
		weight = weight - arcs.get(index).getWeight();
		arcs.remove(index);
	}

	public void printFenotype() {
		if(arcs.isEmpty()){
			System.out.println("There is no gene is this fenotype");
			return;
		}
		String f ="";
		Iterator<Arc> it = arcs.iterator();
		while (it.hasNext()){
			f = f + it.next().getId() + ",";
		}
		System.out.println(f);
	}
	
	public boolean equals(Grafo g){
		ArrayList<Arc> garcs = g.getArcs();
		return (garcs.equals(arcs));
	}

	public boolean contains(Arc arc) {
		Iterator<Arc> it = arcs.iterator();
		while(it.hasNext()){
			if (arc.equals(it.next())) return true;
		}
		return false;
	}

	public int getWorstWeight() {
		int w = 0;
		Iterator<Arc> it = arcs.iterator();
		while(it.hasNext()){
			int aux = it.next().getWeight();
			if(aux > w) w = aux;
		}
		return w;
	}
	
}
