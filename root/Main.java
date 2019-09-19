import java.util.Iterator;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class Main {

	public static Grafo problemgraph;
	public static int startNode;
	public static int endNode;
	private static int testSize = 100;
	
	
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Usage: main graphFilePath startNode endNode");
			return;
		}
        else {
        	IO io = new IO(args[0]);
        	System.out.println("READING GRAPH FILE...");
        	problemgraph = io.loadGraph();
        	System.out.println("GRAPH FILE READ");
    	    startNode = Integer.parseInt(args[1]);
    		endNode = Integer.parseInt(args[2]);
    		solveGeneticProblem(io);
    		//solveDijkstra();
    		//testGeneticAlgorithm(io, testSize);        	
        }
	}

	private static void solveGeneticProblem(IO io) {
		GeneticProblem gp = new GeneticProblem(io);
		System.out.println("SOLVING GP...");
		Graph guigraph = showGraph(problemgraph);
    	int generation = gp.solve();
    	System.out.println("END");
    	System.out.println("START WRITING RESULTS...");
    	io.writeFitnessResult();
    	System.out.println("RESULTS WROTTEN");
    	hihglightGraph(guigraph, gp.getPopulation().getBest());
    	printSolution(gp.getPopulation(), generation);
	}	
	
	private static void solveDijkstra() {
		Graph guigraph = showGraph(problemgraph);
		Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "length");
		// Compute the shortest paths in g from StartNode to all nodes
		dijkstra.init(guigraph);
		dijkstra.setSource(guigraph.getNode(Integer.toString(startNode)));
		dijkstra.compute();
		// Color in red all the nodes on the shortest path form StartNode to endNode
		for (Node node : dijkstra.getPathNodes(guigraph.getNode(Integer.toString(endNode))))
			node.addAttribute("ui.style", "fill-color: red;");
		// Print the shortest path from StartNode to endNode
		System.out.println(dijkstra.getPath(guigraph.getNode(Integer.toString(endNode))));
	}

	private static void testGeneticAlgorithm(IO io, int size) {
		System.out.println("TESTING GA...");
    	for (int i = 1; i <= size; i++){
    		GeneticProblem gp = new GeneticProblem(io);
    		int performance = gp.solve();
    		System.out.println(i + " of " + size + ": " + performance);
    		io.storeGeneration(performance);
    	}
    	System.out.println("END");
    	System.out.println("WRITING RESULTS...");
    	io.writePerformanceResult();
    	System.out.println("RESULTS WROTTEN");
	}

	private static Graph showGraph(Grafo prblmgraph) {
		//setting graph properties
		Graph guigraph = new SingleGraph("GUIgraph");
		String styleSheet = "node.marked { fill-color: red; }" + "edge.marked { fill-color: red; }";
		guigraph.addAttribute("ui.stylesheet", styleSheet);
	    guigraph.addAttribute("ui.quality");
	    guigraph.addAttribute("ui.antialias");
	    guigraph.setStrict(false);
		guigraph.setAutoCreate(true);
	    //building graph
	    guigraph.display();
		for(Arc arc : prblmgraph.getArcs()){
			String node1 = Integer.toString(arc.getNode1());
			String node2 = Integer.toString(arc.getNode2());
			String id = Integer.toString(arc.getId());
			int weight = arc.getWeight();
			guigraph.addEdge(id,node1,node2).addAttribute("length", weight);
		}
		//adding label to nodes
		for (Node node : guigraph) node.addAttribute("label", node.getId());
		//adding label to arcs
		for (Edge edge : guigraph.getEachEdge()) edge.addAttribute("label",edge.getId() + "(" + (int) edge.getNumber("length") + ")");
		//marking start and end
		guigraph.getNode(Integer.toString(startNode)).setAttribute("ui.class", "marked");
		guigraph.getNode(Integer.toString(endNode)).setAttribute("ui.class", "marked");
		return guigraph;
	}

	private static void printSolution(Population population, int generation) {
		Individual best = population.getBest();
		Grafo bestpath = best.getFenotype();
		Iterator<Arc> it = bestpath.getArcs().iterator();
		System.out.println("*****************************");
		while(it.hasNext()){
			Arc arc = it.next();
			System.out.println("Arc: " + arc.getId() + "(" + arc.getWeight() + ")");
		}
		System.out.println("Total Weight: " + bestpath.getWeight());
		System.out.println("Valid Solution: " + best.getValidity());
		System.out.println("Generation: " + generation);
		System.out.println("*****************************");
	}

	private static void hihglightGraph(Graph g, Individual best) {
		//highlighting solution
		Iterator<Arc> it = best.getFenotype().getArcs().iterator();
		while(it.hasNext()){
			g.getEdge(Integer.toString(it.next().getId())).setAttribute("ui.class", "marked");
		}
	}
}