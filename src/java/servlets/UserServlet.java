package servlets;

import entity.Cover;
import entity.CoverFacade;
import entity.History;
import entity.HistoryFacade;
import entity.Person;
import entity.PersonFacade;
import entity.Product;
import entity.ProductFacade;
import entity.RoleFacade;
import entity.User;
import entity.UserFacade;
import entity.UserRolesFacade;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "UserServlet", urlPatterns = {
    "/editPersonForm1",
    "/editPersonForm2",
    "/editPerson",
    "/buyProduct",
    "/showProfile"

})
public class UserServlet extends HttpServlet {
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
    @EJB 
    private HistoryFacade historyFacade;
    @EJB 
    private CoverFacade coverFacade;
     
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
        boolean isRole = userRolesFacade.isRole("customer", user);
        if (!isRole) {
            request.setAttribute("info","У вас нет прав!");
            request.getRequestDispatcher("/index").forward(request, response);
            return;               
        }
        String path = request.getServletPath();
        
        switch (path) {
            case "/editPersonForm1":
                isRole = userRolesFacade.isRole("admin", user);
                if (!isRole) {
                    String personId = user.getPerson().getId().toString();
                    Person pers = personFacade.find(Long.parseLong(personId));
                    request.setAttribute("pers", pers);
                    request.setAttribute("user", user);
                    request.getRequestDispatcher(LoginServlet.pathToJsp.getString("editPersonForm2")).forward(request, response);
                    break;
                }
                List<Person> listPersons = personFacade.findAll();                
                request.setAttribute("listPersons", listPersons);
                request.getRequestDispatcher(LoginServlet.pathToJsp.getString("editPersonForm1")).forward(request, response);
                break;
            case "/editPersonForm2":          
                isRole = userRolesFacade.isRole("admin", user);
                if (!isRole) {
                    String personId = user.getPerson().getId().toString();
                    Person pers = personFacade.find(Long.parseLong(personId));
                    request.setAttribute("pers", pers);
                    request.setAttribute("user", user);
                    request.getRequestDispatcher(LoginServlet.pathToJsp.getString("editPersonForm2")).forward(request, response);  
                    break;
                }
                    String personId = request.getParameter("personId");
                    Person pers = personFacade.find(Long.parseLong(personId));
                    request.setAttribute("pers", pers);
                    request.setAttribute("user", user);
                    request.getRequestDispatcher(LoginServlet.pathToJsp.getString("editPersonForm2")).forward(request, response);
                break;               
            case "/editPerson":
                personId = request.getParameter("persId");
                pers = personFacade.find(Long.parseLong(personId));
                request.setAttribute("person", pers);
                String name = request.getParameter("name");
                String surname = request.getParameter("surname");
                String phone = request.getParameter("phone");
                String money = request.getParameter("money");
                String password = request.getParameter("password");
                if("".equals(name) || name == null
                        || "".equals(surname) || surname == null
                        || "".equals(phone) || phone == null
                        || "".equals(money) || money == null
                        || "".equals(password) || password == null){
                    request.setAttribute("info","Заполните все поля формы");
                    request.setAttribute("name",name);
                    request.setAttribute("surname",surname);
                    request.setAttribute("phone",phone);
                    request.setAttribute("money",money); 
                    request.setAttribute("password", password); 
                    request.setAttribute("personId", pers.getId()); 
                    request.getRequestDispatcher(LoginServlet.pathToJsp.getString("editPersonForm2")).forward(request, response);
                    break; 
                } else if (Integer.parseInt(money) < 0) {
                    request.setAttribute("info","Не может быть денег меньше 0$!");        
                    request.getRequestDispatcher(LoginServlet.pathToJsp.getString("editPersonForm2")).forward(request, response);
                    break;    
                }   
                pers.setName(name);
                pers.setSurname(surname);
                pers.setPhone(phone);
                pers.setMoney(Integer.parseInt(money));
                user.setPassword(password); 
                personFacade.edit(pers);
                userFacade.edit(user);
                user = userFacade.findByLogin(user.getLogin());
                session.setAttribute("user", user);
                session.setAttribute("upuser", session.getAttribute("user").toString()); 
                request.setAttribute("personId", pers.getId());
                request.setAttribute("info","Данные успешно отредактированы: " + " (" + pers.getName() + " " + pers.getSurname() + ")");
                request.getRequestDispatcher(LoginServlet.pathToJsp.getString("index")).forward(request, response);
                break;                
            case "/buyProductForm":
                List<Product> listProductsOr = productFacade.findAll();
                if (listProductsOr.size() == 0) {
                    request.setAttribute("info","Товаров в данный момент нет!");
                    request.getRequestDispatcher(LoginServlet.pathToJsp.getString("index")).forward(request, response);      
                    break;
                }
                List<Product> listProducts = new ArrayList<>();
                if (listProductsOr.size() > 0) {
                    for (int i = 0; i < listProductsOr.size(); i++) {
                        if (listProductsOr.get(i).isAccess() == true) {
                            listProducts.add(listProductsOr.get(i));
                        }
                    }
                }
                List<History> listProducts2 = historyFacade.findBoughtProducts(user.getPerson());
                request.setAttribute("listProducts2", listProducts);
                request.setAttribute("listProducts", listProducts);               
                request.getRequestDispatcher(LoginServlet.pathToJsp.getString("buyProductForm")).forward(request, response);
                break;
            case "/buyProduct":
                personId = user.getPerson().getId().toString();
                pers = personFacade.find(Long.parseLong(personId));
                String productId = request.getParameter("productId");
                Product product = productFacade.find(Long.parseLong(productId));               
                if (pers.getMoney() < product.getPrice()) {
                    request.setAttribute("info","У покупателя недостаточно денег!");
                    listProductsOr = productFacade.findAll();
                    listProducts = new ArrayList<>();
                    if (listProductsOr.size() > 0) {
                        for (int i = 0; i < listProductsOr.size(); i++) {
                            if (listProductsOr.get(i).isAccess() == true) {
                                listProducts.add(listProductsOr.get(i));
                            }
                        }
                    }
                    request.setAttribute("listProducts", listProducts);
                    listPersons = personFacade.findAll();
                    request.getRequestDispatcher(LoginServlet.pathToJsp.getString("buyProductForm")).forward(request, response);
                    break; 
                }
                pers.setMoney(pers.getMoney() - product.getPrice());
                pers.getListProducts().add(product);
                personFacade.edit(pers);
                userFacade.edit(user);
                user = userFacade.findByLogin(user.getLogin());
                session.setAttribute("user", user);
                session.setAttribute("upuser", session.getAttribute("user").toString()); 
                product.setAccess(false);
                History history = new History(product, pers, new GregorianCalendar().getTime(), null);
                historyFacade.create(history);
                productFacade.edit(product);
                request.setAttribute("info", "Товар '" + product.getName() + "' успешно куплен покупателем " + pers.getName() + " " + pers.getSurname() + "!");
                request.getRequestDispatcher(LoginServlet.pathToJsp.getString("index")).forward(request, response);                
                break;
            case "/showProfile":
                user = (User)session.getAttribute("user");
                request.setAttribute("name", user.getPerson().getName());
                request.setAttribute("surname", user.getPerson().getSurname());
                request.setAttribute("money", user.getPerson().getMoney());
                request.setAttribute("phone", user.getPerson().getPhone());
                request.setAttribute("login", user.getLogin());                
                request.getRequestDispatcher(LoginServlet.pathToJsp.getString("showProfile")).forward(request, response);                              
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
