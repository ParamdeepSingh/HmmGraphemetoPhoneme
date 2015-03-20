package hmm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataSorter {
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
	
	public DataSorter(String filename){
		file = filename;
	}
	public void dataReader(){
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    String mod_line;
		    while ((line = br.readLine()) != null) {
		    	mod_line="";
		    	for(int i=0;i<line.length();i++){
		    		if(Character.isLetter(line.charAt(i))||line.charAt(i)==' '){
		    			if(i==0||(i!=0&&!(line.charAt(i-1)==' '&&line.charAt(i)==' ')))
		    			mod_line = mod_line + line.charAt(i);
		    		}
		    	}

		    	int prev_phone=-1;
		    	String[] temp=mod_line.split(" ");
		    	if(temp.length-1==temp[0].length()){
		    		System.out.println(temp[0]);
		    		
		    		for(int i=0; i<temp[0].length(); i++)
		    		{ 
		    			if(!graph_to_num.containsKey(temp[0].charAt(i)))
		    			{
		    				graph_to_num.put(temp[0].charAt(i), count_graph);
		    				for(int j=0; j<count_phone; j++)
		    				{	
		    					(emission_count.get(j)).add(0); 
		    				}
		    				count_graph++;
		    			}
		    			int graph_index = graph_to_num.get(temp[0].charAt(i));
		    			
		    			if(!phone_to_num.containsKey(temp[i+1]))
		    			{
		    				phone_to_num.put(temp[i+1], count_phone);
		    				emission_count.add(new ArrayList<Integer>());
		    				transition_count.add(new ArrayList<Integer>());
		    				start_count.add(0);
		    				
		    				for(int j=0; j<count_graph; j++)
		    				{	
		    					(emission_count.get(count_phone)).add(0); 
		    				}
		    				for(int j=0; j<count_phone; j++)
		    				{	
		    					(transition_count.get(count_phone)).add(0); 
		    				}
		    				
		    				count_phone++;
		    				
		    				for(int j=0; j<count_phone; j++)
		    				{	
		    					(transition_count.get(j)).add(0); 
		    				}
		    				
		    			}	
		    			int phone_index = phone_to_num.get(temp[i+1]);	
		    		
		    			emission_count.get(phone_index).set(graph_index, emission_count.get(phone_index).get(graph_index)+1); 
		    			
		    			if(i!=0)
		    			{
		    				transition_count.get(prev_phone).set(phone_index, transition_count.get(prev_phone).get(phone_index)+1);
		    				prev_phone = phone_index; 
		    			}
		    			else
		    			{
		    			start_count.set(phone_index, start_count.get(phone_index)+1);
		    			}
		    		}
		    	}
		    }
		}catch(Exception e){
			System.out.println(e);
		}
		
		emission_prob = new float[count_phone][count_graph];
		transition_prob = new float[count_phone][count_phone];
		start_prob = new float[count_phone];
		
		int sumall=0;
		
		for(int i=0; i<count_phone; i++){
			int sum = 0;
			for(int j=0; j<count_graph; j++)
				sum+=emission_count.get(i).get(j);
			for(int j=0; j<count_graph; j++)
				emission_prob[i][j]= ( (float) emission_count.get(i).get(j))/sum;
			
			sum=0;
			for(int j=0; j<count_phone; j++)
				sum+=transition_count.get(i).get(j);
			for(int j=0; j<count_phone; j++)
				transition_prob[i][j]= ( (float) transition_count.get(i).get(j))/sum;
			
			sumall+=start_count.get(i);
		}
		for(int i=0; i<count_phone; i++)
			start_prob[i]= ( (float) start_count.get(i))/sumall;
		
	}
	 
	public void Viterbi(char[] y){
		V = new float[y.length][count_phone];
		x = new int[y.length];
		ptr = new int[y.length][count_phone];
		
		for(int j=0; j<count_phone; j++)
			V[0][j] = emission_prob[graph_to_num.get(y[0])][j]*start_prob[j];
		
		for(int i=1; i<y.length; i++){
			for(int j=0; j<count_phone; j++){
				float high=0;
				float temp=0;
				int index=0;
				for(int k=0; k<count_phone; k++){
					temp = emission_prob[graph_to_num.get(y[i])][j]*transition_prob[k][j]*V[i-1][k];
					if(temp>high){
						high=temp;
						index=k;
					}
				}
				V[i][j]=temp;
				ptr[i][j]=index;
			}
		}
		
		float tmp=0;
		int ind=0;
		for(int i=0; i<count_phone; i++){
			if(tmp<V[y.length-1][i]){
				tmp=V[y.length-1][i];
				ind = i;
			}
		}
		
		x[y.length-1]=ind;
		
		for(int i=y.length-2; i>=0; i--)
			x[i]=ptr[x[i+1]][i+1];
	}
}
