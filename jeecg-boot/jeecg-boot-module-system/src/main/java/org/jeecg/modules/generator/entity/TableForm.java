package org.jeecg.modules.generator.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description: 数据库表实体
 * @Author: cj
 * @Date:	2019-06-23
 * @Version:V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="数据库表实体", description="数据库表实体")
public class TableForm implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据库表名")
    private String tableName;

    @ApiModelProperty(value = "数据库表描述")
    private String description;

    @ApiModelProperty(value = "实体名称")
    private String entityName;

    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty(value = "业务包名")
    private String packageName;

    @ApiModelProperty(value = "主键策略")
    private int primaryKeyPolicy;

    @ApiModelProperty(value = "是否分页")
    private boolean isPaging;

    @ApiModelProperty(value = "模版类型")
    private int templateType;

}