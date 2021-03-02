package com.shth.spacexifa.webservice.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import javax.jws.WebService;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shth.spacexifa.model.WeldedJunction;
import com.shth.spacexifa.service.WeldedJunctionService;
import com.shth.spacexifa.webservice.CIWJNWebService;
import com.shth.spacexifa.webservice.impl.staticAppliceFClass.AppliceIoc;

@Transactional
@Service
@WebService(endpointInterface = "com.shth.spacexifa.webservice.CIWJNWebService", serviceName = "CIWJNWebService")
public class CIWJNWebServiceImpl implements CIWJNWebService {
	@Autowired
	private WeldedJunctionService wjm;
	@Override
	public Object enterTheWS(String obj1, String obj2) {
		/*try{
			JSONObject json1 = JSONObject.fromObject(obj1);
			ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"config/spring-common.xml"});
			Class<?>  cls = context.getBean(json1.getString("CLASSNAME")).getClass();
			Method m = cls.getDeclaredMethod(json1.getString("METHOD"),new Class[]{String.class});
			Object obj = m.invoke(context.getBean(json1.getString("CLASSNAME")),new Object[]{obj2});
			return obj;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}*/
		try{
			Object obj=AppliceIoc.enterTheWS(obj1,obj2);
			return obj;
		}catch(Exception e){
			e.getStackTrace();
			return null;
		}
	}
	
	@Override
	public Object enterTheIDU(String obj1, String obj2) {
		/*try{
			JSONObject json1 = JSONObject.fromObject(obj1);
			ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"config/spring-common.xml"});
			Class<?>  cls = context.getBean(json1.getString("CLASSNAME")).getClass();
			Method m = cls.getDeclaredMethod(json1.getString("METHOD"),new Class[]{String.class,String.class});
			Object obj = m.invoke(context.getBean(json1.getString("CLASSNAME")),new Object[]{obj1,obj2});
			return obj;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}*/
		try{
			Object obj=AppliceIoc.enterTheIDU(obj1,obj2);
			return obj;
		}catch(Exception e){
			e.getStackTrace();
			return null;
		}
	}

	@Override
	public Object enterNoParamWs(String obj1) {
		/*try{
			JSONObject json1 = JSONObject.fromObject(obj1);
			ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"config/spring-common2.xml"});
			Class<?>  cls = context.getBean(json1.getString("CLASSNAME")).getClass();
			Method m = cls.getDeclaredMethod(json1.getString("METHOD"));
			Object obj = m.invoke(context.getBean(json1.getString("CLASSNAME")));
	        
			return obj;
			
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}*/
		try{
			Object obj=AppliceIoc.enterNoParamWs(obj1);
			return obj;
		}catch(Exception e){
			e.getStackTrace();
			return null;
		}
		
	}

    public synchronized boolean UploadWeldFile(String fileName,int fileSize,byte[] fileBytes) {
    	if (fileName != null && !"".equals(fileName)) {
    		if(fileSize!=0) {
    			if (fileBytes != null) {
    				WeldedJunction w = new WeldedJunction();
    				BufferedOutputStream bos = null;
    				FileOutputStream fos = null;
    				File file = null;  
					try {  
					    File dir = new File("C:\\WDMS\\apache-tomcat-8.0.53\\webapps\\CardList");  
					    if(!dir.exists()){//判断文件目录是否存在  
					        dir.mkdirs();  
					    }  
					    file = new File("C:\\WDMS\\apache-tomcat-8.0.53\\webapps\\CardList"+"\\"+fileName);  
					    fos = new FileOutputStream(file);  
					    bos = new BufferedOutputStream(fos);  
					    bos.write(fileBytes);  
					} catch (Exception e) {  
					    e.printStackTrace();  
					} finally {  
					    if (bos != null) {  
					        try {  
					            bos.close();  
					        } catch (IOException e1) {  
					            e1.printStackTrace();  
					        }  
					    }  
					    if (fos != null) {  
					        try {  
					            fos.close();  
					        } catch (IOException e1) {  
					            e1.printStackTrace();  
					        }  
					    }  
					} 
					SAXReader reader = new SAXReader();
					try {
						Node node;
						Document document = reader.read(file);
						node = document.selectSingleNode("//taskno"); //任务号
						w.setFtask_no(node.getText());
						node = document.selectSingleNode("//containername"); //跟踪卡号
						w.setFcard_no(node.getText());
						w.setWeldedJunctionno(node.getText());
//						node = document.selectSingleNode("//containerid"); //跟踪卡id
//						w.setFcard_id(node.getText());
						
						String formatStr = document.selectSingleNode("//productnoformat").getText();//产品序号格式
						if(!("").equals(formatStr)&&formatStr!=null) {
							formatStr = formatStr.split(",")[0];
						}else {
							return false;
						}
						String[] formatStrArr = formatStr.split("#");
						String productNo = document.selectSingleNode("//productno").getText().split(",")[0]; //产品序号
						int startIndex = productNo.indexOf(formatStrArr[0]);
						int endIndex;
						String initValue = "";
						if(formatStrArr.length>1) {
							endIndex = productNo.lastIndexOf(formatStrArr[1]); 
							initValue = productNo.substring(startIndex+formatStrArr[0].length(),endIndex); 
						}else {
							initValue = productNo.substring(startIndex+formatStrArr[0].length());
						}
						String ProductCount = document.selectSingleNode("//qty").getText(); //数量
						w.setFprefix_number(formatStrArr[0]);
						if(formatStrArr.length>1) {
							w.setFsuffix_number(formatStrArr[1]);
						}else {
							w.setFsuffix_number("");
						}
						node = document.selectSingleNode("//productname"); //产品图号
						w.setFproduct_drawing_no(node.getText());
						node = document.selectSingleNode("//productdesc"); //产品名称
						w.setFproduct_name(node.getText());
						node = document.selectSingleNode("//productrevision"); //产品版本
						w.setFproduct_version(node.getText());
//						node = document.selectSingleNode("//workflowid"); //工艺规程id
//						w.setFwps_lib_id(node.getText());
						node = document.selectSingleNode("//workflowname"); //工艺规程名称
						w.setFwps_lib_no(node.getText());
						node = document.selectSingleNode("//workflowrevision"); //工艺规程版本
						w.setFwps_lib_version(node.getText());
						
						node = document.selectSingleNode("//specno"); //工序号
						w.setFemployee_id(node.getText());
						node = document.selectSingleNode("//specname"); //工序名称
						w.setFemployee_name(node.getText());
						node = document.selectSingleNode("//specrevision"); //工序版本
						w.setFemployee_version(node.getText());
//						node = document.selectSingleNode("//parentworkflowstepid"); //工步id
//						w.setFstep_id(node.getText());
						node = document.selectSingleNode("//stepno"); //工步号
						w.setFstep_number(node.getText());
						node = document.selectSingleNode("//stepname"); //工步名称
						w.setFstep_name(node.getText());
						node = document.selectSingleNode("//steprevision"); //工步版本
						w.setFstep_version(node.getText());
						node = document.selectSingleNode("//parameternumber"); //焊缝编号
						w.setFjunction(node.getText());
						node = document.selectSingleNode("//parametervalue"); //焊接部位
						w.setFwelding_area(node.getText());
						w.setFile_name(fileName);
//						node = document.selectSingleNode("//tableid"); //质量参数表id
//						node = document.selectSingleNode("//tablevesion"); //质量参数表版本
						String param = "",id = "";
						param = " tb_wps_library where fwps_lib_name='"+w.getFwps_lib_no() + 
								"' AND fproduct_drawing_no='" + w.getFproduct_drawing_no() + 
								"' AND fproduct_name='" + w.getFproduct_name() + 
								"' AND fproduct_version='" + w.getFproduct_version() + 
								"' AND fwps_lib_version='" + w.getFwps_lib_version() +"'";
						id = wjm.getIdByParam(param);
						if(!"".equals(id)&&id!=null) {
							w.setFid(new BigInteger(id));
							w.setFwps_lib_id(id);
						}else {
							wjm.addWps(w);
							w.setFwps_lib_id(String.valueOf(w.getFid()));
						}
						param = " tb_employee where femployee_name='"+w.getFemployee_name() + 
								"' AND femployee_version='" + w.getFemployee_version() + 
								"' AND fwps_lib_id=" + w.getFwps_lib_id();
						id = wjm.getIdByParam(param);
						if(!"".equals(id)&&id!=null) {
							w.setFid(new BigInteger(id));
							w.setFemployee_id(id);
						}else {
							wjm.addEmployee(w);
							w.setFemployee_id(String.valueOf(w.getFid()));
						}
						param = " tb_step where fstep_name='"+w.getFstep_name() + 
								"' AND fstep_version='" + w.getFstep_version() + 
								"' AND femployee_id='" + w.getFemployee_id() +"'";
						id = wjm.getIdByParam(param);
						if(!"".equals(id)&&id!=null) {
							w.setFid(new BigInteger(id));
							w.setFstep_id(id);
//							wjm.addDetail(w);
						}else {
							wjm.addStep(w);
							w.setFstep_id(String.valueOf(w.getFid()));
//							wjm.addDetail(w);
						}
						wjm.addStepFile(w);
						param = " tb_junction inner join TB_STEP_JUNCTION on tb_junction.fid=TB_STEP_JUNCTION.fjunction_id where FJUNCTION='"+w.getFjunction() + 
								"' AND FWELDING_AREA='" + w.getFwelding_area() + "' AND TB_STEP_JUNCTION.fstep_id=" + w.getFstep_id();
						id = wjm.getJunctionIdByParam(param);
						if(!"".equals(id)&&id!=null) {
							wjm.addStepJunction(w.getFstep_id(), id);
						}else {
							wjm.addWeldedJunction(w);
							wjm.addStepJunction(w.getFstep_id(), String.valueOf(w.getFid()));
						}
						
						param = " tb_welded_junction where fwelded_junction_no='"+w.getFcard_no() +"'";
						id = wjm.getIdByParam(param);
						if("".equals(id)||id==null) {
							wjm.addCard(w);
							for(int i=0;i<Integer.parseInt(ProductCount);i++) {
								w.setFproduct_number(String.valueOf(Integer.parseInt(initValue)+i));
								wjm.addProductNum(w);
							}
						}
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}
    			} else {
    				return false;
    			}
    		}
        } else {
            return false;
        }
    	return true;
    }

}
