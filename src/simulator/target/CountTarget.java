package simulator.target;

public class CountTarget extends Target {

    private int count = 0;

    @Override
    public boolean contains(double X, double Y) {
        if (super.contains(X, Y)) {
            count++;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return Integer.toString(count);
    }
}
