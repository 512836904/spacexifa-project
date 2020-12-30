package com.spring.model;

/**
 * 焊缝信息表
 */
public class Junction {

    private long fid;
    private String fjunction;       //焊缝编号
    private String junction_length;     //长度
    private String junction_format;     //规格
    private String current_limit;          //电流上限
    private String current_lower_limit;     //电流下限
    private String junction_name;       //焊缝名称
    private String FMAXVOLTAGE;       //电压上限
    private String FMINVOLTAGE;       //电压下限

    /**
     * 工艺信息和焊缝关联表
     * @return
     */
    private int library_id; //工艺id
    private int junction_id;    //焊缝id

    public long getFid() {
        return fid;
    }

    public void setFid(long fid) {
        this.fid = fid;
    }

    public String getFjunction() {
        return fjunction;
    }

    public void setFjunction(String fjunction) {
        this.fjunction = fjunction;
    }

    public String getJunction_length() {
        return junction_length;
    }

    public void setJunction_length(String junction_length) {
        this.junction_length = junction_length;
    }

    public String getJunction_format() {
        return junction_format;
    }

    public void setJunction_format(String junction_format) {
        this.junction_format = junction_format;
    }

    public String getCurrent_limit() {
        return current_limit;
    }

    public void setCurrent_limit(String current_limit) {
        this.current_limit = current_limit;
    }

    public String getCurrent_lower_limit() {
        return current_lower_limit;
    }

    public void setCurrent_lower_limit(String current_lower_limit) {
        this.current_lower_limit = current_lower_limit;
    }

    public String getJunction_name() {
        return junction_name;
    }

    public String getFMAXVOLTAGE() {
        return FMAXVOLTAGE;
    }

    public void setFMAXVOLTAGE(String FMAXVOLTAGE) {
        this.FMAXVOLTAGE = FMAXVOLTAGE;
    }

    public String getFMINVOLTAGE() {
        return FMINVOLTAGE;
    }

    public void setFMINVOLTAGE(String FMINVOLTAGE) {
        this.FMINVOLTAGE = FMINVOLTAGE;
    }

    public void setJunction_name(String junction_name) {
        this.junction_name = junction_name;
    }

    public int getLibrary_id() {
        return library_id;
    }

    public void setLibrary_id(int library_id) {
        this.library_id = library_id;
    }

    public int getJunction_id() {
        return junction_id;
    }

    public void setJunction_id(int junction_id) {
        this.junction_id = junction_id;
    }
}
