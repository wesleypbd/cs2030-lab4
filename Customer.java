public class Customer {
    private final int id;
    private final double arriveTime;

    // Lab 4
    public Customer(int id, double arriveTime) {
        this.id = id;
        this.arriveTime = arriveTime;
    }

    public Boolean canBeServed(double serverNextAvailableTime) {
        return this.arriveTime >= serverNextAvailableTime;
    }

    public int compareTo(Customer customer) {
        return Double.compare(this.arriveTime, customer.arriveTime);
    }

    public double getArrivalTime() {
        return this.arriveTime;
    }

    public String toString() {
        return "customer " + this.id;
    }

}
