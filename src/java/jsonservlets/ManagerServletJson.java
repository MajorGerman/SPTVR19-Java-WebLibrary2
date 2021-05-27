package jsonservlets;

import entity.Cover;
import entity.CoverFacade;
import entity.Product;
import entity.ProductFacade;
import entity.User;
import entity.UserRolesFacade;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
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

@MultipartConfig()
@WebServlet(name = "ManagerServletJson", urlPatterns = {
    "/addProductJson",
    "/printEditProductFormJson"

})
public class ManagerServletJson extends HttpServlet {
    
    @EJB UserRolesFacade userRolesFacade;
    @EJB CoverFacade coverFacade;
    @EJB ProductFacade productFacade;
    
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
        
        HttpSession session = request.getSession(false);
//        if (session == null) {
//            json = job.add("requestStatus", "false")
//                      .add("info", "У вас нет прав! Войдите в систему!")
//                        .build()
//                        .toString();
//            return;          
//        }
//        User user = (User)session.getAttribute("user");
//        if (user == null) {
//            request.setAttribute("info","У вас нет прав! Войдите в систему!");
//            
//            return;                     
//        }
//        if (!userRolesFacade.isRole("manager", user)) {
//            request.setAttribute("info","У вас нет прав! Войдите в систему!");
//            request.getRequestDispatcher("/loginForm").forward(request, response);
//            return;                     
//        }

    
        
        switch (path) {
            case "/addProductJson":
                JsonObjectBuilder job = Json.createObjectBuilder();
                         
                String name = request.getParameter("name");
                String price = request.getParameter("price");
                String tag = request.getParameter("tag");
                int count = Integer.parseInt(request.getParameter("count"));
                
                String description = request.getParameter("description");

                ////
                List<Part> fileParts = request
                        .getParts()
                        .stream()
                        .filter(part -> "file".equals(part.getName()))
                        .collect(Collectors.toList()
                );
                Set<String> imagesExtension = new HashSet<>();
                imagesExtension.add("jpg");
                imagesExtension.add("png");
                imagesExtension.add("gif");
                
                String fileFolder = "";
                Cover cover = null;
                
                for(Part filePart : fileParts){
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

                    cover = new Cover(description,sbFullPathToFile.toString());
                    coverFacade.create(cover);
                    
                }
                ////
                
                if(cover == null || description == null){
                    json = job.add("requestStatus", "false")
                        .add("info", "Выберите файл обложки и описание!")
                        .build()
                        .toString();
                    break;   
                }
                
                if(name == null || "".equals(name)
                  || price == null || Integer.parseInt(price) < 1
                  || tag == null ){
                    json=job.add("requestStatus", "false")
                        .add("info", "Заполните все поля!")
                        .build()
                       .toString();

                    break;   
                }
                
                Product product = null;
                product = new Product(name, Integer.parseInt(price), cover, count);
                List<String> tags = new ArrayList<>();
                tags.add(tag);
                product.setTags(tags);
                productFacade.create(product);
                
                JsonProductBuilder jbb = new JsonProductBuilder();
                JsonObject jsonProduct = jbb.createJsonProduct(product);
                
                json=job.add("requestStatus", "true")
                    .add("info", "Добавлен товар \'" + product.getName()+"\'.")
                    .add("book", jsonProduct.toString())
                    .build()
                    .toString();
                response.setContentType("application/json"); 
                break;
                
            case "/printEditProductFormJson":
                job = Json.createObjectBuilder();
                
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
                    json = job.add("requestStatus", "false")
                        .add("info", "Товаров сейчас нет!")
                        .build()
                        .toString();      
                    break;
                }
                
                JsonArrayBuilder jab = Json.createArrayBuilder();
                listProducts.forEach((prod) -> {
                    jab.add(new JsonProductBuilder().createJsonProduct(prod));
                });
                json = jab.build().toString();
                break;  
            
            case "/editProductJson":
                job = Json.createObjectBuilder();
                
                String id = request.getParameter("productId");

                name = request.getParameter("name");
                price = request.getParameter("price");
                tag = request.getParameter("tag");
                
                description = request.getParameter("description");

                ////
                fileParts = request
                        .getParts()
                        .stream()
                        .filter(part -> "file".equals(part.getName()))
                        .collect(Collectors.toList()
                );
                 imagesExtension = new HashSet<>();
                imagesExtension.add("jpg");
                imagesExtension.add("png");
                imagesExtension.add("gif");
                
                fileFolder = "";
                cover = null;
                
                for(Part filePart : fileParts){
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

                    cover = new Cover(fileName,sbFullPathToFile.toString());
                    coverFacade.create(cover);
                    
                }
                ////
                
                if(cover == null || description == null){
                    json = job.add("requestStatus", "false")
                        .add("info", "Выберите файл обложки и описание!")
                        .build()
                        .toString();
                    break;   
                }
                
                if(name == null || "".equals(name)
                  || price == null || Integer.parseInt(price) < 1
                  || tag == null ){
                    json=job.add("requestStatus", "false")
                        .add("info", "Заполните все поля!")
                        .build()
                       .toString();

                    break;   
                }
                
                product = null;
                product = productFacade.find(Integer.parseInt(id));
                
                tags = new ArrayList<>();
                tags.add(tag);
                product.setTags(tags);
                
                product.setName(name);
                product.setPrice(Integer.parseInt(price));
                cover.setDescription(description);
                product.setCover(cover);
                
                productFacade.edit(product);
                
                jbb = new JsonProductBuilder();
                jsonProduct = jbb.createJsonProduct(product);
                
                json=job.add("requestStatus", "true")
                    .add("info", "Изменен товар \'" + product.getName()+ "\'.")
                    .add("product", jsonProduct.toString())
                    .build()
                    .toString();
                response.setContentType("application/json"); 
                break;    
                
            case "deleteProductJson":               
                job = Json.createObjectBuilder();              
                id = request.getParameter("productId");    
                
                product = productFacade.find(Integer.parseInt(id));
                
                product.setAccess(false);
                
                productFacade.edit(product)
                        ;
                json=job.add("requestStatus", "true")
                    .add("info", "Удалён товар \'" + product.getName()+ "\'.")
                    .build()
                    .toString();
                response.setContentType("application/json"); 
                break;
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

