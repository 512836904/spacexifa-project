package com.spring.dao;

import com.spring.dto.WeldDto;
import com.spring.model.Email;
import com.spring.model.Gather;
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
