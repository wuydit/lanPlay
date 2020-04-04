package org.wyw.lanplay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity<T> implements Serializable {

    private Integer code;

    private String msg;

    private  T data ;

    public BaseEntity(T data){
        this.code = ResultEnum.SUCCESS.getCode();
        this.msg = ResultEnum.SUCCESS.getMsg();
        this.data = data;
    }

    public BaseEntity(ResultEnum resultEnum){
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

    public BaseEntity(ResultEnum resultEnum, T t){
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
        this.data = t;
    }

    public static BaseEntity ok(Object t){
        return new BaseEntity<>(ResultEnum.SUCCESS, t);
    }

    public static BaseEntity status(ResultEnum t,Object o){
        return new BaseEntity<>(t, o);
    }

    public static BaseEntity status(ResultEnum resultEnum){
        return new BaseEntity<>(resultEnum);
    }
}
