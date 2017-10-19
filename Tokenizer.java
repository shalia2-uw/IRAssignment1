package com;

import static java.util.stream.Collectors.toMap;

import java.io.*;
import java.util.*;

public class Tokenizer {
	long res =0,uniqueCnt=0,numOfFiles=0;
	HashMap<String, Integer> map = new HashMap<>();
	HashMap<String, Integer> sortMap = new HashMap<>();
	List<Double> idfFreq = new ArrayList<>();
	List<Integer> tfFreq = new ArrayList<>();
	File dir  = new File("C:\\Users\\Shalini\\Documents\\Information Retrival\\transcripts\\");
	
	/*term frequency*/
	public List<Integer> tf(HashMap<String, Integer> sortMap) {
		List<Integer> tfFreq = new ArrayList<>();
		int reg =0;
		System.out.println("Term Frequencies of top 30 frequent tokens");
		for(Map.Entry<String, Integer> entry : sortMap.entrySet()){
			if(reg<30){
				System.out.print(entry.getKey()+":"+entry.getValue()+" ");
				tfFreq.add(entry.getValue());
			}else
				break;
			reg++;
		}
		return tfFreq;
    }
	/*Inverse term frequency*/
	public double idf(long numOfFiles,File dir,String word) throws Exception{	
		long total=0;
		for(File file : dir.listFiles()){
			Scanner sc = new Scanner(file);
			String str = sc.nextLine();
			if(str.toLowerCase().contains(word))
				total++;
			sc.close();
		}
		System.out.print(word+":"+Math.log(numOfFiles/total)+" ");
		return Math.log(numOfFiles/total);
	}
	/*tf-idf*/
	public void tfIdf(List<Integer> l1,List<Double> l2){		
		for(int i=0;i<30;i++){
			System.out.print(l1.get(i)*l2.get(i)+" ");
		}
	}
	/*Single occurrence of value*/
	public void singleOccurance(){
		for(Map.Entry<String, Integer> entry : map.entrySet()){						
			int val = entry.getValue();
			if(val==1){
				uniqueCnt= uniqueCnt +1;
			}
		}
		System.out.println("Words that occur only once in database:"+uniqueCnt);
	}
	/*Sorting the map elements*/
	public void sort(){
		sortMap = map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                LinkedHashMap::new));
	}
	/*File Reading*/
	public void readFile() throws Exception{
		for(File file : dir.listFiles()){
			Scanner sc = new Scanner(file);
			String str = sc.nextLine();
			StringTokenizer st = new StringTokenizer(str.toLowerCase(), "[ ,.]");
			long cnt =0;
			while(st.hasMoreTokens()){				
				String s = st.nextToken();				
				if(map.containsKey(s)){
					map.put(s, map.get(s)+1);
				}else{
					map.put(s,1);
				}
				cnt++;				
			}										
			res = res+cnt;
			numOfFiles++;
			sc.close();
		}
		System.out.println("Total word tokens in database:"+res);
		System.out.println("Total unique tokens in database:"+map.size());
	}
	/*Output file path*/
	public void writeFile() throws Exception{
		PrintStream console = System.out;
		File f = new File("C:\\Users\\Shalini\\Documents\\Information Retrival\\out.txt");
		FileOutputStream fos = new FileOutputStream(f);
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);
		console.close();
	}
	public void getProbabilities(){
		int i=0;
		double prob=0.0;
		System.out.println("Probabilities of top 30 tokens");
		for(Map.Entry<String, Integer> entry : sortMap.entrySet()){
			if(i<30){
				prob = entry.getValue()/res;
				System.out.print(entry.getKey()+":"+prob+" ");
			}else
				break;
			i++;
		}
	}
	/*Average of frequencies*/
	public void getAverage(){
		System.out.println();
		System.out.println("Average of frequencies:"+res/numOfFiles);		
	}
	public static void main(String[] args) throws Exception {
		Tokenizer nt = new Tokenizer();
		nt.writeFile();
		nt.readFile();
		nt.singleOccurance();
		nt.sort();
		nt.tfFreq= nt.tf(nt.sortMap);
		int register =0;
		System.out.println();
		System.out.println("Inverse Term Frequencies of top 30 frequent tokens");
		for(Map.Entry<String, Integer> entry : nt.sortMap.entrySet()){
			if(register<30)
				nt.idfFreq.add(nt.idf(nt.numOfFiles, nt.dir, entry.getKey()));
			else
				break;
			register++;
		}
		System.out.println();
		System.out.println("TFIDF values");
		nt.tfIdf(nt.tfFreq, nt.idfFreq);
		System.out.println();
		nt.getProbabilities();
		nt.getAverage();
	}
}
