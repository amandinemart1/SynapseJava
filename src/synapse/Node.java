package synapse;


public class Node {
    private String ip;
    private int id; 
    private int port;

    public Node(String ip, int id, int port) {
        this.ip = ip;
        this.id = id;
        this.port = port;
    }

    public Node(String ip, int port) {
        this(ip,0,port);
    }

    public Node(String node) {
        String[] args = node.split(",");
        if (args.length == 3) {
            this.ip = args[0];
            this.id = Integer.parseInt(args[1]);
            this.port = Integer.parseInt(args[2]);
        } 
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            Node node = (Node) obj;
            return this.ip.equals(node.ip) && this.port == node.port;
        }
        return false;
    }

    @Override
    public String toString() {
        return ip + "," + id + "," + port;
    }

    public String getIp() {
        return ip;
    }

    public int getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
