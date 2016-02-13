import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * 
 * @author TravisWentz
 * Lab 2
 * 5 Feb 2016
 * 
 */
public class Main {
	
	/*
	 * This method is optional! It pre-hashes all the passwords in a txt file. This only ever has to be done
	 * once per dictionary and makes the searching many times faster.
	 */
	private static void translate(String dictionary) throws NoSuchAlgorithmException, IOException{
		//open the text file
		FileInputStream fstream;
		fstream = new FileInputStream(dictionary + ".txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String line;
		
		//create a new file for hashed values
		File hash = new File(dictionary + "Hash.txt");
		FileWriter hashFile = new FileWriter(hash);
		
		MessageDigest m = MessageDigest.getInstance("MD5");
		while ((line = br.readLine()) != null) {
			m.update(line.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			String hashText = bigInt.toString(16);
			hashText += '\n';
			hashFile.write(hashText);
		}
		br.close();
		hashFile.close();
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		long lineNum = 0;
		//dictionary can be .txt file only
		String dictionary = "passwords"; //name of dictionary file without it's .txt extension
		ArrayList<String> passwords = new ArrayList<String>();
		passwords.add("6f047ccaa1ed3e8e05cde1c7ebc7d958");
		passwords.add("275a5602cd91a468a0e10c226a03a39c");
		passwords.add("b4ba93170358df216e8648734ac2d539");
		passwords.add("dc1c6ca00763a1821c5af993e0b6f60a");
		passwords.add("8cd9f1b962128bd3d3ede2f5f101f4fc");
		passwords.add("554532464e066aba23aee72b95f18ba2");
		
		//if the pre-hashed file hasn't been made, make it
		if(!new File(dictionary + "Hash.txt").exists()){
			translate(dictionary);
		}
			
		//open the hash file
		FileInputStream hfstream;
		hfstream = new FileInputStream(dictionary + "Hash.txt");
		BufferedReader hashFile = new BufferedReader(new InputStreamReader(hfstream));
		String hashText = "";
		
		//open the text file
		FileInputStream afstream;
		afstream = new FileInputStream(dictionary + ".txt");
		BufferedReader asciiFile = new BufferedReader(new InputStreamReader(afstream));
		String asciiText = "";
		
		final double startTime = System.currentTimeMillis();
		SEARCH:
		while ((hashText = hashFile.readLine()) != null) { //move through the hash file
			asciiText = asciiFile.readLine();	//move through the text file at the same time
			for(int i = 0; i < passwords.size(); i++){
				if(hashText.equals(passwords.get(i))){
					double curTime = (System.currentTimeMillis() - startTime) / 1000;
					System.out.println("Password for hash value " + passwords.get(i) + " is " + asciiText + ", it takes the program " + curTime + " sec to recover this password.");
					passwords.remove(i);
					lineNum++;
					continue SEARCH;
				}
			}
			lineNum++;
		}
		asciiFile.close();
		hashFile.close();
	}

}
