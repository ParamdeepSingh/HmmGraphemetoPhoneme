package hmm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataSorter ds = new DataSorter("cmudict-0.7b");
		ds.dataReader();
		List<Character> y = new ArrayList<Character>();
		System.out.println(ds.ts_graph_to_num);
		int startIndex=0;
		int length=0;
		int testNum=0;
		int correct=0;
		for (Entry<Integer, Character> entry : ds.ts_graph_to_num.entrySet()){
			
			if(!entry.getValue().equals('#')){
				y.add(entry.getValue());
				length++;
//				System.out.println(y.toString());
			}
			else{
//				System.out.println(y.toArray(new Character[y.size()]).toString());
				if(length!=0){
					testNum++;
				}
				if(length!=0&&ds.Viterbi(y.toArray(new Character[y.size()]), startIndex, length)){
					correct++;
				}
				y = new ArrayList<Character>();
				startIndex=entry.getKey()+1;
				length=0;
			}
		}
		System.out.println((double)correct/testNum);
//		Character[] y=new Character[] {'F','E','E','L'};
//		ds.Viterbi(y);
		
//		DataSorterRev ds2 = new DataSorterRev("cmudict-0.7b");
//		ds2.dataReader();
//		String[] y=new String[] {"K", "AA1", "M", "AH0"};
//		ds2.Viterbi(y);
	}

}
