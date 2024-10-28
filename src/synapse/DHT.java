package synapse;

public interface DHT {
    public String handleRequest(String code); 

    public void kill();

    public String getIndetifier();

    public int keyToH(String key);

    public Node getThisNode();

    public void put(String key, String value);

    public String get(String key);

    public void get(String key,int count);

    public void join(String ip, int port);

    public String deleteReceiver(); 
}
