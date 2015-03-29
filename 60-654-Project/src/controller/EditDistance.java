package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class EditDistance {

	public List<String> findingLeastEditDistance(String word) {
		List<String> matchedWords = new ArrayList<String>();

		File dictionary = new File("Dictionary.txt");
		boolean isExist = dictionary.exists();
		String line = "";
		StringTokenizer stringTokenizer = null;
		BufferedReader br = null;

		int editDistance = 0;
		int keywordScore = 0;

		List<Integer> editDistances = new ArrayList<Integer>();
		List<Integer> minDistances = new ArrayList<Integer>();
		RedBlackBST<Integer, String> rbst = new RedBlackBST<Integer, String>();

		try {
			br = new BufferedReader(new FileReader(dictionary));

			while ((line = br.readLine()) != null) {

				stringTokenizer = new StringTokenizer(line);

				while (stringTokenizer.hasMoreElements()) {

					String token = stringTokenizer.nextElement().toString();

					StringBuilder sb = new StringBuilder();
					sb.append(token);
					String aSingleToken = sb.toString();

					// Calculating edit distance of given keyword with each
					// token
					editDistance = editDistance(word, aSingleToken);
					// editDistances.add(editDistance);
					int rbstMin = -1;

					if (!rbst.isEmpty())
						rbstMin = rbst.min();

					if (rbstMin == editDistance) {

						String wordMatched = rbst.get(rbstMin);
//						System.out.println("Word: " + wordMatched
//								+ " edit distance: " + editDistance);

						if (rbstMin <= 3)
							matchedWords.add(wordMatched);
						rbst.delete(editDistance);
						rbst.put(editDistance, aSingleToken);
					} else {
						rbst.put(editDistance, aSingleToken);
					}
				}
			}

			System.out.println("Edit Distances computed.\n");

//			rbst.printTree();


			for (String words : matchedWords) {
				System.out.println(words);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return matchedWords;
	}

	public static int editDistance(String word1, String word2) {
		int len1 = word1.length();
		int len2 = word2.length();

		// len1+1, len2+1, because finally return dp[len1][len2]
		int[][] dp = new int[len1 + 1][len2 + 1];

		for (int i = 0; i <= len1; i++) {
			dp[i][0] = i;
		}

		for (int j = 0; j <= len2; j++) {
			dp[0][j] = j;
		}

		// iterate though, and check last char
		for (int i = 0; i < len1; i++) {
			char c1 = word1.charAt(i);
			for (int j = 0; j < len2; j++) {
				char c2 = word2.charAt(j);

				// if last two chars equal
				if (c1 == c2) {
					// update dp value for +1 length
					dp[i + 1][j + 1] = dp[i][j];
				} else {
					int replace = dp[i][j] + 1;
					int insert = dp[i][j + 1] + 1;
					int delete = dp[i + 1][j] + 1;

					int min = replace > insert ? insert : replace;
					min = delete > min ? min : delete;
					dp[i + 1][j + 1] = min;
				}
			}
		}

		return dp[len1][len2];
	}

	public static void main(String[] args) {

		EditDistance ed = new EditDistance();
		ed.findingLeastEditDistance("shuodl");

	}

}
