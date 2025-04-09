import java.util.Optional;

public class WaitAgainEvent extends Event {
    private final Server server;

    public WaitAgainEvent(Customer customer, double eventTime, Server server) {
        super(customer, eventTime);
        this.server = server;
    }

    @Override
    public Optional<Pair<Optional<Event>, Shop>> next(Shop shop) {
        return shop.findServer(eventTime, server)
                .flatMap(currentServer -> {
                    if (currentServer.getNextAvailableTime() <= this.eventTime) {
                        // Serve the customer as the server is available
                        Server updatedServer = currentServer.popQueue();
                        Shop updatedShop = shop.update(updatedServer);
                        Event serveEvent = new ServeEvent(
                                customer,
                                eventTime,
                                updatedServer);
                        return Optional.of(new Pair<>(Optional.of(serveEvent), updatedShop));
                    } else {
                        // Schedule another WaitAgainEvent
                        Event waitAgainEvent = new WaitAgainEvent(
                                customer,
                                currentServer.getNextAvailableTime(),
                                currentServer
                        );
                        return Optional.of(new Pair<>(Optional.of(waitAgainEvent), shop));
                    }
                });
                //.or(() -> Optional.empty());
    }

    @Override
    public String toString() {
        return "";
    }
}
