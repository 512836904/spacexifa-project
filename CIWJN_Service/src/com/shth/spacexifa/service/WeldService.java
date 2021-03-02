package com.shth.spacexifa.service;

import java.math.BigInteger;

public interface WeldService {

	BigInteger FindIns_Id(String insname);
	BigInteger AddWeld(String aweld);
	Boolean UpdateWeld(String uweld);
	BigInteger AddJunction(String ajunction);
	Boolean UpdateJunction(String ujunction);
	Boolean DeleteJunction(String djunction);
	

}