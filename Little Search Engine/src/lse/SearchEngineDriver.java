package lse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class SearchEngineDriver {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		LittleSearchEngine testSearchEngine = new LittleSearchEngine();
	
		testSearchEngine.makeIndex("docs.txt", "noisewords.txt");
		System.out.println("what is happening");
		
		//System.out.println(Arrays.toString(testSearchEngine.top5search("apple", "cool").toArray()));
		
		ArrayList<String> stringArray = testSearchEngine.top5search("apple", "cool");
		ArrayList<String> stringArray2 = testSearchEngine.top5search("joey", "banana");
		ArrayList<String> stringArray3 = testSearchEngine.top5search("apple", "zoombini");
		ArrayList<String> stringArray4 = testSearchEngine.top5search("joey", "zoombini");
		ArrayList<String> stringArray5 = testSearchEngine.top5search("dontExist", "zoombini");
		
		ArrayList<String> stringArray6 = testSearchEngine.top5search("cool", "apple");
		ArrayList<String> stringArray7 = testSearchEngine.top5search("banana", "joey");
		ArrayList<String> stringArray8 = testSearchEngine.top5search("zoombini", "apple");
		ArrayList<String> stringArray9 = testSearchEngine.top5search("zoombini", "joey");
		ArrayList<String> stringArray10 = testSearchEngine.top5search("zoombini", "dontExist");
		
		System.out.println("=============================================================================");
		System.out.println("BOTH WORDS EXIST IN MORE THAN 5 DOCUMENTS");
		for (int i = 0; i < stringArray.size(); i++) {
			System.out.println(stringArray.get(i));
		}
		System.out.println("=============================================================================");
		System.out.println("BOTH WORDS EXIST IN LESS THAN 5 DOCUMENTS EACH");
		for (int i = 0; i < stringArray2.size(); i++) {
			System.out.println(stringArray2.get(i));
		}
		System.out.println("=============================================================================");
		System.out.println("FIRST WORD EXISTS IN MORE THAN 5 DOCUMENTS AND THNE OTHER WORD DOES NOT EXIST");
		for (int i = 0; i < stringArray3.size(); i++) {
			System.out.println(stringArray3.get(i));
		}
		System.out.println("=============================================================================");
		System.out.println("SECOND WORD DOES NOT EXIST, THE OTHER EXISTS IN LESS THAN 5 DOCUMENTS");
		for (int i = 0; i < stringArray4.size(); i++) {
			System.out.println(stringArray4.get(i));
		}
		System.out.println("=============================================================================");
		System.out.println("NO WORD EXISTS IN ANY DOCUMENT");
		System.out.println(stringArray5);
		
		System.out.println("\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
		
		System.out.println("=============================================================================");
		System.out.println("BOTH WORDS EXIST, REVERSE ORDER.");
		for (int i = 0; i < stringArray6.size(); i++) {
			System.out.println(stringArray6.get(i));
		}
		System.out.println("=============================================================================");
		System.out.println("BOTH WORDS EXIST IN LESS THAN 5 DOCUMENTS EACH, REVERSE ORDER.");
		for (int i = 0; i < stringArray7.size(); i++) {
			System.out.println(stringArray7.get(i));
		}
	}

}
