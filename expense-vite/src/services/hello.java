//Step 3: Servlet to Read Cookie (ReadCookieServlet.java)

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ReadCookieServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h2>Reading Cookies</h2>");

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie c : cookies) {
                out.println("<p><b>" + c.getName() + ":</b> " + c.getValue() + "</p>");
            }
        } else {
            out.println("<p>No cookies found.</p>");
        }

        out.println("</body></html>");
    }
}


