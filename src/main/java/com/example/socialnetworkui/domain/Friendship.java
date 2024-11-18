package com.example.socialnetworkui.domain;
import java.time.LocalDateTime;

public class Friendship extends Entity<Tuple<Long, Long>> {

    private LocalDateTime date;
    private Boolean pending;
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

    public void setPending(Boolean pending) {
        this.pending = pending;
    }

    public Boolean getPending() {
        return pending;
    }
}
