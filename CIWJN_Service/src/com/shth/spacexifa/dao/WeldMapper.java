package com.shth.spacexifa.dao;

import java.math.BigInteger;

import com.shth.spacexifa.model.Weld;

public interface WeldMapper {
	BigInteger FindIns_Id(Weld user);
	boolean AddWeld(Weld user);
	boolean UpdateWeld(Weld user);
	boolean AddJunction(Weld user);
	boolean UpdateJunction(Weld user);
	boolean DeleteJunction(Weld user);
}