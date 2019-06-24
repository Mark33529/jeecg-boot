package org.jeecg.modules.generator.service;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.system.base.service.JeecgService;
import org.jeecg.modules.demo.test.entity.JeecgDemo;

/**
 * @Description: jeecg 测试demo
 * @Author: jeecg-boot
 * @Date:  2018-12-29
 * @Version: V1.0
 */
public interface IOnlineDevService extends JeecgService<JeecgDemo> {
	
	public int generateCodes(JSONObject json);
}
