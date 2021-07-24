package com.eks.netagent.core

import com.google.gson.Gson
import java.lang.Exception
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 网络请求回调接口实现类
 * Created by Riggs on 2020/3/1
 */
abstract class NetCallbackImpl<Result> : ICallback {
    override fun onSucceed(result: String) {
        //网络响应的字符串在参数result中
        val gson = Gson()

        //得到调用者用怎样的javaBean来接收数据
        val clz = analysisClassInfo(this)

        //把String转成javaBean对象
        try {
            val objResult = gson.fromJson<Result>(result, clz)
            //把结果交给开发者
            onSucceed(objResult)
        } catch (e:Exception){
            onFailed(e.message.toString())
        }
    }

    override fun onFailed(e: Any) {
        when (e) {
            is Throwable -> onFailed(e.message.toString())
        }
    }

    /**
     * 根据当前类对象进行分析,获取类信息,目的是为了得到泛型的具体对象
     */
    private fun analysisClassInfo(resultStr: NetCallbackImpl<Result>): Type? {
        //获取Class字节码
        val resultJavaClass = resultStr.javaClass
        //getGenericSuperClass()返回一个类型对象
        //这个方法可以获取原始类型 参数化 数组 类型变量 基本数据类型
        val genType = resultJavaClass.genericSuperclass
        //getActualTypeArguments()获取真实参数
        val paramsArr = (genType as ParameterizedType).actualTypeArguments
        return paramsArr[0]
    }

    /**
     * 真正返回给开发者的回调成功抽象方法
     */
    abstract fun onSucceed(result: Result?)

    /**
     * 真正返回给开发者的回调失败抽象方法
     */
    abstract fun onFailed(errMsg: String)
}
