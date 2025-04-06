import java.util.Optional;

class ArriveEvent extends Event {
    public ArriveEvent(Customer customer, double eventTime) {
        super(customer, eventTime);
    }

    @Override
    public Optional<Pair<Optional<Event>, Shop>> next(Shop shop) {
        return shop.findServer(customer)
                .map(server -> {
                    Event serve = new ServeEvent(customer, eventTime, server);
                    return new Pair<>(Optional.of(serve), shop);
                })
                .or(() -> shop.findQueue(customer)
                        .map(server -> {
                            Event wait = new WaitEvent(customer, eventTime, server);
                            return new Pair<>(Optional.of(wait), shop);
                        }))
                .or(() -> {
                    Event leave = new LeaveEvent(customer, eventTime);
                    return Optional.of(new Pair<>(Optional.of(leave), shop));
                });
    }

    @Override
    public String toString() {
        return String.format("%.3f %s arrives", eventTime, customer);
    }
}
