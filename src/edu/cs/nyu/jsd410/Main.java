package edu.cs.nyu.jsd410;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class runs the main functionality of the entire project.
 * @author juliansmithdeniro
 * @version 1.0
 */

public class Main {

	/**
	 * Processes the input given by the user.
	 * @param args is an array of String values input from the command line.
	 * @return an ArrayList<Process> which holds Process objects created from
	 * the user input.
	 */
	@SuppressWarnings("resource")
	private ArrayList<Process> processInput(String[] args) {
		Scanner scan = new Scanner(System.in);
		ArrayList<Process> processes = new ArrayList<Process>();
		int count = 0;
		int index = count % 4;
		
		@SuppressWarnings("unused")
		int numOfProcesses, A = 0, B = 0, C = 0, IO;
		File file = new File(args[0]);
		try {
			scan = new Scanner(file);
			numOfProcesses = scan.nextInt();
			while (scan.hasNextInt()) {
				index = count % 4;
				int n = scan.nextInt();
				if (index == 0)
					A = n;
				else if (index == 1)
					B = n;
				else if (index == 2)
					C = n;
				else {
					IO = n;
					Process p = new Process(A,B,C,IO);
					p.setCpuTime(C);
					processes.add(p);
				}
				count++;
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return processes;
		
	}
	
	/**
	 * Main function of the project.
	 * @param args is an array of String values input from the command line.
	 */
	public static void main(String[] args) {
		
		boolean verbose = false;
		String[] mainArgs = new String[1];
		
		if (args[0].equals("--verbose"))
			verbose = true;
		
		if (verbose)
			mainArgs[0] = args[1];
		else
			mainArgs[0] = args[0];
		
		Main main = new Main();
		ArrayList<Process> processes = main.processInput(mainArgs);
		ArrayList<Process> processes2 = new ArrayList<Process>();
		ArrayList<Process> processes3 = new ArrayList<Process>();
		ArrayList<Process> processes4 = new ArrayList<Process>();
		
		int i = 0;
		while (i < 3) {
			for (int j = 0; j < processes.size(); j++) {
				Process p = processes.get(j);
				Process pCopy = new Process(p);
				pCopy.setCpuTime(pCopy.getC());
				if (i == 0)
					processes2.add(pCopy);
				else if (i == 1)
					processes3.add(pCopy);
				else
					processes4.add(pCopy);
			}
			i++;
		}
		

		Scheduler s = new Scheduler();
		s.FCFS(processes, verbose);
		System.out.println("");
//		s.RR(processes2, verbose);
//		System.out.println("");
//		s.uniProgramming(processes3, verbose);
//		System.out.println("");
//		s.SJF(processes4, verbose);
	}
}
