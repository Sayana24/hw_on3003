package com.taraskina.hw_on3003;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;

@WebServlet(name = "CheckServlet", value = "/CheckServlet")
public class CheckServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");

        ArrayList<Cookie> cookies = new ArrayList<>();
        boolean find = false;

        String myStr = "";
        ArrayList<String> interSent = new ArrayList<>();

        String name = req.getParameter("name");
        String password = req.getParameter("password");
        MessageDigest md = null;

        try(Connection connection = DbManager.createConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select name, password from users")){
            md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            String passwordHash = Base64.getEncoder().encodeToString(hash);


            while (resultSet.next()) {
                String nameUsers = resultSet.getString(1);
                String passwordUsers = resultSet.getString(2);
                if (name.equals(nameUsers) && passwordHash.equals(passwordUsers)){
                    find = true;
                    HttpSession session= req.getSession();
                    String entryDate= "";
                    session.setAttribute(entryDate, session.getCreationTime());
                    session.setAttribute(name, nameUsers);
                    session.setAttribute(password, passwordUsers);

                    try {
                        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\sayan\\IdeaProjects\\hw_on3003\\src\\main\\resources\\text.txt"));
                        String line = reader.readLine();
                        while (line != null) {
                            char[] charArray = line.toCharArray();
                            for (char c : charArray) {
                                if (c == '?'){
                                    interSent.add(myStr+'?');
                                    myStr = "";
                                } else if ( c == '.' || c =='!'){
                                    myStr = "";
                                } else if ( c == ' '){
                                    myStr = myStr + "_";
                                } else {
                                    myStr = myStr + c;
                                }
                            }
                            line = reader.readLine();
                        }
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    out.println("Количество вопросительных предложений:  " + interSent.size());
                    out.println("<br>");
                    for (int i = 0; i < interSent.size(); i++) {
                        Cookie cookie = new Cookie((String)("cookie" + i), interSent.get(i));
                        cookies.add(cookie);
                    }
                    for (int i = 0; i < cookies.size(); i++) {
                        out.println("<h3>" + "Name:  " + cookies.get(i).getName() +
                                "     " + "Value:  " + cookies.get(i).getValue() + "</h3>");
                    }

                    String duration = "";
                    session.setAttribute(duration, (new Date().getTime() - session.getCreationTime()));

                    out.println("name   " + session.getAttribute(name)+ "<br>");
                    out.println("password   " + session.getAttribute(password)+ "<br>");
                    out.println("entryDate   " + session.getAttribute(entryDate)+ "<br>");
                    out.println("duration   " + session.getAttribute(duration) + "<br>");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        if (!find) {
            out.println("Данные не найдены. Зарегистрируйтесь, пожалуйста");
            out.println("<a href=\"index.jsp\">Back to Index</a>");
            out.println("The end");
        }
        out.println("</body></html>");
        out.close();
    }
}
