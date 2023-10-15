package com.rent.house.renthouseofjap.daomain;

import lombok.Data;

import java.util.Date;


@Data
public class Content {
    //内容主键
    private int id;
    //表单主键
    private int formID;
    private String formJson;
    private String formData;
    private Date createDate;
}
