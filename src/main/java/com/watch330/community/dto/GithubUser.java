package com.watch330.community.dto;

import lombok.Data;

@Data
/*存储github用户信息的实体*/
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatarUrl;
}
