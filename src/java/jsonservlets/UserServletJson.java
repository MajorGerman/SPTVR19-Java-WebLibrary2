package jsonservlets;

import entity.Cover;
import entity.CoverFacade;
import entity.History;
import entity.HistoryFacade;
import entity.Person;
import entity.PersonFacade;
import entity.Product;
import entity.ProductFacade;
import entity.Role;
import entity.User;
import entity.UserFacade;
import entity.UserRoles;
import entity.UserRolesFacade;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import jsonbuilders.JsonProductBuilder;
import servlets.LoginServlet;
import tools.EncryptPassword;

@MultipartConfig()
@WebServlet(name = "UserServletJson", urlPatterns = {
    "/showProfileJson",
    "/buyProductJson",
    "/printEditPersonFormJson",
    "/editPersonJson"
})

public class UserServletJson extends HttpServlet {
    @EJB UserFacade userFacade;
    @EJB PersonFacade personFacade;
    @EJB UserRolesFacade userRolesFacade;
    @EJB CoverFacade coverFacade;
    @EJB ProductFacade productFacade;
    @EJB HistoryFacade historyFacade;
    
    @Inject EncryptPassword encryptPassword;
    
    public static final ResourceBundle pathToFile = ResourceBundle.getBundle("property.pathToJsp");
     
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
        String uploadFolder = ManagerServletJson.pathToFile.getString("dir");
        
        JsonObjectBuilder job = Json.createObjectBuilder();
        JsonReader jsonReader;
        
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
       
        
            switch (path) {
            case "/showProfileJson":
                user = (User)session.getAttribute("user");

                job = Json.createObjectBuilder();
                
                try {
                    String coverpath = user.getCover().getPath();
                
                    json = job.add("requestStatus", "true")
                            .add("name", user.getPerson().getName())
                            .add("surname", user.getPerson().getSurname())
                            .add("money", user.getPerson().getMoney())
                            .add("phone", user.getPerson().getPhone())
                            .add("login", user.getLogin())
                            .add("coverpath", coverpath)
                            .build()
                            .toString();
                
                } catch (Exception e) {
                    
                    json = job.add("requestStatus", "true")
                            .add("name", user.getPerson().getName())
                            .add("surname", user.getPerson().getSurname())
                            .add("money", user.getPerson().getMoney())
                            .add("phone", user.getPerson().getPhone())
                            .add("login", user.getLogin())
                            .add("coverpath", coverFacade.find(new Long(1)).getPath())
                            .build()
                            .toString();
                                    
                }
                break;
                
            case "/buyProductJson":              
                job = Json.createObjectBuilder();
                
                user = (User)session.getAttribute("user");
                String personId = user.getPerson().getId().toString();
                Person pers;
                pers = personFacade.find(Long.parseLong(personId));
                
                jsonReader = Json.createReader(request.getReader());
                JsonObject jsonObject = jsonReader.readObject();
                
                String productId = jsonObject.getString("productId", "");
                Product product = productFacade.find(Long.parseLong(productId));
                
                int buy_count = Integer.parseInt(jsonObject.getString("buy_count",""));
                
                List<Product> listProductsOr = productFacade.findAll();
                List<Product> listProducts;
                
                if (pers.getMoney() < product.getPrice()) {
                    json=job.add("requestStatus", "false")
                        .add("info", "У пользователя недостаточно денег!")
                        .build()
                       .toString();
                    break;   
                }
                
                if (buy_count > product.getCount()) {
                    json=job.add("requestStatus", "false")
                        .add("info", "На складе нет столько товаров!")
                        .build()
                       .toString();
                    break;                       
                } else {
                    product.setCount(product.getCount() - buy_count);               
                }
                
                if (product.getCount() == 0) {
                    product.setAccess(false);
                }
               
                pers.setMoney(pers.getMoney() - product.getPrice()*buy_count);
                personFacade.edit(pers);
                userFacade.edit(user);
                user = userFacade.findByLogin(user.getLogin());
                session.setAttribute("user", user);
                session.setAttribute("upuser", session.getAttribute("user").toString()); 

                History history = new History(product, pers, new GregorianCalendar().getTime(), null);
                historyFacade.create(history);
                productFacade.edit(product);
                json=job.add("requestStatus", "true")
                        .add("info", "Товар успешно куплен! ( " + product.getName() + " x" + buy_count + " )")
                        .build()
                       .toString();
                break;         
            case "/printEditPersonFormJson":
                job = Json.createObjectBuilder();             
                
                user = (User)session.getAttribute("user");  
                
                try {
                    String coverpath = user.getCover().getPath();              
                    json = job.add("requestStatus", "true")
                            .add("persId", user.getId())
                            .add("name", user.getPerson().getName())
                            .add("surname", user.getPerson().getSurname())
                            .add("money", user.getPerson().getMoney())
                            .add("phone", user.getPerson().getPhone())
                            .add("login", user.getLogin())
                            .add("coverpath", coverpath)
                            .build()
                            .toString();
                
                } catch (Exception e) {
                    
                    json = job.add("requestStatus", "true")
                            .add("persId", user.getId())
                            .add("name", user.getPerson().getName())
                            .add("surname", user.getPerson().getSurname())
                            .add("money", user.getPerson().getMoney())
                            .add("phone", user.getPerson().getPhone())
                            .add("login", user.getLogin())
                            .add("coverpath", coverFacade.find(new Long(1)).getPath())
                            .build()
                            .toString();
                                    
                }
                request.getReader().close();
                break;  
            case "/editPersonJson":
                job = Json.createObjectBuilder();    
                
                String id = request.getParameter("persId");
                
                String name = request.getParameter("name");
                String surname = request.getParameter("surname");
                String money = request.getParameter("money");
                String phone = request.getParameter("phone");
                String password = request.getParameter("password");
                
                String description = "userAvatar";

                ////
                Part filePart = request.getPart("file");
                        //.getParts()
                        //.stream()
                        //.filter(part -> "file".equals(part.getName()))
                        //.collect(Collectors.toList()
                //);
                Set<String> imagesExtension = new HashSet<>();
                imagesExtension.add("jpg");
                imagesExtension.add("png");
                imagesExtension.add("gif");
                
                String fileFolder = "";
                Cover cover = null;
                
                //for(Part filePart : fileParts){
                    String fileName = getFileName(filePart);
                    String fileExtension = fileName.substring(fileName.length()-3, fileName.length());

                    fileFolder = "images";

                    StringBuilder sbFullPathToFile = new StringBuilder();

                    sbFullPathToFile.append(uploadFolder)
                            .append(File.separator)
                            .append(fileFolder)
                            .append(File.separator)
                            .append(fileName);
                    File file = new File(sbFullPathToFile.toString());
                    file.mkdirs();
                    try(InputStream fileContent = filePart.getInputStream()){
                        Files.copy(fileContent, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }

                    cover = new Cover(description, sbFullPathToFile.toString());
                    coverFacade.create(cover);
                    
               // }
                ////
                
                if(cover == null){
                    json = job.add("requestStatus", "false")
                        .add("info", "Выберите аватар!")
                        .build()
                        .toString();
                    break;   
                }
                
                if(name == null || "".equals(name)
                  || surname == null || "".equals(surname)
                  || phone == null || "".equals(phone)
                  || password == null || "".equals(password)
                  || money == null || Integer.parseInt(money) < 0){
                    json = job.add("requestStatus", "false")
                        .add("info", "Заполните все поля!")
                        .build()
                       .toString();

                    break;   
                }
                
                pers = null;
                pers = personFacade.find(Long.parseLong(id));
                
                user = (User)session.getAttribute("user");
                user = userFacade.findByLogin(user.getLogin());
                
                if (pers != null){

                    pers.setName(name);
                    pers.setSurname(surname);
                    pers.setMoney(Integer.parseInt(money));
                    pers.setPhone(phone);
                    cover.setDescription(description);
                    
                    user.setCover(cover);
                    user.setPerson(pers);    
                    
                    String salt = encryptPassword.createSalt();
                    password = encryptPassword.createHash(password, salt);
                    user.setPassword(password);

                    personFacade.edit(pers);
                    userFacade.edit(user);
                    
                    session.setAttribute("user", user);
                    
                    json=job.add("requestStatus", "true")
                        .add("info", "Данные изменены успешно!")
                        .add("token", session.getId())
                        .add("role", userRolesFacade.getTopRoleForUser(user))
                        .build()
                        .toString();
                    response.setContentType("application/json"); 
                    break;    
                }
                
                
        }
        if(json != null && !"".equals(json)) {                    
            try (PrintWriter out = response.getWriter()) {
                out.println(json);           
            }
        }
        
    }
    
    private String getFileName(Part part){
        final String partHeader = part.getHeader("content-disposition");
        for (String content : part.getHeader("content-disposition").split(";")){
            if(content.trim().startsWith("filename")){
                return content
                        .substring(content.indexOf('=')+1)
                        .trim()
                        .replace("\"",""); 
            }
        }
        return null;
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
