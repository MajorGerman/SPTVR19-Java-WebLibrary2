package servlets;

import entity.CoverFacade;
import entity.Person;
import entity.PersonFacade;
import entity.Product;
import entity.ProductFacade;
import entity.RoleFacade;
import entity.User;
import entity.UserFacade;
import entity.UserRolesFacade;
import entity.Cover;
import entity.ProductFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet(name = "ManagerServlet", urlPatterns = {
    "/addProductForm",
    "/addProduct",
    "/editProductForm1",
    "/editProductForm2",
    "/editProduct",
    "/deleteProduct"


})
@MultipartConfig
public class ManagerServlet extends HttpServlet {
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
        boolean isRole = userRolesFacade.isRole("manager", user);
        if (!isRole) {
            request.setAttribute("info","Эта функция доступна только менеджерам!");
            request.getRequestDispatcher("/index").forward(request, response);
            return;               
        }
        String path = request.getServletPath();
        request.setAttribute("managerhidden", "xxxx");
        switch (path) {
            case "/addProductForm":
                request.getRequestDispatcher(LoginServlet.pathToJsp.getString("addProductForm")).forward(request, response);
                break;
            case "/addProduct":
                String name = request.getParameter("name");
                String price = request.getParameter("price");
                String tag = request.getParameter("tag");
                
                ////////////////
                String uploadFolder = "E:\\UploadFolder";
                List<Part> fileParts = request
                    .getParts()
                    .stream()
                    .filter(part -> "file".equals(part.getName()))
                    .collect(Collectors.toList());
                StringBuffer sb = new StringBuffer();
                for(Part filePart : fileParts){
                    sb.append(uploadFolder + File.separator + getFileName(filePart));
                    File file = new File(sb.toString());
                    try(InputStream fileContent = filePart.getInputStream()){
                        Files.copy(fileContent, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                String description = request.getParameter("description");
                Cover cover = new Cover(description, sb.toString());
                String coverId = request.getParameter("coverId");
                coverFacade.create(cover);
                ////////////////

                if("".equals(name) || name == null 
                        || "".equals(price) || price == null
                        || "".equals(description) || description == null
                        || cover == null || tag == null || "".equals(tag)){
                    request.setAttribute("info","Заполните все поля формы");
                    request.setAttribute("name",name);
                    request.setAttribute("price",price);
                    request.setAttribute("cover",cover);
                    request.setAttribute("tag", tag);
                    request.setAttribute("description",description);
                    request.getRequestDispatcher(LoginServlet.pathToJsp.getString("addProductForm")).forward(request, response);
                    break; 
                } else if (Integer.parseInt(price) < 1) {
                    request.setAttribute("info","Цена не может быть меньше 1$!");        
                    request.getRequestDispatcher(LoginServlet.pathToJsp.getString("addProductForm")).forward(request, response);
                    break;                     
                }
                Product product = new Product(name, Integer.parseInt(price), cover, 1);
                List<String> tags = new ArrayList<>();
                tags.add(tag);
                product.setTags(tags);
                productFacade.create(product);
                request.setAttribute("info","Товар успешно добавлен!");
                request.getRequestDispatcher(LoginServlet.pathToJsp.getString("index")).forward(request, response);
                break;
            case "/editProductForm1":
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
                request.setAttribute("listProducts", listProducts);
                request.getRequestDispatcher(LoginServlet.pathToJsp.getString("editProductForm1")).forward(request, response);
            case "/editProductForm2":                  
                String productId = request.getParameter("productId");
                product = productFacade.find(Long.parseLong(productId));
                request.setAttribute("product", product);
                request.getRequestDispatcher(LoginServlet.pathToJsp.getString("editProductForm2")).forward(request, response);
                break;               
            case "/editProduct":
                productId = request.getParameter("productId");
                name = request.getParameter("name");
                price = request.getParameter("price");
                tag = request.getParameter("tags");
                description = request.getParameter("description");
                
                //////////////////////////////////////

                uploadFolder = "E:\\UploadFolder";
                
                fileParts = request
                    .getParts()
                    .stream()
                    .filter(part -> "file".equals(part.getName()))
                    .collect(Collectors.toList());
                sb = new StringBuffer();
                for(Part filePart : fileParts){
                    sb.append(uploadFolder + File.separator + getFileName(filePart));
                    File file = new File(sb.toString());
                    try(InputStream fileContent = filePart.getInputStream()){
                        Files.copy(fileContent, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }                             
                ///////////////////////////////////////////////
                
                product = productFacade.find(Long.parseLong(productId));
                request.setAttribute("product", product);
                
                if("".equals(name) || name == null 
                        || "".equals(price) || price == null 
                        || "".equals(description) || description == null
                        || "".equals(tag) || tag == null
                        || "".equals(sb.toString()) || sb.toString() == null){
                    request.setAttribute("info","Заполните все поля формы");
                    request.setAttribute("name", name);
                    request.setAttribute("price", price);
                    request.setAttribute("tags", tag);
                    request.setAttribute("description", description);
                    request.setAttribute("productId", product.getId()); 
                    request.getRequestDispatcher(LoginServlet.pathToJsp.getString("editProductForm2")).forward(request, response);
                    break; 
                } else if (Integer.parseInt(price) < 1) {
                    request.setAttribute("info","Цена не может быть меньше 1$!");    
                    request.getRequestDispatcher(LoginServlet.pathToJsp.getString("editProductForm2")).forward(request, response);
                    break;    
                }   
                product.setName(name);
                product.setPrice(Integer.parseInt(price));
                product.getTags().clear();
                product.getTags().add(tag);
                cover = new Cover(description, sb.toString());
                product.setCover(cover);
                productFacade.edit(product);
                request.setAttribute("productId", product.getId());
                request.setAttribute("info","Товар успешно отредактирован: " + product.toString() );
                request.getRequestDispatcher(LoginServlet.pathToJsp.getString("index")).forward(request, response);
                break;    
            case "/deleteProduct":
                productId = request.getParameter("productId");
                product = productFacade.find(Long.parseLong(productId));
                product.setAccess(false);
                productFacade.edit(product);
                
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
                
                request.getRequestDispatcher(LoginServlet.pathToJsp.getString("editProductForm1")).forward(request, response);
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
}
