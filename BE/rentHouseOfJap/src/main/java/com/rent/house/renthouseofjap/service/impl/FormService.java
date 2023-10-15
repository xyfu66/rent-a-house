package com.rent.house.renthouseofjap.service.impl;

import com.rent.house.renthouseofjap.bean.ContentExtra;
import com.rent.house.renthouseofjap.bean.FormBody;
import com.rent.house.renthouseofjap.dao.IFormDao;
import com.rent.house.renthouseofjap.daomain.Content;
import com.rent.house.renthouseofjap.daomain.FormInfo;
import com.rent.house.renthouseofjap.service.IFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class FormService implements IFormService {
    @Autowired
    private IFormDao dao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("json.editing")
    private String RESENT_EDITED_FORM_ID;

    @Override
    public boolean createAForm(FormBody body) {
        Date date = new Date();
        FormInfo formInfo = new FormInfo();
        formInfo.setName(body.getFormName());
        formInfo.setCreateDate(date);
        dao.createFormInfo(formInfo);

        // 获取插入后的自增主键值
        int formId = formInfo.getId();
        Content content = new Content();
        content.setFormData(body.getData());
        // 将自增主键值赋值给 content 对象的 formID 属性
        content.setFormID(formId);
        content.setCreateDate(date);
        dao.createContent(content);

        redisTemplate.boundValueOps(RESENT_EDITED_FORM_ID).set(formId);

        return true;
    }

    @Override
    public boolean update(FormBody body) {
        Content content = new Content();
        content.setFormData(body.getData());
        // 将自增主键值赋值给 content 对象的 formID 属性
        content.setFormID(body.getID());
        content.setCreateDate(new Date());
        dao.createContent(content);

        redisTemplate.boundValueOps(RESENT_EDITED_FORM_ID).set(body.getID());

        return true;
    }

    @Override
    public List<FormBody> getAll() {
        return null;
    }

    @Override
    public FormBody getFormBody() {
        Object key = redisTemplate.boundValueOps(RESENT_EDITED_FORM_ID).get();
        if (key == null) {
            return dao.selectLatest();
        }
        return dao.selectFormBodyById(Integer.parseInt(key.toString())).get(0);
    }

    @Override
    public FormBody getFormBody(int id, int version) {
        List<FormBody> formBodies = dao.selectFormBodyById(id);

        return dao.selectFormBodyById(id).get(formBodies.size() - 1 - version);
    }

    @Override
    public List<FormInfo> getAllFormInfo() {
        return dao.selectAllFormInfo();
    }

    @Override
    public List<ContentExtra> getAllContentById(int id) {

        List<Content> contentList = dao.selectContentById(id);

        List<ContentExtra> contentExtraList = IntStream.range(0, contentList.size())
                .boxed()
                .map(index -> {
                    Content content = contentList.get(index);
                    ContentExtra contentExtra = new ContentExtra();
                    contentExtra.setId(content.getId());
                    contentExtra.setFormID(content.getFormID());
                    contentExtra.setFormJson(content.getFormJson());
                    contentExtra.setFormData(content.getFormData());
                    contentExtra.setCreateDate(content.getCreateDate());
                    contentExtra.setVersion(contentList.size() - index);

                    return contentExtra;
                })
                .collect(Collectors.toList());

        return contentExtraList;
    }
}
