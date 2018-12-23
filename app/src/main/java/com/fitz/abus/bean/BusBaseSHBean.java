package com.fitz.abus.bean;

/**
 * @ProjectName: ABus
 * @Package: com.fitz.abus.bean
 * @ClassName: BusBaseSHBean
 * @Author: Fitz
 * @CreateDate: 2018/12/23 17:34
 */
@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
public class BusBaseSHBean {

    private String start_latetime;
    private String line_name;
    private String end_earlytime;
    private String start_earlytime;
    private String end_stop;
    private String line_id;
    private String start_stop;
    private String end_latetime;

    public void setStartLatetime(String start_latetime) {
        this.start_latetime = start_latetime;
    }

    public String getStartLatetime() {
        return start_latetime;
    }

    public void setLine_name(String line_name) {
        this.line_name = line_name;
    }
    public String getLine_name() {
        return line_name;
    }

    public void setEndEarlytime(String end_earlytime) {
        this.end_earlytime = end_earlytime;
    }

    public String getEndEarlytime() {
        return end_earlytime;
    }

    public void setStartEarlytime(String start_earlytime) {
        this.start_earlytime = start_earlytime;
    }

    public String getStartEarlytime() {
        return start_earlytime;
    }

    public void setEnd_stop(String end_stop) {
        this.end_stop = end_stop;
    }
    public String getEnd_stop() {
        return end_stop;
    }

    public void setLineId(String line_id) {
        this.line_id = line_id;
    }

    public String getLineId() {
        return line_id;
    }

    public void setStartStop(String start_stop) {
        this.start_stop = start_stop;
    }

    public String getStartStop() {
        return start_stop;
    }

    public void setEndLatetime(String end_latetime) {
        this.end_latetime = end_latetime;
    }

    public String getEndLatetime() {
        return end_latetime;
    }

    public boolean nonNull(){
        return getLine_name() != null && getEnd_stop() != null;
    }
}