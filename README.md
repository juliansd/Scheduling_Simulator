Lab 2 README

This lab simulates the following scheduling algorithms

First Come First Serve:
A preemptive scheduling algorithm which serves the processes based on a first come first serve basis.

Round Robin (quantum = 2):
A non-preemptive scheduling algorithm which serves the processes in a round robin fashion.

Uniprocessor:
Serves each process fully in the order in which they arrive

Shortest Job First:
Similar to First Come First Serve, except the processes in the ready queue are served based on how much time left they nede to complete their process.

The project consists of 6 Classes:

Scheduler:
This class represents the scheduler and implements each algorithm specified above.

Process:
This class represents the processes which are to be run by each scheduling algorithm.

Main:
This class handles the main processesing of the input file which is then used to construct the proper Process objects used in the simulation. It also runs the main functionality of the lab.

RandomNumbers:
This class is like an echo of Java's Random class, except ours is based on the input file given of random numbers.

ProcessCPUTimeComparator:
This class implements the Comparator interface and is used in the implementation of the Shortest Job First scheduling algorithm.

ProcessArrivalTimeComparator:
This class also implements the Comparator interface and is used in the implementation of each scheduling algorithm. It is used to initially sort the process by their arrival time.