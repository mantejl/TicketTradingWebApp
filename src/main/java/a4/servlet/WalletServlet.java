package a4.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import a4.models.Purchase;
import a4.models.Wallet;
import a4.models.WalletTicket;
import a4.util.GetEventDetails;

/**
 * Servlet implementation class WalletServlet
 */
@WebServlet("/WalletServlet")
public class WalletServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WalletServlet() {
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
			String userID = null;
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if ("userID".equals(cookie.getName())) {
					userID = cookie.getValue();
				}
			}
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			Wallet userWallet = new Wallet(); 
			
			float balance = JDBCConnector.getUserBalance(Integer.parseInt(userID));
			userWallet.cashBalance = balance; 
			
			float totalCost = JDBCConnector.getUserTotalCostTickets(Integer.parseInt(userID));
			userWallet.accountValue = balance + totalCost; 
			
			List<WalletTicket> walletList = JDBCConnector.getWalletList(Integer.parseInt(userID));
			
			
			for (WalletTicket walletTicket : walletList) {
				Favorite favoriteEvent = GetEventDetails.getFavoriteEvent(walletTicket.eventID);
				walletTicket.change = (float) (favoriteEvent.price.getMax() - favoriteEvent.price.getMin());
				walletTicket.currentPrice = (float) favoriteEvent.price.getMax(); 
				walletTicket.marketValue = walletTicket.currentPrice * walletTicket.quantity; 
				walletTicket.eventName = favoriteEvent.event.getName(); 
				walletTicket.minPrice = (float) favoriteEvent.price.getMin(); 
			}
			
			userWallet.tickets = walletList; 
			
			Gson gson = new Gson();
			String userWalletResponse = gson.toJson(userWallet);
			
			pw.write(userWalletResponse);
			pw.flush();
			
			response.setStatus(HttpServletResponse.SC_OK);
			
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
			PrintWriter pw = response.getWriter();
			String userID = null;
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if ("userID".equals(cookie.getName())) {
					userID = cookie.getValue();
				}
			}

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			Purchase purchaseTicket = new Gson().fromJson(request.getReader(), Purchase.class);
			if (userID != null) {
				purchaseTicket.setUserID(Integer.parseInt(userID));
			}
			
			boolean ticketPurchase = false; 
			if (purchaseTicket.getBuySell() == null || "Buy".equals(purchaseTicket.getBuySell())) {
				ticketPurchase = JDBCConnector.purchaseTicket(purchaseTicket);
			} else {
				ticketPurchase = JDBCConnector.sellTicket(purchaseTicket);
			}
			
		
			if (ticketPurchase) {
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
