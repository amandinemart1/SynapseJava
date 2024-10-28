package synapse;

public class Message {
    private String operationCode;
    private String key;
    private String value;
    private Node sender;
    private Node receiver;
    private int ttl;
    private int mrr; 

    public Message(String operationCode, String key, String value, Node sender, Node receiver, int ttl, int mrr2) {
        this.operationCode = operationCode;
        this.key = key;
        this.value = value;
        this.sender = sender;
        this.receiver = receiver;
        this.ttl = ttl;
        this.mrr = mrr;
    }

    public String getOperationCode() { return operationCode; }
    public String getKey() { return key; }
    public String getValue() { return value; }
    public Node getSender() { return sender; }
    public Node getReceiver() { return receiver; }
    public int getTtl() { return ttl; }
    public int getMrr() { return mrr; }

    public void decreaseTtl() {
        if (ttl > 0) ttl--;
    }

    public void decreaseMrr() {
        if (mrr > 0) mrr--;
    }

    public boolean isExpired() {
        return ttl <= 0;
    }

    public boolean isReplicable() {
        return mrr > 0;
    }
}
