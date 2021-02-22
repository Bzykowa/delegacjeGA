package main.models;

import java.time.Duration;

public class Distance implements Comparable<Distance>{
    public double kilometres;
    public Duration duration;
    public int start;
    public int end;
    public String startName;
    public String endName;

    public Distance(double kilometres, int duration, int start, int end, String startName, String endName){
        this.kilometres = kilometres;
        this.duration = Duration.ofSeconds(duration);
        this.start = start;
        this.end = end;
        this.startName = startName;
        this.endName = endName;
    }

    public Distance(){

    }

    @Override
    public int compareTo(Distance o) {
        return Double.compare(this.kilometres,o.kilometres);
    }

    @Override
    public boolean equals(Object obj) {
        return this.start == ((Distance)obj).start && this.end == ((Distance)obj).end;
    }

    //deep copy
    public Distance clone(){
        Distance d =  new Distance();
        d.kilometres = this.kilometres;
        d.duration = Duration.ofSeconds(this.duration.getSeconds());
        d.start = this.start;
        d.end  = this.end;
        d.startName = new String(this.startName);
        d.endName = new String(this.endName);
        return d;
    }

}
