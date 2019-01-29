// Populate solutions list in Java

// Library imports
import java.io.File;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.FileOutputStream;  
import java.io.OutputStreamWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PopulateList {
	
	private final String fileName;
	
	public PopulateList() {
		
		fileName = "solutionsList.txt";
		
	}
	
	private int populate () {
		
		
		int count = 0;
		
		
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"))) {
			
			for (int a = 1; a < 9; a++) {
				for (int b = 1; b < 9; b++) {
					for (int c = 1; c < 9; c++) {
						for (int d = 1; d < 9; d++) {
							for (int e = 1; e < 9; e++) {
								for (int f = 1; f < 9; f++) {
									for (int g = 1; g < 9; g++) {
										for (int h = 1; h < 9; h++) {
											
											// If all the number are distinct, add it to the list of solutions
											if (a != b && a != c & a != d && a != e && a != f && a != g && a != h 
													&& b != c && b != d && b != e && b != f && b != g && b != h 
													&& c != d && c != e && c != f && c != g && c != h
													&& d != e && d != f && d != g && d != h
													&& e != f && e != g && e != h
													&& f != g && f != h
													&& g != h) {
												
												// Put the possible combination into the list of solutions
												// Output into text file here
												writer.write(Integer.toString(a) + " " + Integer.toString(b) + " " + Integer.toString(c) + " " + Integer.toString(d) + " " + Integer.toString(e) + " " + Integer.toString(f) + " " + Integer.toString(g) + " " + Integer.toString(h) + "\n");
												count++;
											
											}		
										}
									}
								}
							}
						}
					}
				}
			}
			
		}
		catch (IOException e) {
		
			System.out.println("IO Error.");
			
		}
	
	return count;
	
	} // End of populate
	
	
	public static void main (String args[]) {
		
		PopulateList pop = new PopulateList();
		int count = pop.populate();
		System.out.println("Number of solutions written is: " + count + ".");
	}
	
} // End of PopulateList