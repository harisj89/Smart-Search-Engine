package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.InvertedIndex;
import model.MatchFound;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class SearchEngine {

	private List<InvertedIndex> getInvertedIndex(String word) {
		boolean isExist = false;

		List<InvertedIndex> invertedIndexObject = new ArrayList<InvertedIndex>();

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			StringBuilder queryString = new StringBuilder();

			queryString.append(" SELECT res ").append(
					" FROM InvertedIndex AS res WHERE res.word='" + word + "'");

			Query queryObject = session.createQuery(queryString.toString());

			invertedIndexObject = (List<InvertedIndex>) queryObject.list();

			session.getTransaction().commit();
		} catch (HibernateException e) {
			// System.out.println("exception");
			e.printStackTrace();
			session.getTransaction().rollback();
		}

		return invertedIndexObject;
	}

	private String getWebFile(Long indexId) {
		boolean isExist = false;

		List<String> webFile = new ArrayList<String>();
		String fileName = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			StringBuilder queryString = new StringBuilder();

			queryString.append(" SELECT res.fileName ").append(
					" FROM WebFile AS res WHERE res.indexId=" + indexId);

			Query queryObject = session.createQuery(queryString.toString());

			webFile = (List<String>) queryObject.list();
			fileName = webFile.get(0);

			session.getTransaction().commit();
		} catch (HibernateException e) {
			// System.out.println("exception");
			e.printStackTrace();
			session.getTransaction().rollback();
		}

		return fileName;
	}

	public static void main(String[] args) {

		/*
		 * Dictionary dictionary = new Dictionary();
		 * dictionary.dictionaryConstrutor();
		 * 
		 * TstImplementation tst = new TstImplementation(); tst.makingTST();
		 */
		SearchEngine searchEngine = new SearchEngine();
		MatchFound matchFound;
		List<MatchFound> FileNames = new ArrayList<MatchFound>();
		List<String> matchedWords = new ArrayList<String>();
		List<InvertedIndex> invertedObjects = new ArrayList<InvertedIndex>();
		List<Integer> frequency = new ArrayList<Integer>();
		QuickSelect q = new QuickSelect();
		String selectedWord = null;

		System.out.println("Enter a word to be matched\n");
		String aKeyword;
		Scanner scanIn = new Scanner(System.in);
		aKeyword = scanIn.nextLine();

		EditDistance ed = new EditDistance();
		matchedWords = ed.findingLeastEditDistance(aKeyword);

		
		boolean correctWord = false;
		
		
		if (!matchedWords.isEmpty()) {
			System.out.println("Following are the best matches with your word. Which word would you like to search?\n");
			System.out.println("Please type below\n");
			
			selectedWord = scanIn.nextLine();
			
			for (String mWord : matchedWords) {
				if (selectedWord.equals(mWord)) {
					correctWord = true;
					// System.out.println("Typed Word was not in the match list. Please try again later.\n");
					// System.exit(0);
				}
			}
			if (!correctWord) {
				System.out
						.println("Typed Word was not in the match list. Please try again later.\n");
				System.exit(0);
			}
		}else{ 
			System.out.println("Your spelling are great!!!\n");
			selectedWord = aKeyword;
		}

		

		invertedObjects = searchEngine.getInvertedIndex(selectedWord);

		int[] frequecyArray = new int[invertedObjects.size()];
		int[] sortedFrequency = new int[invertedObjects.size()];
		for (int i = 0; i < invertedObjects.size(); i++) {
			InvertedIndex invIndex = invertedObjects.get(i);
			String fileName = searchEngine.getWebFile((long) invIndex
					.getIndexId());

			matchFound = new MatchFound();
			matchFound.setFileId(invIndex.getIndexId());
			matchFound.setFileName(fileName);
			matchFound.setFrequency(invIndex.getFrequency());
			matchFound.setWord(invIndex.getWord());
			FileNames.add(matchFound);
			frequecyArray[i] = invIndex.getFrequency();
		}
		for (int i = 0; i < frequecyArray.length - 1; i++) {
			int ind = q.quickSelect(frequecyArray, 0, frequecyArray.length - 1,
					frequecyArray.length - (i + 1));
			System.out.println("highest = " + frequecyArray[ind]);
			sortedFrequency[i] = frequecyArray[ind];
		}

		// for(int i=0; i<FileNames.size(); i++){
		// System.out.println(FileNames.get(i).getFrequency());
		// }
		List<String> finalFileNames = new ArrayList<String>();
		int yo = 0;
		for (int i = 0; i < sortedFrequency.length - 1; i++) {
			int freq = sortedFrequency[i];
			for (int index = 0; index < FileNames.size(); index++) {
				if (FileNames.get(index).getFrequency() == freq)
					if (!finalFileNames.contains(FileNames.get(index)
							.getFileName())) {
						finalFileNames.add(yo, FileNames.get(index)
								.getFileName());
						yo++;
					}
			}
		}

		for (int index = 0; index < finalFileNames.size(); index++) {
			System.out.println(finalFileNames.get(index));
		}
	}

}
