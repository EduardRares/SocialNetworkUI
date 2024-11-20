package com.example.socialnetworkui.domain;
import java.time.LocalDateTime;

public class Friendship extends Entity<Tuple<Long, Long>> {

    private LocalDateTime date;
    public Friendship() {
        date = LocalDateTime.now();
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    @Override
    public String toString() {
        return "Friendship [date=" + date + "]";
    }
}
