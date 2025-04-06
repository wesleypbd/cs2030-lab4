import java.util.Optional;

public class WaitAgainEvent extends Event {
    private final Server server;

    public WaitAgainEvent(Customer customer, double eventTime, Server server) {
        super(customer, eventTime);
        this.server = server;
    }

    /*
    @Override
    public Optional<Pair<Optional<Event>, Shop>> next(Shop shop) {
        // Look for the same server but now available
        Optional<Server> availableServer = shop.findServer(customer, server);

        if (availableServer.isPresent()) {
            // Server is available, serve the customer
            Server updatedServer = availableServer.get();
            updatedServer = updatedServer.popQueue();
            Shop updatedShop = shop.update(updatedServer);
            return Optional.of(new Pair<>(
                    Optional.of(new ServeEvent(customer, eventTime, updatedServer)),
                    updatedShop));
        } else {
            // Server not available yet, check again at server's next available time
            return Optional.of(new Pair<>(
                    Optional.of(new WaitAgainEvent(
                            customer,
                            server.getNextAvailableTime(),
                            server)),
                    shop));
        }
    }
     */

    @Override
    public Optional<Pair<Optional<Event>, Shop>> next(Shop shop) {
        return shop.findServer(customer, server)
                .map(updatedServer -> {
                    Server serverWithoutCustomer = updatedServer.popQueue();
                    Shop updatedShop = shop.update(serverWithoutCustomer);
                    Event serveEvent = new ServeEvent(
                            customer,
                            eventTime,
                            serverWithoutCustomer);
                    return new Pair<>(Optional.of(serveEvent), updatedShop);
                })
                .or(() -> {
                    Event waitAgainEvent = new WaitAgainEvent(
                            customer,
                            server.getNextAvailableTime(),
                            server);
                    return Optional.of(new Pair<>(Optional.of(waitAgainEvent), shop));
                });
    }

    @Override
    public String toString() {
        return "";
    }
}
