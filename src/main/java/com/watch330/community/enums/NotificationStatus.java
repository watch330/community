package com.watch330.community.enums;

public enum NotificationStatus {
    UNREAD(0),
    READ(1);
    private int status;

    public int getStatus() {
        return status;
    }

    NotificationStatus(int status) {
        this.status = status;
    }
}
