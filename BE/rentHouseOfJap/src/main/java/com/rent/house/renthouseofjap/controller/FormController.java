package com.rent.house.renthouseofjap.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rent.house.renthouseofjap.bean.FormBody;
import com.rent.house.renthouseofjap.service.IFormService;
import com.rent.house.renthouseofjap.until.GetConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
public class FormController {

    @Autowired
    private GetConf conf;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private IFormService service;

    @Value("json.editing")
    private String RESENT_EDITED_FORM_ID;

    private ObjectMapper mapper = new ObjectMapper();


    // 处理 POST 请求，接收表单数据，并保存到 JSON 文件中
    @PostMapping("/submit")
    public String submit(
            @RequestParam("form-body") String body) {
        int formID;

        //将json形式参数info转换成bean包下面的formInfo类，把info参数映射formInfo类里面的字段。
        try {
            FormBody formBody = new ObjectMapper().readValue(body, FormBody.class);

            boolean result;
            if (formBody.isNewFlag()){
                result = service.createAForm(formBody);
            }else {
                result = service.update(formBody);
            }

            if (!result) {
                return "表单提交失败！";
            }

            // 返回成功信息
            return "表单提交成功！";


        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


//        String parentPath = conf.getPath() + formName + "/";
//        //判断指定的路径是否存在
//        //如果存在就直接数字加一直接写入
//        if (GetPath.isExist(parentPath)) {
//            Integer number = this.getFileList(formName).stream().max(Integer::compare).get() + 1;
//            String pathWithNumber = parentPath + number + "/";
//
//            this.writeFile(pathWithNumber + conf.getDetail().getData(), data);
//            this.writeFile(pathWithNumber + conf.getDetail().getInfo(), info);
//        }
//        // 不存在就需要创建一份
//        else {
//            String pathWithZero = parentPath + "0/";
//            try {
//                Files.createDirectories(Paths.get(pathWithZero));
//            } catch (IOException e) {
//                throw new RuntimeException("无法创建路径：" + e.getMessage());
//            }
//
//            this.writeFile(pathWithZero + conf.getDetail().getData(), data);
//            this.writeFile(pathWithZero + conf.getDetail().getInfo(), info);
//        }



    }

    @GetMapping("/get-form")
    public ResponseEntity<Object> data(
            @RequestParam(value = "form-id", required = false)
            String formID,
            @RequestParam(value = "version-id")
            int versionId
    ) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

        // form-json
        try (InputStream fis = resourceLoader.getResource(conf.getTemplate() + conf.getDetail().getJson()).getInputStream()) {
            map.add("form-json", mapper.readValue(fis, Map.class));
        } catch (IOException e) {
            throw new RuntimeException("未读取到模板文件" + e.getMessage());
        }

        // form-data
        FormBody latest;
        if (formID == null || formID.equals("new")) {
            latest = service.getFormBody();
        } else {
            latest = service.getFormBody(Integer.parseInt(formID), versionId);
        }

        map.add("form-data", latest.getData());


//        // 初次登录 或是 没有任何表单
//        if (!GetPath.isExist(conf.getPath() + formName)) {
//            map.add("form-data", "{}");
//
//            // form-info
//            try (InputStream fis = resourceLoader.getResource(conf.getTemplate() + conf.getDetail().getInfo()).getInputStream()) {
//                map.add("form-info", mapper.readValue(fis, Map.class));
//            } catch (IOException e) {
//                throw new RuntimeException("未读取到模板文件" + e.getMessage());
//            }
//
//            return new ResponseEntity<>(map, headers, HttpStatus.OK);
//        }
//
//        // 使用 try-with-resources 语句来自动关闭输入流
//        try (FileInputStream fis = new FileInputStream(this.getPath4FormData(formName))) {
//            // 从文件中读取 JSON 字符串，并转换为 Map 对象
//            Map<String, Object> data = mapper.readValue(fis, Map.class);
//
//            // 把 data 和 resource 整合为一个 MultiValueMap 对象
//            map.add("form-data", data);
//
//            // 获取附件文件的路径和名称（这里假设从 Map 对象中获取）
//
//
//        } catch (IOException e) {
//            // 抛出异常，交由异常处理器处理
//            throw new RuntimeException(e);
//        }

        // 创建一个 ResponseEntity 对象，设置状态码为 200（成功），并将 MultiValueMap 对象作为数据对象
        ResponseEntity<Object> response = new ResponseEntity<>(map, headers, HttpStatus.OK);
        // 返回 ResponseEntity 对象
        return response;
    }

    // 获取最近一次提交的表单的名称。
    @GetMapping("/editing-file-id")
    public ResponseEntity<Integer> editingFileName() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        if (redisTemplate.hasKey(RESENT_EDITED_FORM_ID)) {
            int formID = Integer.parseInt(redisTemplate.boundValueOps(RESENT_EDITED_FORM_ID).get().toString());
            ResponseEntity<Integer> response = new ResponseEntity<>(formID, headers, HttpStatus.OK);
            return response;
        }
        return new ResponseEntity<>(null, headers, HttpStatus.OK);
    }

    // 获取所有提交的表单的ID和名称
    @GetMapping("/get-all-form-info")
    public ResponseEntity<Object> allFormInfo() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("form-info-all", service.getAllFormInfo());

        return new ResponseEntity<>(map, headers, HttpStatus.OK);
    }

    // 获取所有某个表单的所有提交
    @GetMapping("/get-content-by-id")
    public ResponseEntity<Object> getContentById(
            @RequestParam(value = "form-id")
            int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("content", service.getAllContentById(id));

        return new ResponseEntity<>(map, headers, HttpStatus.OK);
    }
}
