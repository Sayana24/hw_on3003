package com.taraskina.hw_on3003;

import com.taraskina.hw_on3003.DbManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        boolean nameIsNotValid = false;
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");

        String name = req.getParameter("name1");
        String password1 = req.getParameter("password1");
        String password2 = req.getParameter("password2");

        if (password1.equals(password2)){
            try(Connection connection = DbManager.createConnection()){
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select name from users");
                while (resultSet.next()) {
                    String nameUsers = resultSet.getString(1);
                    if (name.equals(nameUsers)){
                        out.println("Пользователь с таким именем уже существует <br>");
                        out.println("<a href=\"index.jsp\">Back to Index</a>");
                        nameIsNotValid = true;
                    }
                }

                MessageDigest md = null;
                md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(password1.getBytes());
                String passwordHash = Base64.getEncoder().encodeToString(hash);
                if (!nameIsNotValid){
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO users values (default, ?, ?)");

                preparedStatement.setString(1, name);
                preparedStatement.setString(2, passwordHash);
                preparedStatement.executeQuery();
                out.println("<a href=\"index.jsp\">Back to Index</a>");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        } else {
            out.println("Пароли не совпадают. Попробуйте еще раз");
            out.println("<a href=\"index.jsp\">Back to Index</a>");
        }
        out.println("The end");
        out.println("</body></html>");
        out.close();
    }
}

