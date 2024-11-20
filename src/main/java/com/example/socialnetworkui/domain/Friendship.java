package com.example.socialnetworkui.domain;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Friendship)) return false;
        Friendship other = (Friendship) obj;
        return (Objects.equals(this.getId().getLeft(), other.getId().getLeft()) && Objects.equals(this.getId().getRight(), other.getId().getRight()));
    }
}
