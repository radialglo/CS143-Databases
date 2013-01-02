import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class P2B extends HttpServlet { //changed class name to P2B
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
		String title = "P2B Servlet"; //changed title to P2B servlet
		out.println("<title>" + title + "</title>");
		out.println("</head>");
		out.println("<body bgcolor=white>");
		out.println("<h1>" + title + "</h1>");
		String param = request.getParameter("sel");//parameter refers to sel in P2B.html
		if (param != null)
			out.println("You requested " + "<strong>" + param + "</strong>");
		out.println("</body>");
		out.println("</html>");
	}
}
