package main.models;

public class Pair<T1, T2> {

    private final T1 first;
    private final T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "[" + first + ", " + second + "]";
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return this.first == ((Pair<T1, T2>) obj).getFirst() && this.second == ((Pair<T1, T2>) obj).getSecond();
        } catch (Exception e) {
            return false;
        }
    }
}
