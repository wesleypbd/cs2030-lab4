import java.util.Optional;

public abstract class Event implements Comparable<Event> {
    protected final double eventTime;
    protected final Customer customer;
    private static final double EPSILON = 1e-9;

    public Event(Customer customer, double eventTime) {
        this.customer = customer;
        this.eventTime = eventTime;
    }

    public abstract Optional<Pair<Optional<Event>, Shop>> next(Shop shop);

    public Pair<Double, Integer> updateStats(Pair<Double, Integer> stats) {
        return stats;
    }

    @Override
    public int compareTo(Event other) {
        if (Math.abs(this.eventTime - other.eventTime) < EPSILON) {
            return this.customer.compareTo(other.customer);
        }
        return Double.compare(this.eventTime, other.eventTime);
    }

    @Override
    public abstract String toString();
}
