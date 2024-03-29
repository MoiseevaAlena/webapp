package ru.itpark.servlet;

import ru.itpark.service.AutoService;
import ru.itpark.service.FileService;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


public class CatalogServlet extends HttpServlet {
    private AutoService autoService;
    private FileService fileService;

    @Override
    public void init() throws ServletException {
        InitialContext context = null;
        try {
            context = new InitialContext();
            autoService = (AutoService) context.lookup("java:/comp/env/bean/auto-service");
            fileService = (FileService) context.lookup("java:/comp/env/bean/file-service");
        } catch (NamingException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("items", autoService.getAll());
            req.getRequestDispatcher("/WEB-INF/catalog.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("зашли в метод Post в CatalogServlet");
        try {
            req.setCharacterEncoding("UTF-8");
            var name = req.getParameter("name");
            var description = req.getParameter("description");
            var part = req.getPart("image");

            System.out.println("Получили данные, name = " + name + "description=" + description);

            var image = fileService.writeFile(part);

            System.out.println("начинаем вызывать метож для записи в таблицу");
            autoService.create(name, description, image);
            System.out.println("Записали данные в таблицу");
            resp.sendRedirect(String.join("/", req.getContextPath(), req.getServletPath()));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}
