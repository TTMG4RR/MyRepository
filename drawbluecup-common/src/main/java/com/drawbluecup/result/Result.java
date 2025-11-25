package com.drawbluecup.result;

public class Result<T> {//给控制端作**返回对象**,从前端接收路径后执行方法后返回对象
    private int code;//给前端状态码,200成功,400失败
    private String msg;//要传向前端的信息(提示信息
    private T data;//给前端真正的数据//声明这是业务数据,和T挂钩




    public Result() {

    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> success() {
        return build(200, "操作成功", null);
    }

    public static <T> Result<T> success(T data) {
        return build(200, "操作成功", data);
    }

    public static <T>Result<T> success(String message,T data){
        return build(200, message, data);
    }

    public static <T>Result<T> error(String message){
        return fail(400,message);
    }//这个类不仅包含字段和方法(可填充数据),还包含"可以将字段填充数据方法"的运用方法


    //有时候想自定义状态码,有时候不想,那么可以创造两种方法,根据需求选择!!😋😋😋
    // 新增：支持自定义 code 的方法
    public static <T>Result<T> success(int code,String message,T data){
        return build(code,message,data);
    }


    public static <T> Result<T> error(int code, String message) {
        return fail(code,message);
    }

    public static <T> Result<T> fail(Integer code, String message){
        return build(code == null ? 500 : code, message, null);
    }

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
