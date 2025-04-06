import java.util.Optional;

public class LeaveEvent extends Event {
    public LeaveEvent(Customer customer, double eventTime) {
        super(customer, eventTime);
    }

    public Optional<Pair<Optional<Event>, Shop>> next(Shop shop) {
        return Optional.of(new Pair<>(Optional.empty(), shop));
    }

    @Override
    public String toString() {
        return String.format("%.3f %s leaves", eventTime, customer);
    }
}
