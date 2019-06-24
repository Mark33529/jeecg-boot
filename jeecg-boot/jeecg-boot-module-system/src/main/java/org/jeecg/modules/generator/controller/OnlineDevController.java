package org.jeecg.modules.generator.controller;

import com.alibaba.fastjson.JSON;
import org.jeecg.modules.generator.service.IOnlineDevService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.demo.test.entity.JeecgDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description: 在线开发
 * @Author: Leo he
 * @Date:2019-06-19
 * @Version:V1.0
 */
@Slf4j
@Api(tags="单表DEMO")
@RestController
@RequestMapping("/online")
public class OnlineDevController extends JeecgController<JeecgDemo,IOnlineDevService> {

	@Autowired
	private IOnlineDevService onlineDevService;

	@Autowired
	private RedisUtil redisUtil;

    /**
     *
     * @param json
     * @return
     */
	@PostMapping(value = "/add")
	public Result add(@RequestBody JSONObject json) {
		Result result = new Result();
		try {
            String table = readJson("classpath:org/jeecg/modules/demo/mock/json/formAdd.json");
            if(StringUtils.isBlank(table)) {
                result.error500("无效的数据库表，请重新设计");
            }
            JSONObject tableJSON = JSON.parseObject(table);
            int generateResult = this.onlineDevService.generateCodes(tableJSON);
            if(generateResult > 0) {
                result.success("成功创建数据库表，及生成" + generateResult + "个代码文件！");
            }else {
                result.error500("创建失败");
            }
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}

    @GetMapping(value = "/service")
    public String service() {
        return readJson("classpath:org/jeecg/modules/demo/mock/json/service.json");
    }

    /**
     * 读取json格式文件
     * @param jsonSrc
     * @return
     */
    private String readJson(String jsonSrc) {
        String json = "";
        try {
            //File jsonFile = ResourceUtils.getFile(jsonSrc);
            //json = FileUtils.re.readFileToString(jsonFile);
            //换个写法，解决springboot读取jar包中文件的问题
            InputStream stream = getClass().getClassLoader().getResourceAsStream(jsonSrc.replace("classpath:", ""));
            json = IOUtils.toString(stream);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return json;
    }

}
