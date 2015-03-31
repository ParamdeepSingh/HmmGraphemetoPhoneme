package hmm;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		DataSorter ds = new DataSorter("cmudict-0.7b");
//		ds.dataReader();
//		char[] y=new char[] {'F','E','E','L'};
//		ds.Viterbi(y);
		
		DataSorterRev ds2 = new DataSorterRev("cmudict-0.7b");
		ds2.dataReader();
		String[] y=new String[] {"K", "AA1", "M", "AH0"};
		ds2.Viterbi(y);
	}

}
