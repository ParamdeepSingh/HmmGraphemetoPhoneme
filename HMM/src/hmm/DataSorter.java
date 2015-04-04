package hmm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataSorter {
	String file;
	Map<Character, Integer> tr_graph_to_num = new HashMap<Character, Integer>();
	Map<String, Integer> tr_phone_to_num = new HashMap<String, Integer>();
	Map<Integer, Character> ts_graph_to_num = new HashMap<Integer, Character>();
	Map<Integer, String> ts_phone_to_num = new HashMap<Integer, String>();
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
		try {
			BufferedReader br = new BufferedReader(new FileReader(file)); 
		    String line;
		    String mod_line;
		    int line_num=0;
		    int count_ts_graph=0;
		    int count_ts_phone=0;
		    while ((line = br.readLine()) != null) {
		    	line_num++;
		    	mod_line="";
		    	for(int i=0;i<line.length();i++){
		    		if(Character.isLetter(line.charAt(i))||line.charAt(i)==' '||Character.isDigit(line.charAt(i))){
		    			if(i==0||(i!=0&&!(line.charAt(i-1)==' '&&line.charAt(i)==' ')))
		    			mod_line = mod_line + line.charAt(i);
		    		}
		    	}

		    	int prev_phone=-1;
		    	String[] temp=mod_line.split(" ");
		    	if(temp.length-1==temp[0].length()){
		    		//System.out.println(temp[0]);
		    		//System.out.println(count_phone);
		    		//System.out.println(count_graph);
		    		if(line_num%5==2){
		    			
		    			for(int i=0; i<temp[0].length(); i++){
		    				ts_graph_to_num.put(count_ts_graph, temp[0].charAt(i));
		    				ts_phone_to_num.put(count_ts_phone, temp[i+1]);
		    				count_ts_graph++;
		    				count_ts_phone++;
		    			}
		    			ts_graph_to_num.put(count_ts_graph, '#');
	    				ts_phone_to_num.put(count_ts_phone, "#");
	    				count_ts_graph++;
	    				count_ts_phone++;
	    				continue;
		    		}
		    		for(int i=0; i<temp[0].length(); i++)
		    		{
		    			if(!tr_graph_to_num.containsKey(temp[0].charAt(i)))
		    			{
		    				tr_graph_to_num.put(temp[0].charAt(i), count_graph);
		    				for(int j=0; j<count_phone; j++)
		    				{	
		    					(emission_count.get(j)).add(0); 
		    				}
		    				count_graph++;
		    			}
		    			int graph_index = tr_graph_to_num.get(temp[0].charAt(i));
		    			if(!tr_phone_to_num.containsKey(temp[i+1]))
		    			{
		    				System.out.println(temp[i+1]);
		    				tr_phone_to_num.put(temp[i+1], count_phone);
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
		    			int phone_index = tr_phone_to_num.get(temp[i+1]);	
		    		
		    			emission_count.get(phone_index).set(graph_index, emission_count.get(phone_index).get(graph_index)+1); 

		    			if(i!=0)
		    			{
		    				transition_count.get(prev_phone).set(phone_index, transition_count.get(prev_phone).get(phone_index)+1); 
		    			}
		    			else
		    			{
		    				start_count.set(phone_index, start_count.get(phone_index)+1);
		    			}
	    				prev_phone= phone_index;
		    		}
		    	}
		    }
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e);
		}
		
		emission_prob = new float[count_phone][count_graph];
		transition_prob = new float[count_phone][count_phone];
		start_prob = new float[count_phone];
		
		
//		for(int j=0; j<count_phone; j++)
//			System.out.print(start_count.get(j));
			
//		for(int i=0; i<count_phone; i++){
//			for(int j=0; j<count_phone; j++)
//				System.out.print(transition_count.get(i).get(j));
//			System.out.println();
//		}
		
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
			{
				if(sum!=0.0)
					transition_prob[i][j]= ( (float) transition_count.get(i).get(j))/sum;
				else 
					transition_prob[i][j] = ((float)1)/count_phone;
			}
			sumall+=start_count.get(i);
		}
		for(int i=0; i<count_phone; i++)
			start_prob[i]= ( (float) start_count.get(i))/sumall;
	
		
//		for(int i=0; i<count_phone; i++){
//			for(int j=0; j<count_graph; j++)
//				System.out.print(emission_prob[i][j] + " ");
//			System.out.println();
//		}
		for(int i=0; i<count_phone; i++){
//			System.out.print(start_prob[i]+"  ");
		}
//		System.out.println();
	}
	 
	public boolean Viterbi(Character[] y, Integer startIndex, Integer length){
//		System.out.println('#'+y.toString());
		V = new float[y.length][count_phone];
		x = new int[y.length];
		ptr = new int[y.length][count_phone];
		for(int i=0; i<count_phone; i++)
			ptr[0][i]=i;
		
		for(int j=0; j<count_phone; j++){
			V[0][j] = emission_prob[j][tr_graph_to_num.get(y[0])]*start_prob[j];
//			System.out.print(V[0][j]+" ");
		}
//		System.out.println();
		
		for(int i=1; i<y.length; i++){
			for(int j=0; j<count_phone; j++){
				float high=0;
				float temp=0;
				int index=0;
				for(int k=0; k<count_phone; k++){
					temp = emission_prob[j][tr_graph_to_num.get(y[i])]*transition_prob[k][j]*V[i-1][k];
					//System.out.print(temp);
					if(temp>high){
						high=temp;
						index=k;
					}
				}
				V[i][j]=high;
//				System.out.print(V[i][j]+" ");
				ptr[i][j]=index;
			}
//			System.out.println();
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
			x[i]=ptr[i+1][x[i+1]];
		boolean same=true;
		for(int i=0; i<x.length; i++){
			for (Map.Entry<String, Integer> entry : tr_phone_to_num.entrySet()) {
		        if ((new Integer(x[i])).equals(entry.getValue())) {
		        	if(!entry.getKey().equals(ts_phone_to_num.get(startIndex+i))){
		        		same=false;//phone_to_num.g(x[i]);
		        		return same;
		        	}
		        }
			}
		}
		return same;
	}
}
