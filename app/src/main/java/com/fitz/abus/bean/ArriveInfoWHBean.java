package com.fitz.abus.bean;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.bean
 * @Author: Fitz
 * @CreateDate: 2019/1/15
 * GSON数据类，不要改动
 */
public class ArriveInfoWHBean {

    private Result result;
    public void setResult(Result result) {
        this.result = result;
    }
    public Result getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "result:"+result.toString();
    }
}