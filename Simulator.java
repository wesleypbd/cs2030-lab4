import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.function.Supplier;

class Simulator {
    private final int numOfServers;
    private final int qmax;
    private final Supplier<Double> serviceTime;
    private final int numOfCustomers;
    private final List<Pair<Integer, Double>> arrivals;

    Simulator(int numOfServers,
              int qmax,
              Supplier<Double> serviceTime,
              int numOfCustomers,
              List<Pair<Integer, Double>> arrivals) {
        this.numOfServers = numOfServers;
        this.qmax = qmax;
        this.serviceTime = serviceTime;
        this.numOfCustomers = numOfCustomers;
        this.arrivals = arrivals;
    }

    Pair<String, String> run() {
        PQ<Event> pq = new PQ<>();
        for (Pair<Integer, Double> arrival : arrivals) {
            int customerId = arrival.t();
            double arrivalTime = arrival.u();
            Customer customer = new Customer(customerId, arrivalTime);
            pq = pq.add(new ArriveEvent(customer, arrivalTime));
        }

        State init = new State(pq, new Shop(numOfServers, serviceTime, qmax));

        List<State> states = Stream.iterate(Optional.of(init),
                        ostate -> ostate.isPresent(),
                        ostate -> ostate.flatMap(state -> state.next()))
                .limit(numOfCustomers + 100)
                .map(ostate -> ostate.get())
                .filter(state -> !state.isEmpty())
                .toList();

        // Accumulate stats
        Pair<Double, Integer> stats = states.stream()
                .map(state -> state.getEvent())
                .filter(oevent -> oevent.isPresent())
                .map(oevent -> oevent.get())
                .reduce(new Pair<>(0.0, 0), (acc, event) ->
                        event.updateStats(acc), (a, b) ->
                        new Pair<>(a.t() + b.t(), a.u() + b.u())
                );

        List<String> eventLog = states.stream()
                .map(state -> state.getEvent())
                .filter(oevent -> oevent.isPresent())
                .map(oevent -> oevent.get())
                .map(event -> event.toString())
                .filter(event -> !event.isEmpty())
                .toList();

        double avgWaitTime = stats.u() > 0 ? stats.t() / stats.u() : 0.0;
        long numCustomersLeft = this.numOfCustomers - stats.u();

        String t = String.join("\n", eventLog);
        String u = "\n" + "[" +
                String.format("%.3f", avgWaitTime) + " " +
                stats.u() + " " +
                numCustomersLeft +
                "]";

        return new Pair<>(t, u);
    }
}