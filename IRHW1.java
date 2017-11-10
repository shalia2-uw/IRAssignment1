package com;

import static java.util.stream.Collectors.toMap;

import java.io.*;
import java.util.*;

public class IRHW1 {
	HashMap<String, Integer> map = new HashMap<>();
	HashMap<String, Integer> sortMap = new HashMap<>();
	int total=0,uniqueCnt=0,noOfDocs=0;
	File dir  = new File("C:\\Users\\Shalini\\Documents\\Information Retrival\\transcripts\\");
	ArrayList<String> al = new ArrayList<>();
	ArrayList<Double> d1 =new ArrayList<>(), d2 = new ArrayList<>();
	/*reading files*/
	public void readFile() throws Exception{
		for(File file : dir.listFiles()){
			getTokens(file);
			noOfDocs++;
		}
	}
	/*tokenization*/
	public void getTokens(File file){
		BufferedReader in = null;
		StringTokenizer st;
		try {
			in = new BufferedReader(new FileReader(file));
			String line="";
			while((line=in.readLine())!=null){
				st = new StringTokenizer(line.toLowerCase().trim(), " .,--:?!&{}//\\\"");
				while(st.hasMoreTokens()){
					String s = st.nextToken();
					if(map.containsKey(s)){
						map.put(s, map.get(s)+1);
					}else{
						map.put(s,1);
					}
					total++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*Unique occurrence*/
	public void singleOccurance(){
		int uniqueCnt=0;
		for(String sr:map.keySet()){
			int val = map.get(sr);
			if(val==1){
				uniqueCnt++;
			}
		}
		System.out.println("Words that occur only once in database:"+uniqueCnt);
	}
	/*Sorting the values*/
	public void sort(){
		sortMap = map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                LinkedHashMap::new));
	}
	/*Term frequency*/
	public void getTop(){
		sort();
		int top=1;
		System.out.print("TF:");
		for(String sr:sortMap.keySet()){
			if(top>30)
				break;
			al.add(sr);
			System.out.print("'"+sr+"': "+sortMap.get(sr)+" ");
			d1.add((double)sortMap.get(sr));
			top++;
		}
	}
	/*Average of frequencies*/
	public void getAverage(){
		System.out.println();
		double avg = total/(double)noOfDocs;
		System.out.println("Average of frequencies:"+avg);		
	}
	/*Probabilities*/
	public void getProbability(){
		System.out.print("Probabilities");
		for(String s:al){
			int a = sortMap.get(s);
			double d = (a/(double)total);
			System.out.print(s+": "+d+" ");
		}
	}
	/*IDF*/
	public void getTerm(){
		System.out.println();
		System.out.print("IDF:");
		for(String s:al){
			getListDocs(s);
		}
	}
	public void getListDocs(String al){
		int x=0;
		for(File file:dir.listFiles()){
			BufferedReader in = null;
			StringTokenizer st;
			try {
				in = new BufferedReader(new FileReader(file));
				String line="";
				label:while((line=in.readLine())!=null){
					st = new StringTokenizer(line.toLowerCase().trim(), " .,--:?!&{}//\\\"");
					while(st.hasMoreTokens()){
						String s = st.nextToken();
						if(s.equalsIgnoreCase(al)){
							x++;
							break label;
						}
							
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		double val = Math.log(noOfDocs/(double)x);
		d2.add(val);
		System.out.print(al+": "+val+" ");
	}
	public void gettfidf(ArrayList<Double> d1,ArrayList<Double> d2){
		System.out.println("TF*IDF");
		for(int i=0;i<30;i++){
			System.out.print(al.get(i)+": "+d1.get(i)*d2.get(i)+" ");
		}
	}
	/*writing data into a file*/
	public void writeFile() throws Exception{
		PrintStream console = System.out;
		File f = new File("C:\\Users\\Shalini\\Documents\\Information Retrival\\out.txt");
		FileOutputStream fos = new FileOutputStream(f);
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);
		console.close();
	}
	public static void main(String[] args) throws Exception{
		IRHW1 ir = new IRHW1();
		try{
			ir.readFile();
			ir.writeFile();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("Total word tokens in database:"+ir.total);
		System.out.println("Total unique tokens in database:"+ir.map.size());
		ir.singleOccurance();
		ir.getTop();
		ir.getAverage();
		ir.getProbability();
		ir.getTerm();
		System.out.println();
		ir.gettfidf(ir.d1, ir.d2);
	}
	
}
