package hmm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class DataSorter {
	String file;
	Map dataset;
	public DataSorter(String filename){
		file = filename;
		dataset = new HashMap();
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
		    	int graphemes=0;
		    	int phonemes=0;
		    	String[] temp=mod_line.split(" ");
		    	if(temp.length-1==temp[0].length()){
		    		
		    	}
		    }
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
}
