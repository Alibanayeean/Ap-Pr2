package back.Messages;

import back.enums.MessagesTypeForTeacher;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

public class MessagesForTeacher {

    private TreeMap<String, String> messages = new TreeMap<>();
    private TreeMap<String, MessagesTypeForTeacher> messagesType = new TreeMap<>();

    public TreeMap<String, String> getMessages() {
        return messages;
    }

    public void setMessages(TreeMap<String, String> messages) {
        this.messages = messages;
    }

    public TreeMap<String, MessagesTypeForTeacher> getMessagesType() {
        return messagesType;
    }

    public void setMessagesType(TreeMap<String, MessagesTypeForTeacher> messagesType) {
        this.messagesType = messagesType;
    }

    public void addNewMessage(String text, MessagesTypeForTeacher messagesTypeForTeacher){
        LocalDateTime localDateTime = LocalDateTime.now();
        String time = localDateTime.getYear() + "-" + localDateTime.getMonthValue() + "-"
                 + localDateTime.getDayOfMonth() + "-" + localDateTime.getHour() + "-" +
                localDateTime.getMinute() + "-" + localDateTime.getSecond();
        messages.put(time, text);
        messagesType.put(time, messagesTypeForTeacher);
    }
}
