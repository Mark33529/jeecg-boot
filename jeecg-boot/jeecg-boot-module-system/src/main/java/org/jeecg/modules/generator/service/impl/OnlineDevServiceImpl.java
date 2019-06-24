package org.jeecg.modules.generator.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.system.base.service.impl.JeecgServiceImpl;
import org.jeecg.common.util.FileGeneratorUtil;
import org.jeecg.modules.demo.test.entity.JeecgDemo;
import org.jeecg.modules.demo.test.mapper.JeecgDemoMapper;
import org.jeecg.modules.generator.entity.OnlineProperties;
import org.jeecg.modules.generator.entity.TableForm;
import org.jeecg.modules.generator.service.IOnlineDevService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: jeecg 测试demo
 * @Author: jeecg-boot
 * @Date:  2018-12-29
 * @Version: V1.0
 */
@Service
public class OnlineDevServiceImpl extends JeecgServiceImpl<JeecgDemoMapper, JeecgDemo> implements IOnlineDevService {

	@Autowired
    OnlineProperties onlineProperties;

	@Override
	public int generateCodes(JSONObject json) {

        JSONArray columns = json.getJSONArray("columns");
        json.remove("columns");

        TableForm tableObj = JSONObject.toJavaObject(json, TableForm.class);

        //默认按模版1生成
        String templateName = onlineProperties.getTemplate1();
        if(tableObj.getTemplateType() == 2) {
            templateName = onlineProperties.getTemplate2();
        }else if(tableObj.getTemplateType() == 3) {
            templateName = onlineProperties.getTemplate3();
        }
        //生成数据库表 - TODO


        //生成代码文件
        int result = FileGeneratorUtil.generateCodes(tableObj.getBusinessName(), tableObj.getPackageName(), tableObj.getEntityName(),
                onlineProperties.getTemplate().getPath(), onlineProperties.getCode().getPath(), templateName,
                tableObj.getTableName(), tableObj.getDescription(), columns);

	    return result;
	}

}
