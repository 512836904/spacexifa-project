package com.shth.spacexifa.model;

import java.math.BigInteger;

public class Parameter {
    private BigInteger id;
    private BigInteger folt;
    private int fvv;
    private String fcn;
    private String fst;
    private String fsft;
    private String fct;
    private String fww;
    private String fafv;
    private String fspeed;
    private String fwc;
    private String fsp;
    private String fds;
    private String fas;
    private String fns;
    private BigInteger numversion;
    private int SUPERGAGE_STATUS; //超规范信息是否展示

    public Parameter() {
        super();
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getNumversion() {
        return numversion;
    }

    public void setNumversion(BigInteger numversion) {
        this.numversion = numversion;
    }

    public BigInteger getFolt() {
        return folt;
    }

    public void setFolt(BigInteger folt) {
        this.folt = folt;
    }

    public int getFvv() {
        return fvv;
    }

    public void setFvv(int fvv) {
        this.fvv = fvv;
    }

    public String getFcn() {
        return fcn;
    }

    public void setFcn(String fcn) {
        this.fcn = fcn;
    }

    public String getFst() {
        return fst;
    }

    public void setFst(String fst) {
        this.fst = fst;
    }

    public String getFsft() {
        return fsft;
    }

    public void setFsft(String fsft) {
        this.fsft = fsft;
    }

    public String getFct() {
        return fct;
    }

    public void setFct(String fct) {
        this.fct = fct;
    }

    public String getFww() {
        return fww;
    }

    public void setFww(String fww) {
        this.fww = fww;
    }

    public String getFafv() {
        return fafv;
    }

    public void setFafv(String fafv) {
        this.fafv = fafv;
    }

    public String getFspeed() {
        return fspeed;
    }

    public void setFspeed(String fspeed) {
        this.fspeed = fspeed;
    }

    public String getFwc() {
        return fwc;
    }

    public void setFwc(String fwc) {
        this.fwc = fwc;
    }

    public String getFsp() {
        return fsp;
    }

    public void setFsp(String fsp) {
        this.fsp = fsp;
    }

    public String getFds() {
        return fds;
    }

    public void setFds(String fds) {
        this.fds = fds;
    }

    public String getFns() {
        return fns;
    }

    public void setFns(String fns) {
        this.fns = fns;
    }

    public String getFas() {
        return fas;
    }

    public void setFas(String fas) {
        this.fas = fas;
    }

    public int getSUPERGAGE_STATUS() {
        return SUPERGAGE_STATUS;
    }

    public void setSUPERGAGE_STATUS(int SUPERGAGE_STATUS) {
        this.SUPERGAGE_STATUS = SUPERGAGE_STATUS;
    }
}
