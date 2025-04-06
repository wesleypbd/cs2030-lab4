import java.util.Optional;

public class DoneEvent extends Event {
    private final Server server;

    public DoneEvent(Customer customer, double eventTime, Server server) {
        super(customer, eventTime);
        this.server = server;
    }

    @Override
    public Optional<Pair<Optional<Event>, Shop>> next(Shop shop) {
        Shop updatedShop = shop.update(new Server(server));
        return Optional.of(new Pair<>(Optional.empty(), updatedShop));
    }

    @Override
    public String toString() {
        return String.format("%.3f %s done", eventTime, customer);
    }
}
