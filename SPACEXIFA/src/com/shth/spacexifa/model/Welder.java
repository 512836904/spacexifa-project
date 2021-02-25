package com.shth.spacexifa.model;

import java.math.BigInteger;

/**
 * 焊工
 * @author gpyf16
 *
 */
public class Welder {
	private BigInteger id;
	private String welderno;
	private String name;
	private String iname;
	private String FSTATUS; //焊工状态：0离线（默认）

	public String getIname() {
		return iname;
	}
	public void setIname(String iname) {
		this.iname = iname;
	}
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getWelderno() {
		return welderno;
	}
	public void setWelderno(String welderno) {
		this.welderno = welderno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getFSTATUS() {
		return FSTATUS;
	}

	public void setFSTATUS(String FSTATUS) {
		this.FSTATUS = FSTATUS;
	}
}
