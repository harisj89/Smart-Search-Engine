package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import model.InvertedIndex;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;


public class TstImplementation {

	public void makingTST(){
		
		
		String line = "";
		StringTokenizer stringTokenizer = null;
		
		String url = TestTasks.class.getClassLoader()
		.getResource("WebFiles").getPath();

		File dir = new File(url);
		BufferedReader br = null;
		List<String> words = new ArrayList<String>();
		TSTOriginal<Integer> st = new TSTOriginal<Integer>();
		try {
			
		for(File f : dir.listFiles()){ 
//			System.out.println(f.getName());
			br = new BufferedReader(new FileReader(f));
			
			while ((line = br.readLine()) != null) {

				stringTokenizer = new StringTokenizer(line," .,'\"\"*&^%$#@`~-_+=;:?<>()[]/|!\t\n\r\f,.:;?![]'\\{}");
				
				
				while (stringTokenizer.hasMoreElements()) {

					String token = stringTokenizer.nextElement().toString();

					StringBuilder sb = new StringBuilder();
					sb.append(token);
					String aSingleToken = sb.toString();
					
					if(aSingleToken.length()>0){ 
						if(st.get(aSingleToken) == null){
							st.put(aSingleToken, 1);
						}
						else {
	                        int value = st.get(aSingleToken);
	                        value++;
	                        st.put(aSingleToken, value);
	                    }
					}
					
					
//					words.add(aSingleToken);
				}
//				writingFile(lineWords, "Dictionary.txt");
				
			}
			int fileId = getFileIndex(f.getName());
			//method to insert all values, word, occurences values, fileId
			
			boolean isExist = false;
			
			for (String key : st.keys()) {
//	            StdOut.println(key + " " + st.get(key));
				isExist = getInvertedIndex(key, fileId);
				if(!isExist){
					insertIntoInvertedIndex(key,st.get(key),fileId);
				}
	        }
			br.close();
			
		}
		
		// print results
//        for (String key : st.keys()) {
//            StdOut.println(key + " " + st.get(key));
//        }
		
	}catch(Exception ex){ 
//		System.out.println("Exception at dictionary occured\n");
		ex.printStackTrace();
	}
		
	}
	
	
	public boolean fileIdExist(int fileId){
		
		boolean isExist = false;
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try{
		StringBuilder queryString2 = new StringBuilder();
		
		queryString2.append(" SELECT res.indexId")
		   .append(" FROM InvertedIndex AS res WHERE res.indexId="+fileId);

		Query queryObject2 = session.createQuery(queryString2.toString());
		
		List<Integer> idExist = (List<Integer>) queryObject2.list();	
		
		int fileIdExist= idExist.get(0);
		session.getTransaction().commit();
		}catch(HibernateException e){
//			System.out.println("exception");
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return isExist;
	}
	
	private boolean getInvertedIndex(String word, int fileId){ 
		boolean isExist = false;
		
		List<String> wordPresent = new ArrayList<String>();
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try{
			StringBuilder queryString = new StringBuilder();
			
				queryString.append(" SELECT res.word ")
						   .append(" FROM InvertedIndex AS res WHERE res.indexId="+fileId+" AND res.word='"+word+"'");
				
				Query queryObject = session.createQuery(queryString.toString());
			
	
				wordPresent= (List<String>) queryObject.list();	
				
				if(wordPresent.size()>0){
					String wordFound = wordPresent.get(0);
					
					if(wordFound != null && !wordFound.isEmpty()){ 
						if(wordFound.equals(word)){ 
							isExist = true;
						}
					}
				}
			session.getTransaction().commit();
		}catch(HibernateException e){
//			System.out.println("exception");
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		
		return isExist;
	}
	
	private void insertIntoInvertedIndex(String word, int occur, int fileId){
		InvertedIndex in = new InvertedIndex();
		
		in.setWord(word);
		in.setFrequency(occur);
		in.setIndexId(fileId);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	    session.beginTransaction();
		 try {

	    session.save(in);
	    session.getTransaction().commit();
		 }
		 catch (HibernateException e) {
			 e.printStackTrace();
			 session.getTransaction().rollback();
		}
		
	}
	
	public int getFileIndex(String fileName)   {

		List<Long> name = new ArrayList<Long>();
		int fileId = -1;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try{
			StringBuilder queryString = new StringBuilder();
			
			queryString.append(" SELECT res.indexId ")
					   .append(" FROM WebFile AS res WHERE res.fileName='"+fileName+"'");
			
			Query queryObject = session.createQuery(queryString.toString());
		

			name= (List<Long>) queryObject.list();	
			
			 fileId= (int)(long)name.get(0);
			 session.getTransaction().commit();
		}catch(HibernateException e){
//			System.out.println("exception");
			e.printStackTrace();
			session.getTransaction().rollback();
			
		}
		
		return fileId;
	}
	
	/*
	private void insertingWordPosition(){ 

		
		String line = "";
		StringTokenizer stringTokenizer = null;
		
		String url = TestTasks.class.getClassLoader()
		.getResource("WebFiles").getPath();

		File dir = new File(url);
		BufferedReader br = null;
		List<String> words = new ArrayList<String>();
		TSTOriginal<Integer> st = new TSTOriginal<Integer>();
		try {
			
		for(File f : dir.listFiles()){ 
			br = new BufferedReader(new FileReader(f));
			
			while ((line = br.readLine()) != null) {

				stringTokenizer = new StringTokenizer(line," .,'\"\"*&^%$#@`~-_+=;:?<>()[]/|!\t\n\r\f,.:;?![]'\\{}");
				
				
				while (stringTokenizer.hasMoreElements()) {

					String token = stringTokenizer.nextElement().toString();

					StringBuilder sb = new StringBuilder();
					sb.append(token);
					String aSingleToken = sb.toString();
					int posFound = line.indexOf(aSingleToken);
					if(aSingleToken.length()>0){ 
						if(st.get(aSingleToken) == null){
							st.put(aSingleToken, 1);
						}
						else {
	                        int value = st.get(aSingleToken);
	                        value++;
	                        st.put(aSingleToken, value);
	                    }
					}
					
					
				}
				
			}
			int fileId = getFileIndex(f.getName());
			
			boolean isExist = false;
			
			for (String key : st.keys()) {
				isExist = getInvertedIndex(key, fileId);
				if(!isExist){
					insertIntoInvertedIndex(key,st.get(key),fileId);
				}
	        }
			br.close();
			
		}
		
		
	}catch(Exception ex){ 
		ex.printStackTrace();
	}
	}
	*/
	
	public static void main(String[] args) {  
		TstImplementation tst = new TstImplementation();
		tst.makingTST();
//		tst.fileIdExist(1);
	}
	
}
