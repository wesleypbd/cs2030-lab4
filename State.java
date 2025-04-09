import java.util.Optional;

public class State {
    private final PQ<Event> pq;
    private final Shop shop;
    private final Optional<Event> event;
    private final String output;

    public State(PQ<Event> pq, Shop shop) {
        this.pq = pq;
        this.shop = shop;
        this.event = Optional.empty();
        this.output = "";
    }

    private State(PQ<Event> pq, Shop shop, Optional<Event> event, String output) {
        this.pq = pq;
        this.shop = shop;
        this.event = event;
        this.output = output;
    }

    public Optional<State> next() {
        Pair<Optional<Event>, PQ<Event>> polled = pq.poll();
        //System.out.println(pq);

        Optional<Event> currEvent = polled.t();
        //System.out.println(currEvent);
        PQ<Event> currPQ = polled.u();

        Optional<State> result = currEvent.flatMap(event ->
                event.next(this.shop).flatMap(nextPair -> {
                    Optional<Event> nextEvent = nextPair.t();
                    Shop newShop = nextPair.u();
                    //System.out.println("Event: " + event);
                    //System.out.println("Shop: " + this.shop);
                    return nextEvent
                        .map(e -> new State(
                                currPQ.add(e), newShop, Optional.of(event), event.toString()))
                        .or(() -> Optional.of(new State(
                                //currPQ, newShop, Optional.of(event), event.toString())));
                                currPQ, newShop, Optional.empty(), event.toString())));
                })
        //).or(() -> Optional.of(new State(currPQ, shop, Optional.empty(), output)));
        );

        //System.out.println(result);
        return result;
    }

    public boolean isEmpty() {
        //return pq.isEmpty() && event.toString().isEmpty();
        return pq.isEmpty() && output.isEmpty();
    }

    public Optional<Event> getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return output;
    }
}