package edu.cs.nyu.jsd410;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * This class is a is meant to represent a scheduler which has four 
 * scheduling algorithms implemented for processes that it gets as input.
 * @author juliansmithdeniro
 * @version 1.0
 */

public class Scheduler {
	
	/**
	 * Default constructor
	 */
	public Scheduler() {}
	
	/**
	 * Implements First Come First Serve Scheduling Algorithm, which handles processes
	 * on a first come first serve basis.
	 * @param processes is an ArrayList<Process> which holds Process objects.
	 * @param verbose is a boolean value which determines whether or not there is
	 * going to be a detailed output.
	 */
	protected void FCFS(ArrayList<Process> processes, boolean verbose) {
		
		float avgTurnaround = 0;
		float avgWait = 0;
		
		System.out.println("FIRST COME FIRST SERVE");
		System.out.println("----------------------");
		
		Comparator<Process> comparator = new ProcessArrivalTimeComparator();
		
		Collections.sort(processes, comparator);
		
		ArrayList<Process> done = new ArrayList<Process>();
		
		ArrayList<Process> running = new ArrayList<Process>(1);
		LinkedList<Process> readyQueue = new LinkedList<Process>();
		ArrayList<Process> blocked = new ArrayList<Process>();
		
//		Random random = new Random();
		RandomNumbers random = new RandomNumbers();
		random.processInput();
		
		int cycle = 0;
		while (done.size() < processes.size()) {
			if (cycle <= processes.get(0).getA()) {
				if (verbose) {
					System.out.print("Before cycle " + cycle + ":\t");
					for (int i = 0; i < processes.size(); i++) {
						Process p = processes.get(i);
	
						if (p.getState().equals("unstarted"))
							System.out.print(p.getState() + "\t0\t");
						
						else if (p.getState().equals("ready"))
							System.out.print(p.getState() + "\t\t0\t");
						
						else if (p.getState().equals("blocked"))
							System.out.print("blocked\t\t" + p.getIoBurst() + "\t");
						
						else if (p.getState().equals("done"))
							System.out.print("done\t\t0\t");
						
						else {
							int toPrint = p.getCpuBurst()+1;
							System.out.print("running\t\t" + toPrint + "\t");
						}
					}
					System.out.println("");
				}
			}
			
			Process nextBlocked = null;
			@SuppressWarnings("unused")
			Process toRemove = null;
			
			for (Process p : processes) {
				if (!p.isVisited()) {
					if (cycle >= p.getA()) {
						p.setVisited(true);
						readyQueue.add(p);
						p.setState("ready");
					}
					
					if (running.size() == 0 && readyQueue.size() != 0) {
						Process nextRunning = readyQueue.poll();
						nextRunning.setState("running");
						nextRunning.setWARNING(true);
						running.add(nextRunning);
						nextRunning.setCpuBurst(random.nextInt(nextRunning.getB()));
						if (nextRunning.getCpuBurst() > nextRunning.getCpuTime())
							nextRunning.setCpuBurst(nextRunning.getCpuTime());
					}
				}
			}
			
			if (cycle > processes.get(0).getA()) {
				if (verbose) {
					System.out.print("Before cycle " + cycle + ":\t");
					for (int i = 0; i < processes.size(); i++) {
						Process p = processes.get(i);

						if (p.getState().equals("unstarted"))
							System.out.print(p.getState() + "\t0\t");
						
						else if (p.getState().equals("ready"))
							System.out.print(p.getState() + "\t\t0\t");
						
						else if (p.getState().equals("blocked"))
							System.out.print("blocked\t\t" + p.getIoBurst() + "\t");
						
						else if (p.getState().equals("done"))
							System.out.print("done\t\t0\t");
						
						else {
							int toPrint = p.getCpuBurst();
							System.out.print("running\t\t" + toPrint + "\t");
						}
					}
					System.out.println("");
				}
				if (running.size() != 0) {
					Process currentRunning = running.get(0);
					currentRunning.setCpuBurst(currentRunning.getCpuBurst()-1);
					currentRunning.setCpuTime(currentRunning.getCpuTime()-1);
					
					if (currentRunning.getCpuTime() == 0) {
						done.add(currentRunning);
						currentRunning.setState("done");
						currentRunning.setFinishTime(cycle);
						running.remove(0);
						if (readyQueue.size() != 0) {
							Process nextRunning = readyQueue.poll();
							nextRunning.setCpuBurst(random.nextInt(nextRunning.getB()));
							if (nextRunning.getCpuBurst() > nextRunning.getCpuTime())
								nextRunning.setCpuBurst(nextRunning.getCpuTime());
							running.add(nextRunning);
							nextRunning.setState("running");
						}
					}
					
					else if (currentRunning.getCpuBurst() == 0) {
						currentRunning.setIoBurst(random.nextInt(currentRunning.getiO()));
						nextBlocked = currentRunning;
						running.remove(0);
						if (readyQueue.size() != 0 && running.size() == 0) {
							Process nextRunning = readyQueue.poll();
							nextRunning.setCpuBurst(random.nextInt(nextRunning.getB()));
							if (nextRunning.getCpuBurst() > nextRunning.getCpuTime())
								nextRunning.setCpuBurst(nextRunning.getCpuTime());
							running.add(nextRunning);
							nextRunning.setState("running");
						}
					}
				} else {
					if (readyQueue.size() != 0) {
						Process nextRunning = readyQueue.poll();
						nextRunning.setCpuBurst(random.nextInt(nextRunning.getB()));
						if (nextRunning.getCpuBurst() > nextRunning.getCpuTime())
							nextRunning.setCpuBurst(nextRunning.getCpuTime());
						running.add(nextRunning);
						nextRunning.setState("running");
					}
				}
				
				ArrayList<Process> newBlocked = new ArrayList<Process>();
				
				for (Process p : blocked) {
					p.setIoBurst(p.getIoBurst()-1);
					p.setTotalIOTime(p.getTotalIOTime()+1);
					if (p.getIoBurst() == 0) {
						p.setCpuBurst(random.nextInt(p.getB()));
						if (p.getCpuBurst() > p.getCpuTime())
							p.setCpuBurst(p.getCpuTime());
						readyQueue.add(p);
						p.setState("ready");
						toRemove = p;
					} else
						newBlocked.add(p);
				}
				
				blocked = newBlocked;
				
				if (nextBlocked != null) {
					newBlocked.add(nextBlocked);
					nextBlocked.setState("blocked");
				}
			}
			cycle++;
		}
		
		if (verbose) {
			System.out.print("Before cycle " + cycle + ":\t");
			for (int i = 0; i < processes.size(); i++) {
				Process p = processes.get(i);
				if (p.getState().equals("unstarted"))
					System.out.print(p.getState() + "\t0\t");
				else if (p.getState().equals("ready"))
					System.out.print(p.getState() + "\t\t0\t");
				else if (p.getState().equals("running"))
					System.out.print(p.getState() + "\t\t" + p.getCpuBurst() + "\t");
				else
					System.out.print(p.getState() + "\t\t" + p.getIoBurst() + "\t");
			}
			System.out.println("");
		}
		System.out.println("");
		
		for (int i = 0; i < processes.size(); i++) {
			Process p = processes.get(i);
			int turnaroundTime = p.getFinishTime() - p.getA();
			int totalWaitTime = p.getFinishTime() - (p.getC() + p.getTotalIOTime());
			
			avgTurnaround += turnaroundTime;
			avgWait += totalWaitTime;
			
			System.out.println(
					"Process " + i + " (" + p.getA() + "," + 
			p.getB() + "," + p.getC() + "," + p.getiO() + ")");
			System.out.println("Finish Time: " + p.getFinishTime());
			System.out.println("Turnaround Time: " + turnaroundTime);
			System.out.println("I/O Time: " + p.getTotalIOTime());
			System.out.println("Total Waiting Time: " + totalWaitTime);
			System.out.println("");
		}
		float cpuUtilization = 0;
		float totalFinishTime = 0;
		for (Process p : processes) {
			cpuUtilization += p.getC();
			if (totalFinishTime < p.getFinishTime())
				totalFinishTime = p.getFinishTime();
		}
		cpuUtilization /= totalFinishTime;
		float ioUtilization = 0;
		for (Process p : processes) {
			ioUtilization += p.getTotalIOTime();
		}
		ioUtilization /= totalFinishTime;
		float throughput = 100*processes.size()/totalFinishTime;
		
		System.out.println("SUMMARY DATA:");
		System.out.println("Total Finish Time: " + totalFinishTime);
		System.out.println("CPU Utilization: " + (cpuUtilization*100) + "%");
		System.out.println("I/O Utilization: " + (ioUtilization*100) + "%");
		System.out.println("Throughput per 100 cycles: " + throughput);
		System.out.println("Average Turnaround: " + avgTurnaround/processes.size());
		System.out.println("Average Wait: " + avgWait/processes.size());
		System.out.println("----------------------");
	}
	
	/**
	 * Implements Round Robin Scheduling Algorithm (quantum = 2), which handles processes
	 * in a round robin fashion for a quantum value of 2.
	 * @param processes is an ArrayList<Process> which holds Process objects.
	 * @param verbose is a boolean value which determines whether or not there is
	 * going to be a detailed output.
	 */
	
	protected void RR(ArrayList<Process> processes, boolean verbose) {
		
		float avgTurnaround = 0;
		float avgWait = 0;
		
		System.out.println("ROUND ROBIN");
		System.out.println("----------------------");
		
//		Random random = new Random();
		RandomNumbers random = new RandomNumbers();
		random.processInput();
		
		Comparator<Process> comparator = new ProcessArrivalTimeComparator();
		
		processes.sort(comparator);
		
		ArrayList<Process> done = new ArrayList<Process>();
		
		ArrayList<Process> running = new ArrayList<Process>(1);
		LinkedList<Process> readyQueue = new LinkedList<Process>();
		ArrayList<Process> blocked = new ArrayList<Process>();
		
		int cycle = 0;
		
		while (done.size() < processes.size()) {
			
			if (cycle <= processes.get(0).getA()) {
				if (verbose) {
					System.out.print("Before cycle " + cycle + ":\t");
					for (int i = 0; i < processes.size(); i++) {
						Process p = processes.get(i);
	
						if (p.getState().equals("unstarted"))
							System.out.print(p.getState() + "\t0\t");
						
						else if (p.getState().equals("ready"))
							System.out.print(p.getState() + "\t\t0\t");
						
						else if (p.getState().equals("blocked"))
							System.out.print("blocked\t\t" + p.getIoBurst() + "\t");
						
						else if (p.getState().equals("done"))
							System.out.print("done\t\t0\t");
						
						else {
							int toPrint = p.getCpuBurst()+1;
							System.out.print("running\t\t" + toPrint + "\t");
						}
					}
					System.out.println("");
				}
			}
			
			Process nextBlocked = null;
			@SuppressWarnings("unused")
			Process toRemove = null;
			
			for (Process p : processes) {
				if (!p.isVisited()) {
					if (cycle >= p.getA()) {
						p.setVisited(true);
						readyQueue.add(p);
						p.setState("ready");
					}
					
					if (running.size() == 0 && readyQueue.size() != 0) {
						Process nextRunning = readyQueue.poll();
						nextRunning.setState("running");
						running.add(nextRunning);
						nextRunning.setCpuBurst(random.nextInt(nextRunning.getB()));
						if (nextRunning.getCpuBurst() > nextRunning.getCpuTime())
							nextRunning.setCpuBurst(nextRunning.getCpuTime());
					}
				}
			}
			
			if (cycle > processes.get(0).getA()) {
				if (verbose) {
					System.out.print("Before cycle " + cycle + ":\t");
					for (int i = 0; i < processes.size(); i++) {
						Process p = processes.get(i);

						if (p.getState().equals("unstarted"))
							System.out.print(p.getState() + "\t0\t");
						
						else if (p.getState().equals("ready"))
							System.out.print(p.getState() + "\t\t0\t");
						
						else if (p.getState().equals("blocked"))
							System.out.print("blocked\t\t" + p.getIoBurst() + "\t");
						
						else if (p.getState().equals("done"))
							System.out.print("done\t\t0\t");
						
						else {
							int toPrint = p.getCpuBurst();
							System.out.print("running\t\t" + toPrint + "\t");
						}
					}
					System.out.println("");
				}
				if (running.size() != 0) {
					Process currentRunning = running.get(0);
					if (currentRunning.getCpuBurst() == 1 && cycle == currentRunning.getA())
						currentRunning.setPrintRunning(true);
					currentRunning.setCpuBurst(currentRunning.getCpuBurst()-1);
					currentRunning.setCpuTime(currentRunning.getCpuTime()-1);
					currentRunning.setQuantum(currentRunning.getQuantum()-1);
					
					if (currentRunning.getCpuTime() == 0) {
						done.add(currentRunning);
						currentRunning.setState("done");
						currentRunning.setFinishTime(cycle);
						running.remove(0);
						if (readyQueue.size() != 0) {
							Process nextRunning = readyQueue.poll();
							nextRunning.setQuantum(2);
							if (nextRunning.getCpuBurst() == 0)
								nextRunning.setCpuBurst(random.nextInt(nextRunning.getB()));
							if (nextRunning.getCpuBurst() > nextRunning.getCpuTime())
								nextRunning.setCpuBurst(nextRunning.getCpuTime());
							running.add(nextRunning);
						}
					}
					
					else if (currentRunning.getCpuBurst() == 0 || currentRunning.getQuantum() ==0) {
						currentRunning.setIoBurst(random.nextInt(currentRunning.getiO()));
						nextBlocked = currentRunning;
						running.remove(0);
						if (readyQueue.size() != 0 && running.size() == 0) {
							Process nextRunning = readyQueue.poll();
							nextRunning.setQuantum(2);
							if (nextRunning.getCpuBurst() == 0)
								nextRunning.setCpuBurst(random.nextInt(nextRunning.getB()));
							if (nextRunning.getCpuBurst() > nextRunning.getCpuTime())
								nextRunning.setCpuBurst(nextRunning.getCpuTime());
							running.add(nextRunning);
							nextRunning.setState("running");
						}
					}
				} else {
					if (readyQueue.size() != 0) {
						Process nextRunning = readyQueue.poll();
						nextRunning.setQuantum(2);
						if (nextRunning.getCpuBurst() == 0)
							nextRunning.setCpuBurst(random.nextInt(nextRunning.getB()));
						if (nextRunning.getCpuBurst() > nextRunning.getCpuTime())
							nextRunning.setCpuBurst(nextRunning.getCpuTime());
						running.add(nextRunning);
						nextRunning.setState("running");
					}
				}
				
				ArrayList<Process> newBlocked = new ArrayList<Process>();
				
				for (Process p : blocked) {
					p.setIoBurst(p.getIoBurst()-1);
					p.setTotalIOTime(p.getTotalIOTime()+1);
					if (p.getIoBurst() == 0) {
						p.setQuantum(2);
						if (p.getCpuBurst() == 0)
							p.setCpuBurst(random.nextInt(p.getB()));
						if (p.getCpuBurst() > p.getCpuTime())
							p.setCpuBurst(p.getCpuTime());
						readyQueue.add(p);
						p.setState("ready");
						toRemove = p;
					} else {
						newBlocked.add(p);
					}
				}
				
				blocked = newBlocked;
				
				if (nextBlocked != null) {
					newBlocked.add(nextBlocked);
					nextBlocked.setState("blocked");
				}
			}
			cycle++;
		}

		if (verbose) {
			System.out.print("Before cycle " + cycle + ":\t");
			for (int i = 0; i < processes.size(); i++) {
				Process p = processes.get(i);
				if (p.getState().equals("unstarted"))
					System.out.print(p.getState() + "\t0\t");
				else if (p.getState().equals("ready"))
					System.out.print(p.getState() + "\t\t0\t");
				else if (p.getState().equals("running"))
					System.out.print(p.getState() + "\t\t" + p.getCpuBurst() + "\t");
				else
					System.out.print(p.getState() + "\t\t" + p.getIoBurst() + "\t");
			}
			System.out.println("");
		}
		
		System.out.println("");
		
		for (int i = 0; i < processes.size(); i++) {
			Process p = processes.get(i);
			int turnaroundTime = p.getFinishTime() - p.getA();
			int totalWaitTime = p.getFinishTime() - (p.getC() + p.getTotalIOTime());
			
			avgTurnaround += turnaroundTime;
			avgWait += totalWaitTime;
			
			System.out.println(
					"Process " + i + " (" + p.getA() + "," + 
			p.getB() + "," + p.getC() + "," + p.getiO() + ")");
			System.out.println("Finish Time: " + p.getFinishTime());
			System.out.println("Turnaround Time: " + turnaroundTime);
			System.out.println("I/O Time: " + p.getTotalIOTime());
			System.out.println("Total Waiting Time: " + totalWaitTime);
			System.out.println("");
		}
		float cpuUtilization = 0;
		int totalFinishTime = 0;
		for (Process p : processes) {
			cpuUtilization += p.getC();
			if (totalFinishTime < p.getFinishTime())
				totalFinishTime = p.getFinishTime();
		}
		cpuUtilization /= totalFinishTime;
		float ioUtilization = 0;
		for (Process p : processes) {
			ioUtilization += p.getTotalIOTime();
		}
		ioUtilization /= totalFinishTime;
		float throughput = 100*processes.size()/totalFinishTime;
		
		System.out.println("SUMMARY DATA:");
		System.out.println("Total Finish Time: " + totalFinishTime);
		System.out.println("CPU Utilization: " + (cpuUtilization*100) + "%");
		System.out.println("I/O Utilization: " + (ioUtilization*100) + "%");
		System.out.println("Throughput per 100 cycles: " + throughput);
		System.out.println("Average Turnaround: " + avgTurnaround/processes.size());
		System.out.println("Average Wait: " + avgWait/processes.size());
		System.out.println("----------------------");
	}
	
	/**
	 * Implements Uni-Programming Scheduling Algorithm, which handles processes
	 * individually until completed and does not run another processes while one is
	 * being blocked.
	 * @param processes is an ArrayList<Process> which holds Process objects.
	 * @param verbose is a boolean value which determines whether or not there is
	 * going to be a detailed output.
	 */
	
	protected void uniProgramming(ArrayList<Process> processes, boolean verbose) {
		
		System.out.println("UNIPROGRAMMING");
		System.out.println("----------------------");
		
//		Random random = new Random();
		RandomNumbers random = new RandomNumbers();
		random.processInput();
		
		Comparator<Process> comparator = new ProcessArrivalTimeComparator();
		
		Collections.sort(processes, comparator);
		
		LinkedList<Process> readyQueue = new LinkedList<Process>();
		ArrayList<Process> running = new ArrayList<Process>(1);
		ArrayList<Process> blocked = new ArrayList<Process>();
		ArrayList<Process> done = new ArrayList<Process>();
		
		int cycle = 0;
			
		while (done.size() < processes.size()) {
			if (verbose)
				System.out.print("Before cycle " + cycle + ":\t");
			for (Process p : processes) {
				if (verbose) {
					if (p.getState().equals("unstarted"))
						System.out.print(p.getState() + "\t0\t");
					
					else if (p.getState().equals("ready"))
						System.out.print(p.getState() + "\t\t0\t");
					
					else if (p.getState().equals("blocked")) {
						int toPrint = p.getIoBurst()+1;
						System.out.print("blocked\t\t" + toPrint + "\t");
					}
					
					else if (p.getState().equals("done"))
						System.out.print("done\t\t0\t");
					
					else {
						int toPrint = p.getCpuBurst()+1;
						System.out.print("running\t\t" + toPrint + "\t");
					}
				}
				
				if (cycle >= p.getA() && !p.isVisited()) {
					if (running.size() != 0 && blocked.size() == 0) {
						readyQueue.add(p);
						p.setState("ready");
					} else {
						running.add(p);
						p.setState("running");
						p.setCpuBurst(random.nextInt(p.getB()));
						p.setCpuBurst(p.getCpuBurst()-1);
						p.setCpuTime(p.getCpuTime()-1);
					}
					p.setVisited(true);
					
				} else if (p.getCpuTime() == 0 && p.getState().equals("running")) {
					
					p.setState("done");
					running.remove(0);
					done.add(p);
					p.setFinishTime(cycle);
					
				} else if (p.getCpuBurst() == 0 && p.getState().equals("running")) {
					
					p.setState("blocked");
					p.setIoBurst(random.nextInt(p.getiO()));
					p.setIoBurst(p.getIoBurst()-1);
					p.setTotalIOTime(p.getTotalIOTime()+1);
					running.remove(0);
					blocked.add(p);
					
				} else if (p.getIoBurst() == 0 && p.getState().equals("blocked")) {
					
					p.setState("running");
					p.setCpuBurst(random.nextInt(p.getB()));
					p.setCpuBurst(p.getCpuBurst()-1);
					p.setCpuTime(p.getCpuTime()-1);
					running.add(p);
					blocked.remove(0);
					
				} else if (p.getState().equals("running")) {
					
					p.setCpuBurst(p.getCpuBurst()-1);
					p.setCpuTime(p.getCpuTime()-1);
					
				} else if (p.getState().equals("blocked")) {
					
					p.setIoBurst(p.getIoBurst()-1);
					p.setTotalIOTime(p.getTotalIOTime()+1);
					
				} else if (p.getState().equals("ready")) {
					
					if (running.size() == 0 && blocked.size() == 0) {
						running.add(p);
						p.setState("running");
						readyQueue.poll();
					}
					
				}
			}
			
			cycle++;
			System.out.println("");
		}
		
		System.out.println("");
		
		for (int i = 0; i < processes.size(); i++) {
			Process p = processes.get(i);
			
			int turnaroundTime = p.getFinishTime()-p.getA();
			int waitTime = p.getFinishTime() - (p.getC() + p.getTotalIOTime());
			
			System.out.println("Process " + i + " (" + p.getA() + "," + p.getB() + "," + p.getC() + "," + p.getiO() + ")");
			System.out.println("Finishing Time: " + p.getFinishTime());
			System.out.println("Turnaround Time: " + turnaroundTime);
			System.out.println("I/O TIme: " + p.getTotalIOTime());
			System.out.println("Waiting Time: " + waitTime);
			System.out.println("");
		}
		
		int totalFinishTime = processes.get(processes.size()-1).getFinishTime();
		
		float cpuUtilization = 0;
		float ioUtilization = 0;
		float avgTurnaround = 0;
		float avgWaitingTime = 0;
		
		for (Process p : processes) {
			cpuUtilization += p.getC();
			ioUtilization += p.getTotalIOTime();
			avgTurnaround += p.getFinishTime()-p.getA();
			avgWaitingTime += p.getFinishTime() - (p.getC() + p.getTotalIOTime());
		}
		
		cpuUtilization /= totalFinishTime;
		ioUtilization /= totalFinishTime;
		avgTurnaround /= processes.size();
		avgWaitingTime /= processes.size();
		
		float throughput = (100*processes.size())/totalFinishTime;
		
		System.out.println("SUMMARY DATA");
		System.out.println("Finishing Time: " + totalFinishTime);
		System.out.println("CPU Utilization: " + cpuUtilization*100);
		System.out.println("I/O Utilization: " + ioUtilization*100);
		System.out.println("Throughput per 100 cycles: " + throughput);
		System.out.println("Average Turnaround Time: " + avgTurnaround);
		System.out.println("Average Waiting Time: " + avgWaitingTime);
	}
	
	/**
	 * Implements Shortest Job First Scheduling Algorithm, which handles processes
	 * based on which process has the least amount of cpu time left in their job.
	 * @param processes is an ArrayList<Process> which holds Process objects.
	 * @param verbose is a boolean value which determines whether or not there is
	 * going to be a detailed output.
	 */

	protected void SJF(ArrayList<Process> processes, boolean verbose) {
		
		float avgTurnaround = 0;
		float avgWait = 0;
		
		System.out.println("SHORTEST JOB FIRST");
		System.out.println("----------------------");
		
		Comparator<Process> comparator = new ProcessArrivalTimeComparator();
		Comparator<Process> queueComparator = new ProcessCPUTimeComparator();
		
		Collections.sort(processes, comparator);
		
		ArrayList<Process> done = new ArrayList<Process>();
		
		ArrayList<Process> running = new ArrayList<Process>(1);
		PriorityQueue<Process> readyQueue = new PriorityQueue<Process>(queueComparator);
		ArrayList<Process> blocked = new ArrayList<Process>();
		
//		Random random = new Random();
		RandomNumbers random = new RandomNumbers();
		random.processInput();
		
		int cycle = 0;
		while (done.size() < processes.size()) {
			
			if (cycle <= processes.get(0).getA()) {
				if (verbose) {
					System.out.print("Before cycle " + cycle + ":\t");
					for (int i = 0; i < processes.size(); i++) {
						Process p = processes.get(i);

						if (p.getState().equals("unstarted"))
							System.out.print(p.getState() + "\t0\t");
						
						else if (p.getState().equals("ready"))
							System.out.print(p.getState() + "\t\t0\t");
						
						else if (p.getState().equals("blocked"))
							System.out.print("blocked\t\t" + p.getIoBurst() + "\t");
						
						else if (p.getState().equals("done"))
							System.out.print("done\t\t0\t");
						
						else {
							int toPrint = p.getCpuBurst();
							System.out.print("running\t\t" + toPrint + "\t");
						}
					}
					System.out.println("");
				}
			}
			
			Process nextBlocked = null;
			@SuppressWarnings("unused")
			Process toRemove = null;
			
			for (Process p : processes) {
				if (!p.isVisited()) {
					if (cycle >= p.getA()) {
						p.setVisited(true);
						readyQueue.add(p);
						p.setState("ready");
					}
					
					if (running.size() == 0 && readyQueue.size() != 0) {
						Process nextRunning = readyQueue.poll();
						nextRunning.setState("running");
						running.add(nextRunning);
						nextRunning.setCpuBurst(random.nextInt(nextRunning.getB()));
						if (nextRunning.getCpuBurst() > nextRunning.getCpuTime())
							nextRunning.setCpuBurst(nextRunning.getCpuTime());
					}
				}
			}
			
			if (cycle > processes.get(0).getA()) {
				if (verbose) {
					System.out.print("Before cycle " + cycle + ":\t");
					for (int i = 0; i < processes.size(); i++) {
						Process p = processes.get(i);
	
						if (p.getState().equals("unstarted"))
							System.out.print(p.getState() + "\t0\t");
						
						else if (p.getState().equals("ready"))
							System.out.print(p.getState() + "\t\t0\t");
						
						else if (p.getState().equals("blocked"))
							System.out.print("blocked\t\t" + p.getIoBurst() + "\t");
						
						else if (p.getState().equals("done"))
							System.out.print("done\t\t0\t");
						
						else {
							int toPrint = p.getCpuBurst();
							System.out.print("running\t\t" + toPrint + "\t");
						}
					}
					System.out.println("");
				}
				if (running.size() != 0) {
					Process currentRunning = running.get(0);
					currentRunning.setCpuBurst(currentRunning.getCpuBurst()-1);
					currentRunning.setCpuTime(currentRunning.getCpuTime()-1);
					
					if (currentRunning.getCpuTime() == 0) {
						done.add(currentRunning);
						currentRunning.setState("done");
						currentRunning.setFinishTime(cycle);
						running.remove(0);
						if (readyQueue.size() != 0) {
							Process nextRunning = readyQueue.poll();
							nextRunning.setCpuBurst(random.nextInt(nextRunning.getB()));
							if (nextRunning.getCpuBurst() > nextRunning.getCpuTime())
								nextRunning.setCpuBurst(nextRunning.getCpuTime());
							running.add(nextRunning);
							nextRunning.setState("running");
						}
					}
					
					else if (currentRunning.getCpuBurst() == 0) {
						currentRunning.setIoBurst(random.nextInt(currentRunning.getiO()));
						nextBlocked = currentRunning;
						running.remove(0);
						if (readyQueue.size() != 0 && running.size() == 0) {
							Process nextRunning = readyQueue.poll();
							nextRunning.setCpuBurst(random.nextInt(nextRunning.getB()));
							if (nextRunning.getCpuBurst() > nextRunning.getCpuTime())
								nextRunning.setCpuBurst(nextRunning.getCpuTime());
							running.add(nextRunning);
							nextRunning.setState("running");
						}
					}
				} else {
					if (readyQueue.size() != 0) {
						Process nextRunning = readyQueue.poll();
						nextRunning.setCpuBurst(random.nextInt(nextRunning.getB()));
						if (nextRunning.getCpuBurst() > nextRunning.getCpuTime())
							nextRunning.setCpuBurst(nextRunning.getCpuTime());
						running.add(nextRunning);
						nextRunning.setState("running");
					}
				}
				
				ArrayList<Process> newBlocked = new ArrayList<Process>();
				
				for (Process p : blocked) {
					p.setIoBurst(p.getIoBurst()-1);
					p.setTotalIOTime(p.getTotalIOTime()+1);
					if (p.getIoBurst() == 0) {
						p.setCpuBurst(random.nextInt(p.getB()));
						if (p.getCpuBurst() > p.getCpuTime())
							p.setCpuBurst(p.getCpuTime());
						readyQueue.add(p);
						p.setState("ready");
						toRemove = p;
					} else {
						newBlocked.add(p);
						p.setState("blocked");
					}
				}
				
				blocked = newBlocked;
				
				if (nextBlocked != null) {
					newBlocked.add(nextBlocked);
					nextBlocked.setState("blocked");
				}
			}
			cycle++;
		}
		
		if (verbose) {
			System.out.print("Before cycle " + cycle + ":\t");
			for (int i = 0; i < processes.size(); i++) {
				Process p = processes.get(i);
				if (p.getState().equals("unstarted"))
					System.out.print(p.getState() + "\t0\t");
				else if (p.getState().equals("ready"))
					System.out.print(p.getState() + "\t\t0\t");
				else if (p.getState().equals("running"))
					System.out.print(p.getState() + "\t\t" + p.getCpuBurst() + "\t");
				else
					System.out.print(p.getState() + "\t\t" + p.getIoBurst() + "\t");
			}
			System.out.println("");
		}
		System.out.println("");
		
		for (int i = 0; i < processes.size(); i++) {
			Process p = processes.get(i);
			int turnaroundTime = p.getFinishTime() - p.getA();
			int totalWaitTime = p.getFinishTime() - (p.getC() + p.getTotalIOTime());
			
			avgTurnaround += turnaroundTime;
			avgWait += totalWaitTime;
			
			System.out.println(
					"Process " + i + " (" + p.getA() + "," + 
			p.getB() + "," + p.getC() + "," + p.getiO() + ")");
			System.out.println("Finish Time: " + p.getFinishTime());
			System.out.println("Turnaround Time: " + turnaroundTime);
			System.out.println("I/O Time: " + p.getTotalIOTime());
			System.out.println("Total Waiting Time: " + totalWaitTime);
			System.out.println("");
		}
		float cpuUtilization = 0;
		int totalFinishTime = 0;
		for (Process p : processes) {
			cpuUtilization += p.getC();
			if (totalFinishTime < p.getFinishTime())
				totalFinishTime = p.getFinishTime();
		}
		cpuUtilization /= totalFinishTime;
		float ioUtilization = 0;
		for (Process p : processes) {
			ioUtilization += p.getTotalIOTime();
		}
		ioUtilization /= totalFinishTime;
		float throughput = 100*processes.size()/totalFinishTime;
		
		System.out.println("SUMMARY DATA:");
		System.out.println("Total Finish Time: " + totalFinishTime);
		System.out.println("CPU Utilization: " + (cpuUtilization*100) + "%");
		System.out.println("I/O Utilization: " + (ioUtilization*100) + "%");
		System.out.println("Throughput per 100 cycles: " + throughput);
		System.out.println("Average Turnaround: " + avgTurnaround/processes.size());
		System.out.println("Average Wait: " + avgWait/processes.size());
		System.out.println("----------------------");
	}
}
