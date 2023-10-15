package com.rent.house.renthouseofjap.dao;

import com.rent.house.renthouseofjap.bean.FormBody;
import com.rent.house.renthouseofjap.daomain.Content;
import com.rent.house.renthouseofjap.daomain.FormInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IFormDao {
    //新建表单
    public void createFormInfo(FormInfo formInfo);
    public void createContent(Content content);

    //更新对应表单
    //@Update("UPDATE content SET form_id = #{formID}, form_json = #{formJson}, form_data = #{formData}, create_date = #{createDate} WHERE id = #{id}")
//    public void update(FormBody body);
//
//    //获取历史版本
//    //@Select("SELECT * FROM content")
//    public List<FormBody> getAll();

    public FormBody selectLatest();

    public List<FormBody> selectFormBodyById(int id);

    public List<FormInfo> selectAllFormInfo();

    public List<Content> selectContentById(int id);

}
