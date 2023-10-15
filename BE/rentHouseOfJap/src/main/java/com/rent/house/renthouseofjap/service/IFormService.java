package com.rent.house.renthouseofjap.service;

import com.rent.house.renthouseofjap.bean.ContentExtra;
import com.rent.house.renthouseofjap.bean.FormBody;
import com.rent.house.renthouseofjap.daomain.FormInfo;

import java.util.List;

public interface IFormService {
    //新建表单
    public boolean createAForm(FormBody body);
    //更新对应表单
    public boolean update(FormBody body);
    //获取历史版本
    public List<FormBody> getAll();

    //获取
    public FormBody getFormBody();
    public FormBody getFormBody(int id, int version);

    public List<FormInfo> getAllFormInfo();

    public List<ContentExtra> getAllContentById(int id);
}
