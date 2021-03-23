package servlets;

import entity.Cover;
import entity.History;
import entity.HistoryFacade;
import entity.Person;
import entity.PersonFacade;
import entity.Product;
import entity.ProductFacade;
import entity.Role;
import entity.RoleFacade;
import entity.User;
import entity.UserFacade;
import entity.UserRoles;
import entity.UserRolesFacade;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(name = "LoginServlet", loadOnStartup = 1, urlPatterns = {
    "/showLoginForm",
    "/login",
    "/logout",
    "/addPersonForm",
    "/addPerson",
    "/buyProductForm"
        
        })


public class LoginServlet extends HttpServlet {
    @EJB 
    private UserFacade userFacade;
    @EJB 
    private ProductFacade productFacade;
    @EJB 
    private PersonFacade personFacade;
    @EJB 
    private RoleFacade roleFacade;
    @EJB 
    private UserRolesFacade userRolesFacade;
    @EJB 
    private HistoryFacade historyFacade;
    
    public static final ResourceBundle pathToJsp = ResourceBundle.getBundle("property.pathToJsp");
    
    @Override
    public void init() throws ServletException {
        super.init(); 
        
        
        if (userFacade.findAll().size() > 0) return;
        
        //Create SuperAdmin
        
        Person pers = new Person("Georg", "Laabe", "+37258317253", 99999999);
        personFacade.create(pers);
        User user = new User("admin", "12345", pers);
        userFacade.create(user);
        
        Role role = new Role("admin");
        roleFacade.create(role);
        UserRoles userRoles = new UserRoles(user, role);
        userRolesFacade.create(userRoles);       
        
        role = new Role("manager");
        roleFacade.create(role);
        userRoles = new UserRoles(user, role);
        userRolesFacade.create(userRoles);
        
        role = new Role("customer");
        roleFacade.create(role);
        userRoles = new UserRoles(user, role);
        userRolesFacade.create(userRoles);
        
    }
        
    
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
        String path = request.getServletPath();
        
        switch (path) {
            case "/showLoginForm":
                request.getRequestDispatcher(LoginServlet.pathToJsp.getString("loginForm")).forward(request, response);
                break;
            case "/login":
                String login = request.getParameter("login");
                String password = request.getParameter("password");
                User user = userFacade.findByLogin(login);
                if (user == null) {
                    request.setAttribute("info", "Неправильный логин или пароль!"); 
                    request.getRequestDispatcher("/showLoginForm").forward(request, response);
                    break;
                }
                if (!password.equals(user.getPassword())) {
                    request.setAttribute("info", "Неправильный логин или пароль!"); 
                    request.getRequestDispatcher("/showLoginForm").forward(request, response);
                    break;                    
                }
                request.setAttribute("key", user.getId());
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                request.setAttribute("info", "Успешный вход!");
                session.setAttribute("hiddenlogout", "XXXXXX"); 
                session.setAttribute("hiddenreg", "hidden");
                session.setAttribute("hiddenlogin", "hidden"); 
                boolean isRole = userRolesFacade.isRole("admin", user);
                if (isRole) {
                    session.setAttribute("adminhidden", "xxxxx");
                }
                isRole = userRolesFacade.isRole("manager", user);
                if (isRole) {
                    session.setAttribute("managerhidden", "xxxxx");
                }
                session.setAttribute("userhidden", "xxxxx");
                session.setAttribute("upuser", session.getAttribute("user").toString());   
                request.getRequestDispatcher(LoginServlet.pathToJsp.getString("index")).forward(request, response);
                break;            
            case "/logout":
                session = request.getSession(false);
                if (session != null) {
                    request.setAttribute("info", "Успешный выход!");
                    request.setAttribute("hiddenlogout", ""); 
                    request.setAttribute("hiddenlogin", ""); 
                    session.setAttribute("userhidden", "");
                    session.invalidate();
                }

                request.getRequestDispatcher(LoginServlet.pathToJsp.getString("index")).forward(request, response);
                break;
            case "/addPersonForm":
                request.getRequestDispatcher(LoginServlet.pathToJsp.getString("addPersonForm")).forward(request, response);
                break;
            case "/addPerson":
                String name = request.getParameter("name");
                String surname = request.getParameter("surname");
                String phone = request.getParameter("phone");
                String money = request.getParameter("money");
                login = request.getParameter("login");
                password = request.getParameter("password");
                if("".equals(name) || name == null
                        || "".equals(surname) || surname == null
                        || "".equals(phone) || phone == null
                        || "".equals(money) || money == null
                        || "".equals(login) || login == null
                        || "".equals(password) || password == null ){
                    request.setAttribute("info","Заполните все поля формы!");
                    request.setAttribute("name",name);
                    request.setAttribute("surname",surname);
                    request.setAttribute("phone",phone);
                    request.setAttribute("money",money);     
                    request.setAttribute("login",login);     
                    request.setAttribute("password",password);     
                    request.getRequestDispatcher(LoginServlet.pathToJsp.getString("addPersonForm")).forward(request, response);
                    break; 
                } else if (Integer.parseInt(money) < 1) {
                    request.setAttribute("info","Не может быть денег меньше 0$!");        
                    request.getRequestDispatcher(LoginServlet.pathToJsp.getString("addPersonForm")).forward(request, response);
                    break;                     
                }
                Person pers = new Person(name, surname, phone, Integer.parseInt(money));
                personFacade.create(pers);
                user = new User(login, password, pers);
                userFacade.create(user);
                Role roleReader = roleFacade.findByName("customer");
                UserRoles userRoles = new UserRoles(user, roleReader);
                userRolesFacade.create(userRoles);  
                request.setAttribute("info","Добавлен пользователь: " + pers.toString());
                request.getRequestDispatcher(LoginServlet.pathToJsp.getString("index")).forward(request, response);
                break;
            case "/buyProductForm":
                List<Product> listProductsOr = productFacade.findAll();
                List<Product> listProducts = new ArrayList<>();
                if (listProductsOr.size() > 0) {
                    for (int i = 0; i < listProductsOr.size(); i++) {
                        if (listProductsOr.get(i).isAccess() == true) {
                            listProducts.add(listProductsOr.get(i));
                        }
                    }
                }
                
                if (listProducts.isEmpty()) {
                    request.setAttribute("info","Товаров в данный момент нет!");
                    request.getRequestDispatcher(LoginServlet.pathToJsp.getString("index")).forward(request, response);      
                    break;
                }
                
                request.setAttribute("listProducts2", listProducts);
                request.setAttribute("listProducts", listProducts);
                
                request.getRequestDispatcher(LoginServlet.pathToJsp.getString("buyProductForm")).forward(request, response);
                break;
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
