import java.util.Optional;
import java.util.stream.IntStream;
import java.util.List;
import java.util.function.Supplier;

public class Shop {
    private final int numOfServers;
    private final List<Server> servers;
    private final Supplier<Double> serviceTime;
    private final int qmax;

    // Lab 4
    public Shop(int numOfServers, Supplier<Double> serviceTime, int qmax) {
        this.numOfServers = numOfServers;
        this.serviceTime = serviceTime;
        this.qmax = qmax;
        this.servers = IntStream.rangeClosed(1, numOfServers)
                .mapToObj(i -> new Server(i, qmax))
                .toList();
    }

    private Shop(List<Server> servers, Supplier<Double> serviceTime, int qmax) {
        this.numOfServers = servers.size();
        this.serviceTime = serviceTime;
        this.qmax = qmax;
        this.servers = servers;
    }

    public Optional<Server> findServer(Customer customer) {
        return servers.stream()
                .filter(server -> server.canServe(customer))
                .findFirst();
    }

    public Optional<Server> findServer(double eventTime, Server oldServer) {
        return servers.stream()
                .filter(server -> server.isServer(oldServer))
                .findFirst();
    }

    public Optional<Server> findQueue(Customer customer) {
        return servers.stream()
                .filter(server -> server.canWait())
                .findFirst();
    }

    public Shop update(Server newServer) {
        List<Server> newServers = servers.stream()
                .map(oldServer -> oldServer.isServer(newServer) ?
                        newServer :
                        oldServer)
                .toList();

        return new Shop(newServers, this.serviceTime, this.qmax);
    }

    public Supplier<Double> getServiceTimeSupplier() {
        return this.serviceTime;
    }

    public String toString() {
        return servers.toString();
    }
}

