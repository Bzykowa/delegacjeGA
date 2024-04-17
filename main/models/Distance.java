package main.models;

import java.time.Duration;

public class Distance implements Comparable<Distance> {
    // for Pairs first is start to border, second border to end
    // for PL - use border id = -1 and pairs (start to end, 0)
    private Pair<Double, Double> kilometres;
    private Pair<Duration, Duration> duration;
    public int start;
    public int border;
    public int end;
    public String startName;
    public String borderName;
    public String endName;

    public Distance(
            Pair<Double, Double> kilometres,
            Pair<Duration, Duration> duration,
            int start,
            int border,
            int end,
            String startName,
            String borderName,
            String endName) {
        this.kilometres = kilometres;
        this.duration = duration; // Duration.ofSeconds(duration) original
        this.start = start;
        this.border = border;
        this.end = end;
        this.startName = startName;
        this.borderName = borderName;
        this.endName = endName;
    }

    public Distance() {

    }

    /*
     * If the border is -1 then it's not Germany distance
     */
    public boolean isGermany() {
        if (border == -1)
            return false;
        else
            return true;
    }

    /*
     * Get full kilometer length of this Distance
     */
    public double getFullKilometers() {
        return kilometres.getFirst() + kilometres.getSecond();
    }

    /*
     * Get first part of the kilometer length for this Distance (start to border)
     */
    public double getFirstKilometers() {
        return kilometres.getFirst();
    }

    /*
     * Get second part of the kilometer length for this Distance (border to end)
     */
    public double getSecondKilometers() {
        return kilometres.getSecond();
    }

    /*
     * Get full duration of this Distance
     */
    public Duration getFullDuration() {
        return duration.getFirst().plus(duration.getSecond());
    }

    /*
     * Get first part of the duration of journey for this Distance (start to border)
     */
    public Duration getFirstDuration() {
        return duration.getFirst();
    }

    /*
     * Get second part of the duration of journey for this Distance (border to end)
     */
    public Duration getSecondDuration() {
        return duration.getSecond();
    }

    @Override
    public int compareTo(Distance o) {
        return Double.compare(this.getFullKilometers(), o.getFullKilometers());
    }

    @Override
    public boolean equals(Object obj) {
        return this.start == ((Distance) obj).start && this.end == ((Distance) obj).end;
    }

    // deep copy
    public Distance clone() {
        Distance d = new Distance();
        d.kilometres = new Pair<Double, Double>(this.kilometres.getFirst(), this.kilometres.getSecond());
        d.duration = new Pair<Duration, Duration>(Duration.ofSeconds(this.duration.getFirst().getSeconds()),
                Duration.ofSeconds(this.duration.getSecond().getSeconds()));
        d.start = this.start;
        d.border = this.border;
        d.end = this.end;
        d.startName = new String(this.startName);
        d.borderName = new String(this.borderName);
        d.endName = new String(this.endName);
        return d;
    }

}
