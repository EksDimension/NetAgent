package com.eks.netagentdemo;

/**
 * Created by Riggs on 12/18/2019
 */
public class BeanBase<T> {

    /**
     * code : 0
     * errCode : 1510
     * message : SUCCESS
     * responseTime : 2019-09-10 17:07:28
     * data : {"amount":10000,"check":1,"info":{"openid":"oU5Yyt8mrts9V41Rmx-ZxS_3bRwM","name":"爱吃柠檬的BHC","portraitImg":"https://static.zysccn.com/zykgsc/upload/2019/09/20190903/20190903154357wxf5437a2a35d818c5.o6zAJs6I0wzqXNpfu0Lw6TxJ6Ji8.vB0fCm7L9DPh4c856f0fc5fe6967b1b138074863a0a9.png","userName":"爱吃柠檬的薄荷ss草","portrait":"https://static.zysccn.com/zykgsc/upload/2019/09/20190903/20190903154357wxf5437a2a35d818c5.o6zAJs6I0wzqXNpfu0Lw6TxJ6Ji8.vB0fCm7L9DPh4c856f0fc5fe6967b1b138074863a0a9.png","telNo":"18520101211","createDate":{"date":18,"hours":15,"seconds":15,"month":6,"timezoneOffset":-480,"year":118,"minutes":28,"time":1531898895000,"day":3},"username":"爱吃柠檬的BHC"}}
     */

    private int code;
    private int errCode;
    private String message;
    private String responseTime;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
