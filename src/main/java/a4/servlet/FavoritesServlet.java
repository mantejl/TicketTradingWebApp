package a4.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import a4.db.JDBCConnector;
import a4.models.Favorite;
import a4.models.FavoriteEvent;
import a4.models.User;
import a4.util.GetEventDetails;

/**
 * Servlet implementation class FavoritesServlet
 */
@WebServlet("/FavoritesServlet")
public class FavoritesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FavoritesServlet() {
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
		try {
			PrintWriter pw = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			String userID = null;
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if ("userID".equals(cookie.getName())) {
					userID = cookie.getValue();
				}
			}

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			List<String> eventIDs = JDBCConnector.favoritesList(Integer.parseInt(userID));
			List<Favorite> favoriteEvents = new ArrayList<Favorite>();

			for (String eventID : eventIDs) {
				Favorite favoriteEvent = GetEventDetails.getFavoriteEvent(eventID);
				favoriteEvent.event.setEventID(eventID);
				favoriteEvents.add(favoriteEvent);
			}

			Gson gson = new Gson();
			String responseFavoriteList = gson.toJson(favoriteEvents);

			pw.write(responseFavoriteList);
			pw.flush();

			response.setStatus(HttpServletResponse.SC_OK);

//			if (event) {
//				response.setStatus(HttpServletResponse.SC_OK);
//			} else {
//				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			String userID = null;
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if ("userID".equals(cookie.getName())) {
					userID = cookie.getValue();
				}
			}

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			FavoriteEvent event = new Gson().fromJson(request.getReader(), FavoriteEvent.class);
			if (userID != null) {
				event.setUserID(Integer.parseInt(userID));
			}

			boolean favoriteEvent = JDBCConnector.addFavoriteEvent(event);

			if (favoriteEvent) {
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			String userID = null;
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if ("userID".equals(cookie.getName())) {
					userID = cookie.getValue();
				}
			}

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			FavoriteEvent event = new Gson().fromJson(request.getReader(), FavoriteEvent.class);
			if (userID != null) {
				event.setUserID(Integer.parseInt(userID));
			}

			boolean favoriteEvent = JDBCConnector.deleteFavoriteEvent(event);

			if (favoriteEvent) {
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
