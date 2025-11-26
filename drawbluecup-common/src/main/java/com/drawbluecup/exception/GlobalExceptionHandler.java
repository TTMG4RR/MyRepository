package com.drawbluecup.exception;

import com.drawbluecup.result.Result;//包名加类名
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.drawbluecup.exception.BusinessException;//同一个包下的类，无需 import 即可直接使用（这是包作用域的默认规则），所以编译器不会报错。
//但是还是建议加上😉


/*
 * 全局异常处理器
 * @RestControllerAdvice：作用于所有 @RestController 注解的类(说的就是你,控制层)，捕获它们抛出的异常//详细看笔记~~~
 */
//全局异常处理器不生效，大概率是包扫描路径不匹配导致 Spring 没识别到处理器类。
//依旧按照result来封装


@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * 处理自定义业务异常（优先级最高，先捕获业务错误）
     * 比如：用户名已存在、订单不存在等
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        return Result.fail(e.getCode(),e.getMessage());//这里不同下面,不用String message,直接调用getMessage方法,获取服务层存储在自定义异常里面的信息
    }




    /*
     * 处理参数类型不匹配异常（如前端传字符串，后端要整数）
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String message = "参数类型错误：" + e.getName() + "应为" + e.getRequiredType().getSimpleName();
        return Result.fail(400,message);
        // e.getName()：获取参数名（如 "id"）
    }   // e.getRequiredType()：获取后端期望的参数类型（如 Integer.class）
    /*前端传递的 id 是字符串 "abc"，而后端接口期望的是一个 Integer 类型。
    这时，Spring MVC 在尝试将 "abc" 转换成 Integer 失败后，就会抛出一个 MethodArgumentTypeMismatchException 异常。

    在这个异常对象 e 内部，就包含了以下关键信息：
    参数名 (Name): "id" (因为是 id 这个参数出了问题)
    期望类型 (Required Type): Integer.class (后端期望的类型)
    实际传入的值 (Value): "abc" (前端实际传过来的东西)

    当你调用 e.getName() 时，它就会返回这个参数名 "id"

    不同的异常类，其内部的字段（或者说属性、成员变量）通常是不一样的。
    虽然它们都继承自 Exception 类，会有一些共同的属性（比如异常信息 message、cause 等），但每个具体的异常类都会根据其要描述的 “异常场景”，添加自己特有的字段来存储相关信息。
    可以这么说：异常类的字段，是为了精准地记录 “错误现场” 的关键信息而设计的。


     */



    /*
     * 处理 JSON 格式错误（如前端传的 JSON 格式不对）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleJsonParseException(HttpMessageNotReadableException e) {
        String message = "请求参数格式错误：请检查JSON格式是否正确";
        // 开发环境可打印详细日志，生产环境仅返回友好提示
        e.printStackTrace();
        return Result.fail(400,message);
    }


    /*
     * 处理空指针异常（最常见的系统异常）
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<Void> handleNullPointerException(NullPointerException e) {
        // 生产环境不暴露具体错误位置，用通用消息
        String message = "系统异常，请联系管理员";
        // 开发环境可以打印日志方便调试//既然不暴露,那么就要打印日志方便维护
        e.printStackTrace();
        return Result.fail(500,message);
    }




    //兜底异常
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        String message = "系统繁忙，请联系管理员";
        e.printStackTrace(); // 开发环境打印日志
        return Result.fail(500,message);
    }

    /**
     * 处理 @Valid @RequestBody 校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("请求参数校验失败");
        return Result.fail(400,message);
    }

    /**
     * 处理 @Validated + 基础类型参数校验异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolation(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .findFirst()
                .map(cv -> cv.getMessage())
                .orElse("请求参数校验失败");
        return Result.fail(400,message);
    }

    /**
     * 处理表单参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("请求参数校验失败");
        return Result.fail(400,message);
    }
}
