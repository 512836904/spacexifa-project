package com.spring.model;

import java.math.BigInteger;
import java.util.Date;

/**
 * 工艺信息库
 */

public class Wps {
	private long fid;	//id
	private String JOB_NUMBER;	//工作号
	private String SET_NUMBER;	//部套号
	private String PART_DRAWING_NUMBER;	//零件图号
	private String PART_NAME;	//零件名
	/**
	 * 电子跟踪卡字段新增
	 * @return
	 */
	private String workticket_number;	//工票编号
	private String craft_param;			//工艺参数
	private String raw_materi;		//原料
	private String process;				//工序
	private BigInteger productionCraftId;	//生产工艺id

	private long library_id;
	private long junction_id;
	private String junction_name; //焊缝名称

	private BigInteger insid;
	private BigInteger macid;
	private String insname;
	private BigInteger welderid;
	private String weldername;
	private String updatename;
	private String fwpsnum;
	private int fweld_i;
	private int fweld_v;
	private int fweld_i_max;
	private int fweld_i_min;
	private int fweld_v_max;
	private int fweld_v_min;
	private int fweld_alter_i;
	private int fweld_alter_v;
	private int fweld_prechannel;
	private Date fcreatedate;
	private Date fupdatedate;
	private long fcreater;
	private long fupdater;
	private long fowner;
	private String fback;
	private String fname;
	private double fdiameter;
	private double ftime;
	private double fadvance;
	private double fhysteresis;
	private double fini_ele;
	private double fini_vol;
	private double fini_vol1;
	private double fweld_ele;
	private double fweld_vol;
	private double fweld_vol1;
	private double farc_ele;
	private double farc_vol;
	private double farc_vol1;
	private double fweld_tuny_ele;
	private double fweld_tuny_vol;
	private double farc_tuny_ele;
	private double farc_tuny_vol;
	private String finitial;
	private String fcontroller;
	private String fmode;
	private int fstatus;
	private String arcname;
	private String selectname;
	private String gasname;
	private String dianame;
	private String materialname;
	private String conname;
	

	private double fpreset_ele_top;
	private double fpreset_vol_top;
	private double fpreset_ele_bottom;
	private double fpreset_vol_bottom;
	private double farc_vol_top;
	private double fpreset_ele_warn_top;
	private double fpreset_vol_warn_top;
	private double fpreset_ele_warn_bottom;
	private double fpreset_vol_warn_bottom;
	private double fini_ele_warn_top;
	private double fini_vol_warn_top;
	private double fini_ele_warn_bottom;
	private double fini_vol_warn_bottom;
	private double farc_ele_warn_top;
	private double farc_vol_warn_top;
	private double farc_ele_warn_bottom;
	private double farc_vol_warn_bottom;
	private double farc_delay_time;
	private double fwarn_delay_time;
	private double fwarn_stop_time;
	private double fflow_top;
	private double fflow_bottom;
	private double fdelay_time;
	private double fover_time;
	private double ffixed_cycle;
	private double fwarn_ele_up;
	private double fwarn_ele_down;
	private double fwarn_vol_up;
	private double fwarn_vol_down;
	private int fselect;
	private int farc;
	private int fcharacter;
	private int fmaterial;
	private int fgas;
	private int fini;
	private int ftorch;
	private int fprocessid;
	private String fitem;
	private String fprocessname;
	
	private String fsolder_layer;
	private String fweld_bead;
	private String fweld_method;
	private String fpower_polarity;
	private String fgas_flow;
	private String fweld_speed;
	
	private String f001;
	private String f002;
	private String f003;
	private String f004;
	private String f005;
	private String f006;
	private String f007;
	private String f008;
	private String f009;
	private String f010;
	private String f011;
	private String f012;
	private String f013;
	private String f014;
	private String f015;
	private String f016;
	private String f017;
	private String f018;
	private String f019;
	private String f020;
	private String f021;
	private String f022;
	private String f023;
	private String f024;
	private String f025;
	private String f026;
	private String f027;
	private String f028;
	private String f029;
	private String f030;
	private String f031;
	private String f032;
	private String f033;
	private String f034;
	private String f035;
	private String f036;
	private String f037;
	private String f038;
	private String f039;
	private String f040;
	private String f041;
	private String f042;
	private String f043;
	private String f044;
	private String f045;
	private String f046;
	private String f047;
	private String f048;
	private String f049;
	private String f050;
	
	private String fproduct_drawing_no;
	private String fproduct_name;
	private String fwps_lib_version;
	private String fproduct_version;
	private String fproduct_number;
	private String fprefix_number;
	private String fsuffix_number;
	private int flag;

	private String fquantitative_project;
	private String frequired_value;
	private String fupper_deviation;
	private String flower_deviation;
	private String funit_of_measurement;
	private String femployee_id;
	private String femployee_version;
	private String femployee_name;
	private String fstep_number;
	private String fstep_id;
	private String fstep_name;
	private String fstep_version;
	private String fjunction;	//焊缝编号
	private String fwelding_area;
	private String fstarttime;
	private String endtime;
	private BigInteger unstandardtime;
	private String fwelded_junction_no;	//电子跟踪卡号

	/**
	 * 字段新增 2020.10.20
	 */
	private String fspeed;
	private String model;
	private String fwpsback;
	private String fini_tuny_vol;
	private String ffrequency;
	private String fselectstep;
	private String farc_tuny_speed;
	private String farc_speed;


	/**
	 * 工艺参数下发字段修改
	 * @return
	 */
	private double weldingratio;
	private double firsttime;
	private double farc_time;
	private double Rush;
	private double handarc_ele;
	private double handarc_time;
	private double hand_ele;
	private double Base_ele;
	private double Base_vol;
	private double Base_vol1;
	private double fargon;
	private double manual_weld;
	private double arc_length;
	private double pulse;
	private double fweldparameters;
	private double rise_time;
	private double decline_time;
	private double thrust_ele;
	private double pulse_ratio;
	private double point_speed;

	private double frequency;
	private double gasflow;

	/**
	 *  工艺下发规范实体类（与数据库字段对应）
	 * @return
	 */
	private int fspe_num;
	private int fmachine_id;
	private int fgasflow;
	private int fwelding_process;
	private int fwater_cooled_torch;
	private int fweldingratio;
	private int pulse_ele;
	private int ac_frequency;
	private int clean_width;
	private int ac_dc;
	private int pulse_width;
	private int ac_ratio;
	private int ac_wave;
	private int pulse_tuny_ele;
	private int special_arcorder;
	private int special_arc_initial;
	private int special_arctime;
	private int click_ele;
	private int two_click_ele;
	private int repeat_end;
	private int guide;
	private int slope;
	private int specialarc;
	private int specialarc_rep;
	private int ts_condition;

	//任务完成状态
	private Integer FOPERATETYPE;

	private Double wirefeedrate;
	private Double worktime;

	public Double getWirefeedrate() {
		return wirefeedrate;
	}

	public void setWirefeedrate(Double wirefeedrate) {
		this.wirefeedrate = wirefeedrate;
	}

	public Double getWorktime() {
		return worktime;
	}

	public void setWorktime(Double worktime) {
		this.worktime = worktime;
	}

	public String getWorkticket_number() {
		return workticket_number;
	}

	public void setWorkticket_number(String workticket_number) {
		this.workticket_number = workticket_number;
	}

	public String getCraft_param() {
		return craft_param;
	}

	public void setCraft_param(String craft_param) {
		this.craft_param = craft_param;
	}

	public String getRaw_materi() {
		return raw_materi;
	}

	public void setRaw_materi(String raw_materi) {
		this.raw_materi = raw_materi;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public BigInteger getProductionCraftId() {
		return productionCraftId;
	}

	public void setProductionCraftId(BigInteger productionCraftId) {
		this.productionCraftId = productionCraftId;
	}

	public Integer getFOPERATETYPE() {
		return FOPERATETYPE;
	}

	public void setFOPERATETYPE(Integer FOPERATETYPE) {
		this.FOPERATETYPE = FOPERATETYPE;
	}

	public int getFspe_num() {
		return fspe_num;
	}

	public void setFspe_num(int fspe_num) {
		this.fspe_num = fspe_num;
	}

	public int getFmachine_id() {
		return fmachine_id;
	}

	public void setFmachine_id(int fmachine_id) {
		this.fmachine_id = fmachine_id;
	}

	public int getFgasflow() {
		return fgasflow;
	}

	public void setFgasflow(int fgasflow) {
		this.fgasflow = fgasflow;
	}

	public int getFwelding_process() {
		return fwelding_process;
	}

	public void setFwelding_process(int fwelding_process) {
		this.fwelding_process = fwelding_process;
	}

	public int getFwater_cooled_torch() {
		return fwater_cooled_torch;
	}

	public void setFwater_cooled_torch(int fwater_cooled_torch) {
		this.fwater_cooled_torch = fwater_cooled_torch;
	}

	public int getFweldingratio() {
		return fweldingratio;
	}

	public void setFweldingratio(int fweldingratio) {
		this.fweldingratio = fweldingratio;
	}

	public int getPulse_ele() {
		return pulse_ele;
	}

	public void setPulse_ele(int pulse_ele) {
		this.pulse_ele = pulse_ele;
	}

	public int getAc_frequency() {
		return ac_frequency;
	}

	public void setAc_frequency(int ac_frequency) {
		this.ac_frequency = ac_frequency;
	}

	public int getClean_width() {
		return clean_width;
	}

	public void setClean_width(int clean_width) {
		this.clean_width = clean_width;
	}

	public int getAc_dc() {
		return ac_dc;
	}

	public void setAc_dc(int ac_dc) {
		this.ac_dc = ac_dc;
	}

	public int getPulse_width() {
		return pulse_width;
	}

	public void setPulse_width(int pulse_width) {
		this.pulse_width = pulse_width;
	}

	public int getAc_ratio() {
		return ac_ratio;
	}

	public void setAc_ratio(int ac_ratio) {
		this.ac_ratio = ac_ratio;
	}

	public int getAc_wave() {
		return ac_wave;
	}

	public void setAc_wave(int ac_wave) {
		this.ac_wave = ac_wave;
	}

	public int getPulse_tuny_ele() {
		return pulse_tuny_ele;
	}

	public void setPulse_tuny_ele(int pulse_tuny_ele) {
		this.pulse_tuny_ele = pulse_tuny_ele;
	}

	public int getSpecial_arcorder() {
		return special_arcorder;
	}

	public void setSpecial_arcorder(int special_arcorder) {
		this.special_arcorder = special_arcorder;
	}

	public int getSpecial_arc_initial() {
		return special_arc_initial;
	}

	public void setSpecial_arc_initial(int special_arc_initial) {
		this.special_arc_initial = special_arc_initial;
	}

	public int getSpecial_arctime() {
		return special_arctime;
	}

	public void setSpecial_arctime(int special_arctime) {
		this.special_arctime = special_arctime;
	}

	public int getClick_ele() {
		return click_ele;
	}

	public void setClick_ele(int click_ele) {
		this.click_ele = click_ele;
	}

	public int getTwo_click_ele() {
		return two_click_ele;
	}

	public void setTwo_click_ele(int two_click_ele) {
		this.two_click_ele = two_click_ele;
	}

	public int getRepeat_end() {
		return repeat_end;
	}

	public void setRepeat_end(int repeat_end) {
		this.repeat_end = repeat_end;
	}

	public int getGuide() {
		return guide;
	}

	public void setGuide(int guide) {
		this.guide = guide;
	}

	public int getSlope() {
		return slope;
	}

	public void setSlope(int slope) {
		this.slope = slope;
	}

	public int getSpecialarc() {
		return specialarc;
	}

	public void setSpecialarc(int specialarc) {
		this.specialarc = specialarc;
	}

	public int getSpecialarc_rep() {
		return specialarc_rep;
	}

	public void setSpecialarc_rep(int specialarc_rep) {
		this.specialarc_rep = specialarc_rep;
	}

	public int getTs_condition() {
		return ts_condition;
	}

	public void setTs_condition(int ts_condition) {
		this.ts_condition = ts_condition;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public double getGasflow() {
		return gasflow;
	}

	public void setGasflow(double gasflow) {
		this.gasflow = gasflow;
	}

	public double getWeldingratio() {
		return weldingratio;
	}

	public void setWeldingratio(double weldingratio) {
		this.weldingratio = weldingratio;
	}

	public double getFirsttime() {
		return firsttime;
	}

	public void setFirsttime(double firsttime) {
		this.firsttime = firsttime;
	}

	public double getFarc_time() {
		return farc_time;
	}

	public void setFarc_time(double farc_time) {
		this.farc_time = farc_time;
	}

	public double getRush() {
		return Rush;
	}

	public void setRush(double rush) {
		Rush = rush;
	}

	public double getHandarc_ele() {
		return handarc_ele;
	}

	public void setHandarc_ele(double handarc_ele) {
		this.handarc_ele = handarc_ele;
	}

	public double getHandarc_time() {
		return handarc_time;
	}

	public void setHandarc_time(double handarc_time) {
		this.handarc_time = handarc_time;
	}

	public double getHand_ele() {
		return hand_ele;
	}

	public void setHand_ele(double hand_ele) {
		this.hand_ele = hand_ele;
	}

	public double getBase_ele() {
		return Base_ele;
	}

	public void setBase_ele(double base_ele) {
		Base_ele = base_ele;
	}

	public double getBase_vol() {
		return Base_vol;
	}

	public void setBase_vol(double base_vol) {
		Base_vol = base_vol;
	}

	public double getBase_vol1() {
		return Base_vol1;
	}

	public void setBase_vol1(double base_vol1) {
		Base_vol1 = base_vol1;
	}

	public double getFargon() {
		return fargon;
	}

	public void setFargon(double fargon) {
		this.fargon = fargon;
	}

	public double getManual_weld() {
		return manual_weld;
	}

	public void setManual_weld(double manual_weld) {
		this.manual_weld = manual_weld;
	}

	public double getArc_length() {
		return arc_length;
	}

	public void setArc_length(double arc_length) {
		this.arc_length = arc_length;
	}

	public double getPulse() {
		return pulse;
	}

	public void setPulse(double pulse) {
		this.pulse = pulse;
	}

	public double getFweldparameters() {
		return fweldparameters;
	}

	public void setFweldparameters(double fweldparameters) {
		this.fweldparameters = fweldparameters;
	}

	public double getRise_time() {
		return rise_time;
	}

	public void setRise_time(double rise_time) {
		this.rise_time = rise_time;
	}

	public double getDecline_time() {
		return decline_time;
	}

	public void setDecline_time(double decline_time) {
		this.decline_time = decline_time;
	}

	public double getThrust_ele() {
		return thrust_ele;
	}

	public void setThrust_ele(double thrust_ele) {
		this.thrust_ele = thrust_ele;
	}

	public double getPulse_ratio() {
		return pulse_ratio;
	}

	public void setPulse_ratio(double pulse_ratio) {
		this.pulse_ratio = pulse_ratio;
	}

	public double getPoint_speed() {
		return point_speed;
	}

	public void setPoint_speed(double point_speed) {
		this.point_speed = point_speed;
	}

	public String getFwpsback() {
		return fwpsback;
	}

	public void setFwpsback(String fwpsback) {
		this.fwpsback = fwpsback;
	}

	public String getFspeed() {
		return fspeed;
	}

	public void setFspeed(String fspeed) {
		this.fspeed = fspeed;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getFini_tuny_vol() {
		return fini_tuny_vol;
	}

	public void setFini_tuny_vol(String fini_tuny_vol) {
		this.fini_tuny_vol = fini_tuny_vol;
	}

	public void setFarc_tuny_speed(String farc_tuny_speed) {
		this.farc_tuny_speed = farc_tuny_speed;
	}

	public void setFarc_speed(String farc_speed) {
		this.farc_speed = farc_speed;
	}

	public String getFfrequency() {
		return ffrequency;
	}

	public void setFfrequency(String ffrequency) {
		this.ffrequency = ffrequency;
	}

	public String getFselectstep() {
		return fselectstep;
	}

	public void setFselectstep(String fselectstep) {
		this.fselectstep = fselectstep;
	}

	public String getFarc_tuny_speed() {
		return farc_tuny_speed;
	}

	public String getFarc_speed() {
		return farc_speed;
	}

	public String getJOB_NUMBER() {
		return JOB_NUMBER;
	}

	public void setJOB_NUMBER(String JOB_NUMBER) {
		this.JOB_NUMBER = JOB_NUMBER;
	}

	public String getSET_NUMBER() {
		return SET_NUMBER;
	}

	public void setSET_NUMBER(String SET_NUMBER) {
		this.SET_NUMBER = SET_NUMBER;
	}

	public String getPART_DRAWING_NUMBER() {
		return PART_DRAWING_NUMBER;
	}

	public void setPART_DRAWING_NUMBER(String PART_DRAWING_NUMBER) {
		this.PART_DRAWING_NUMBER = PART_DRAWING_NUMBER;
	}

	public String getPART_NAME() {
		return PART_NAME;
	}

	public void setPART_NAME(String PART_NAME) {
		this.PART_NAME = PART_NAME;
	}

	public long getLibrary_id() {
		return library_id;
	}

	public void setLibrary_id(long library_id) {
		this.library_id = library_id;
	}

	public long getJunction_id() {
		return junction_id;
	}

	public void setJunction_id(long junction_id) {
		this.junction_id = junction_id;
	}

	public String getJunction_name() {
		return junction_name;
	}

	public void setJunction_name(String junction_name) {
		this.junction_name = junction_name;
	}

	public int getFini() {
		return fini;
	}
	public void setFini(int fini) {
		this.fini = fini;
	}
	private BigInteger fwpslib_id;
	
	public String getConname() {
		return conname;
	}
	public void setConname(String conname) {
		this.conname = conname;
	}
	public BigInteger getFwpslib_id() {
		return fwpslib_id;
	}
	public void setFwpslib_id(BigInteger fwpslib_id) {
		this.fwpslib_id = fwpslib_id;
	}
	public double getFarc_tuny_vol() {
		return farc_tuny_vol;
	}
	public void setFarc_tuny_vol(double farc_tuny_vol) {
		this.farc_tuny_vol = farc_tuny_vol;
	}
	public int getFarc() {
		return farc;
	}
	public void setFarc(int farc) {
		this.farc = farc;
	}
	public int getFcharacter() {
		return fcharacter;
	}
	public void setFcharacter(int fcharacter) {
		this.fcharacter = fcharacter;
	}
	public int getFmaterial() {
		return fmaterial;
	}
	public void setFmaterial(int fmaterial) {
		this.fmaterial = fmaterial;
	}
	public int getFgas() {
		return fgas;
	}
	public void setFgas(int fgas) {
		this.fgas = fgas;
	}
	public int getFselect() {
		return fselect;
	}
	public void setFselect(int fselect) {
		this.fselect = fselect;
	}
	public Wps(){
		super();
	}
	public long getFid() {
		return fid;
	}
	public void setFid(long fid) {
		this.fid = fid;
	}
	public BigInteger getWelderid() {
		return welderid;
	}
	public void setWelderid(BigInteger welderid) {
		this.welderid = welderid;
	}
	public BigInteger getMacid() {
		return macid;
	}
	public void setMacid(BigInteger macid) {
		this.macid = macid;
	}
	public BigInteger getInsid() {
		return insid;
	}
	public void setInsid(BigInteger insid) {
		this.insid = insid;
	}
	public String getWeldername(){
		return weldername;
	}
	public void setWeldername(String weldername){
		this.weldername = weldername;
	}
	public String getUpdatename(){
		return updatename;
	}
	public void setUpdatename(String updatename){
		this.updatename = updatename;
	}
	public String getInsname(){
		return insname;
	}
	public void setInsname(String insname){
		this.insname = insname;
	}
	public String getFwpsnum(){
		return fwpsnum;
	}
	public void setFwpsnum(String fwpsnum){
		this.fwpsnum = fwpsnum;
	}
	public int getFweld_i() {
		return fweld_i;
	}
	public void setFweld_i(int fweld_i) {
		this.fweld_i = fweld_i;
	}
	public int getFweld_v() {
		return fweld_v;
	}
	public void setFweld_v(int fweld_v) {
		this.fweld_v = fweld_v;
	}
	public int getFweld_i_max() {
		return fweld_i_max;
	}
	public void setFweld_i_max(int fweld_i_max) {
		this.fweld_i_max = fweld_i_max;
	}
	public int getFweld_i_min() {
		return fweld_i_min;
	}
	public void setFweld_i_min(int fweld_i_min) {
		this.fweld_i_min = fweld_i_min;
	}
	public int getFweld_v_max() {
		return fweld_v_max;
	}
	public void setFweld_v_max(int fweld_v_max) {
		this.fweld_v_max = fweld_v_max;
	}
	public int getFweld_v_min() {
		return fweld_v_min;
	}
	public void setFweld_v_min(int fweld_v_min) {
		this.fweld_v_min = fweld_v_min;
	}
	public int getFweld_alter_i() {
		return fweld_alter_i;
	}
	public void setFweld_alter_i(int fweld_alter_i) {
		this.fweld_alter_i = fweld_alter_i;
	}
	public int getFweld_alter_v() {
		return fweld_alter_v;
	}
	public void setFweld_alter_v(int fweld_alter_v) {
		this.fweld_alter_v = fweld_alter_v;
	}
	public int getFweld_prechannel() {
		return fweld_prechannel;
	}
	public void setFweld_prechannel(int fweld_prechannel) {
		this.fweld_prechannel = fweld_prechannel;
	}
	public Date getFcreatedate(){
		return fcreatedate;
	}
	public void setFcreatedate(Date fcreatedate){
		this.fcreatedate = fcreatedate;
	}
	public Date getFupdatedate(){
		return fupdatedate;
	}
	public void setFupdatedate(Date fupdatedate){
		this.fupdatedate = fupdatedate;
	}
	public long getFcreater() {
		return fcreater;
	}
	public void setFcreater(long fcreater) {
		this.fcreater = fcreater;
	}
	public long getFupdater() {
		return fupdater;
	}
	public void setFupdater(long fupdater) {
		this.fupdater = fupdater;
	}
	public long getFowner() {
		return fowner;
	}
	public void setFowner(long fowner) {
		this.fowner = fowner;
	}
	public String getFback(){
		return fback;
	}
	public void setFback(String fback){
		this.fback = fback;
	}
	public String getFname(){
		return fname;
	}
	public void setFname(String fname){
		this.fname = fname;
	}
	public double getFdiameter(){
		return fdiameter;
	}
	public void setFdiameter(double fdiameter){
		this.fdiameter = fdiameter;
	}
	public Wps(long fid,BigInteger insid,BigInteger macid,String insname,BigInteger welderid,String weldername,String updatename,String fwpsnum,
			int fweld_i,int fweld_v,int fweld_i_max,int fweld_i_min,int fweld_v_max,int fweld_v_min,int fweld_alter_i,int fweld_alter_v,int fweld_prechannel,Date fcreatedate,Date fupdatedate,long fcreater,long fupdater,long fowner,
			String fback,String fname,double fdiameter,double ftime,double fadvance,double fhysteresis,double fini_ele,double fini_vol,double fini_vol1,double fweld_ele,
			double fweld_vol,double fweld_vol1,double farc_ele,double farc_vol,double farc_vol1,double fweld_tuny_ele,double fweld_tuny_vol,double farc_tuny_ele,String finitial,
			String fcontroller,String fmode,int fstatus,String arcname,String selectname,String gasname,String dianame,String materialname,int ftorch,int fprocessid,
			String fprocessname,double fwarn_ele_up,double fwarn_ele_down,double fwarn_vol_up,double fwarn_vol_down
			,String f001,String f002,String f003,String f004,String f005,String f006,String f007,String f008,String f009,String f010,String f011,String f012,String f013,String f014,String f015,String f016,String f017,String f018,String f019,String f020,String f021,String f022, String f023,String f024,String f025
			,String f026,String f027,String f028,String f029,String f030,String f031,String f032,String f033,String f034,String f035,String f036,String f037,String f038,String f039,String f040,String f041,String f042,String f043,String f044,String f045,String f046,String f047, String f048,String f049,String f050
			,String fsolder_layer,String fweld_bead,String fweld_method,String fpower_polarity,String fgas_flow,String fweld_speed) {
		super();
		this.macid = macid;
		this.insname = insname;
		this.weldername = weldername;
		this.updatename = updatename;
		this.insid = insid;
		this.welderid = welderid;
		this.fid = fid;
		this.fwpsnum = fwpsnum;
		this.fweld_i = fweld_i;
		this.fweld_v = fweld_v;
		this.fweld_i_max = fweld_i_max;
		this.fweld_i_min = fweld_i_min;
		this.fweld_v_max = fweld_v_max;
		this.fweld_v_min = fweld_v_min;
		this.fweld_alter_i = fweld_alter_i;
		this.fweld_alter_v = fweld_alter_v;
		this.fweld_prechannel = fweld_prechannel;
		this.fcreatedate = fcreatedate;
		this.fupdatedate = fupdatedate;
		this.fcreater = fcreater;
		this.fupdater = fupdater;
		this.fowner = fowner;
		this.fback = fback;
		this.fname = fname;
		this.fdiameter = fdiameter;
		this.ftime = ftime;
		this.fadvance = fadvance;
		this.fhysteresis = fhysteresis;
		this.fini_ele = fini_ele;
		this.fini_vol = fini_vol;
		this.fini_vol1 = fini_vol1;
		this.fweld_ele = fweld_ele;
		this.fweld_vol = fweld_vol1;
		this.farc_ele = farc_ele;
		this.farc_vol = farc_vol;
		this.farc_vol1 = farc_vol1;
		this.fweld_tuny_ele = fweld_tuny_ele;
		this.fweld_tuny_vol = fweld_tuny_vol;
		this.farc_tuny_ele = farc_tuny_ele;
		this.finitial = finitial;
		this.fcontroller = fcontroller;
		this.fmode = fmode;
		this.fstatus = fstatus;
		this.arcname = arcname;
		this.gasname = gasname;
		this.selectname = selectname;
		this.materialname = materialname;
		this.dianame = dianame;
		this.fprocessid = fprocessid;
		this.fprocessname = fprocessname;
		this.ftorch = ftorch;
		this.fwarn_ele_up = fwarn_ele_up;
		this.fwarn_ele_down = fwarn_ele_down;
		this.fwarn_vol_up = fwarn_vol_up;
		this.fwarn_vol_down = fwarn_vol_down;
		this.fsolder_layer = fsolder_layer;
		this.fweld_method = fweld_method;
		this.fpower_polarity = fpower_polarity;
		this.fgas_flow = fgas_flow;
		this.fweld_speed = fweld_speed;
		this.f001 = f001;
		this.f002 = f002;
		this.f003 = f003;
		this.f004 = f004;
		this.f005 = f005;
		this.f006 = f006;
		this.f007 = f007;
		this.f008 = f008;
		this.f009 = f009;
		this.f010 = f010;
		this.f011 = f011;
		this.f012 = f012;
		this.f013 = f013;
		this.f014 = f014;
		this.f015 = f015;
		this.f016 = f016;
		this.f017 = f017;
		this.f018 = f018;
		this.f019 = f019;
		this.f020 = f020;
		this.f021 = f021;
		this.f022 = f022;
		this.f023 = f023;
		this.f024 = f024;
		this.f025 = f025;
		this.f026 = f026;
		this.f027 = f027;
		this.f028 = f028;
		this.f029 = f029;
		this.f030 = f030;
		this.f031 = f031;
		this.f032 = f032;
		this.f033 = f033;
		this.f034 = f034;
		this.f035 = f035;
		this.f036 = f036;
		this.f037 = f037;
		this.f038 = f038;
		this.f039 = f039;
		this.f040 = f040;
		this.f041 = f041;
		this.f042 = f042;
		this.f043 = f043;
		this.f044 = f044;
		this.f045 = f045;
		this.f046 = f046;
		this.f047 = f047;
		this.f048 = f048;
		this.f049 = f049;
		this.f050 = f050;
	}
	public double getFtime() {
		return ftime;
	}
	public void setFtime(double ftime) {
		this.ftime = ftime;
	}
	public double getFadvance() {
		return fadvance;
	}
	public void setFadvance(double fadvance) {
		this.fadvance = fadvance;
	}
	public double getFhysteresis() {
		return fhysteresis;
	}
	public void setFhysteresis(double fhysteresis) {
		this.fhysteresis = fhysteresis;
	}
	public double getFini_ele() {
		return fini_ele;
	}
	public void setFini_ele(double fini_ele) {
		this.fini_ele = fini_ele;
	}
	public double getFini_vol() {
		return fini_vol;
	}
	public void setFini_vol(double fini_vol) {
		this.fini_vol = fini_vol;
	}
	public double getFweld_ele() {
		return fweld_ele;
	}
	public void setFweld_ele(double fweld_ele) {
		this.fweld_ele = fweld_ele;
	}
	public double getFweld_vol() {
		return fweld_vol;
	}
	public void setFweld_vol(double fweld_vol) {
		this.fweld_vol = fweld_vol;
	}
	public double getFarc_ele() {
		return farc_ele;
	}
	public void setFarc_ele(double farc_ele) {
		this.farc_ele = farc_ele;
	}
	public double getFarc_vol() {
		return farc_vol;
	}
	public void setFarc_vol(double farc_vol) {
		this.farc_vol = farc_vol;
	}
	public double getFweld_tuny_ele() {
		return fweld_tuny_ele;
	}
	public void setFweld_tuny_ele(double fweld_tuny_ele) {
		this.fweld_tuny_ele = fweld_tuny_ele;
	}
	public double getFweld_tuny_vol() {
		return fweld_tuny_vol;
	}
	public void setFweld_tuny_vol(double fweld_tuny_vol) {
		this.fweld_tuny_vol = fweld_tuny_vol;
	}
	public double getFarc_tuny_ele() {
		return farc_tuny_ele;
	}
	public void setFarc_tuny_ele(double farc_tuny_ele) {
		this.farc_tuny_ele = farc_tuny_ele;
	}
	public String getFinitial() {
		return finitial;
	}
	public void setFinitial(String finitial) {
		this.finitial = finitial;
	}
	public String getFcontroller() {
		return fcontroller;
	}
	public void setFcontroller(String fcontroller) {
		this.fcontroller = fcontroller;
	}
	public String getFmode() {
		return fmode;
	}
	public void setFmode(String fmode) {
		this.fmode = fmode;
	}
	public double getFini_vol1() {
		return fini_vol1;
	}
	public void setFini_vol1(double fini_vol1) {
		this.fini_vol1 = fini_vol1;
	}
	public double getFweld_vol1() {
		return fweld_vol1;
	}
	public void setFweld_vol1(double fweld_vol1) {
		this.fweld_vol1 = fweld_vol1;
	}
	public double getFarc_vol1() {
		return farc_vol1;
	}
	public void setFarc_vol1(double farc_vol1) {
		this.farc_vol1 = farc_vol1;
	}
	public int getFstatus() {
		return fstatus;
	}
	public void setFstatus(int fstatus) {
		this.fstatus = fstatus;
	}
	public String getArcname() {
		return arcname;
	}
	public void setArcname(String arcname) {
		this.arcname = arcname;
	}
	public String getSelectname() {
		return selectname;
	}
	public void setSelectname(String selectname) {
		this.selectname = selectname;
	}
	public String getGasname() {
		return gasname;
	}
	public void setGasname(String gasname) {
		this.gasname = gasname;
	}
	public String getDianame() {
		return dianame;
	}
	public void setDianame(String dianame) {
		this.dianame = dianame;
	}
	public String getMaterialname() {
		return materialname;
	}
	public void setMaterialname(String materialname) {
		this.materialname = materialname;
	}
	public double getFpreset_ele_top() {
		return fpreset_ele_top;
	}
	public void setFpreset_ele_top(double fpreset_ele_top) {
		this.fpreset_ele_top = fpreset_ele_top;
	}
	public double getFpreset_vol_top() {
		return fpreset_vol_top;
	}
	public void setFpreset_vol_top(double fpreset_vol_top) {
		this.fpreset_vol_top = fpreset_vol_top;
	}
	public double getFpreset_ele_bottom() {
		return fpreset_ele_bottom;
	}
	public void setFpreset_ele_bottom(double fpreset_ele_bottom) {
		this.fpreset_ele_bottom = fpreset_ele_bottom;
	}
	public double getFpreset_vol_bottom() {
		return fpreset_vol_bottom;
	}
	public void setFpreset_vol_bottom(double fpreset_vol_bottom) {
		this.fpreset_vol_bottom = fpreset_vol_bottom;
	}
	public double getFarc_vol_top() {
		return farc_vol_top;
	}
	public void setFarc_vol_top(double farc_vol_top) {
		this.farc_vol_top = farc_vol_top;
	}
	public double getFpreset_ele_warn_top() {
		return fpreset_ele_warn_top;
	}
	public void setFpreset_ele_warn_top(double fpreset_ele_warn_top) {
		this.fpreset_ele_warn_top = fpreset_ele_warn_top;
	}
	public double getFpreset_vol_warn_top() {
		return fpreset_vol_warn_top;
	}
	public void setFpreset_vol_warn_top(double fpreset_vol_warn_top) {
		this.fpreset_vol_warn_top = fpreset_vol_warn_top;
	}
	public double getFpreset_ele_warn_bottom() {
		return fpreset_ele_warn_bottom;
	}
	public void setFpreset_ele_warn_bottom(double fpreset_ele_warn_bottom) {
		this.fpreset_ele_warn_bottom = fpreset_ele_warn_bottom;
	}
	public double getFpreset_vol_warn_bottom() {
		return fpreset_vol_warn_bottom;
	}
	public void setFpreset_vol_warn_bottom(double fpreset_vol_warn_bottom) {
		this.fpreset_vol_warn_bottom = fpreset_vol_warn_bottom;
	}
	public double getFini_ele_warn_top() {
		return fini_ele_warn_top;
	}
	public void setFini_ele_warn_top(double fini_ele_warn_top) {
		this.fini_ele_warn_top = fini_ele_warn_top;
	}
	public double getFini_vol_warn_top() {
		return fini_vol_warn_top;
	}
	public void setFini_vol_warn_top(double fini_vol_warn_top) {
		this.fini_vol_warn_top = fini_vol_warn_top;
	}
	public double getFini_ele_warn_bottom() {
		return fini_ele_warn_bottom;
	}
	public void setFini_ele_warn_bottom(double fini_ele_warn_bottom) {
		this.fini_ele_warn_bottom = fini_ele_warn_bottom;
	}
	public double getFini_vol_warn_bottom() {
		return fini_vol_warn_bottom;
	}
	public void setFini_vol_warn_bottom(double fini_vol_warn_bottom) {
		this.fini_vol_warn_bottom = fini_vol_warn_bottom;
	}
	public double getFarc_ele_warn_top() {
		return farc_ele_warn_top;
	}
	public void setFarc_ele_warn_top(double farc_ele_warn_top) {
		this.farc_ele_warn_top = farc_ele_warn_top;
	}
	public double getFarc_vol_warn_top() {
		return farc_vol_warn_top;
	}
	public void setFarc_vol_warn_top(double farc_vol_warn_top) {
		this.farc_vol_warn_top = farc_vol_warn_top;
	}
	public double getFarc_ele_warn_bottom() {
		return farc_ele_warn_bottom;
	}
	public void setFarc_ele_warn_bottom(double farc_ele_warn_bottom) {
		this.farc_ele_warn_bottom = farc_ele_warn_bottom;
	}
	public double getFarc_vol_warn_bottom() {
		return farc_vol_warn_bottom;
	}
	public void setFarc_vol_warn_bottom(double farc_vol_warn_bottom) {
		this.farc_vol_warn_bottom = farc_vol_warn_bottom;
	}
	public double getFarc_delay_time() {
		return farc_delay_time;
	}
	public void setFarc_delay_time(double farc_delay_time) {
		this.farc_delay_time = farc_delay_time;
	}
	public double getFwarn_delay_time() {
		return fwarn_delay_time;
	}
	public void setFwarn_delay_time(double fwarn_delay_time) {
		this.fwarn_delay_time = fwarn_delay_time;
	}
	public double getFwarn_stop_time() {
		return fwarn_stop_time;
	}
	public void setFwarn_stop_time(double fwarn_stop_time) {
		this.fwarn_stop_time = fwarn_stop_time;
	}
	public double getFflow_top() {
		return fflow_top;
	}
	public void setFflow_top(double fflow_top) {
		this.fflow_top = fflow_top;
	}
	public double getFflow_bottom() {
		return fflow_bottom;
	}
	public void setFflow_bottom(double fflow_bottom) {
		this.fflow_bottom = fflow_bottom;
	}
	public double getFdelay_time() {
		return fdelay_time;
	}
	public void setFdelay_time(double fdelay_time) {
		this.fdelay_time = fdelay_time;
	}
	public double getFover_time() {
		return fover_time;
	}
	public void setFover_time(double fover_time) {
		this.fover_time = fover_time;
	}
	public double getFfixed_cycle() {
		return ffixed_cycle;
	}
	public void setFfixed_cycle(double ffixed_cycle) {
		this.ffixed_cycle = ffixed_cycle;
	}
	public int getFprocessid() {
		return fprocessid;
	}
	public void setFprocessid(int fprocessid) {
		this.fprocessid = fprocessid;
	}
	public String getFprocessname() {
		return fprocessname;
	}
	public void setFprocessname(String fprocessname) {
		this.fprocessname = fprocessname;
	}
	public int getFtorch() {
		return ftorch;
	}
	public void setFtorch(int ftorch) {
		this.ftorch = ftorch;
	}
	public double getFwarn_ele_up() {
		return fwarn_ele_up;
	}
	public void setFwarn_ele_up(double fwarn_ele_up) {
		this.fwarn_ele_up = fwarn_ele_up;
	}
	public double getFwarn_ele_down() {
		return fwarn_ele_down;
	}
	public void setFwarn_ele_down(double fwarn_ele_down) {
		this.fwarn_ele_down = fwarn_ele_down;
	}
	public double getFwarn_vol_up() {
		return fwarn_vol_up;
	}
	public void setFwarn_vol_up(double fwarn_vol_up) {
		this.fwarn_vol_up = fwarn_vol_up;
	}
	public double getFwarn_vol_down() {
		return fwarn_vol_down;
	}
	public void setFwarn_vol_down(double fwarn_vol_down) {
		this.fwarn_vol_down = fwarn_vol_down;
	}
	public String getF001() {
		return f001;
	}
	public void setF001(String f001) {
		this.f001 = f001;
	}
	public String getF002() {
		return f002;
	}
	public void setF002(String f002) {
		this.f002 = f002;
	}
	public String getF003() {
		return f003;
	}
	public void setF003(String f003) {
		this.f003 = f003;
	}
	public String getF004() {
		return f004;
	}
	public void setF004(String f004) {
		this.f004 = f004;
	}
	public String getF005() {
		return f005;
	}
	public void setF005(String f005) {
		this.f005 = f005;
	}
	public String getF006() {
		return f006;
	}
	public void setF006(String f006) {
		this.f006 = f006;
	}
	public String getF007() {
		return f007;
	}
	public void setF007(String f007) {
		this.f007 = f007;
	}
	public String getF008() {
		return f008;
	}
	public void setF008(String f008) {
		this.f008 = f008;
	}
	public String getF009() {
		return f009;
	}
	public void setF009(String f009) {
		this.f009 = f009;
	}
	public String getF010() {
		return f010;
	}
	public void setF010(String f010) {
		this.f010 = f010;
	}
	public String getF011() {
		return f011;
	}
	public void setF011(String f011) {
		this.f011 = f011;
	}
	public String getF012() {
		return f012;
	}
	public void setF012(String f012) {
		this.f012 = f012;
	}
	public String getF013() {
		return f013;
	}
	public void setF013(String f013) {
		this.f013 = f013;
	}
	public String getF014() {
		return f014;
	}
	public void setF014(String f014) {
		this.f014 = f014;
	}
	public String getF015() {
		return f015;
	}
	public void setF015(String f015) {
		this.f015 = f015;
	}
	public String getF016() {
		return f016;
	}
	public void setF016(String f016) {
		this.f016 = f016;
	}
	public String getF017() {
		return f017;
	}
	public void setF017(String f017) {
		this.f017 = f017;
	}
	public String getF018() {
		return f018;
	}
	public void setF018(String f018) {
		this.f018 = f018;
	}
	public String getF019() {
		return f019;
	}
	public void setF019(String f019) {
		this.f019 = f019;
	}
	public String getF020() {
		return f020;
	}
	public void setF020(String f020) {
		this.f020 = f020;
	}
	public String getF021() {
		return f021;
	}
	public void setF021(String f021) {
		this.f021 = f021;
	}
	public String getF022() {
		return f022;
	}
	public void setF022(String f022) {
		this.f022 = f022;
	}
	public String getF023() {
		return f023;
	}
	public void setF023(String f023) {
		this.f023 = f023;
	}
	public String getF024() {
		return f024;
	}
	public void setF024(String f024) {
		this.f024 = f024;
	}
	public String getF025() {
		return f025;
	}
	public void setF025(String f025) {
		this.f025 = f025;
	}
	public String getF026() {
		return f026;
	}
	public void setF026(String f026) {
		this.f026 = f026;
	}
	public String getF027() {
		return f027;
	}
	public void setF027(String f027) {
		this.f027 = f027;
	}
	public String getF028() {
		return f028;
	}
	public void setF028(String f028) {
		this.f028 = f028;
	}
	public String getF029() {
		return f029;
	}
	public void setF029(String f029) {
		this.f029 = f029;
	}
	public String getF030() {
		return f030;
	}
	public void setF030(String f030) {
		this.f030 = f030;
	}
	public String getF031() {
		return f031;
	}
	public void setF031(String f031) {
		this.f031 = f031;
	}
	public String getF032() {
		return f032;
	}
	public void setF032(String f032) {
		this.f032 = f032;
	}
	public String getF033() {
		return f033;
	}
	public void setF033(String f033) {
		this.f033 = f033;
	}
	public String getF034() {
		return f034;
	}
	public void setF034(String f034) {
		this.f034 = f034;
	}
	public String getF035() {
		return f035;
	}
	public void setF035(String f035) {
		this.f035 = f035;
	}
	public String getF036() {
		return f036;
	}
	public void setF036(String f036) {
		this.f036 = f036;
	}
	public String getF037() {
		return f037;
	}
	public void setF037(String f037) {
		this.f037 = f037;
	}
	public String getF038() {
		return f038;
	}
	public void setF038(String f038) {
		this.f038 = f038;
	}
	public String getF039() {
		return f039;
	}
	public void setF039(String f039) {
		this.f039 = f039;
	}
	public String getF040() {
		return f040;
	}
	public void setF040(String f040) {
		this.f040 = f040;
	}
	public String getF041() {
		return f041;
	}
	public void setF041(String f041) {
		this.f041 = f041;
	}
	public String getF042() {
		return f042;
	}
	public void setF042(String f042) {
		this.f042 = f042;
	}
	public String getF043() {
		return f043;
	}
	public void setF043(String f043) {
		this.f043 = f043;
	}
	public String getF044() {
		return f044;
	}
	public void setF044(String f044) {
		this.f044 = f044;
	}
	public String getF045() {
		return f045;
	}
	public void setF045(String f045) {
		this.f045 = f045;
	}
	public String getF046() {
		return f046;
	}
	public void setF046(String f046) {
		this.f046 = f046;
	}
	public String getF047() {
		return f047;
	}
	public void setF047(String f047) {
		this.f047 = f047;
	}
	public String getF048() {
		return f048;
	}
	public void setF048(String f048) {
		this.f048 = f048;
	}
	public String getF049() {
		return f049;
	}
	public void setF049(String f049) {
		this.f049 = f049;
	}
	public String getF050() {
		return f050;
	}
	public void setF050(String f050) {
		this.f050 = f050;
	}
	public String getFsolder_layer() {
		return fsolder_layer;
	}
	public void setFsolder_layer(String fsolder_layer) {
		this.fsolder_layer = fsolder_layer;
	}
	public String getFweld_bead() {
		return fweld_bead;
	}
	public void setFweld_bead(String fweld_bead) {
		this.fweld_bead = fweld_bead;
	}
	public String getFweld_method() {
		return fweld_method;
	}
	public void setFweld_method(String fweld_method) {
		this.fweld_method = fweld_method;
	}
	public String getFpower_polarity() {
		return fpower_polarity;
	}
	public void setFpower_polarity(String fpower_polarity) {
		this.fpower_polarity = fpower_polarity;
	}
	public String getFgas_flow() {
		return fgas_flow;
	}
	public void setFgas_flow(String fgas_flow) {
		this.fgas_flow = fgas_flow;
	}
	public String getFweld_speed() {
		return fweld_speed;
	}
	public void setFweld_speed(String fweld_speed) {
		this.fweld_speed = fweld_speed;
	}
	public String getFproduct_drawing_no() {
		return fproduct_drawing_no;
	}
	public void setFproduct_drawing_no(String fproduct_drawing_no) {
		this.fproduct_drawing_no = fproduct_drawing_no;
	}
	public String getFproduct_name() {
		return fproduct_name;
	}
	public void setFproduct_name(String fproduct_name) {
		this.fproduct_name = fproduct_name;
	}
	public String getFwps_lib_version() {
		return fwps_lib_version;
	}
	public void setFwps_lib_version(String fwps_lib_version) {
		this.fwps_lib_version = fwps_lib_version;
	}
	public String getFproduct_version() {
		return fproduct_version;
	}
	public void setFproduct_version(String fproduct_version) {
		this.fproduct_version = fproduct_version;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getFquantitative_project() {
		return fquantitative_project;
	}
	public void setFquantitative_project(String fquantitative_project) {
		this.fquantitative_project = fquantitative_project;
	}
	public String getFrequired_value() {
		return frequired_value;
	}
	public void setFrequired_value(String frequired_value) {
		this.frequired_value = frequired_value;
	}
	public String getFupper_deviation() {
		return fupper_deviation;
	}
	public void setFupper_deviation(String fupper_deviation) {
		this.fupper_deviation = fupper_deviation;
	}
	public String getFlower_deviation() {
		return flower_deviation;
	}
	public void setFlower_deviation(String flower_deviation) {
		this.flower_deviation = flower_deviation;
	}
	public String getFunit_of_measurement() {
		return funit_of_measurement;
	}
	public void setFunit_of_measurement(String funit_of_measurement) {
		this.funit_of_measurement = funit_of_measurement;
	}
	public String getFemployee_id() {
		return femployee_id;
	}
	public void setFemployee_id(String femployee_id) {
		this.femployee_id = femployee_id;
	}
	public String getFstep_number() {
		return fstep_number;
	}
	public void setFstep_number(String fstep_number) {
		this.fstep_number = fstep_number;
	}
	public String getFjunction() {
		return fjunction;
	}
	public void setFjunction(String fjunction) {
		this.fjunction = fjunction;
	}
	public String getFemployee_version() {
		return femployee_version;
	}
	public void setFemployee_version(String femployee_version) {
		this.femployee_version = femployee_version;
	}
	public String getFstep_id() {
		return fstep_id;
	}
	public void setFstep_id(String fstep_id) {
		this.fstep_id = fstep_id;
	}
	public String getFstarttime() {
		return fstarttime;
	}
	public void setFstarttime(String fstarttime) {
		this.fstarttime = fstarttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getFitem() {
		return fitem;
	}
	public void setFitem(String fitem) {
		this.fitem = fitem;
	}
	public BigInteger getUnstandardtime() {
		return unstandardtime;
	}
	public void setUnstandardtime(BigInteger unstandardtime) {
		this.unstandardtime = unstandardtime;
	}
	public String getFwelding_area() {
		return fwelding_area;
	}
	public void setFwelding_area(String fwelding_area) {
		this.fwelding_area = fwelding_area;
	}
	public String getFstep_name() {
		return fstep_name;
	}
	public void setFstep_name(String fstep_name) {
		this.fstep_name = fstep_name;
	}
	public String getFemployee_name() {
		return femployee_name;
	}
	public void setFemployee_name(String femployee_name) {
		this.femployee_name = femployee_name;
	}
	public String getFproduct_number() {
		return fproduct_number;
	}
	public void setFproduct_number(String fproduct_number) {
		this.fproduct_number = fproduct_number;
	}
	public String getFprefix_number() {
		return fprefix_number;
	}
	public void setFprefix_number(String fprefix_number) {
		this.fprefix_number = fprefix_number;
	}
	public String getFwelded_junction_no() {
		return fwelded_junction_no;
	}
	public void setFwelded_junction_no(String fwelded_junction_no) {
		this.fwelded_junction_no = fwelded_junction_no;
	}
	public String getFsuffix_number() {
		return fsuffix_number;
	}
	public void setFsuffix_number(String fsuffix_number) {
		this.fsuffix_number = fsuffix_number;
	}
	public String getFstep_version() {
		return fstep_version;
	}
	public void setFstep_version(String fstep_version) {
		this.fstep_version = fstep_version;
	}
}
