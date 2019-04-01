package edu.cs.nyu.jsd410;

import java.util.Comparator;

/**
 * A comparator which compares Process objects based on their arrival time.
 * @author juliansmithdeniro
 * @version 1.0
 */

public class ProcessArrivalTimeComparator implements Comparator<Process> {

	@Override
	public int compare(Process p1, Process p2) {
		if (p1.getA() > p2.getA())
			return 1;
		else if (p1.getA() == p2.getA())
			return 0;
		else
			return -1;
	}
}
