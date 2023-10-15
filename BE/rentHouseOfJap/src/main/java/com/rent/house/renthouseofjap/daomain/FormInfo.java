package com.rent.house.renthouseofjap.daomain;

import lombok.Data;

import java.util.Date;


@Data
public class FormInfo {
    //主键
    private int id;
    private String name;
    private Date createDate;
    private Date updateDate;
}
