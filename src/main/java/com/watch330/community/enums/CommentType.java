package com.watch330.community.enums;

/**
 * 评论类型枚举, 1为问题的回复,2为回复的回复
 */
public enum CommentType {
    QUESTION(1),
    COMMENT(2);

    private Integer type;

    public static boolean isExist(Integer type) {
        for (CommentType commentType : CommentType.values()) {
            if (commentType.getType()==type)
                return true;
        }
        return false;
    }

    public Integer getType() {
        return type;
    }

    CommentType(Integer type) {
        this.type = type;
    }
}
