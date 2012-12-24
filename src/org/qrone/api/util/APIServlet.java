package org.qrone.api.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qrone.util.Token;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import net.arnx.jsonic.JSON;

@SuppressWarnings("serial")
public abstract class APIServlet extends HttpServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp){
		
		String auth = req.getHeader("Authorization");
		if(auth != null){
			if(auth.startsWith("Bearer")){
				auth = auth.substring("Bearer".length()).trim();
			}
			
			Token token = Token.parse(auth);
			//token.validate(MasterKey.getSecret());
			
		}
		
		Map map = new HashMap();
		map.put("status", "ok");
		
		Object result = apiRequest(req, resp, null, map);
		if(result instanceof Integer){

			try {
				resp.getWriter().println("{\"status\":\"error\",\"code\":" + result + "}");
			} catch (IOException e) {}
			
		}else{
			String json = JSON.encode(result);
			try {
				String type = req.getParameter(".type");
				if(type != null){
					type = type.toLowerCase();
					if(type.equals("json")){
						// output JSON.
						resp.setContentType("text/javascript; charset=utf-8");
						resp.getWriter().println(json);
					}else if(type.equals("jsonp")){
						// output JSONP.
						resp.setContentType("text/javascript; charset=utf-8");
						resp.getWriter().println("apiresult(" + json + ");");
					}
				}else{
					// output JSON.
					resp.setContentType("text/javascript; charset=utf-8");
					resp.getWriter().println(json);
				}
				
			} catch (IOException e) {}
		}
		
	}
	
	public abstract Object apiRequest(
			HttpServletRequest req, 
			HttpServletResponse resp,
			UUID uuid,
			Map result);
}
