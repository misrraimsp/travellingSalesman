import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class IO {
	String readpath, folderpath;
	ArrayList<Integer> bestbuffer;
	ArrayList<Integer> avgbuffer;
	ArrayList<Integer> generationbuffer;
	
	
	public IO(String path){
		folderpath = path.substring(0, path.lastIndexOf('\\'));
		readpath = path;
		bestbuffer = new ArrayList<Integer>();
		avgbuffer = new ArrayList<Integer>();
		generationbuffer = new ArrayList<Integer>();
	}
	
	public Grafo loadGraph() {
		Grafo graph = new Grafo();
		try {
	    	BufferedReader input =new BufferedReader(new FileReader(readpath));
	    	String line = new String();
	    	String[] parameters;
	    	while((line = input.readLine())!= null) {
	    		parameters = line.split(",");
	    		Arc arc = new Arc(parameters);
	    		graph.addArc(arc);
	    	}
	    	input.close();
		}
		catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		return graph;
	}

	public void storeFitness(int bestFitness, int averageFitness) {
		bestbuffer.add(bestFitness);
		avgbuffer.add(averageFitness);
	}
	
	public void storeGeneration(int generation) {
		generationbuffer.add(generation);
	}

	public void writeFitnessResult() {
		String filename = "Fitness" + GeneticProblem.populationSize + "." + GeneticProblem.cProb + "." + GeneticProblem.mProb + ".txt";
		try{
			File fout = new File(folderpath + "\\" + filename);
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			for(int i = 0; i < bestbuffer.size(); i++){
				bw.write(bestbuffer.get(i) + "," + avgbuffer.get(i));
				bw.newLine();
			}
			bw.close();
		}
		catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void writePerformanceResult() {
		String filename = "Performance" + GeneticProblem.populationSize + "." + GeneticProblem.cProb + "." + GeneticProblem.mProb + ".txt";
		try{
			File fout = new File(folderpath + "\\" + filename);
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			for(int i = 0; i < generationbuffer.size(); i++){
				bw.write((i + 1) + "," + generationbuffer.get(i));
				bw.newLine();
			}
			bw.close();
		}
		catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

}