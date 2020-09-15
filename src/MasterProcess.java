import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MasterProcess implements Runnable {
    private int[] process_ids;
    private static Set<Thread> myProcesses;
    public SignalIndicator signalIndicator = new SignalIndicator();
    public MessageChannel myMessages;
    public Iterator<Thread> iterator;
    private boolean isLeaderElected;

    public MasterProcess(int[] process_ids) {
        this.process_ids = process_ids;
        myMessages = new MessageChannel(process_ids.length);
        isLeaderElected = false;
    }

    public void spawnProcesses() throws InterruptedException {
        myProcesses = new HashSet<>();
        for (int i=0; i<process_ids.length; i++) {
            Thread process = new Thread(new Process(process_ids[i], i, signalIndicator, myMessages));
            signalIndicator.increment();
            process.start();
            myProcesses.add(process);
        }

        while (!isLeaderElected) {
            while(!signalIndicator.isRoundCompleted()) {
                Thread.sleep(10);
            }
            for(int i=0; i<process_ids.length; i++) {
                myMessages.messageList[i] = myMessages.newMessageList[i];
            }
            synchronized (signalIndicator) {
                iterator = myProcesses.iterator();
                while (iterator.hasNext()) {
                    Thread process = iterator.next();
                    if (process.getState() != Thread.State.TERMINATED) {
                        signalIndicator.increment();
                    }
                }
                if (signalIndicator.isRoundCompleted()) {
                    isLeaderElected = true;
                }
                signalIndicator.notifyAll();
            }
        }
        iterator = myProcesses.iterator();
        // all the processes know the leader, terminating them
        while (iterator.hasNext()) {
            iterator.next().join();
        }
        System.out.println("Parent thread exits");
    }

    @Override
    public void run() {
        try {
            spawnProcesses();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
