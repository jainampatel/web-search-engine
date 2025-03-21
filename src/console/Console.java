package console;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import SearchEngine.PageRanking;
import SearchEngine.SearchEngine;
import SearchEngine.NotStaticSearchEngine;

public class Console {

	private static final String url = "https://www.w3.org/";
	private static final String RegularExprWord = "[[ ]*|[,]*|[)]*|[(]*|[\"]*|[;]*|[-]*|[:]*|[']*|[�]*|[\\.]*|[:]*|[/]*|[!]*|[?]*|[+]*]+";
	public static NotStaticSearchEngine searchEngineMain;

	public static void startSearchEngine() {
		try {

			File file = new File(
					"Users/jainampatel/UOW/ACC/Project/ACC-Search-Engine-Project-master/SearchEngine.ser");
			if (file.exists() && !file.isDirectory()) {

				FileInputStream fileIn = new FileInputStream(
						"Users/jainampatel/UOW/ACC/Project/ACC-Search-Engine-Project-master/SearchEngine.ser");

				ObjectInputStream in = new ObjectInputStream(fileIn);
				searchEngineMain = (NotStaticSearchEngine) in.readObject();
				System.out.println(search("testing") + " Are the results------------------");
				System.out.println("SearchEngine Restored from File.");

				in.close();
				fileIn.close();
			} else {

				searchEngineMain = new NotStaticSearchEngine();
				System.out.println(
						"---------------------------Program has started-------------------------------------------");

				HashSet<String> ht = searchEngineMain.createTrie(url);// create hash table using trie
				System.out.println("Trie created.");

				FileOutputStream fileOut = new FileOutputStream(
						"Users/jainampatel/UOW/ACC/Project/ACC-Search-Engine-Project-master/SearchEngine.ser");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(searchEngineMain);
				out.close();
				fileOut.close();
				System.out.printf("SearchEngine Serilized");
			}

		} catch (Exception e) {
			System.out.print(e);
		}

	}

	public static LinkedHashMap<String, Integer> search(String serword) {
		try {

			boolean bool = true;
			Scanner sc = new Scanner(System.in);
			while (bool) {
				bool = false;
				System.out.println("\t** SEARCH YOUR WORD HERE**");
				if (!serword.equals(null)) {
					String[] sp = serword.split(RegularExprWord);// spilt the regular expression word and the entered
																	// word
					String[] allsearchpages = searchEngineMain.search(sp);
					try {
						if (allsearchpages == null) {// to suggest the similar words
							// SearchEngine.suggestWords(serword);
						} else {
							Map<String, Integer> ul = null;// for unsorted Links
							ul = new HashMap<>();

							for (String url : allsearchpages) {

								ul.put(url, PageRanking.WordOcuurence(url, serword));// store all the links found in the
																						// hashmap
							}
							LinkedHashMap<String, Integer> reverseMap = new LinkedHashMap<>();// to reverse the map , in
																								// accordance with the
																								// frequency

							ul.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
									.forEachOrdered(x -> reverseMap.put(x.getKey(), x.getValue()));
							return reverseMap;
						}

					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				} else {
					System.out.println("Kindly enter a valid word, " + serword + " is not a valid word.");
					continue;
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	// public static
	public static void main(String[] args) throws IOException {

		SearchEngine se = new SearchEngine();
		System.out.println("---------------------------Program has started-------------------------------------------");

		HashSet<String> ht = se.createTrie(url);// create hash table using trie
		System.out.println("Trie created.");
		boolean bool = true;
		Scanner sc = new Scanner(System.in);
		String serword;
		while (bool) {
			System.out.println("\t** SEARCH YOUR WORD HERE**");
			serword = sc.next();// take the input from the user
			if (!serword.equals(null)) {
				String[] sp = serword.split(RegularExprWord);// spilt the regular expression word and the entered word
				String[] allsearchpages = se.search(sp);
				try {
					if (allsearchpages == null) {// to suggest the similar words
						// SearchEngine.suggestWords(serword);
					} else {
						Map<String, Integer> ul = null;// for unsorted Links
						ul = new HashMap<>();

						for (String url : allsearchpages) {

							ul.put(url, PageRanking.WordOcuurence(url, serword));// store all the links found in the
																					// hashmap
						}
						LinkedHashMap<String, Integer> reverseMap = new LinkedHashMap<>();// to reverse the map , in
																							// accordance with the
																							// frequency

						ul.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
								.forEachOrdered(x -> reverseMap.put(x.getKey(), x.getValue()));
						System.out.println("");
						// to print the page ranks and corresponding web page links
						System.out.println("---------- | Page Rank |  | Search |");
						System.out.println("\n");
						int count = 1;
						for (Map.Entry<String, Integer> putin : reverseMap.entrySet()) {
							if (count > 10)
								break;
							System.out.println("---------- | " + putin.getValue() + " | --> |" + putin.getKey() + " ");
							count++;
						}
						System.out.println("\n\n");
						// prompt to choose to continue or exit the web search engine
						System.out.println("More words to search? -------- Press 's' \n Exit? -------- Press 'e' ");

						while (true) {
							String inp = sc.next();
							if (inp.equals("s")) {
								break;
							} else if (inp.equals("e")) {
								bool = false;
								System.out.println("Do visit again.");
								System.exit(1);
								sc.close();
							} else {
								System.out.println(
										"Wrong input. Please input valid character. \n More words to search? -------- Press 's' \\n Exit? -------- Press 'e' ");
							}
						}
					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} else {
				System.out.println("Kindly enter a valid word, " + serword + " is not a valid word.");
				continue;
			}
		}
	}
}
