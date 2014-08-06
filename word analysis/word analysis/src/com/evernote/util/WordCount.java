package com.evernote.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class WordCount {

	public static void main(String[] args) {

		// Chu-Chi: In MapReduce, wordcount is like helloworld, but I am not
		// going to use MapReduce here.

		// Chu-Chi: However, MapReduce is still good for big data environment
		// since things are done in parallel.

		// Chu-Chi: I assume the file is not big (i.e., it can be put inside
		// memory sufficiently).

		// Chu-Chi: We cannot use comparison to do the sorting since we want the
		// run time to be O(n) here. Therefore, I am going to use bucket sort.

		// Chu-Chi: I created more methods than what you asked for because I like to write reusable code :)
		
		// Chu-Chi: The method the question is asking for is 	
		// public static String[] sortedWordByCount(String filePath, int rtnNum)
		
		try {
			System.out.println("*** testing wordCount ***");
			String filePath = "/tmp/testing.txt";
			HashMap<String, Integer> wordCountResult = wordCount(filePath);
			for (String word : wordCountResult.keySet()) {
				int count = wordCountResult.get(word).intValue();
				System.out.println(word + "\t" + count);
			}
			System.out.println();

			System.out.println("*** testing sortedWordCount ***");
			String[] sortedWordCountResult = sortedWordCount(filePath);
			for (int i = sortedWordCountResult.length - 1; i > 0; i--) {
				String sortedItem = sortedWordCountResult[i];
				if (sortedItem != null) {
					System.out.println(sortedItem + "\nappears\t" + i + "\ttime(s).\n");
				}
			}
			System.out.println();

			System.out.println("*** testing sortedWordByCount ***");
			String[] sortedWordByCountResult = sortedWordByCount(filePath);
			for (int i = 0; i < sortedWordByCountResult.length; i++) {
				String sortedItem = sortedWordByCountResult[i];
				System.out.println(sortedItem);
			}
			System.out.println();

			System.out.println("*** testing sortedWordByCount with the parameter, number of returns ***");
			String[] sortedWordByCountResult2 = sortedWordByCount(filePath, 4);
			for (int i = 0; i < sortedWordByCountResult2.length; i++) {
				String sortedItem = sortedWordByCountResult2[i];
				if (sortedItem != null) {
					System.out.println(sortedItem);
				}
			}
			System.out.println();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static final String NOT_WORD = "[ .,?:)]";
	private static final String EMPTY_STR = "";
	private static final String DELIMIT_STR = "\t";

	public static String[] sortedWordByCount(String filePath, int rtnNum) throws FileNotFoundException, IOException {

		if (rtnNum < 0) {
			return null;
		}
		if (rtnNum == 0) {
			return new String[0];
		}

		String[] sortedWordByCountResult = sortedWordByCount(filePath);
		String[] rtn = new String[rtnNum];
		for (int i = 0; i < sortedWordByCountResult.length && i < rtnNum; i++) {
			rtn[i] = sortedWordByCountResult[i];
		}
		return rtn;
	}

	public static String[] sortedWordByCount(String filePath) throws FileNotFoundException, IOException {
		ArrayList<String> rtn = new ArrayList<String>();
		String[] sortedWordCountResult = sortedWordCount(filePath);
		for (int i = sortedWordCountResult.length - 1; i > 0; i--) {
			String delimitItems = sortedWordCountResult[i];
			if (delimitItems == null) {
				continue;
			} else {
				String[] rtnItems = delimitItems.split(DELIMIT_STR);
				for (int j = 0; j < rtnItems.length; j++) {
					String rtnItem = rtnItems[j];
					rtn.add(rtnItem);
				}
			}
		}
		return rtn.toArray(new String[rtn.size()]);
	}

	public static String[] sortedWordCount(String filePath) throws FileNotFoundException, IOException {
		HashMap<String, Integer> wordCountResult = wordCount(filePath);

		int bucketSize = 0;
		for (String word : wordCountResult.keySet()) {
			int count = wordCountResult.get(word).intValue();
			if (count > bucketSize) {
				bucketSize = count;
			}
		}

		// fill the bucket using count as index
		String[] sortedBucket = new String[bucketSize + 1];
		for (String word : wordCountResult.keySet()) {
			int count = wordCountResult.get(word).intValue();
			if (sortedBucket[count] != null) {
				sortedBucket[count] += DELIMIT_STR + word;// used tab as delimit
			} else {
				sortedBucket[count] = word;
			}
		}
		return sortedBucket;
	}

	public static HashMap<String, Integer> wordCount(String filePath) throws FileNotFoundException, IOException {
		HashMap<String, Integer> word2count = new HashMap<String, Integer>();
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(filePath);
			bufferedReader = new BufferedReader(fileReader);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				String[] words = line.split(NOT_WORD);
				for (int i = 0; i < words.length; i++) {
					String word = words[i];
					if (word.equals(EMPTY_STR)) {
						continue;
					}
					Integer count = word2count.get(word);
					if (count == null) {
						word2count.put(word, new Integer(1));
					} else {
						word2count.put(word, new Integer(count + 1));
					}
				}
			}
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
		return word2count;
	}
}
