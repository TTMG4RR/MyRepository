package com.drawbluecup.result;

public class Result<T> {//给控制端作**返回对象**,从前端接收路径后执行方法后返回对象


    //成员变量
    private int code;
    private String msg;//提示信息
    private T data;//给前端的业务数据（查询/新增/修改后的实际数据，无数据时为null）// T是泛型，适配不同类型数据





    //空构造和全构造方法
    public Result() {

    }
    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }




    //成员方法-success

    //无数据、固定提示：仅告知前端“操作成功”，无需返回业务数据
    public static <T> Result<T> success() {

        return build(200, "操作成功", null);
    }

    //有数据、固定提示：返回业务数据+默认“操作成功”
    public static <T> Result<T> success(T data) {
        return build(200, "操作成功", data);
    }

    //有数据、自定义提示：返回业务数据+自定义提示
    public static <T>Result<T> success(String message,T data){
        return build(200, message, data);
    }

    //全自定义：自定义状态码+提示+数据（适配特殊成功场景，比如201=创建成功）//待使用
    public static <T>Result<T> success(int code,String message,T data){
        return build(code,message,data);
    }


    //成员方法-error（所有失败场景的快捷方法，基于fail方法实现，无需重复写兜底逻辑）

    //固定状态码（400=参数错误）+ 自定义提示
    public static <T>Result<T> error(String message){
        return fail(400,message);
    }
    //自定义状态码+自定义提示（适配特殊失败场景，比如404=资源不存在、403=无权限）
    public static <T> Result<T> error(int code, String message) {
        return fail(code,message);
    }




    // 失败场景核心方法：给错误码兜底（防止传null），失败场景默认无业务数据
        // 作用：统一处理所有失败逻辑，避免重复判断“code是否为null”

    //在打包封装之上封装,给错误code兜底的封装函数--->fail
    public static <T> Result<T> fail(Integer code, String message){
        // 若code为null（比如异常捕获时不知道具体错误码），默认用500（服务器内部错误
        return build(code == null ? 500 : code, message, null);//设置默认值,还有错误一般不传输数据
    }


    //给Result类打包返回--->函数build()减少重复打包返回代码
    private static <T> Result<T> build(int code, String message, T data){
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(message);
        result.setData(data);
        return result;
    }



    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String message) {
        this.msg = message;
    }

    public String getMessage() {
        return msg;
    }
    public void setMessage(String message) {
        this.msg = message;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }



}
