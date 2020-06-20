package com.watch330.community.enums;

public enum NotificationType {
    REPLY_COMMENT(1, "回复了评论"),
    REPLY_QUESTION(2, "回复了问题");
    private int type;
    private String name;

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    NotificationType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String nameOfType(int type){
        for (NotificationType value : NotificationType.values()) {
            if(value.getType()==type){
                return value.getName();
            }
        }
        return "";
    }

}
