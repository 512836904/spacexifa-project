package com;

import java.math.BigDecimal;

/**
 * 实时表实体类
 */
public class TbLiveData {

    /**
     * 焊机上传数据字段
     */
    private String fwelder_no;          //焊工编号
    private String fgather_no;          //采集模块编号
    private String fjunction_no;        //焊口号
    private Long fstatus;               //报警状态
    private BigDecimal fchannel;        //通道号
    private String fmachinemodel;       //焊机型号
    private BigDecimal felectricity;        //焊接电流
    private BigDecimal fvoltage;            //焊接电压
    private BigDecimal fwirefeedrate;       //送丝速度
    private String fweldtime;               //焊机工作时间
    private BigDecimal fmax_electricity;    //最大电流
    private BigDecimal fmin_electricity;    //最大电流
    private BigDecimal fmax_voltage;        //最大电压
    private BigDecimal fmin_voltage;        //最小电压
    private BigDecimal fwirediameter;       //焊丝直径
    private BigDecimal fmaterialgas;        //材质气体
    private BigDecimal frateofflow;         //保护气流量
    private BigDecimal flon_air_flow;       //离子气流量
    private BigDecimal fhatwirecurrent;     //热丝电流
    private String fpreheating_temperature;    //预热温度
    private String fswing;                  //摆幅
    private String fswing_speed;            //摆速
    private BigDecimal fvibrafrequency;     //频率
    private String flaser_power;            //激光功率
    private String fdefocus_amount;         //工作距离
    private String fdefocus_quantity;       //离焦量
    private BigDecimal fpeak_electricity;   //峰值电流
    private BigDecimal fbase_electricity;   //基值电流
    private String fpeak_time;              //峰值时间
    private String fbase_time;              //基值时间
    private BigDecimal faccelerat_voltage;  //加速电压
    private BigDecimal ffocus_current;      //聚焦电流
    private BigDecimal felectron_beam;      //电子束流
    private String fscan_frequency;         //扫描频率
    private String fscan_amplitude;         //扫描幅度

    /**
     * 实时表需要字段
     */
    private BigDecimal fwelder_id;          //焊工id
    private BigDecimal fmachine_id;         //焊机id
    private BigDecimal fjunction_id;        //焊口id
    private BigDecimal fitemid;             //组织机构id
    private String fuploadDatetime;         //上传日期
    private String fweld_no;                //焊机固号
    private BigDecimal fwelder_itemid;      //焊工组织id
    private BigDecimal fmachine_itemid;     //焊机组织id
    private BigDecimal fjunction_itemid;    //焊口组织id
    private BigDecimal fcard_id;            //电子跟踪卡id
    private BigDecimal fwps_lib_id;         //工艺规程id
    private BigDecimal fproduct_number_id;  //产品id
    private BigDecimal femployee_id;        //工序id
    private BigDecimal fstep_id;            //工步id


    public String getFwelder_no() {
        return fwelder_no;
    }

    public void setFwelder_no(String fwelder_no) {
        this.fwelder_no = fwelder_no;
    }

    public String getFgather_no() {
        return fgather_no;
    }

    public void setFgather_no(String fgather_no) {
        this.fgather_no = fgather_no;
    }

    public String getFjunction_no() {
        return fjunction_no;
    }

    public void setFjunction_no(String fjunction_no) {
        this.fjunction_no = fjunction_no;
    }

    public Long getFstatus() {
        return fstatus;
    }

    public void setFstatus(Long fstatus) {
        this.fstatus = fstatus;
    }

    public BigDecimal getFchannel() {
        return fchannel;
    }

    public void setFchannel(BigDecimal fchannel) {
        this.fchannel = fchannel;
    }

    public String getFmachinemodel() {
        return fmachinemodel;
    }

    public void setFmachinemodel(String fmachinemodel) {
        this.fmachinemodel = fmachinemodel;
    }

    public BigDecimal getFelectricity() {
        return felectricity;
    }

    public void setFelectricity(BigDecimal felectricity) {
        this.felectricity = felectricity;
    }

    public BigDecimal getFvoltage() {
        return fvoltage;
    }

    public void setFvoltage(BigDecimal fvoltage) {
        this.fvoltage = fvoltage;
    }

    public BigDecimal getFwirefeedrate() {
        return fwirefeedrate;
    }

    public void setFwirefeedrate(BigDecimal fwirefeedrate) {
        this.fwirefeedrate = fwirefeedrate;
    }

    public String getFweldtime() {
        return fweldtime;
    }

    public void setFweldtime(String fweldtime) {
        this.fweldtime = fweldtime;
    }

    public BigDecimal getFmax_electricity() {
        return fmax_electricity;
    }

    public void setFmax_electricity(BigDecimal fmax_electricity) {
        this.fmax_electricity = fmax_electricity;
    }

    public BigDecimal getFmin_electricity() {
        return fmin_electricity;
    }

    public void setFmin_electricity(BigDecimal fmin_electricity) {
        this.fmin_electricity = fmin_electricity;
    }

    public BigDecimal getFmax_voltage() {
        return fmax_voltage;
    }

    public void setFmax_voltage(BigDecimal fmax_voltage) {
        this.fmax_voltage = fmax_voltage;
    }

    public BigDecimal getFmin_voltage() {
        return fmin_voltage;
    }

    public void setFmin_voltage(BigDecimal fmin_voltage) {
        this.fmin_voltage = fmin_voltage;
    }

    public BigDecimal getFwirediameter() {
        return fwirediameter;
    }

    public void setFwirediameter(BigDecimal fwirediameter) {
        this.fwirediameter = fwirediameter;
    }

    public BigDecimal getFmaterialgas() {
        return fmaterialgas;
    }

    public void setFmaterialgas(BigDecimal fmaterialgas) {
        this.fmaterialgas = fmaterialgas;
    }

    public BigDecimal getFrateofflow() {
        return frateofflow;
    }

    public void setFrateofflow(BigDecimal frateofflow) {
        this.frateofflow = frateofflow;
    }

    public BigDecimal getFlon_air_flow() {
        return flon_air_flow;
    }

    public void setFlon_air_flow(BigDecimal flon_air_flow) {
        this.flon_air_flow = flon_air_flow;
    }

    public BigDecimal getFhatwirecurrent() {
        return fhatwirecurrent;
    }

    public void setFhatwirecurrent(BigDecimal fhatwirecurrent) {
        this.fhatwirecurrent = fhatwirecurrent;
    }

    public String getFpreheating_temperature() {
        return fpreheating_temperature;
    }

    public void setFpreheating_temperature(String fpreheating_temperature) {
        this.fpreheating_temperature = fpreheating_temperature;
    }

    public String getFswing() {
        return fswing;
    }

    public void setFswing(String fswing) {
        this.fswing = fswing;
    }

    public String getFswing_speed() {
        return fswing_speed;
    }

    public void setFswing_speed(String fswing_speed) {
        this.fswing_speed = fswing_speed;
    }

    public BigDecimal getFvibrafrequency() {
        return fvibrafrequency;
    }

    public void setFvibrafrequency(BigDecimal fvibrafrequency) {
        this.fvibrafrequency = fvibrafrequency;
    }

    public String getFlaser_power() {
        return flaser_power;
    }

    public void setFlaser_power(String flaser_power) {
        this.flaser_power = flaser_power;
    }

    public String getFdefocus_amount() {
        return fdefocus_amount;
    }

    public void setFdefocus_amount(String fdefocus_amount) {
        this.fdefocus_amount = fdefocus_amount;
    }

    public String getFdefocus_quantity() {
        return fdefocus_quantity;
    }

    public void setFdefocus_quantity(String fdefocus_quantity) {
        this.fdefocus_quantity = fdefocus_quantity;
    }

    public BigDecimal getFpeak_electricity() {
        return fpeak_electricity;
    }

    public void setFpeak_electricity(BigDecimal fpeak_electricity) {
        this.fpeak_electricity = fpeak_electricity;
    }

    public BigDecimal getFbase_electricity() {
        return fbase_electricity;
    }

    public void setFbase_electricity(BigDecimal fbase_electricity) {
        this.fbase_electricity = fbase_electricity;
    }

    public String getFpeak_time() {
        return fpeak_time;
    }

    public void setFpeak_time(String fpeak_time) {
        this.fpeak_time = fpeak_time;
    }

    public String getFbase_time() {
        return fbase_time;
    }

    public void setFbase_time(String fbase_time) {
        this.fbase_time = fbase_time;
    }

    public BigDecimal getFaccelerat_voltage() {
        return faccelerat_voltage;
    }

    public void setFaccelerat_voltage(BigDecimal faccelerat_voltage) {
        this.faccelerat_voltage = faccelerat_voltage;
    }

    public BigDecimal getFfocus_current() {
        return ffocus_current;
    }

    public void setFfocus_current(BigDecimal ffocus_current) {
        this.ffocus_current = ffocus_current;
    }

    public BigDecimal getFelectron_beam() {
        return felectron_beam;
    }

    public void setFelectron_beam(BigDecimal felectron_beam) {
        this.felectron_beam = felectron_beam;
    }

    public String getFscan_frequency() {
        return fscan_frequency;
    }

    public void setFscan_frequency(String fscan_frequency) {
        this.fscan_frequency = fscan_frequency;
    }

    public String getFscan_amplitude() {
        return fscan_amplitude;
    }

    public void setFscan_amplitude(String fscan_amplitude) {
        this.fscan_amplitude = fscan_amplitude;
    }

    public BigDecimal getFwelder_id() {
        return fwelder_id;
    }

    public void setFwelder_id(BigDecimal fwelder_id) {
        this.fwelder_id = fwelder_id;
    }

    public BigDecimal getFmachine_id() {
        return fmachine_id;
    }

    public void setFmachine_id(BigDecimal fmachine_id) {
        this.fmachine_id = fmachine_id;
    }

    public BigDecimal getFjunction_id() {
        return fjunction_id;
    }

    public void setFjunction_id(BigDecimal fjunction_id) {
        this.fjunction_id = fjunction_id;
    }

    public BigDecimal getFitemid() {
        return fitemid;
    }

    public void setFitemid(BigDecimal fitemid) {
        this.fitemid = fitemid;
    }

    public String getFuploadDatetime() {
        return fuploadDatetime;
    }

    public void setFuploadDatetime(String fuploadDatetime) {
        this.fuploadDatetime = fuploadDatetime;
    }

    public String getFweld_no() {
        return fweld_no;
    }

    public void setFweld_no(String fweld_no) {
        this.fweld_no = fweld_no;
    }

    public BigDecimal getFwelder_itemid() {
        return fwelder_itemid;
    }

    public void setFwelder_itemid(BigDecimal fwelder_itemid) {
        this.fwelder_itemid = fwelder_itemid;
    }

    public BigDecimal getFmachine_itemid() {
        return fmachine_itemid;
    }

    public void setFmachine_itemid(BigDecimal fmachine_itemid) {
        this.fmachine_itemid = fmachine_itemid;
    }

    public BigDecimal getFjunction_itemid() {
        return fjunction_itemid;
    }

    public void setFjunction_itemid(BigDecimal fjunction_itemid) {
        this.fjunction_itemid = fjunction_itemid;
    }

    public BigDecimal getFcard_id() {
        return fcard_id;
    }

    public void setFcard_id(BigDecimal fcard_id) {
        this.fcard_id = fcard_id;
    }

    public BigDecimal getFwps_lib_id() {
        return fwps_lib_id;
    }

    public void setFwps_lib_id(BigDecimal fwps_lib_id) {
        this.fwps_lib_id = fwps_lib_id;
    }

    public BigDecimal getFproduct_number_id() {
        return fproduct_number_id;
    }

    public void setFproduct_number_id(BigDecimal fproduct_number_id) {
        this.fproduct_number_id = fproduct_number_id;
    }

    public BigDecimal getFemployee_id() {
        return femployee_id;
    }

    public void setFemployee_id(BigDecimal femployee_id) {
        this.femployee_id = femployee_id;
    }

    public BigDecimal getFstep_id() {
        return fstep_id;
    }

    public void setFstep_id(BigDecimal fstep_id) {
        this.fstep_id = fstep_id;
    }
}
