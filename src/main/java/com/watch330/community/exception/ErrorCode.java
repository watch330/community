package com.watch330.community.exception;

public enum ErrorCode implements IErrorCode {

    SYS_ERROR(201,"服务出了点故障..."),
    QUESTION_NOT_FOUND(202, "竟然没有这个问题了..."),
    COMMENT_NOT_FOUND(203,"竟然没有这个回复了..."),
    TARGET_PARAM_NOT_FOUND(204, "回复的评论或问题不见了..."),
    TYPE_PARAM_NOT_FOUND(205,"没有回复类型..."),
    NOTIFICATION_NOT_FOUND(206,"消息不见了..."),
    USER_NOT_LOGIN(101,"用户没有登录..."),
    CONTENT_IS_EMPTY(102,"内容不能为空..."),
    READ_NOTIFICATION_FAILED(103,"不要看其他人的通知...");

    private String message;
    private Integer code;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    ErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
