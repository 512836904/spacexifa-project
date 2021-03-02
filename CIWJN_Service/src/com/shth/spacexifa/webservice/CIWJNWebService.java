package com.shth.spacexifa.webservice;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.soap.MTOM;

@WebService
@MTOM
public interface CIWJNWebService {
	Object enterTheWS(@WebParam(name="obj1")String obj1,@WebParam(name="obj2")String obj2);
	Object enterTheIDU(@WebParam(name="obj1")String obj1,@WebParam(name="obj2")String obj2);
	Object enterNoParamWs(@WebParam(name="obj1")String obj1);

	boolean UploadWeldFile(@WebParam(name = "filename")String filename,@WebParam(name = "fileSize")int fileSize,@WebParam(name = "fileBytes")byte[] fileBytes);
}
