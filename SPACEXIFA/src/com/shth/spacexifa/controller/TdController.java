package com.shth.spacexifa.controller;

import com.shth.spacexifa.model.*;
import com.shth.spacexifa.service.*;
import com.shth.spacexifa.util.IsnullUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/td", produces = {"text/json;charset=UTF-8"})
public class TdController {

    @Autowired
    private TdService tdService;
    @Autowired
    private LiveDataService lm;
    @Autowired
    private InsframeworkService insm;
    @Autowired
    private PersonService ps;

    @Autowired
    private WeldingMachineService wm;

    @Autowired
    private WpsService wpsService;
    @Autowired
    private JunctionService junctionService;

    IsnullUtil iutil = new IsnullUtil();

    /**
     * 获取所有用户列表
     *
     * @param request
     * @return
     */
    @RequestMapping("/AllTdbf")
    @ResponseBody
    public String Alltdbf(HttpServletRequest request) {
        String websocket = request.getSession().getServletContext().getInitParameter("websocket");
//		request.setAttribute("web_socket", websocket);
        StringBuffer requestURL = request.getRequestURL();
        System.out.println("requestURL:=======================" + requestURL);
        if (String.valueOf(requestURL).contains("10.110.11.3")) {
            websocket = "10.110.11.3:8083";
        }
        JSONObject obj = new JSONObject();
        obj.put("web_socket", websocket);
        return obj.toString();
    }

    /**
     * 跳转实时监控页面
     *
     * @param request
     * @return
     */
    @RequestMapping("/AllTd")
    public String Alltd(HttpServletRequest request) {
/*		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal();
		long uid = myuser.getId();
		String insname = tdService.findInsname(tdService.findIns(uid));
		request.setAttribute("insname", insname);*/
        lm.getUserId(request);
        return "td/newCurve";
    }

    /**
     * 跳转焊接管理界面
     *
     * @param request
     * @return
     */
    @RequestMapping("/openWeldModule")
    public String openWeldModule(HttpServletRequest request) {
        lm.getUserId(request);
        return "screen/weldmodule";
    }

    /**
     * 跳转质量管理界面
     *
     * @param request
     * @return
     */
    @RequestMapping("/openQuality")
    public String openQuality(HttpServletRequest request) {
        lm.getUserId(request);
        return "screen/quality";
    }

    /**
     * 跳转项目管理界面
     *
     * @param request
     * @return
     */
    @RequestMapping("/openWorkNumber")
    public String openWorkNumber(HttpServletRequest request) {
        lm.getUserId(request);
        return "screen/worknumber";
    }

    /**
     * 任务访问实时
     *
     * @param request
     * @return
     */
    @RequestMapping("/AllTdcard")
    public String AllTdcard(HttpServletRequest request) {
/*		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal();
		long uid = myuser.getId();
		String insname = tdService.findInsname(tdService.findIns(uid));
		request.setAttribute("insname", insname);*/
        lm.getUserId(request);
        String value = request.getParameter("machineid");
        request.setAttribute("value", value);
        return "td/nextCurve";
    }


    @RequestMapping("/goNextcurve")
    public String goNextcurve(HttpServletRequest request) {
        lm.getUserId(request);
        String value = request.getParameter("value");//采集编号
        String valuename = request.getParameter("valuename");//焊机固号
        String type = request.getParameter("type");//焊机类型
        String model = request.getParameter("model");
        String typeValue = request.getParameter("typeValue");
        String flag = request.getParameter("flag");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
//	    String time = tdService.getBootTime(df.format(new Date())+" 00:00:00", new BigInteger(value));
        request.setAttribute("value", value);//采集序号
        request.setAttribute("valuename", valuename);
        request.setAttribute("type", type);
        request.setAttribute("model", model);
        request.setAttribute("xmlTypeValue", typeValue);
        request.setAttribute("xmlFlag", flag);
        request.setAttribute("time", "--");
/*	    if("".equals(time)||time==null||"null".equals(time)){
		    request.setAttribute("time", "--");
	    }else{
		    request.setAttribute("time", time.substring(11));
	    }*/
        return "td/nextCurve";
    }

    @RequestMapping("/AllTdd")
    public String AllTdd(HttpServletRequest request) {
        request.setAttribute("divi", request.getParameter("value"));
        return "/division";
    }

    @RequestMapping("/AllTddp")
    public String AllTddp(HttpServletRequest request) {
        request.setAttribute("proj", request.getParameter("value"));
        return "/project";
    }

    @RequestMapping("/AllTdp")
    public String AllTdp(HttpServletRequest request) {
        return "/project";
    }

    @RequestMapping("/AllTdad")
    @ResponseBody
    public String AllTdad(HttpServletRequest request) {
        JSONObject obj = new JSONObject();
        String eq = request.getParameter("e");
        String an = request.getParameter("a");
        String vo = request.getParameter("v");
        String value = request.getParameter("value");
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            String[] equ = eq.split(",");
            String[] anp = an.split(",");
            String[] vol = vo.split(",");
            System.out.println(equ);
            for (int i = 0; i < equ.length; i++) {
                if (value.equals(equ[i])) {
                    json.put("voltage", vol[i]);
                    json.put("electricity", anp[i]);
                    ary.add(json);
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();

    }

    @RequestMapping("/AllTda")
    public String AllTda(HttpServletRequest request) {
        request.setAttribute("av", request.getParameter("value"));
        return "/AV";
    }

    @RequestMapping("/getAllTd")
    @ResponseBody
    public String getAllTd(HttpServletRequest request) {

        JSONObject obj = new JSONObject();
        String da = request.getParameter("data");
        /*		System.out.println(da);*/
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            for (int i = 0; i < da.length(); i += 53) {
                json.put("fstatus_id", da.substring(0 + i, 2 + i));
                json.put("finsframework_id", da.substring(2 + i, 4 + i));
                json.put("fequipment_no", da.substring(4 + i, 8 + i));
                json.put("fwelder_no", da.substring(8 + i, 12 + i));
                json.put("voltage", da.substring(12 + i, 16 + i));
                json.put("electricity", da.substring(16 + i, 20 + i));
                json.put("time", da.substring(20 + i, 41 + i));
                json.put("maxele", da.substring(41 + i, 44 + i));
                json.put("minele", da.substring(44 + i, 47 + i));
                json.put("maxvol", da.substring(47 + i, 50 + i));
                json.put("minvol", da.substring(50 + i, 53 + i));
                ary.add(json);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getAllTddiv")
    @ResponseBody
    public String getAllTddiv(HttpServletRequest request) {

        MyUser myuser = (MyUser) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        JSONObject obj = new JSONObject();
        long uid = myuser.getId();
        String insname = tdService.findInsname(uid);
        List<Td> findAlld = tdService.findAlldiv(tdService.findIns(uid));
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            for (Td td : findAlld) {
                json.put("fid", td.getFdi());
                json.put("fname", td.getFdn());
                json.put("fparent", td.getFdp());
                json.put("ftype", td.getFdt());
                ary.add(json);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getAllTdp")
    @ResponseBody
    public String getAllTdp(HttpServletRequest request) {

        JSONObject obj = new JSONObject();
        String da = request.getParameter("data");
        System.out.println(da);
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            for (int i = 0; i < da.length(); i += 53) {
                json.put("fstatus_id", da.substring(0 + i, 2 + i));
                json.put("finsframework_id", da.substring(2 + i, 4 + i));
                json.put("fequipment_no", da.substring(4 + i, 8 + i));
                json.put("fwelder_no", da.substring(8 + i, 12 + i));
                String weldname = tdService.findweld(da.substring(8 + i, 12 + i));
                json.put("fname", weldname);
                json.put("voltage", da.substring(12 + i, 16 + i));
                json.put("electricity", da.substring(16 + i, 20 + i));
                json.put("time", da.substring(20 + i, 41 + i));
                json.put("maxele", da.substring(41 + i, 44 + i));
                json.put("minele", da.substring(44 + i, 47 + i));
                json.put("maxvol", da.substring(47 + i, 50 + i));
                json.put("minvol", da.substring(50 + i, 53 + i));
                String position = tdService.findPosition(da.substring(4 + i, 8 + i));
                json.put("fposition", position);
                ary.add(json);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getAllTdp1")
    @ResponseBody
    public String getAllTdp1(HttpServletRequest request) {

        JSONObject obj = new JSONObject();
        long uid = Long.parseLong(request.getParameter("ins"));
        List<Td> findAllpro = tdService.findAlldiv(uid);
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            for (Td td : findAllpro) {
                json.put("fid", td.getFdi());
                json.put("fname", td.getFdn());
                json.put("fparent", td.getFdp());
                json.put("ftype", td.getFdt());
                ary.add(json);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getAllTdp2")
    @ResponseBody
    public String getAllTdp2(HttpServletRequest request) {

        JSONObject obj = new JSONObject();
        String insname = request.getParameter("div");
        long insid = tdService.findInsid(insname);
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {

            json.put("fid", insid);
            ary.add(json);

        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getAllTdd")
    @ResponseBody
    public String getAllTdd(HttpServletRequest request) {

        JSONObject obj = new JSONObject();
        String insname = request.getParameter("div");
        long insid = tdService.findInsid(insname);
        List<Td> findAlld = tdService.findAlldiv(insid);
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            for (Td td : findAlld) {
                json.put("fid", td.getFdi());
                json.put("fname", td.getFdn());
                json.put("fparent", td.getFdp());
                json.put("ftype", td.getFdt());
                ary.add(json);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getAllTdd1")
    @ResponseBody
    public String getAllTdd1(HttpServletRequest request) {

        JSONObject obj = new JSONObject();
        List<Td> findAllcom = tdService.findAllcom();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            for (Td td : findAllcom) {
/*				json.put("fpname",td.getFinsp());
				json.put("fdname",td.getFinsd());
				json.put("fcname",td.getFinsc());*/
                ary.add(json);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getAllTdd2")
    @ResponseBody
    public String getAllTdd2(HttpServletRequest request) {

        JSONObject obj = new JSONObject();
        List<Td> findAllcom = tdService.findAllcom();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            for (Td td : findAllcom) {
/*				json.put("fpname",td.getFinsp());
				json.put("fdname",td.getFinsd());
				json.put("fcname",td.getFinsc());*/
                ary.add(json);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getWeld")
    @ResponseBody
    public String getWeld(HttpServletRequest request) {

        String weldid = request.getParameter("weldid");
        JSONObject obj = new JSONObject();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            json.put("fweldname", tdService.findweld(weldid));
            ary.add(json);
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getPosition")
    @ResponseBody
    public String getPosition(HttpServletRequest request) {

        String equip = request.getParameter("equip");
        String eee = tdService.findPosition(equip);
        JSONObject obj = new JSONObject();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            json.put("fpositin", eee);
            ary.add(json);
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getAllPosition")
    @ResponseBody
    public String getAllPosition(HttpServletRequest request) {
        String parentId = request.getParameter("parent");
        String str = request.getParameter("str");
        String[] ssr = null;
        if (str != null && "" != str) {
            ssr = str.split(",");
        }
        str = "";
        int bz = 0;
        if (ssr != null) {
            if (Integer.valueOf(ssr[1]) == 1) {

            } else if (Integer.valueOf(ssr[1]) == 4) {
                str = "finsframework_id=" + ssr[0];
            } else {
                List<Insframework> ls = insm.getInsIdByParent(new BigInteger(ssr[0]), 24);
                for (Insframework inns : ls) {
                    if (bz == 0) {
                        str = str + "(finsframework_id=" + inns.getId();
                    } else {
                        str = str + " or finsframework_id=" + inns.getId();
                    }
                    bz++;
                }
                str = str + " or finsframework_id=" + ssr[0] + ")";
            }
        }
        BigInteger parent = null;
        if (iutil.isNull(parentId)) {
            parent = new BigInteger(parentId);
        }
        List<Td> getAP = tdService.getAllPosition(parent, str);
        JSONObject obj = new JSONObject();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            for (Td td : getAP) {
                json.put("fid", td.getId());
                json.put("fequipment_no", td.getFequipment_no());
                json.put("fposition", td.getFposition());
                json.put("finsid", td.getFci());
                json.put("finsname", td.getFcn());
                ary.add(json);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getLiveMachines")
    @ResponseBody
    public String getLiveMachines(HttpServletRequest request) {
        String parentId = request.getParameter("parent");
        BigInteger parent = null;
        JSONObject obj = new JSONObject();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        if (iutil.isNull(parentId)) {
            parent = new BigInteger(parentId);
            List<Td> getAP = tdService.getAllPositions(parent);
            try {
                for (Td td : getAP) {
                    json.put("fid", td.getId());
                    json.put("fequipment_no", td.getFequipment_no());
                    json.put("fposition", td.getFposition());
                    json.put("finsid", td.getFci());
                    json.put("finsname", td.getFcn());
                    json.put("fgather_no", td.getFstatus_id());
                    if (td.getModel().contains("NB")) {
                        json.put("model", 1);
                    } else if (td.getModel().contains("CPVE")) {
                        json.put("model", 2);
                    } else if (td.getModel().contains("EP")) {
                        json.put("model", 3);
                    } else if (td.getModel().contains("WB")) {
                        json.put("model", 4);
                    } else if (td.getModel().contains("SPC")) {
                        json.put("model", 5);
                    } else if (td.getModel().contains("A350P")) {
                        json.put("model", 6);
                    } else {
                        json.put("model", 0);
                    }
                    ary.add(json);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MyUser myuser = (MyUser) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            long uid = myuser.getId();
            List<Insframework> insframework = insm.getInsByUserid(BigInteger.valueOf(uid));
            parent = insframework.get(0).getId();
            if (insframework.get(0).getType() == 20) {
                List<Td> getAP = tdService.getAllPositions(parent);
                try {
                    for (Td td : getAP) {
                        json.put("fid", td.getId());
                        json.put("fequipment_no", td.getFequipment_no());
                        json.put("fposition", td.getFposition());
                        json.put("finsid", td.getFci());
                        json.put("finsname", td.getFcn());
                        json.put("fgather_no", td.getFstatus_id());
                        if (td.getModel().contains("NB")) {
                            json.put("model", 1);
                        } else if (td.getModel().contains("CPVE")) {
                            json.put("model", 2);
                        } else if (td.getModel().contains("EP")) {
                            json.put("model", 3);
                        } else if (td.getModel().contains("WB")) {
                            json.put("model", 4);
                        } else {
                            json.put("model", 0);
                        }
                        ary.add(json);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                List<Insframework> in = insm.getInsIdByParent(insm.getInsByUserid(BigInteger.valueOf(uid)).get(0).getId(), 24);
                List<Td> getAP = tdService.getAllPositions(parent);
                try {
                    for (Td td : getAP) {
                        for (Insframework ins : in) {
                            if (td.getFci() == Integer.valueOf(ins.getId().toString())) {
                                json.put("fid", td.getId());
                                json.put("fequipment_no", td.getFequipment_no());
                                json.put("fposition", td.getFposition());
                                json.put("finsid", td.getFci());
                                json.put("finsname", td.getFcn());
                                json.put("fgather_no", td.getFstatus_id());
                                if (td.getModel().contains("NB")) {
                                    json.put("model", 1);
                                } else if (td.getModel().contains("CPVE")) {
                                    json.put("model", 2);
                                } else if (td.getModel().contains("EP")) {
                                    json.put("model", 3);
                                } else if (td.getModel().contains("WB")) {
                                    json.put("model", 4);
                                } else {
                                    json.put("model", 0);
                                }
                                ary.add(json);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getLiveMachine")
    @ResponseBody
    public String getLiveMachine(HttpServletRequest request) {
        String parentId = request.getParameter("parent");
        BigInteger parent = null;
        JSONObject obj = new JSONObject();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        if (iutil.isNull(parentId)) {
            try {
                parent = new BigInteger(parentId);
                List<Td> getAP = tdService.getAllPosition(parent, null);
                for (Td td : getAP) {
                    json.put("fid", td.getId());
                    json.put("fequipment_no", td.getFequipment_no());//设备名称
                    json.put("fgather_no", td.getFstatus_id());//采集序号
                    json.put("fposition", td.getFposition());
                    json.put("finsid", td.getFci());
                    json.put("finsname", td.getFcn());
                    json.put("type", td.getTypeid());
                    json.put("modelId", td.getFpp());        //焊机型号id
                    //焊机型号
                    if (td.getModel().contains("NB")) {
                        json.put("model", 1);
                    } else if (td.getModel().contains("CPVE")) {
                        json.put("model", 2);
                    } else if (td.getModel().contains("EP")) {
                        json.put("model", 3);
                    } else if (td.getModel().contains("WB")) {
                        json.put("model", 4);
                    } else if (td.getModel().contains("SPC")) {
                        json.put("model", 5);
                    } else if (td.getModel().contains("A350P")) {
                        json.put("model", 6);
                    } else {
                        json.put("model", 0);
                    }
                    ary.add(json);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                MyUser myuser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                long uid = myuser.getId();
                List<Insframework> insframework = insm.getInsByUserid(BigInteger.valueOf(uid));
                parent = insframework.get(0).getId();
                List<Td> getAP = tdService.getAllPosition(parent, null);
                if (insframework.get(0).getType() == 20) {
                    for (Td td : getAP) {
                        json.put("fid", td.getId());
                        json.put("fequipment_no", td.getFequipment_no());
                        json.put("fgather_no", td.getFstatus_id());
                        json.put("fposition", td.getFposition());
                        json.put("finsid", td.getFci());
                        json.put("finsname", td.getFcn());
                        json.put("type", td.getTypeid());
                        ary.add(json);
                    }
                } else {
                    List<Insframework> in = insm.getInsIdByParent(insm.getInsByUserid(BigInteger.valueOf(uid)).get(0).getId(), 24);
                    //List<Td> getAP = tdService.getAllPosition(parent, null);
                    for (Td td : getAP) {
                        for (Insframework ins : in) {
                            if (ins.getId().equals(td.getFci())) {
                                json.put("fid", td.getId());
                                json.put("fequipment_no", td.getFequipment_no());
                                json.put("fgather_no", td.getFstatus_id());
                                json.put("fposition", td.getFposition());
                                json.put("finsid", td.getFci());
                                json.put("finsname", td.getFcn());
                                json.put("type", td.getTypeid());
                                ary.add(json);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        obj.put("rows", ary);
        return obj.toString();
    }


    @RequestMapping("/getMachine")
    @ResponseBody
    public String getMachine(HttpServletRequest request) {
        String mach = request.getParameter("mach");
        String parentId = request.getParameter("parent");
        BigInteger parent = null;
        BigInteger machs = null;
        if (iutil.isNull(mach)) {
            machs = new BigInteger("");
        }
        if (iutil.isNull(parentId)) {
            parent = new BigInteger(parentId);
        }
        JSONObject obj = new JSONObject();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            List<Td> getAP = tdService.getMachine(machs, parent);
            for (Td td : getAP) {
                json.put("fid", td.getId());
                json.put("fequipment_no", td.getFequipment_no());
                json.put("fposition", td.getFposition());
                json.put("finsid", td.getFci());
                json.put("finsname", td.getFcn());
                ary.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        obj.put("rows", ary);
        return obj.toString();
    }
	
/*	@RequestMapping("/isnull")
	@ResponseBody
	public String isnull(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		List<Td> getAP = tdService.getAllPosition();
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		try{
			for(Td td:getAP){
				json.put("fequipment_no",td.getFequipment_no());
				json.put("fposition", td.getFposition());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}*/

    @RequestMapping("/geInsname")
    @ResponseBody
    public String geInsname(HttpServletRequest request) {

        int iid = Integer.parseInt(request.getParameter("iid"));
        String insname = tdService.findInsname(iid);
        ;
        JSONObject obj = new JSONObject();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            json.put("fid", insname);
            ary.add(json);
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/allWeldname")
    @ResponseBody
    public String allWeldname(HttpServletRequest request) {
        JSONObject obj = new JSONObject();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            Td welderStatusNum = tdService.countWelderStatusNum();
            List<Td> fwn = tdService.allWeldname();
            for (Td td : fwn) {
                json.put("fid", td.getId());
                json.put("fname", td.getFname());
                json.put("fwelder_no", td.getFwelder_no());
                ary.add(json);
            }
            obj.put("welderStatusNum", welderStatusNum);
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }


    @RequestMapping("/getLiveWelder")
    @ResponseBody
    public String getLiveWelder(HttpServletRequest request) {
        BigInteger uid = lm.getUserId(request);
        BigInteger parent = null;
        List<Person> list = ps.findAll(parent);
        String parentId = request.getParameter("parent");
        if (iutil.isNull(parentId)) {
            parent = new BigInteger(parentId);
        } else {
            parent = insm.getUserInsfId(uid);
        }
        JSONObject obj = new JSONObject();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            Insframework insname = insm.getInsById(parent);
            for (int i = 0; i < list.size(); i++) {
                json.put("fid", list.get(i).getId());
                json.put("fname", list.get(i).getName());
                json.put("fwelder_no", list.get(i).getWelderno());
                json.put("fitemid", list.get(i).getInsid());
                json.put("fitemname", insname.getName());
                ary.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getLiveTime")
    @ResponseBody
    public String getLiveTime(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(new Date());
/*			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, 1); //得到后一天
			String totime = sdf.format(calendar.getTime());*/
//			Td list = tdService.getLiveTime(time.substring(0,11)+"00:00:00", time.substring(0,14)+"00:00", new BigInteger(request.getParameter("machineid")));
            //根据焊机id查询焊机相关信息
            WeldingMachine machinelist = wm.getWeldingMachineById(new BigInteger(request.getParameter("machineid")));
            if (null != machinelist) {
                json.put("machineno", machinelist.getTypename());//焊机型号
                json.put("mvaluename", machinelist.getMvaluename());//厂商
                json.put("machine", machinelist.getEquipmentNo());//焊机固号（焊机名称）
                json.put("insName", machinelist.getInsframeworkId().getName());//焊机班组
            } else {
                json.put("machineno", "");//焊机型号
                json.put("mvaluename", "");//厂商
                json.put("machine", "");//焊机固号（焊机名称）
                json.put("insName", "");//焊机班组
            }
//			if(list!=null){
//				json.put("worktime",list.getWorktime());
//				json.put("time",list.getWorktime());
//			}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /**
     * 根据工作号id查询工作号信息
     *
     * @param request
     * @return
     */
    @RequestMapping("getWorkInfoByCardId")
    @ResponseBody
    public String getWorkInfoByCardId(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String JOB_NUMBER = "";//工作号
        String SET_NUMBER = "";//工作号
        String PART_DRAWING_NUMBER = "";//工作号
        String PART_NAME = "";//工作号
        String junctionName = "";//焊缝名称
        try {
            String cardId = request.getParameter("cardId");//工作号id
            String junctionId = request.getParameter("junctionId");//焊缝id
            if (null != cardId && !"".equals(cardId)) {
                Wps wps = wpsService.getWeldedJunctionById(BigInteger.valueOf(Long.parseLong(cardId)));
                if (null != wps) {
                    JOB_NUMBER = wps.getJOB_NUMBER();
                    SET_NUMBER = wps.getSET_NUMBER();
                    PART_DRAWING_NUMBER = wps.getPART_DRAWING_NUMBER();
                    PART_NAME = wps.getPART_NAME();
                }
            }
            if (null != junctionId && !"".equals(junctionId)) {
                Junction junctionById = junctionService.getJunctionById(Long.parseLong(junctionId));
                if (null != junctionById) {
                    junctionName = junctionById.getJunction_name();
                }
            }
            jsonObject.put("JOB_NUMBER", JOB_NUMBER);
            jsonObject.put("SET_NUMBER", SET_NUMBER);
            jsonObject.put("PART_DRAWING_NUMBER", PART_DRAWING_NUMBER);
            jsonObject.put("PART_NAME", PART_NAME);
            jsonObject.put("junctionName", junctionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @RequestMapping("/getTrackCard")
    @ResponseBody
    public String getTrackCard(HttpServletRequest request) {
        JSONObject obj = new JSONObject();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        List<Wps> wps = wpsService.gettrackcard();
        try {
            for (Wps w : wps) {
                json.put("fid", w.getFid());
                json.put("fwelded_junction_no", w.getFwelded_junction_no());
                json.put("fwpsnum", w.getFwpsnum());
                if ("".equals(w.getFsuffix_number()) || w.getFsuffix_number() == null) {
                    json.put("fproduct_number", w.getFprefix_number() + w.getFproduct_number());
                } else {
                    json.put("fproduct_number", w.getFprefix_number() + w.getFproduct_number() + w.getFsuffix_number());
                }
                json.put("fproduct_drawing_no", w.getFproduct_drawing_no());
                json.put("fprocessname", w.getFprocessname());//工艺规程编号
                json.put("fwps_lib_version", w.getFwps_lib_version());//规程版本
                json.put("femployee_name", w.getFemployee_name());
                json.put("femployee_id", w.getFemployee_id());
                json.put("fstep_name", w.getFstep_name());
                json.put("fstep_number", w.getFstep_number());
                json.put("fjunction", w.getFjunction());
                json.put("insid", w.getInsid());
                json.put("fwelding_area", w.getFwelding_area());
                ary.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    public static String trimnull(String string) throws UnsupportedEncodingException {
        ArrayList<Byte> list = new ArrayList<Byte>();
        byte[] bytes = string.getBytes("UTF-8");
        for (int i = 0; bytes != null && i < bytes.length; i++) {
            if (0 != bytes[i]) {
                list.add(bytes[i]);
            }
        }
        byte[] newbytes = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            newbytes[i] = (Byte) list.get(i);
        }
        String str = new String(newbytes, "UTF-8");
        return str;
    }

}