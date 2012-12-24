package org.qrone.api;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.*;

import org.qrone.api.util.APIServlet;

@SuppressWarnings("serial")
public class UUIDAPI extends APIServlet {
	
	@Override
	public Object apiRequest(HttpServletRequest req, HttpServletResponse resp,
			UUID uuid, Map result) {
		
		UUID uid = UUID.randomUUID();
		result.put("uuid", uid.toString());
		return result;
	}
	
}
