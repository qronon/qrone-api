package org.qrone.api;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class UUIDAPI extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		
		UUID uuid = UUID.randomUUID();
		resp.getWriter().println("{\"uuid\":\"" + uuid.toString() + "\"}");
	}
}
