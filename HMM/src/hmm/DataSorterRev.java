package hmm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataSorterRev {
	String file;
	Map<Character, Integer> graph_to_num = new HashMap<Character, Integer>();
	Map<String, Integer> phone_to_num = new HashMap<String, Integer>();
	int count_graph = 0;
	int count_phone = 0;
	ArrayList<ArrayList<Integer>> emission_count = new ArrayList<ArrayList<Integer>>();
	ArrayList<ArrayList<Integer>> transition_count = new ArrayList<ArrayList<Integer>>();
	ArrayList<Integer> start_count = new ArrayList<Integer>();
	
	float[][] emission_prob;
	float[][] transition_prob;
	float[] start_prob;
	
	
	float[][] V;
	int[] x;
	int[][] ptr;
	
	public DataSorterRev(String filename){
		file = filename;
	}
	public void dataReader(){
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    String mod_line;
		    while ((line = br.readLine()) != null) {
		    	mod_line="";
		    	for(int i=0;i<line.length();i++){
		    		if(Character.isLetter(line.charAt(i))||line.charAt(i)==' '||Character.isDigit(line.charAt(i))){
		    			if(i==0||(i!=0&&!(line.charAt(i-1)==' '&&line.charAt(i)==' ')))
		    			mod_line = mod_line + line.charAt(i);
		    		}
		    	}

		    	int prev_graph=-1;
		    	String[] temp=mod_line.split(" ");
		    	if(temp.length-1==temp[0].length()){
		    		//System.out.println(temp[0]);
		    		//System.out.println(count_phone);
		    		//System.out.println(count_graph);
		    		for(int i=0; i<temp[0].length(); i++)
		    		{
		    			if(!graph_to_num.containsKey(temp[0].charAt(i)))
		    			{
		    				graph_to_num.put(temp[0].charAt(i), count_graph);
		    				
		    				emission_count.add(new ArrayList<Integer>());
		    				transition_count.add(new ArrayList<Integer>());
		    				start_count.add(0);
		    				
		    				for(int j=0; j<count_phone; j++)
		    				{	
		    					(emission_count.get(count_graph)).add(0); 
		    				}
		    				for(int j=0; j<count_graph; j++)
		    				{	
		    					(transition_count.get(count_graph)).add(0); 
		    				}
		    				
		    				count_graph++;
		    				
		    				for(int j=0; j<count_graph; j++)
		    				{	
		    					(transition_count.get(j)).add(0); 
		    				}
		    			}
		    			int graph_index = graph_to_num.get(temp[0].charAt(i));
		    			if(!phone_to_num.containsKey(temp[i+1]))
		    			{
		    				System.out.println(temp[i+1]);
		    				phone_to_num.put(temp[i+1], count_phone);
		    				
		    				for(int j=0; j<count_graph; j++)
		    				{	
		    					(emission_count.get(j)).add(0); 
		    				}
		    				
		    				count_phone++;
		    				
		    			}
		    			int phone_index = phone_to_num.get(temp[i+1]);	
		    		
		    			emission_count.get(graph_index).set(phone_index, emission_count.get(graph_index).get(phone_index)+1); 

		    			if(i!=0)
		    			{
		    				transition_count.get(prev_graph).set(graph_index, transition_count.get(prev_graph).get(graph_index)+1); 
		    			}
		    			else
		    			{
		    				start_count.set(graph_index, start_count.get(graph_index)+1);
		    			}
	    				prev_graph= graph_index;
		    		}
		    	}
		    }
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e);
		}
		
		emission_prob = new float[count_graph][count_phone];
		transition_prob = new float[count_graph][count_graph];
		start_prob = new float[count_graph];
		
		
//		for(int j=0; j<count_graph; j++)
//			System.out.print(start_count.get(j)+" ");
//		System.out.println();
//		for(int i=0; i<count_graph; i++){
//			for(int j=0; j<count_graph; j++)
//				System.out.print(transition_count.get(i).get(j) + "  ");
//			System.out.println();
//		}
		
		int sumall=0;
		
		for(int i=0; i<count_graph; i++){
			int sum = 0;
			for(int j=0; j<count_phone; j++)
				sum+=emission_count.get(i).get(j);
			for(int j=0; j<count_phone; j++)
				emission_prob[i][j]= ( (float) emission_count.get(i).get(j))/sum;
			
			sum=0;
			for(int j=0; j<count_graph; j++)
				sum+=transition_count.get(i).get(j);
			for(int j=0; j<count_graph; j++)
			{
				if(sum!=0.0)
					transition_prob[i][j]= ( (float) transition_count.get(i).get(j))/sum;
				else 
					transition_prob[i][j] = ((float)1)/count_graph;
			}
			sumall+=start_count.get(i);
		}
		for(int i=0; i<count_graph; i++)
			start_prob[i]= ( (float) start_count.get(i))/sumall;
	
		
//		for(int i=0; i<count_graph; i++){
//			for(int j=0; j<count_graph; j++)
//				System.out.print(transition_prob[i][j] + " ");
//			System.out.println();
//		}
//		
//		for(int i=0; i<count_graph; i++){
//			System.out.print(start_prob[i]+"  ");
//		}
//		System.out.println();
	}
	 
	public void Viterbi(String[] y){
		V = new float[y.length][count_graph];
		x = new int[y.length];
		ptr = new int[y.length][count_graph];
		
		for(int i=0; i<count_graph; i++)
			ptr[0][i]=i;
		
		for(int j=0; j<count_graph; j++){
			V[0][j] = emission_prob[j][phone_to_num.get(y[0])]*start_prob[j];
			System.out.print(V[0][j]+" ");
		}
		System.out.println();
		
		for(int i=1; i<y.length; i++){
			for(int j=0; j<count_graph; j++){
				float high=0;
				float temp=0;
				int index=0;
				for(int k=0; k<count_graph; k++){
					temp = emission_prob[j][phone_to_num.get(y[i])]*transition_prob[k][j]*V[i-1][k];
					//System.out.print(temp);
					if(temp>high){
						high=temp;
						index=k;
					}
				}
				V[i][j]=high;
				System.out.print(V[i][j]+" ");
				ptr[i][j]=index;
			}
			System.out.println();
		}
		
		
		
		
		float tmp=0;
		int ind=0;
		for(int i=0; i<count_graph; i++){
			if(tmp<V[y.length-1][i]){
				tmp=V[y.length-1][i];
				ind = i;
			}
		}
		
		x[y.length-1]=ind;
		
		for(int i=y.length-2; i>=0; i--)
			x[i]=ptr[i+1][x[i+1]];
		
		for(int i=0; i<x.length; i++)
			for (Map.Entry<Character, Integer> entry : graph_to_num.entrySet()) {
		        if ((new Integer(x[i])).equals(entry.getValue())) {
		        	System.out.println(entry.getKey());//phone_to_num.g(x[i]);
		        }
			}
	}
}
