package com.watch330.community.dto;


import lombok.Data;

@Data
/*获取github用户授权必要的信息实体*/
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
