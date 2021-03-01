package com.shth.spacexifa.dao;

import com.shth.spacexifa.dto.WeldDto;
import com.shth.spacexifa.model.Email;
import com.shth.spacexifa.model.Gather;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface EmailMapper extends Mapper<Gather>{
	List<Email> getEmailAll(@Param("str")String str);
	
	int getEmailAddressCount(@Param("femailaddress")String femailaddress);
	
	boolean addEmail(Email email);
	
	boolean editEmail(Email email);
	
	boolean deleteEmail(@Param("femailaddress")String femailaddress);
	
	List<Email> getEmailHistory(@Param("dto") WeldDto dto);
}
