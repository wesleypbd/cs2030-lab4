import java.util.Optional;

public class WaitEvent extends Event {
    private final Server server;

    public WaitEvent(Customer customer, double eventTime, Server server) {
        super(customer, eventTime);
        this.server = server;
    }

    @Override
    public Optional<Pair<Optional<Event>, Shop>> next(Shop shop) {
        // Add customer to queue
        Server updatedServer = server.pushQueue();
        Shop updatedShop = shop.update(updatedServer);

        // Create WaitAgainEvent to check server
        // availability at server's next available time
        return Optional.of(new Pair<>(
                Optional.of(new WaitAgainEvent(
                        customer,
                        updatedServer.getNextAvailableTime(),
                        updatedServer)),
                updatedShop));
    }

    @Override
    public String toString() {
        return String.format("%.3f %s waits at %s", eventTime, customer, server);
    }
}
