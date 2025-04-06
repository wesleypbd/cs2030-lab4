import java.util.Optional;

public class State {
    private final PQ<Event> pq;
    private final Shop shop;
    private final Optional<Event> event;

    public State(PQ<Event> pq, Shop shop) {
        this.pq = pq;
        this.shop = shop;
        this.event = Optional.empty();
    }

    private State(PQ<Event> pq, Shop shop, Optional<Event> event) {
        this.pq = pq;
        this.shop = shop;
        this.event = event;
    }

    public Optional<State> next() {
        Pair<Optional<Event>, PQ<Event>> polled = pq.poll();

        Optional<Event> currEvent = polled.t();
        PQ<Event> currPQ = polled.u();

        return currEvent.flatMap(event ->
                event.next(this.shop).flatMap(nextPair -> {
                    Optional<Event> nextEvent = nextPair.t();
                    Shop newShop = nextPair.u();

                    return nextEvent
                        .map(e -> new State(currPQ.add(e), newShop, Optional.of(event)))
                        .or(() -> Optional.of(new State(currPQ, newShop, Optional.of(event))));
                })
        ).or(() -> Optional.of(new State(currPQ, shop, Optional.empty())));
    }

    public boolean isEmpty() {
        return pq.isEmpty() && event.isEmpty();
    }

    public Optional<Event> getEvent() {
        return event;
    }
}