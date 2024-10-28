package synapse;

public class Message {
    private String operationCode;
    private String key;
    private String value;
    private Node sender;
    private Node receiver;
    private int ttl;

    public Message(String operationCode, String key, String value, Node sender, Node receiver, int ttl) {
        this.operationCode = operationCode;
        this.key = key;
        this.value = value;
        this.sender = sender;
        this.receiver = receiver;
        this.ttl = ttl;
    }

    public String getOperationCode() { return operationCode; }
    public String getKey() { return key; }
    public String getValue() { return value; }
    public Node getSender() { return sender; }
    public Node getReceiver() { return receiver; }
    public int getTtl() { return ttl; }

    public void decreaseTtl() {
        if (ttl > 0) ttl--;
    }

    public boolean isExpired() {
        return ttl <= 0;
    }
}
