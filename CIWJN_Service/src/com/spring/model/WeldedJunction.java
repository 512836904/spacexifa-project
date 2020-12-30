package com.spring.model;

import java.math.BigInteger;

import javax.persistence.Transient;

import org.springframework.stereotype.Component;

/**
 * 焊口
 * @author gpyf16
 *
 */
@Component
public class WeldedJunction {
	private BigInteger id;
	private BigInteger fid;
	private String weldedJunctionno;
	private String serialNo;
	private String pipelineNo;
	private String roomNo;
	private String unit;
	private String area;
	private String systems;
	private String children;
	private String externalDiameter;
	private String wallThickness;
	private int dyne;
	private String specification;
	private double maxElectricity;
	private double minElectricity;
	private double maxValtage;
	private double minValtage;
	private String material;//材质（新增字段）
	private String nextexternaldiameter;//下游外径（新增字段）
	private String startTime;
	private String endTime;
	private String creatTime;
	private String updateTime;
	private int updatecount;
	private String nextwall_thickness;
	private String next_material;
	private String electricity_unit;
	private String valtage_unit;
	private BigInteger taskid;
	private BigInteger welderid;
	private BigInteger machineid;
	private BigInteger operatorid;
	private BigInteger iid;
	private String iname;

	private String creator;
	private String modifier;
	
	private String fcard_id;
	private String fcard_no;
	private String ftask_no;
	private String fproduct_drawing_no;
	private String fproduct_name;
	private String fproduct_version;
	private String fwps_lib_id;
	private String fwps_lib_no;
	private String fwps_lib_version;
	private String femployee_id;
	private String femployee_version;
	private String femployee_name;
	private String fstep_number;
	private String fstep_id;
	private String fstep_name;
	private String fstep_version;
	private String fquantitative_project;
	private String frequired_value;
	private String fupper_deviation;
	private String flower_deviation;
	private String funit_of_measurement;
	private String fjunction;
	private String fwelding_area;
	private String fprefix_number;
	private String fsuffix_number;
	private String fproduct_number;
	private String file_name;
	
	public String getFcard_no() {
		return fcard_no;
	}
	public void setFcard_no(String fcard_no) {
		this.fcard_no = fcard_no;
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
	public String getFwps_lib_no() {
		return fwps_lib_no;
	}
	public void setFwps_lib_no(String fwps_lib_no) {
		this.fwps_lib_no = fwps_lib_no;
	}
	public String getFwps_lib_version() {
		return fwps_lib_version;
	}
	public void setFwps_lib_version(String fwps_lib_version) {
		this.fwps_lib_version = fwps_lib_version;
	}
	public String getFemployee_id() {
		return femployee_id;
	}
	public void setFemployee_id(String femployee_id) {
		this.femployee_id = femployee_id;
	}
	public String getFemployee_version() {
		return femployee_version;
	}
	public void setFemployee_version(String femployee_version) {
		this.femployee_version = femployee_version;
	}
	public String getFemployee_name() {
		return femployee_name;
	}
	public void setFemployee_name(String femployee_name) {
		this.femployee_name = femployee_name;
	}
	public String getFstep_number() {
		return fstep_number;
	}
	public void setFstep_number(String fstep_number) {
		this.fstep_number = fstep_number;
	}
	public String getFstep_id() {
		return fstep_id;
	}
	public void setFstep_id(String fstep_id) {
		this.fstep_id = fstep_id;
	}
	public String getFstep_name() {
		return fstep_name;
	}
	public void setFstep_name(String fstep_name) {
		this.fstep_name = fstep_name;
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
	public String getFjunction() {
		return fjunction;
	}
	public void setFjunction(String fjunction) {
		this.fjunction = fjunction;
	}
	public String getFwelding_area() {
		return fwelding_area;
	}
	public void setFwelding_area(String fwelding_area) {
		this.fwelding_area = fwelding_area;
	}
	public String getFprefix_number() {
		return fprefix_number;
	}
	public void setFprefix_number(String fprefix_number) {
		this.fprefix_number = fprefix_number;
	}
	public String getFproduct_number() {
		return fproduct_number;
	}
	public void setFproduct_number(String fproduct_number) {
		this.fproduct_number = fproduct_number;
	}
	
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getElectricity_unit() {
		return electricity_unit;
	}
	public void setElectricity_unit(String electricity_unit) {
		this.electricity_unit = electricity_unit;
	}
	public String getValtage_unit() {
		return valtage_unit;
	}
	public void setValtage_unit(String valtage_unit) {
		this.valtage_unit = valtage_unit;
	}
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getWeldedJunctionno() {
		return weldedJunctionno;
	}
	public void setWeldedJunctionno(String weldedJunctionno) {
		this.weldedJunctionno = weldedJunctionno;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getPipelineNo() {
		return pipelineNo;
	}
	public void setPipelineNo(String pipelineNo) {
		this.pipelineNo = pipelineNo;
	}
	public String getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getSystems() {
		return systems;
	}
	public void setSystems(String systems) {
		this.systems = systems;
	}
	public String getChildren() {
		return children;
	}
	public void setChildren(String children) {
		this.children = children;
	}
	public String getExternalDiameter() {
		return externalDiameter;
	}
	public void setExternalDiameter(String externalDiameter) {
		this.externalDiameter = externalDiameter;
	}
	public String getWallThickness() {
		return wallThickness;
	}
	public void setWallThickness(String wallThickness) {
		this.wallThickness = wallThickness;
	}
	public int getDyne() {
		return dyne;
	}
	public void setDyne(int dyne) {
		this.dyne = dyne;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public double getMaxElectricity() {
		return maxElectricity;
	}
	public void setMaxElectricity(double maxElectricity) {
		this.maxElectricity = maxElectricity;
	}
	public double getMinElectricity() {
		return minElectricity;
	}
	public void setMinElectricity(double minElectricity) {
		this.minElectricity = minElectricity;
	}
	public double getMaxValtage() {
		return maxValtage;
	}
	public void setMaxValtage(double maxValtage) {
		this.maxValtage = maxValtage;
	}
	public double getMinValtage() {
		return minValtage;
	}
	public void setMinValtage(double minValtage) {
		this.minValtage = minValtage;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getNextexternaldiameter() {
		return nextexternaldiameter;
	}
	public void setNextexternaldiameter(String nextexternaldiameter) {
		this.nextexternaldiameter = nextexternaldiameter;
	}
	public String getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public int getUpdatecount() {
		return updatecount;
	}
	public void setUpdatecount(int updatecount) {
		this.updatecount = updatecount;
	}
	public String getNextwall_thickness() {
		return nextwall_thickness;
	}
	public void setNextwall_thickness(String nextwall_thickness) {
		this.nextwall_thickness = nextwall_thickness;
	}
	public String getNext_material() {
		return next_material;
	}
	public void setNext_material(String next_material) {
		this.next_material = next_material;
	}
	public BigInteger getTaskid() {
		return taskid;
	}
	public void setTaskid(BigInteger taskid) {
		this.taskid = taskid;
	}
	public BigInteger getWelderid() {
		return welderid;
	}
	public void setWelderid(BigInteger welderid) {
		this.welderid = welderid;
	}
	public BigInteger getMachineid() {
		return machineid;
	}
	public void setMachineid(BigInteger machineid) {
		this.machineid = machineid;
	}
	public BigInteger getOperatorid() {
		return operatorid;
	}
	public void setOperatorid(BigInteger operatorid) {
		this.operatorid = operatorid;
	}
	public BigInteger getIid() {
		return iid;
	}
	public void setIid(BigInteger iid) {
		this.iid = iid;
	}
	public String getIname() {
		return iname;
	}
	public void setIname(String iname) {
		this.iname = iname;
	}
	public BigInteger getFid() {
		return fid;
	}
	public void setFid(BigInteger fid) {
		this.fid = fid;
	}
	public String getFproduct_version() {
		return fproduct_version;
	}
	public void setFproduct_version(String fproduct_version) {
		this.fproduct_version = fproduct_version;
	}
	public String getFwps_lib_id() {
		return fwps_lib_id;
	}
	public void setFwps_lib_id(String fwps_lib_id) {
		this.fwps_lib_id = fwps_lib_id;
	}
	public String getFtask_no() {
		return ftask_no;
	}
	public void setFtask_no(String ftask_no) {
		this.ftask_no = ftask_no;
	}
	public String getFcard_id() {
		return fcard_id;
	}
	public void setFcard_id(String fcard_id) {
		this.fcard_id = fcard_id;
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
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	
}
