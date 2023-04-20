package back.Messages;

import back.enums.MessageTypeForStudent;

import java.time.LocalDateTime;
import java.util.TreeMap;

public class MessagesForStudent {

    private TreeMap<String, String> messages = new TreeMap<>();
    private TreeMap<String, MessageTypeForStudent> messagesType = new TreeMap<>();

    public TreeMap<String, String> getMessages() {
        return messages;
    }

    public void setMessages(TreeMap<String, String> messages) {
        this.messages = messages;
    }

    public TreeMap<String, MessageTypeForStudent> getMessagesType() {
        return messagesType;
    }

    public void setMessagesType(TreeMap<String, MessageTypeForStudent> messagesType) {
        this.messagesType = messagesType;
    }

    public void addNewMessage(String text, MessageTypeForStudent messageTypeForStudent){
        LocalDateTime localDateTime = LocalDateTime.now();
        String time = localDateTime.getYear() + "-" + localDateTime.getMonthValue() + "-"
                + localDateTime.getDayOfMonth() + "-" + localDateTime.getHour() + "-" +
                localDateTime.getMinute() + "-" + localDateTime.getSecond();
        messages.put(time, text);
        messagesType.put(time, messageTypeForStudent);
    }
}
