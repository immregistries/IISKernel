package org.immregistries.iis.kernal.servlet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.immregistries.iis.kernal.model.OrgAccess;

@SuppressWarnings("serial")
public class VXUDownloadServlet extends VXUDownloadFormServlet {


  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    doGet(req, resp);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    HttpSession session = req.getSession(true);

    resp.setContentType("text/plain");
    PrintWriter out = new PrintWriter(resp.getOutputStream());
    Session dataSession = PopServlet.getDataSession();
    OrgAccess orgAccess = (OrgAccess) session.getAttribute("orgAccess");
    if (orgAccess == null) {
      RequestDispatcher dispatcher = req.getRequestDispatcher("home");
      dispatcher.forward(req, resp);
      return;
    }

    try {
      VXUDownloadGenerator generator = (VXUDownloadGenerator) session.getAttribute(CACHED_GENERATOR);
      if (generator.isFileReady()) {
        FileInputStream fileInputStream = new FileInputStream(generator.getFile());
        BufferedReader in = new BufferedReader(new InputStreamReader(fileInputStream));
        String line;
        while ((line = in.readLine()) != null) {
          out.print(line);
          out.print("\r");
        }
        in.close();
      }

    } catch (

    Exception e) {
      System.err.println("Unable to render page: " + e.getMessage());
      e.printStackTrace(System.err);
    } finally {
      dataSession.close();
    }
    out.flush();
    out.close();
  }


}
