package edu.cs.nyu.jsd410;

import java.util.Comparator;

/**
 * A comparator which compares Process objects based on the
 * cpu time left needed to complete their job.
 * @author juliansmithdeniro
 * @version 1.0
 */

public class ProcessCPUTimeComparator implements Comparator<Process> {

	@Override
	public int compare(Process p1, Process p2) {
		if (p1.getCpuTime() > p2.getCpuTime())
			return 1;
		else if (p1.getCpuTime() == p2.getCpuTime())
			return 0;
		else
			return -1;
	}

}
