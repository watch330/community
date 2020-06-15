package com.watch330.community.cache;


import com.watch330.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {
    public static List<TagDTO> get(){
        List<TagDTO> tagDTOS = new ArrayList<>();

        TagDTO language = new TagDTO();
        language.setCategoryName("开发语言");
        language.setTags(Arrays.asList("Java","C","C++","Python","Javascript","Php"));
        tagDTOS.add(language);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("Spring","Flask","Spring Boot","Django"));
        tagDTOS.add(framework);

        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("Linux","nginx","Docker","Apache","Tomcat"));
        tagDTOS.add(server);

        TagDTO database = new TagDTO();
        database.setCategoryName("数据库与缓存");
        database.setTags(Arrays.asList("Mysql","MariaDB","Oracle","Redis","MongoDB","SqlServer"));
        tagDTOS.add(database);

        TagDTO others = new TagDTO();
        others.setCategoryName("其他");
        others.setTags(Arrays.asList("资源分享","小说","剧集","电影","音乐","漫画"));
        tagDTOS.add(others);

        return tagDTOS;
    }

    public static String isValid(String tags){
        String[] split = StringUtils.split(tags, ",");
        List<TagDTO> tagDTOS = get();
        List<String> tagList = tagDTOS.stream().flatMap(tagDTO -> tagDTO.getTags().stream()).collect(Collectors.toList());
        String inValidTags = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));

        return inValidTags;
    }
}
