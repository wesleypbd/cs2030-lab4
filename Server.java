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

    public Boolean canServe(Customer customer) {
        return customer.canBeServed(this.nextAvailableTime);
    }

    public Boolean canServe(double atTime) {
        return atTime >= this.nextAvailableTime;
    }

    public Server pushQueue() {
        return new Server(this, this.qsize + 1, this.nextAvailableTime);
    }

    public Server popQueue() {
        return new Server(this, this.qsize - 1, this.nextAvailableTime);
    }

    public Boolean canWait() {
        return this.qsize < this.qmax;
    }

    public double getNextAvailableTime() {
        return this.nextAvailableTime;
    }

    public int getQsize() {
        return this.qsize;
    }

    public Boolean isServer(Server server) {
        return this.id == server.id;
    }

    public String toString() {
        //return "server " + this.id + ": " +qsize; // Change back
        return "server " + this.id;
    }
}
