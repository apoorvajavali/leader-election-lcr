import java.lang.reflect.Array;
import java.util.Arrays;

public class MessageChannel {
    private int channelSize;
    public String[] messageList;
    public String[] newMessageList;

    public MessageChannel(int size) {
        channelSize = size;

        messageList = new String[channelSize];
        Arrays.fill(messageList, "");

        newMessageList = new String[channelSize];
        Arrays.fill(newMessageList, "");
    }

    public String getInputMessage(int idx) {
        String message = messageList[idx];
        messageList[idx] = "";
        return message;
    }

    public void sendOutputMessage(int idx, String value) {
        newMessageList[(idx+1)%channelSize] = value;
    }
}
