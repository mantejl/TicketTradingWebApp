package a4.models;

public class Purchase {
	
	private String eventID;
	private int userID;
	private int numTickets; 
	private float minPrice; 
	private float maxPrice; 
	private String buySell; 

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getEventID() {
		return eventID;
	}

	public void setEventID(String eventID) {
		this.eventID = eventID;
	}

	public int getNumTickets() {
		return numTickets;
	}

	public void setNumTickets(int numTickets) {
		this.numTickets = numTickets;
	}

	public float getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(float minPrice) {
		this.minPrice = minPrice;
	}

	public float getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(float maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getBuySell() {
		return buySell;
	}

	public void setBuySell(String buySell) {
		this.buySell = buySell;
	} 
}
