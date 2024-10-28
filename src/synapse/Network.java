package synapse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Network implements DHT {
    protected List<DHT> dhts;
    public String identifier; 
    protected HashFunction h; 
    protected int TTL; 
    protected boolean goodDeal; 
    protected Node node;
    protected Node manager;
    protected Boolean alive = true; 
    protected int MessageNumber = 0; 
    protected int tiemout = 90000;

    private HashMap<Integer, Node> forwardingTable; 

    public Network(String identifier, String ip, int port, String manager) {
        this.identifier = identifier;
        this.h = new HashFunction(identifier);
        int id = h.SHA1ToInt(ip+port); 
        this.forwardingTable = new HashMap<Integer, Node>();
        this.node= new Node(ip, id, port);
        this.TTL = 10;
        this.manager = new Node(manager);
    }

    public void onReceiptOPE(String code, String key, String value, String ipSend) {
        String tag = newTag(ipSend);
        sendFind(code, TTL, 100, tag, key, value, this.node.getIp()); // Assuming mrr=100
    }

    public void onReceiptFIND(String code, int ttl, int mrr, String tag, String key, String value, String ipDest, String ipSend) {
        if (ttl == 0 || gameOver(tag)) {
            return;
        } else {
            pushTag(tag);
            Map<Node, Integer> nextMrr = distribMrr(mrr, dhts);
            for (DHT dht : dhts) {
                Node net = dht.getThisNode();
                if (isResponsible(net, key)) {
                    sendFound(code, net, mrr, key, value, ipDest);
                } else if (goodDeal(net, ipSend)) {
                    sendFind(code, ttl - 1, nextMrr.get(net), tag, key, value, nextHop(key));
                }
            }
        }
    }

    public void onReceiptFOUND(String code, Node net, int mrr, String key, String value, String ipSend) {
        goodDealUpdate(net, ipSend);
        if (code.equals("GET")) {
            sendReadTable(net, key, ipSend);
        } else if (code.equals("PUT")) {
            if (mrr >= 0) {
                sendWriteTable(net, key, value, ipSend);
            }
        }
    }

    public void onReceiptINVITE(Node net, String ipSend) {
        if (goodDeal(net, ipSend)) {
            sendJoin(net, ipSend);
        }
    }

    public void onReceiptJOIN(Node net, String ipSend) {
        if (goodDeal(net, ipSend)) {
            insertNet(net, ipSend);
        }
    }

    private String newTag(String ipSend) {
        return ipSend + System.currentTimeMillis();
    }

    private boolean gameOver(String tag) {
        return false;
    }

    private void pushTag(String tag) {
    }

    private Map<Node, Integer> distribMrr(int mrr, List<DHT> dhts) {
        return new HashMap<>();
    }

    private boolean isResponsible(Node net, String key) {
        return false;
    }

    private boolean goodDeal(Node net, String ipSend) {
        return false;
    }

    private String nextHop(String key) {
        return null;
    }

    private void goodDealUpdate(Node net, String ipSend) {
    }

    private void insertNet(Node net, String ipSend) {
    }

    private void sendFind(String code, int ttl, int mrr, String tag, String key, String value, String ipDest) {
    }

    private void sendFound(String code, Node net, int mrr, String key, String value, String ipDest) {
    }

    private void sendReadTable(Node net, String key, String ipSend) {
    }

    private void sendWriteTable(Node net, String key, String value, String ipSend) {
    }

    private void sendJoin(Node net, String ipSend) {
    }

    @Override
    public String handleRequest(String code) {
        return null;
    }

    @Override
    public void kill() {
    }

    @Override
    public String getIndetifier() {
        return identifier;
    }

    @Override
    public int keyToH(String key) {
        return h.SHA1ToInt(key);
    }

    @Override
    public Node getThisNode() {
        return node;
    }

    @Override
    public void put(String key, String value) {
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public void get(String key, int count) {
    }

    @Override
    public void join(String ip, int port) {
        
    }

    @Override
    public String deleteReceiver() {
        return null;
    }
}