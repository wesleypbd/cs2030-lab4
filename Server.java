public class Server {
    private final int id;
    private final int qmax;
    private final int qsize;
    private final double nextAvailableTime;

    public Server(int id, int qmax) {
        this.id = id;
        this.qmax = qmax;
        this.qsize = 0;                 // init to 0
        this.nextAvailableTime = 0.0;   // init to 0
    }

    public Server(Server server, int qsize, double nextAvailableTime) {
        this.id = server.id;
        this.qmax = server.qmax;
        this.qsize = qsize;
        this.nextAvailableTime = nextAvailableTime;
    }

    // after DoneEvent
    public Server(Server server) {
        this.id = server.id;
        this.qmax = server.qmax;
        this.qsize = server.qsize;
        this.nextAvailableTime = 0.0;
    }

    // Lab 4
    public Server serveTill(double doneTime) {
        return new Server(this, this.qsize, doneTime);
    }

    public Server popQueue() {
        return new Server(this, this.qsize - 1, this.nextAvailableTime);
    }

    public Boolean canServe(Customer customer) {
        return customer.canBeServed(this.nextAvailableTime);
    }

    public Boolean canWait(Customer customer) {
        //return this.qsize < this.qmax && customer.canBeServed(this.nextAvailableTime);
        return this.qsize < this.qmax;
    }

    public Server pushQueue() {
        return new Server(this, this.qsize + 1, this.nextAvailableTime);
    }

    public double getNextAvailableTime() {
        return this.nextAvailableTime;
    }

    public Boolean isServer(Server server) {
        return this.id == server.id;
    }

    public String toString() {
        return "server " + this.id;
    }
}
