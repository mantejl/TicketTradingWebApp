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
import a4.models.User;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			PrintWriter pw = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			User user = new Gson().fromJson(request.getReader(), User.class);

			String password = user.getPassword();
			String username = user.getUsername();

			Gson gson = new Gson();

			if (username == null || username.isBlank() || password == null || password.isBlank()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				String error = "User info is missing";
				pw.write(gson.toJson(error));
				pw.flush();
			}

			int userID = JDBCConnector.loginUser(user);
			
			if (userID > 0) {
				response.addCookie(new Cookie("login", "1"));
				response.addCookie(new Cookie("userID", Integer.toString(userID)));
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.addCookie(new Cookie("login", "0"));
				response.addCookie(new Cookie("userID", ""));
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
