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
        Optional<Pair<Optional<Event>, PQ<Event>>> optionalPair =
                Optional.of(pq.poll());

        return optionalPair.flatMap(currPair -> {
            Optional<Event> currEvent = currPair.t();
            PQ<Event> currPQ = currPair.u();

            return currEvent.flatMap(event ->
                    event.next(this.shop).map(nextPair -> {
                        Optional<Event> nextEvent = nextPair.t();
                        Shop newShop = nextPair.u();
                        PQ<Event> newPQ = nextEvent
                                .map(e -> currPQ.add(e))
                                .orElse(currPQ);

                        return new State(newPQ, newShop, Optional.of(event));
                    })
            ).or(() -> Optional.of(new State(currPQ, shop, Optional.empty())));
        });
    }

    public boolean isEmpty() {
        return pq.isEmpty() && event.isEmpty();
    }

    public Optional<Event> getEvent() {
        return event;
    }

    /*
    @Override
    public String toString() {
        return event.map(e -> e.toString()).orElse("");
    }
     */
}