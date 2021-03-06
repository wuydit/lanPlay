package org.wyw.lanplay.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wuYd
 * @since 2020-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("apply_server_record")
@ApiModel(value="ApplyServerRecordEntity对象", description="")
public class ApplyServerRecordEntity extends Model<ApplyServerRecordEntity> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("name")
    private String name;

    @TableField("address")
    private String address;

    @TableField("max_people")
    private Integer maxPeople;

    @TableField("node")
    private String node;

    @TableField("createAt")
    private LocalDateTime createAt;

    @TableField("is_del")
    private Boolean isDel;

    @TableField("is_public")
    private Boolean isPublic;

    @TableField("password")
    private String serverPwd;

    @TableField("img_url")
    private String imgUrl;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
