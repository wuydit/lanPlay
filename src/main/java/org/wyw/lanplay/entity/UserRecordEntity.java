package org.wyw.lanplay.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.security.Principal;
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
@TableName("user_record")
@ApiModel(value="UserRecordEntity对象", description="")
public class UserRecordEntity extends Model<UserRecordEntity> implements Principal {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("identity")
    private String identity;

    @TableField("username")
    private String username;

    /**
     * 存为加密后密码
     */
    @TableField("password")
    private String password;

    @TableField("nickname")
    private String nickname;

    @TableField("ip")
    private String ip;

    @TableField("createAt")
    private LocalDateTime createAt;

    @TableField("status")
    private Integer status;

    @TableField("url")
    private String url;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.username;
    }
}
