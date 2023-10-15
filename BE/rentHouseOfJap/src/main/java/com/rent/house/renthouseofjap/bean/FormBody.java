package com.rent.house.renthouseofjap.bean;

import lombok.Data;

import java.util.Date;

@Data
public class FormBody {
    //表单id
    private Integer ID;
    //表单名称
    private String formName;
    //日期
    private Date createDate;
    //日期
    private Date updateDate;
    //表单内容
    private String data;
    //是否新建
    private boolean newFlag;
}
