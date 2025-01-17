package org.jeecg.modules.generator.entity;

import java.io.Serializable;

import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: jeecg 测试demo 
 * @Author: jeecg-boot 
 * @Date:	2018-12-29 
 * @Version:V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="测试DEMO对象", description="测试DEMO")
@TableName("demo")
public class JeecgDemo extends JeecgEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 部门编码 */
	@Excel(name="部门编码",width=25)
	@ApiModelProperty(value = "部门编码")
	private String sysOrgCode;
	/** 姓名 */
	@Excel(name="姓名",width=25)
	@ApiModelProperty(value = "姓名")
	private String name;
	/** 关键词 */
	@ApiModelProperty(value = "关键词")
	@Excel(name="关键词",width=15)
	private String keyWord;
	/** 打卡时间 */
	@ApiModelProperty(value = "打卡时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name="打卡时间",width=20,format="yyyy-MM-dd HH:mm:ss")
	private java.util.Date punchTime;
	/** 工资 */
	@ApiModelProperty(value = "工资",example = "0")
	@Excel(name="工资",width=15)
	private java.math.BigDecimal salaryMoney;
	/** 奖金 */
	@ApiModelProperty(value = "奖金",example = "0")
	@Excel(name="奖金",width=15)
	private Double bonusMoney;
	/** 性别 {男:1,女:2} */
	@ApiModelProperty(value = "性别")
	@Excel(name = "性别", width = 15, dicCode = "sex")
	private String sex;
	/** 年龄 */
	@ApiModelProperty(value = "年龄",example = "0")
	@Excel(name="年龄",width=15)
	private Integer age;
	/** 生日 */
	@ApiModelProperty(value = "生日")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Excel(name="生日",format="yyyy-MM-dd")
	private java.util.Date birthday;
	/** 邮箱 */
	@ApiModelProperty(value = "邮箱")
	@Excel(name="邮箱",width=30)
	private String email;
	/** 个人简介 */
	@ApiModelProperty(value = "个人简介")
	private String content;
}
