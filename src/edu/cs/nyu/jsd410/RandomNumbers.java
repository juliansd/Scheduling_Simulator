package edu.cs.nyu.jsd410;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * This class is like an echo of Java's Random class, 
 * except ours is based on the input file given of random numbers.
 * @author juliansmithdeniro
 *
 */
public class RandomNumbers {
	
	private final String FILE_PATH = "random.txt";
	private LinkedList<Integer> numbers;

	public RandomNumbers() {}
	
	@SuppressWarnings("resource")
	protected void processInput() {
		LinkedList<Integer> numbers = new LinkedList<Integer>();
		Scanner scan = new Scanner(System.in);
		File random = new File(this.FILE_PATH);
		try {
			scan = new Scanner(random);
			while (scan.hasNextInt()) {
				int n = scan.nextInt();
				numbers.add(n);
			}
			scan.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setNumbers(numbers);
	}
	
	protected int nextInt(int n) {
		LinkedList<Integer> numbers = this.getNumbers();
		int randn = numbers.pollFirst();
		this.setNumbers(numbers);
//		System.out.println("randn: " + randn);
		return (randn % n)+1;
	}

	public LinkedList<Integer> getNumbers() {
		return numbers;
	}

	public void setNumbers(LinkedList<Integer> numbers) {
		this.numbers = numbers;
	}
}
