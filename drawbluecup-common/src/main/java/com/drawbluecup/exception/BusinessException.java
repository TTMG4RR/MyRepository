package com.drawbluecup.exception;

//自定义异常,处理业务逻辑异常

public class BusinessException extends RuntimeException {
    private Integer code;

    //默认code的构造方法
    public BusinessException(String message) {
        super(message);//给父类构造并初始化,使得继承生效,可以实现"在父类里面存储信息和调用getter获取信息"
        this.code = 400;
    }
    //可以自定义code的构造方法
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    // getter
    public int getCode() {
        return code;
    }//为了符合Javabean的规范,也为了让框架能正确读取字段值,所以不要漏了😚

    public void setCode(Integer code) {
        this.code = code;
    }//写多不费脑
}
