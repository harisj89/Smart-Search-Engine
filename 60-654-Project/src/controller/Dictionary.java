package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

public class Dictionary {
	
	public void dictionaryConstrutor(){ 
		
		String line = "";
		StringTokenizer stringTokenizer = null;
		
		String url = TestTasks.class.getClassLoader()
		.getResource("WebFiles").getPath();

		File dir = new File(url);
		BufferedReader br = null;
		List<String> words = new ArrayList<String>();
		try {
			
		for(File f : dir.listFiles()){ 
			System.out.println(f.getName());
			br = new BufferedReader(new FileReader(f));
			
			while ((line = br.readLine()) != null) {

				stringTokenizer = new StringTokenizer(line," .,'\"\"*&^%$#@`~-_+=;:?<>()[]/|!\t\n\r\f,.:;?![]'\\{}");
				
				
				while (stringTokenizer.hasMoreElements()) {

					String token = stringTokenizer.nextElement().toString();

					StringBuilder sb = new StringBuilder();
					sb.append(token);
					String aSingleToken = sb.toString();
					words.add(aSingleToken);
				}
//				writingFile(lineWords, "Dictionary.txt");
				
			}

			
		}
		
		//Removing duplicating values in array list
		HashSet<String> hs = new HashSet<String>();
		hs.addAll(words);
		ArrayList<String> uniqueWords = new ArrayList<String>();
		uniqueWords.addAll(hs);
		
		writingFile(uniqueWords, "Dictionary.txt");
		
		}
		catch(Exception ex){ 
			System.out.println("Exception at dictionary occured\n");
		}
	}
	
	//Writes the list into the filename passed
	private void writingFile(List<String> subList, String fileName) {

		try {

			File file = new File(fileName);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			for (String string : subList) {
				bw.write(string.trim() + "\n");
			}
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	
	public static void main(String[] args) { 
		
//		String url = TestTasks.class.getClassLoader()
//		.getResource("WebFiles").getPath();
//
//		File dir = new File(url);
//		for(File f : dir.listFiles()){ 
//			System.out.println(f.getName());
//			
//		}
		
		Dictionary dictionary = new Dictionary();
		dictionary.dictionaryConstrutor();
	}

}
