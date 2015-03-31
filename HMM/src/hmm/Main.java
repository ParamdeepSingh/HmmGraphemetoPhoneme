package hmm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataSorter ds = new DataSorter("cmudict-0.7b");
		ds.dataReader();
		List<Character> y = new ArrayList<Character>();
		for (Map.Entry<Character, Integer> entry : ds.ts_graph_to_num.entrySet()){
			
			if(!entry.getKey().equals('#')){
				y.add(entry.getKey());
//				System.out.println(y.toString());
			}
			else{
//				System.out.println(y.toArray());
				ds.Viterbi(y.toArray(new Character[y.size()]));
				y = new ArrayList<Character>();
			}
		}
//		Character[] y=new Character[] {'F','E','E','L'};
//		ds.Viterbi(y);
		
//		DataSorterRev ds2 = new DataSorterRev("cmudict-0.7b");
//		ds2.dataReader();
//		String[] y=new String[] {"K", "AA1", "M", "AH0"};
//		ds2.Viterbi(y);
	}

}
