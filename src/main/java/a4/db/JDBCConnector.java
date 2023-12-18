package a4.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import a4.models.FavoriteEvent;
import a4.models.Purchase;
import a4.models.User;
import a4.models.WalletTicket;

public class JDBCConnector {

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int loginUser(User user) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/ticket_db?user=root&password=root");
			ps = conn.prepareStatement("SELECT user_id FROM User WHERE username = ? AND password = ?");
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("user_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static int registerUser(User user) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int userID = 0;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/ticket_db?user=root&password=root");
			ps = conn.prepareStatement("INSERT INTO User (username, password, email, balance) VALUES (?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getEmail());
			ps.setDouble(4, user.getBalance());

			int rowAffected = ps.executeUpdate();
			if (rowAffected == 1) {
				rs = ps.getGeneratedKeys();
				if (rs.next()) {
					userID = rs.getInt(1);
				}
			}

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return userID;
	}

	public static boolean addFavoriteEvent(FavoriteEvent favoriteEvent) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/ticket_db?user=root&password=root");
			ps = conn.prepareStatement("INSERT INTO Favorites (user_id, event_id) VALUES (?, ?)");
			ps.setInt(1, favoriteEvent.getUserID());
			ps.setString(2, favoriteEvent.getEventID());

			int rowAffected = ps.executeUpdate();
			if (rowAffected == 1) {
				return true;
			}
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return false;
	}

	public static boolean purchaseTicket(Purchase purchaseTicket) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/ticket_db?user=root&password=root");
			// check if user balance is valid
			ps = conn.prepareStatement("SELECT balance FROM User WHERE user_id = ?");
			ps.setInt(1, purchaseTicket.getUserID());
			rs = ps.executeQuery();
			float balance = 0;
			if (rs.next()) {
				balance = rs.getFloat("balance");
			}
			

			float cost = purchaseTicket.getMinPrice() * purchaseTicket.getNumTickets();
			if (balance > cost) {
				ps = conn.prepareStatement(
						"INSERT INTO Wallet (user_id, event_id, num_tickets, cost_tickets) VALUES (?, ?, ?, ?)");
				ps.setInt(1, purchaseTicket.getUserID());
				ps.setString(2, purchaseTicket.getEventID());
				ps.setInt(3, purchaseTicket.getNumTickets());
				ps.setFloat(4, cost);
				int rowAffected = ps.executeUpdate();
				if (rowAffected == 1) {
					ps = conn.prepareStatement("UPDATE User SET balance = ? WHERE user_id = ?");
					ps.setDouble(1, balance - cost);
					ps.setInt(2, purchaseTicket.getUserID());
					rowAffected = ps.executeUpdate();
					if (rowAffected == 1) {
						return true;
					}
				}
			}
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return false;
	}

	public static List<String> favoritesList(int userID) {
		List<String> eventIDs = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/ticket_db?user=root&password=root");
			ps = conn.prepareStatement("SELECT event_id FROM Favorites WHERE user_id = ?");
			ps.setInt(1, userID);
			rs = ps.executeQuery();
			while (rs.next()) {
				eventIDs.add(rs.getString("event_id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventIDs;
	}

	public static boolean deleteFavoriteEvent(FavoriteEvent favoriteEvent) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/ticket_db?user=root&password=root");
			ps = conn.prepareStatement("DELETE FROM Favorites WHERE user_id = ? AND event_id = ?");
			ps.setInt(1, favoriteEvent.getUserID());
			ps.setString(2, favoriteEvent.getEventID());

			int rowAffected = ps.executeUpdate();
			if (rowAffected == 1) {
				return true;
			}
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return false;
	}

	public static float getUserBalance(int userID) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		float balance = 0;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/ticket_db?user=root&password=root");
			ps = conn.prepareStatement("SELECT balance FROM User WHERE user_id = ?");
			ps.setInt(1, userID);

			rs = ps.executeQuery();

			if (rs.next()) {
				balance = rs.getFloat("balance");
			}

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return balance;
	}

	public static float getUserTotalCostTickets(int userID) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		float totalCost = 0;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/ticket_db?user=root&password=root");
			ps = conn.prepareStatement("SELECT SUM(cost_tickets) total_cost FROM Wallet WHERE user_id = ?");
			ps.setInt(1, userID);

			rs = ps.executeQuery();

			if (rs.next()) {
				totalCost = rs.getFloat("total_cost");
			}

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return totalCost;
	}

	public static List<WalletTicket> getWalletList(int userID) {
		List<WalletTicket> walletList = new ArrayList<WalletTicket>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/ticket_db?user=root&password=root");
			ps = conn.prepareStatement("SELECT event_id, num_tickets, cost_tickets FROM Wallet WHERE user_id = ?");
			ps.setInt(1, userID);
			rs = ps.executeQuery();
			while (rs.next()) {
				WalletTicket wt = new WalletTicket();
				wt.eventID = rs.getString("event_id");
				wt.quantity = rs.getInt("num_tickets");
				wt.totalCost = rs.getFloat("cost_tickets");
				wt.avgCost = wt.totalCost / wt.quantity;
				walletList.add(wt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return walletList;
	}

	public static boolean sellTicket(Purchase purchaseTicket) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/ticket_db?user=root&password=root");
			// check if user balance is valid
			ps = conn.prepareStatement("SELECT balance FROM User WHERE user_id = ?");
			ps.setInt(1, purchaseTicket.getUserID());
			rs = ps.executeQuery();
			float balance = 0;
			if (rs.next()) {
				balance = rs.getFloat("balance");
			}

			ps = conn.prepareStatement(
					"SELECT num_tickets, cost_tickets FROM Wallet WHERE user_id = ? AND event_id = ?");
			ps.setInt(1, purchaseTicket.getUserID());
			ps.setString(2, purchaseTicket.getEventID());
			rs = ps.executeQuery();
			float cost_tickets = 0;
			int num_tickets = 0;
			if (rs.next()) {
				cost_tickets = rs.getFloat("cost_tickets");
				num_tickets = rs.getInt("num_tickets");
			}

			int rowAffected = 0;
			if (num_tickets >= purchaseTicket.getNumTickets()) {
				int newNumTickets = num_tickets - purchaseTicket.getNumTickets();
				float sale = purchaseTicket.getNumTickets() * purchaseTicket.getMaxPrice();
				float newBalance = balance + sale;
				float newCostTickets = (cost_tickets / num_tickets) * newNumTickets;
				ps = conn.prepareStatement("UPDATE User SET balance = ? WHERE user_id = ?");
				ps.setDouble(1, newBalance);
				ps.setInt(2, purchaseTicket.getUserID());
				rowAffected = ps.executeUpdate();
				if (rowAffected != 1) {
					return false;
				}
				ps = conn.prepareStatement(
						"UPDATE Wallet SET num_tickets = ?, cost_tickets = ? WHERE user_id = ? AND event_id = ?");
				ps.setInt(1, newNumTickets);
				ps.setFloat(2, newCostTickets);
				ps.setInt(3, purchaseTicket.getUserID());
				ps.setString(4, purchaseTicket.getEventID());
				rowAffected = ps.executeUpdate();
				if (rowAffected != 1) {
					return false;
				}

				if (newNumTickets == 0) {
					ps = conn.prepareStatement("DELETE FROM Wallet WHERE user_id = ? AND event_id = ?");
					ps.setInt(1, purchaseTicket.getUserID());
					ps.setString(2, purchaseTicket.getEventID());
					rowAffected = ps.executeUpdate();
					if (rowAffected != 1) {
						return false;
					}
				}
				return true;
			}
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return false;
	}
	
	public static boolean isEmailUserExists(String username, String email) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/ticket_db?user=root&password=root");
			ps = conn.prepareStatement("SELECT * FROM User WHERE LOWER(username) = ? OR LOWER(email) = ?");
			ps.setString(1, username.toLowerCase());
			ps.setString(2, email.toLowerCase());
			rs = ps.executeQuery();
			if (rs.next()) {
				return true; 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false; 
	}
}
