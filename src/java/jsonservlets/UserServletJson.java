package jsonservlets;

import entity.Person;
import entity.PersonFacade;
import entity.Role;
import entity.User;
import entity.UserFacade;
import entity.UserRoles;
import entity.UserRolesFacade;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tools.EncryptPassword;

@WebServlet(name = "UserServletJson", urlPatterns = {
    "/showProfileJson",
    "/buyProductJson"
})
public class UserServletJson extends HttpServlet {
    @EJB UserFacade userFacade;
    @EJB PersonFacade personFacade;
    @EJB UserRolesFacade userRolesFacade;
    
    @Inject EncryptPassword encryptPassword;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String json = "";
        String path = request.getServletPath();
        
        HttpSession session = request.getSession(false);
        if (session == null) {
            request.setAttribute("info","У вас нет прав! Войдите в систему!");
            request.getRequestDispatcher("/loginForm").forward(request, response);
            return;          
        }
        User user = (User)session.getAttribute("user");
        if (user == null) {
            request.setAttribute("info","У вас нет прав! Войдите в систему!");
            request.getRequestDispatcher("/loginForm").forward(request, response);
            return;                     
        }
        
        switch (path) {
            case "/showProfileJson":
                JsonReader jsonReader = Json.createReader(request.getReader());
                JsonObjectBuilder job = Json.createObjectBuilder();
                                     
                user = (User)session.getAttribute("user");

                job = Json.createObjectBuilder();
                
                json = job.add("requestStatus", "true")
                        .add("name", user.getPerson().getName())
                        .add("surname", user.getPerson().getSurname())
                        .add("money", user.getPerson().getMoney())
                        .add("phone", user.getPerson().getPhone())
                        .add("login", user.getLogin())
                        .build()
                        .toString();
  
                break;
        }
        if(json != null && !"".equals(json)) {                    
            try (PrintWriter out = response.getWriter()) {
                out.println(json);           
            }
        }
        
    }   
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
