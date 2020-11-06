package com.spring.model;

import java.math.BigInteger;

/**
 * 生产工艺库实体类
 */
public class ProductionCraft {

    private BigInteger FID;
    private String FNAME;               //工艺名
    private String PREHEAT;             //预热
    private String INTERLAMINATION;     //层间
    private String WELDING_MATERIAL;    //焊材
    private int ELECTRICITY_FLOOR;      //电流下限
    private int ELECTRICITY_UPPER;      //电流上限
    private int VOLTAGE_FLOOR;          //电压下限
    private int VOLTAGE_UPPER;          //电压上限
    private int SOLDER_SPEED_FLOOR;     //焊速下限
    private int SOLDER_SPEED_UPPER;     //焊速上限
    private int WIDE_SWING;             //摆宽
    private String RESTS;               //其他
    private int DATA_SOURCES;           //数据来源：1.系统录入，2.终端扫码录入
    private String CREATOR;
    private String CREATE_TIME;
    private String MENDER;
    private String UPDATE_TIME;

    private BigInteger TRACKINGCARD_ID;     //电子跟踪卡id
    private BigInteger PRODUCTION_ID;     //生产工艺库id

    public BigInteger getFID() {
        return FID;
    }

    public void setFID(BigInteger FID) {
        this.FID = FID;
    }

    public String getFNAME() {
        return FNAME;
    }

    public void setFNAME(String FNAME) {
        this.FNAME = FNAME;
    }

    public String getPREHEAT() {
        return PREHEAT;
    }

    public void setPREHEAT(String PREHEAT) {
        this.PREHEAT = PREHEAT;
    }

    public String getINTERLAMINATION() {
        return INTERLAMINATION;
    }

    public void setINTERLAMINATION(String INTERLAMINATION) {
        this.INTERLAMINATION = INTERLAMINATION;
    }

    public String getWELDING_MATERIAL() {
        return WELDING_MATERIAL;
    }

    public void setWELDING_MATERIAL(String WELDING_MATERIAL) {
        this.WELDING_MATERIAL = WELDING_MATERIAL;
    }

    public int getELECTRICITY_FLOOR() {
        return ELECTRICITY_FLOOR;
    }

    public void setELECTRICITY_FLOOR(int ELECTRICITY_FLOOR) {
        this.ELECTRICITY_FLOOR = ELECTRICITY_FLOOR;
    }

    public int getELECTRICITY_UPPER() {
        return ELECTRICITY_UPPER;
    }

    public void setELECTRICITY_UPPER(int ELECTRICITY_UPPER) {
        this.ELECTRICITY_UPPER = ELECTRICITY_UPPER;
    }

    public int getVOLTAGE_FLOOR() {
        return VOLTAGE_FLOOR;
    }

    public void setVOLTAGE_FLOOR(int VOLTAGE_FLOOR) {
        this.VOLTAGE_FLOOR = VOLTAGE_FLOOR;
    }

    public int getVOLTAGE_UPPER() {
        return VOLTAGE_UPPER;
    }

    public void setVOLTAGE_UPPER(int VOLTAGE_UPPER) {
        this.VOLTAGE_UPPER = VOLTAGE_UPPER;
    }

    public int getSOLDER_SPEED_FLOOR() {
        return SOLDER_SPEED_FLOOR;
    }

    public void setSOLDER_SPEED_FLOOR(int SOLDER_SPEED_FLOOR) {
        this.SOLDER_SPEED_FLOOR = SOLDER_SPEED_FLOOR;
    }

    public int getSOLDER_SPEED_UPPER() {
        return SOLDER_SPEED_UPPER;
    }

    public void setSOLDER_SPEED_UPPER(int SOLDER_SPEED_UPPER) {
        this.SOLDER_SPEED_UPPER = SOLDER_SPEED_UPPER;
    }

    public int getWIDE_SWING() {
        return WIDE_SWING;
    }

    public void setWIDE_SWING(int WIDE_SWING) {
        this.WIDE_SWING = WIDE_SWING;
    }

    public String getRESTS() {
        return RESTS;
    }

    public void setRESTS(String RESTS) {
        this.RESTS = RESTS;
    }

    public int getDATA_SOURCES() {
        return DATA_SOURCES;
    }

    public void setDATA_SOURCES(int DATA_SOURCES) {
        this.DATA_SOURCES = DATA_SOURCES;
    }

    public String getCREATOR() {
        return CREATOR;
    }

    public void setCREATOR(String CREATOR) {
        this.CREATOR = CREATOR;
    }

    public String getCREATE_TIME() {
        return CREATE_TIME;
    }

    public void setCREATE_TIME(String CREATE_TIME) {
        this.CREATE_TIME = CREATE_TIME;
    }

    public String getMENDER() {
        return MENDER;
    }

    public void setMENDER(String MENDER) {
        this.MENDER = MENDER;
    }

    public String getUPDATE_TIME() {
        return UPDATE_TIME;
    }

    public void setUPDATE_TIME(String UPDATE_TIME) {
        this.UPDATE_TIME = UPDATE_TIME;
    }

    public BigInteger getTRACKINGCARD_ID() {
        return TRACKINGCARD_ID;
    }

    public void setTRACKINGCARD_ID(BigInteger TRACKINGCARD_ID) {
        this.TRACKINGCARD_ID = TRACKINGCARD_ID;
    }

    public BigInteger getPRODUCTION_ID() {
        return PRODUCTION_ID;
    }

    public void setPRODUCTION_ID(BigInteger PRODUCTION_ID) {
        this.PRODUCTION_ID = PRODUCTION_ID;
    }
}
