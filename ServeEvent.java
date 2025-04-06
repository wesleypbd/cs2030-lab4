import java.util.Optional;

public class ServeEvent extends Event {
    private final Server server;

    public ServeEvent(Customer customer, double eventTime, Server server) {
        super(customer, eventTime);
        this.server = server;
    }

    public Optional<Pair<Optional<Event>, Shop>> next(Shop shop) {
        double doneTime = eventTime + shop.getServiceTimeSupplier().get();
        Server updatedServer = server.serveTill(doneTime);
        Shop updatedShop = shop.update(updatedServer);
        return Optional.of(new Pair<>(
                Optional.of(new DoneEvent(customer, doneTime, updatedServer)),
                updatedShop));
    }
    
    @Override
    public Pair<Double, Integer> updateStats(Pair<Double, Integer> stats) {
        double totalWaitTime = stats.t() + (eventTime - customer.getArrivalTime());
        int numCustomersServed = stats.u() + 1;
        return new Pair<>(totalWaitTime, numCustomersServed);
    }

    @Override
    public String toString() {
        return String.format("%.3f %s serve by %s", eventTime, customer, server);
    }
}
