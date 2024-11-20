package com.example.socialnetworkui.utils.events;
import com.example.socialnetworkui.domain.User;

public class EntityChangeEvent implements Event {
    private ChangeEventType type;
    private Long data, oldData;

    public EntityChangeEvent(ChangeEventType type, Long data) {
        this.type = type;
        this.data = data;
    }
    public EntityChangeEvent(ChangeEventType type, Long data, Long oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Long getData() {
        return data;
    }

    public Long getOldData() {
        return oldData;
    }
}