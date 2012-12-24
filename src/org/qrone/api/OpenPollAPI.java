package org.qrone.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.*;

import net.arnx.jsonic.JSON;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

@SuppressWarnings("serial")
public class OpenPollAPI extends HttpServlet {
	
	MemcacheService mem = MemcacheServiceFactory.getMemcacheService();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		String[] actions = req.getParameterValues("action");
		String[] keys = req.getParameterValues("key");
		if(keys != null && keys.length == 1){
			
			Object o = mem.get("org.qrone.api.OpenPollAPI." + keys[0]);
		
			Map ary;
			if(o != null){
				ary = JSON.decode(o.toString());
			}else{
				ary = new HashMap();
			}
		
			if(actions != null && actions.length == 1 && actions[0].equals("get")){
				resp.setContentType("text/plain; charset=UTF8");
				
				Map obj = new HashMap();
				obj.put("status", "OK");
				obj.put("action", "get");
				obj.put("data", ary);

				resp.getWriter().println(obj.toString());
				mem.delete("org.qrone.api.OpenPollAPI." + keys[0]);
				return;
				
			}else if(actions != null && actions.length == 1 && actions[0].equals("set")){
				resp.setContentType("text/javascript; charset=UTF8");
				
				String[] datas = req.getParameterValues("data");
				if(datas != null && datas.length == 1){

					Map obj = new HashMap();
					obj.put("status", "OK");
					obj.put("action", "set");
					obj.put("data", datas[0]);

					resp.getWriter().println("jsonpCallback(" + obj.toString() + ");");
					mem.put("org.qrone.api.OpenPollAPI." + keys[0], ary.toString());
					return;
				}
			}
		}
		
		resp.getWriter().println("{\"status\":\"ERROR\"}");
	}
}
