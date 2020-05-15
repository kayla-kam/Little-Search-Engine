package lse;

import java.io.FileNotFoundException;
import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
	
		if(docFile == null) {
			throw new FileNotFoundException("File isn't found, please try again loser.");
		}
			Scanner sc = new Scanner(new File(docFile));
			HashMap<String, Occurrence> keymap = new HashMap<String, Occurrence>();
			
			while(sc.hasNext()) {
				String keyw = getKeyword(sc.next());
				if(keyw != null) {
					if(keymap.containsKey(keyw)) {
						
						keymap.get(keyw).frequency++;
					
					}
					else {
						
						Occurrence oculus = new Occurrence(docFile, 1);
						keymap.put(keyw,  oculus);
					
					}
				}
			}
			sc.close();
			return keymap;
		
}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
	
		for(String k: kws.keySet()) {
			ArrayList<Occurrence> recently = new ArrayList<Occurrence>();
			if(keywordsIndex.containsKey(k)){
				
				keywordsIndex.get(k).add(kws.get(k));
				insertLastOccurrence(keywordsIndex.get(k));
			
			} 
			else {
				
				recently.add(kws.get(k));
				keywordsIndex.put(k,recently);
			
			}
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {

		word = word.toLowerCase();
		for(int i = word.length()-1; i>=0; i--){
			if((word.charAt(i)==',') ||(word.charAt(i)==':')||(word.charAt(i)=='.')||(word.charAt(i)=='!')||(word.charAt(i)==';')||(word.charAt(i)=='?')){
				word = word.substring(0,i);
			}
			else{
				break;
			}
		}
		if(noiseWords.contains(word)) {
			return null;
		}
		for(int i = word.length()-1; i>=0; i--){
			if (!Character.isLetter(word.charAt(i))){
				return null;		
			}
		}
		return word;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		
		if(occs.size() == 1 ) {
			return null;
		}
		
		Occurrence objective = occs.get(occs.size()-1);
		int max = occs.size()-2, min = 0, middle = (min+max)/2;
		ArrayList<Integer> middlesibling = new ArrayList<Integer>();
		
			while(min <= max) {
				middle = (max+min)/2;
				middlesibling.add(middle);
				if(occs.get(middle).frequency == objective.frequency) {
					break;
				}
				else if(occs.get(middle).frequency > objective.frequency) {
					min = middle+1;
				}
				else {
					max = middle-1;
				}
			}
		occs.add(middle+1, (occs.remove(occs.size()-1)));
		if(max < min) {
			
			occs.add(min, occs.remove(occs.size()-1));
			
		}
		
		return middlesibling;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		
		ArrayList<Occurrence> tarray = new ArrayList<Occurrence>();
		ArrayList<Occurrence> ultracombo = new ArrayList<Occurrence>();
		ArrayList<Occurrence> oarray = new ArrayList<Occurrence>();
		ArrayList<String> watchmojotop5 = new ArrayList<String>();
		
		if (keywordsIndex.containsKey(kw1)) {
			oarray = keywordsIndex.get(kw1);
		}
		if (keywordsIndex.containsKey(kw2)) {
			
			tarray = keywordsIndex.get(kw2);
		}
		
		ultracombo.addAll(oarray);
		ultracombo.addAll(tarray);
		
		if ((oarray != null) && (tarray != null)){
			
			for (int i = 0; i < ultracombo.size()-1; i++){
				
				for (int j = 1; j < ultracombo.size()-i; j++){
					
					if (ultracombo.get(j-1).frequency < ultracombo.get(j).frequency)
					{
						Occurrence anothertemporary = ultracombo.get(j-1);
						ultracombo.set(j-1, ultracombo.get(j));
						ultracombo.set(j,  anothertemporary);
					}
				}
			}

			for (int x = 0; x < ultracombo.size()-1; x++){
				
				for (int y = x + 1; y < ultracombo.size(); y++){
					
					if (ultracombo.get(x).document == ultracombo.get(y).document)
						ultracombo.remove(y);
				}
			}
		}

		while (ultracombo.size() > 5) {
			ultracombo.remove(ultracombo.size()-1);
			}
		
		System.out.println(ultracombo);
		
		for (Occurrence occ : ultracombo) {
			watchmojotop5.add(occ.document);
			}

		return watchmojotop5;
	}
}


