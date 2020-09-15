public class Process implements Runnable {
    private int processId;
    private int processIndex;
    private SignalIndicator signalIndicator;
    private MessageChannel myMessages;
    private String sendMessage;
    private boolean isLeaderElected;

    public Process(int id, int idx, SignalIndicator signal, MessageChannel messages) {
        processId = id;
        processIndex = idx;
        signalIndicator = signal;
        myMessages = messages;
        sendMessage = Integer.toString(id);
        isLeaderElected = false;
    }

    @Override
    public void run() {
        while (!isLeaderElected) {
            String incomingMessage = myMessages.getInputMessage(processIndex);
            if (incomingMessage.length() > 0) {
                if (incomingMessage.contains("LEADER_ELECTED")) {
                    sendMessage = incomingMessage;
                    isLeaderElected = true;
                    String leader = incomingMessage.split(" ")[1];
                    System.out.println("My Process ID is: " + processId + " My Leader ID is: " + leader);
                } else {
                    int v = Integer.parseInt(incomingMessage);
                    if (v > processId) {
                        sendMessage = incomingMessage;
                    } else if (v == processId) {
                        sendMessage = "LEADER_ELECTED " + v;
                        isLeaderElected = true;
                        System.out.println("I am the Leader and my ID is: " + processId);
                    }
                }
            }
            myMessages.sendOutputMessage(processIndex, sendMessage);
            sendMessage = "";
            signalIndicator.decrement();

            if (!isLeaderElected) {
                synchronized (signalIndicator) {
                    try {
                        signalIndicator.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
