package edu.cs.nyu.jsd410;

/**
 * This class represents the process which is to be handled by our four different
 * scheduling algorithms implemented in the Scheduler class.
 * @author juliansmithdeniro
 * @version 1.0
 */

public class Process {

	private int A, B, C, IO, cpuBurst, cpuTime, ioBurst, finishTime, totalIOTime;
	private int quantum = 2;
	
	private String state = "unstarted";
	
	private boolean visited, printRunning, WARNING;
	
	public Process() {}
	
	public Process(int A, int B, int C, int IO) {
		this.setA(A);
		this.setB(B);
		this.setC(C);
		this.setiO(IO);
	}
	
	public Process(Process p) {
		this(p.getA(), p.getB(), p.getC(), p.getiO());
	}

	public int getA() {
		return A;
	}

	public void setA(int a) {
		A = a;
	}

	public int getB() {
		return B;
	}

	public void setB(int b) {
		B = b;
	}

	public int getC() {
		return C;
	}

	public void setC(int c) {
		C = c;
	}

	public int getiO() {
		return IO;
	}

	public void setiO(int iO) {
		IO = iO;
	}

	public int getCpuBurst() {
		return cpuBurst;
	}

	public void setCpuBurst(int cpuBurst) {
		this.cpuBurst = cpuBurst;
	}

	public int getIoBurst() {
		return ioBurst;
	}

	public void setIoBurst(int ioBurst) {
		this.ioBurst = ioBurst;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public int getCpuTime() {
		return cpuTime;
	}

	public void setCpuTime(int cpuTIme) {
		this.cpuTime = cpuTIme;
	}

	public int getQuantum() {
		return quantum;
	}

	public void setQuantum(int quantum) {
		this.quantum = quantum;
	}

	public int getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(int finishTime) {
		this.finishTime = finishTime;
	}

	public int getTotalIOTime() {
		return totalIOTime;
	}

	public void setTotalIOTime(int totalIOTime) {
		this.totalIOTime = totalIOTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	

	public boolean isPrintRunning() {
		return printRunning;
	}
	
	public void setPrintRunning(boolean printRunning) {
		this.printRunning = printRunning;
	}

	public boolean isWARNING() {
		return WARNING;
	}

	public void setWARNING(boolean wARNING) {
		WARNING = wARNING;
	}
}
