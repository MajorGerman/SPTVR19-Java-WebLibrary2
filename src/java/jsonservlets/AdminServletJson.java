package jsonservlets;

import entity.RoleFacade;
import entity.User;
import entity.UserFacade;
import entity.UserRoles;
import entity.UserRolesFacade;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jsonbuilders.JsonProductBuilder;
import jsonbuilders.JsonUserBuilder;
import servlets.LoginServlet;


@WebServlet(name = "AdminServletJson", urlPatterns = {
    "/listPersonsJson",
    "/makeManagerJson",
    "/makeAdminJson",
    "/makeNobodyJson"


})
public class AdminServletJson extends HttpServlet {
    
    @EJB UserRolesFacade userRolesFacade;
    @EJB UserFacade userFacade;
    @EJB RoleFacade roleFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        request.setCharacterEncoding("UTF-8");
    
        String json = "";
        String path = request.getServletPath();
        
        JsonReader jsonReader = Json.createReader(request.getReader());
        JsonObjectBuilder job = Json.createObjectBuilder();
        
        HttpSession session = request.getSession(false); 
        
        if (session == null) {
            json = job.add("requestStatus", "false")
                .add("info", "У вас недостаточно прав! Войдите в систему!")
                .build()
                .toString();  
            return;          
        }
        User user = (User)session.getAttribute("user");
        if (user == null) {
            json = job.add("requestStatus", "false")
                .add("info", "У вас недостаточно прав! Войдите в систему!")
                .build()
                .toString();  
            return;                     
        }
        boolean isRole = userRolesFacade.isRole("admin", user);
        if (!isRole) {
            json = job.add("requestStatus", "false")
                .add("info", "У вас недостаточно прав! Войдите в систему!")
                .build()
                .toString();  
            return;               
        }
        
        switch (path) {
            case "/listPersonsJson":
                List<User> listUsers = userFacade.findAll();
                listUsers.remove(0);    
                
                System.out.println(listUsers);
                
                JsonArrayBuilder jab = Json.createArrayBuilder();
                listUsers.forEach((usr) ->  {
                    jab.add(new JsonUserBuilder().createJsonUser(usr));
                });
                json = jab.build().toString();
                break;
            case "/makeAdminJson":
                JsonObject jsonObject = jsonReader.readObject();
                String userId = jsonObject.getString("userId", "");
                user = userFacade.find(Long.parseLong(userId));
                isRole = userRolesFacade.isRole("admin", user);
                if (isRole) {
                    json = job.add("requestStatus", "false")
                        .add("info", "Пользовать уже является админом!")
                        .build()
                       .toString();
                    break;                    
                }
                if (user != null) {
                    UserRoles userRoles = new UserRoles(user, roleFacade.findByName("admin"));
                    userRolesFacade.create(userRoles);
                    json = job.add("requestStatus", "false")
                        .add("info", "Пользовать успешно сделан админом!")
                        .build()
                       .toString();
                    break;        
                } else {
                    json = job.add("requestStatus", "false")
                        .add("info", "Ошибка!")
                        .build()
                       .toString();
                    break;                      
                }
            case "/makeManagerJson":
                jsonObject = jsonReader.readObject();
                userId = jsonObject.getString("userId", "");
                user = userFacade.find(Long.parseLong(userId));
                isRole = userRolesFacade.isRole("manager", user);
                if (isRole) {
                    json = job.add("requestStatus", "false")
                        .add("info", "Пользовать уже является менеджером!")
                        .build()
                       .toString();
                    break;                    
                }
                if (user != null) {
                    UserRoles userRoles = new UserRoles(user, roleFacade.findByName("manager"));
                    userRolesFacade.create(userRoles);
                    json = job.add("requestStatus", "false")
                        .add("info", "Пользовать успешно сделан менеджером!")
                        .build()
                       .toString();
                    break;        
                } else {
                    json = job.add("requestStatus", "false")
                        .add("info", "Ошибка!")
                        .build()
                       .toString();
                    break;                      
                }
            case "/makeNobodyJson":
                jsonObject = jsonReader.readObject();
                userId = jsonObject.getString("userId", "");
                user = userFacade.find(Long.parseLong(userId));
                isRole = userRolesFacade.isRole("admin", user);
                boolean isRole2 = userRolesFacade.isRole("manager", user);
                if (!isRole && !isRole2) {
                    json = job.add("requestStatus", "false")
                        .add("info", "Пользовать уже никто!")
                        .build()
                       .toString();
                    break;                    
                }
                if (user != null) {
                    UserRoles userRoles = userRolesFacade.findByUserAndRoleName(user, "manager");
                    userRolesFacade.remove(userRoles);
                    userRoles = userRolesFacade.findByUserAndRoleName(user, "admin");
                    userRolesFacade.remove(userRoles);
                    json = job.add("requestStatus", "false")
                        .add("info", "Пользовать успешно сделан никем!")
                        .build()
                       .toString();
                    break;        
                } else {
                    json = job.add("requestStatus", "false")
                        .add("info", "Ошибка!")
                        .build()
                       .toString();
                    break;                      
                }
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

