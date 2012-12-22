package org.qrone.api;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class StatusAPI extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("{\"status\":\"OK\"}");
	}
}
