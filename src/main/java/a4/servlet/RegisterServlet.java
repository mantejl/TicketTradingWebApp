package a4.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import a4.db.JDBCConnector;
import a4.models.RegisterResponse;
import a4.models.User;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			PrintWriter pw = response.getWriter(); 
			response.setContentType("application/json"); 
			response.setCharacterEncoding("UTF-8");
			
			User user = new Gson().fromJson(request.getReader(), User.class); 
			
			String password = user.getPassword(); 
			String username = user.getUsername();
			String email = user.getEmail(); 
			double balance = user.getBalance(); 
			
			Gson gson = new Gson(); 
			
			if (username == null || username.isBlank() || password == null || password.isBlank() || email == null || email.isBlank() ) {
				response.setStatus(HttpServletResponse.SC_OK);
				RegisterResponse err = new RegisterResponse(); 
				err.errorMessage = "User info is missing"; 
				err.userID = 0; 
				pw.write(gson.toJson(err));
				pw.flush();
				return; 
			}
			
			boolean userExists = JDBCConnector.isEmailUserExists(username, email); 
			
			if (userExists) {
				response.setStatus(HttpServletResponse.SC_OK);
				RegisterResponse err = new RegisterResponse(); 
				err.errorMessage = "User already exists."; 
				err.userID = 0; 
				pw.write(gson.toJson(err));
				pw.flush();
				return; 
			}
			
			user.setBalance(3000);
			int userID = JDBCConnector.registerUser(user); 
						
			if (userID == 0) {
				response.setStatus(HttpServletResponse.SC_OK);
				RegisterResponse err = new RegisterResponse(); 
				err.errorMessage =  "User not registered properly";
				err.userID = 0; 
				pw.write(gson.toJson(err));
				pw.flush();
			} else {
				response.setStatus(HttpServletResponse.SC_OK);
				RegisterResponse err = new RegisterResponse();
				response.addCookie(new Cookie("login", "1"));
				response.addCookie(new Cookie("userID", Integer.toString(userID)));
				err.userID = (userID); 
				pw.write(gson.toJson(err));
				pw.flush();
			}
			
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			
		}
	}

}
