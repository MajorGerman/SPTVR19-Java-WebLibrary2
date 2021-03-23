package servlets;

import entity.Person;
import entity.PersonFacade;
import entity.ProductFacade;
import entity.Role;
import entity.RoleFacade;
import entity.User;
import entity.UserFacade;
import entity.UserRoles;
import entity.UserRolesFacade;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Georg
 */
@WebServlet(name = "AdminServlet", urlPatterns = {
    "/listPersons",
    "/makeManager",
    "/makeAdmin",
    "/makeNobody"




})

public class AdminServlet extends HttpServlet {
    @EJB 
    private ProductFacade productFacade;
    @EJB 
    private PersonFacade personFacade;
    @EJB 
    private UserFacade userFacade;
    @EJB 
    private RoleFacade roleFacade;
    @EJB 
    private UserRolesFacade userRolesFacade;
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
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
        boolean isRole = userRolesFacade.isRole("admin", user);
        if (!isRole) {
            request.setAttribute("info","У вас нет прав!");
            request.getRequestDispatcher("/index").forward(request, response);
            return;               
        }
        String path = request.getServletPath();
        
        switch (path) {
            case "/listPersons":
                List<User> listUsers = userFacade.findAll();
                listUsers.remove(0);
                request.setAttribute("listUsers", listUsers);
                request.getRequestDispatcher(LoginServlet.pathToJsp.getString("listPersons")).forward(request, response);
                break;
            case "/makeManager":
                String userId = request.getParameter("userId");
                user = userFacade.find(Long.parseLong(userId));
                isRole = userRolesFacade.isRole("manager", user);
                if (isRole) {
                    listUsers = userFacade.findAll();
                    listUsers.remove(0);
                    request.setAttribute("listUsers", listUsers);
                    request.setAttribute("info", "Пользователь уже является менеджером!");
                    request.getRequestDispatcher(LoginServlet.pathToJsp.getString("listPersons")).forward(request, response);
                    break;                    
                }
                if (user != null) {
                UserRoles userRoles = new UserRoles(user, roleFacade.findByName("manager"));
                userRolesFacade.create(userRoles);
                listUsers = userFacade.findAll();
                listUsers.remove(0);
                request.setAttribute("listUsers", listUsers);
                request.setAttribute("info", "Пользователь успешно сделан менеджером!");
                request.getRequestDispatcher(LoginServlet.pathToJsp.getString("listPersons")).forward(request, response);
                break;
                }
            case "/makeAdmin":
                userId = request.getParameter("userId");
                user = userFacade.find(Long.parseLong(userId));
                isRole = userRolesFacade.isRole("admin", user);
                if (isRole) {
                    listUsers = userFacade.findAll();
                    listUsers.remove(0);
                    request.setAttribute("listUsers", listUsers);
                    request.setAttribute("info", "Пользователь уже является админом!");
                    request.getRequestDispatcher(LoginServlet.pathToJsp.getString("listPersons")).forward(request, response);
                    break;                    
                }
                if (user != null) {
                UserRoles userRoles = new UserRoles(user, roleFacade.findByName("admin"));
                userRolesFacade.create(userRoles);
                listUsers = userFacade.findAll();
                listUsers.remove(0);
                request.setAttribute("listUsers", listUsers);
                request.setAttribute("info", "Пользователь успешно сделан админом!");
                request.getRequestDispatcher(LoginServlet.pathToJsp.getString("listPersons")).forward(request, response);
                break;
                }
            case "/makeNobody":
                userId = request.getParameter("userId");
                user = userFacade.find(Long.parseLong(userId));
                isRole = userRolesFacade.isRole("admin", user);
                boolean isRole2 = userRolesFacade.isRole("manager", user);
                if (!isRole && !isRole2) {
                    listUsers = userFacade.findAll();
                    listUsers.remove(0);
                    request.setAttribute("listUsers", listUsers);
                    request.setAttribute("info", "Пользователь и так не имеет прав!");
                    request.getRequestDispatcher(LoginServlet.pathToJsp.getString("listPersons")).forward(request, response);
                    break;                    
                }
                if (user != null) {
                    UserRoles userRoles = userRolesFacade.findByUserAndRoleName(user, "manager");
                    userRolesFacade.remove(userRoles);
                    userRoles = userRolesFacade.findByUserAndRoleName(user, "admin");
                    userRolesFacade.remove(userRoles);
                    listUsers = userFacade.findAll();
                    listUsers.remove(0);
                    request.setAttribute("listUsers", listUsers);
                    request.setAttribute("info", "Пользователь успешно разжалован!");
                    request.getRequestDispatcher(LoginServlet.pathToJsp.getString("listPersons")).forward(request, response);
                    break;
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
