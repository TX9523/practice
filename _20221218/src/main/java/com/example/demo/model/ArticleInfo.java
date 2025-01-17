package com.example.demo.model;

import lombok.Data;

@Data
public class ArticleInfo {
    private int id;
    private String title;
    private String content;
    private String createtime;
    private String updatetime;
    private int uid;
    private int rcount;
    private int state;
    // 包含了userinfo对象
    private UserInfo userinfo;
}
