function getCookie(name) {
  // Split the cookies into an array
  const cookies = document.cookie.split(';');

  // Loop through the cookies to find the one with the specified name
  for (let i = 0; i < cookies.length; i++) {
    const cookie = cookies[i].trim();

    // Check if this cookie matches the specified name
    if (cookie.startsWith(`${name}=`)) {
      // Return the value of the cookie
      return cookie.substring(name.length + 1);
    }
  }
  // If the cookie isn't found, return null
  return null;
}

function rowClicked(value) {
  fetch('https://us-west2-csci201-376723.cloudfunctions.net/explore-events/eventDetail/' + value)
    .then(response => response.json())
    .then(data => displayEventInfo(data, value))
    .catch(error => console.error(error))
}

function displayEventInfo(data, eventID) {
  const starElement = document.getElementById("myStar");
  starElement.classList.remove("fa-star", "star-yellow");
  starElement.classList.add("fa-star-o");
  
  var eventDiv = document.getElementById("eventDetailsDiv");
  var eventName = document.getElementById("eventName");
  eventName.textContent = data.event.name;
  var dateValue = document.getElementById("dateValue");
  dateValue.textContent = data.date.localDate + " " + data.date.localTime;
  var venueValue = document.getElementById("venueValue");
  venueValue.textContent = data.event.venue;
  var priceValue = document.getElementById("priceValue");
  if (data.price.min < 0 || data.price.max < 0) {
    priceValue.textContent = "N/A"; 
  } else {
    priceValue.textContent = data.price.min + " - " + data.price.max; 
  }
  var moreInfoLink = document.getElementById("moreInfoLink");
  moreInfoLink.setAttribute('href', data.event.url);
  var eventIDInput = document.getElementById("eventID");
  eventIDInput.value = eventID;
  var minPriceInput = document.getElementById("minPrice");
  minPriceInput.value = data.price.min;
  var maxPriceInput = document.getElementById("maxPrice");
  maxPriceInput.value = data.price.max;

  if (data.price.min < 0 || data.price.max < 0) {
    document.getElementById("purchaseButton").disabled = true;
    document.getElementById("purchaseButton").style.backgroundColor = "#D3D3D3"; 
    document.getElementById("purchaseButton").style.borderTop = "5px solid #D3D3D3"; 
    document.getElementById("purchaseButton").style.borderLeft = "5px solid #D3D3D3"; 
    document.getElementById("purchaseButton").style.borderRight = "5px solid #D3D3D3"; 
    document.getElementById("purchaseButton").style.borderBottom = "10px solid #D3D3D3";
  } else {
    document.getElementById("purchaseButton").disabled = false;
    document.getElementById("purchaseButton").style.backgroundColor = "#fa771f"; 
    document.getElementById("purchaseButton").style.borderTop = "5px solid #fa771f"; 
    document.getElementById("purchaseButton").style.borderLeft = "5px solid #fa771f"; 
    document.getElementById("purchaseButton").style.borderRight = "5px solid #fa771f"; 
    document.getElementById("purchaseButton").style.borderBottom = "10px solid #f3682c";
  }
  eventDiv.style.display = "block";
}

function eventDetails() {
  var key = document.getElementById("key");
  var city = document.getElementById("loc");
  fetch('https://us-west2-csci201-376723.cloudfunctions.net/explore-events/search?keyword=' + key.value + '&city=' + city.value)
    .then(response => response.json())
    .then(data => displaySearchResults(data))
    .catch(error => console.error(error));
}



function displaySearchResults(data) {
  const tableBody = document.querySelector('#eventTable tbody');
  tableBody.innerHTML = "";

  for (const item of data) {
    const row = document.createElement('tr');
    const date = document.createElement('td');
    const iconCell = document.createElement('td');
    const icon = document.createElement('img');
    const event = document.createElement('td');
    const venue = document.createElement('td');

    //row.setAttribute('onclick', "rowClicked('" + item.eventId + "')");

    if (getCookie("login") != null) {
      if (getCookie("login") == 1) {
        row.setAttribute('onclick', "rowClicked('" + item.eventId + "')");
      }
    }

    date.textContent = item.localDate;
    icon.setAttribute('src', item.images);
    icon.setAttribute('alt', item.name);
    icon.style.width = "50px";
    icon.style.height = "50px";
    iconCell.appendChild(icon);
    event.textContent = item.name;
    venue.textContent = item.venue;

    row.appendChild(date);
    row.appendChild(iconCell);
    row.appendChild(event);
    row.appendChild(venue);

    tableBody.appendChild(row);
  }

  document.getElementById("eventTableDiv").style.display = "block";
  document.getElementById("eventDetailsDiv").style.display = "none";
}

function addFavorite() {
  let data = {};
  data.eventID = document.getElementById("eventID").value;
  fetch('http://localhost:8080/a4/FavoritesServlet', {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  })
    .then(response => {
      if (response.ok) {
        const starElement = document.getElementById("myStar");
        if (starElement.classList.contains("fa-star-o")) {
          starElement.classList.remove("fa-star-o");
          starElement.classList.add("fa-star", "star-yellow");
        } else {
          starElement.classList.remove("fa-star", "star-yellow");
          starElement.classList.add("fa-star-o");
        }
        alert("Added to favorites");
      }
    })
    .catch(error => {
      console.error("Error:", error);
    });
}

function purchase() {
  const quantity = document.getElementById("quantity").value;
  if (quantity < 1 || quantity == "") {
    alert("FAILED: Purchase not possible");
  } else {
    let data = {};
    data.eventID = document.getElementById("eventID").value;
    data.numTickets = quantity;
    data.minPrice = document.getElementById("minPrice").value;
    data.maxPrice = document.getElementById("maxPrice").value;
    fetch('http://localhost:8080/a4/WalletServlet', {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    })
      .then(response => {
        if (response.ok) {
          alert("Bought " + quantity + " " + document.getElementById("eventName").textContent + " tickets");
        } else {
          alert("FAILED: Purchase not possible");
        }
      })
      .catch(error => {
        console.error("Error:", error);
      });
  }
}

function displayMenu() {
  const indexTab = document.getElementById("index");
  const walletTab = document.getElementById("wallet");
  const loginTab = document.getElementById("login");
  const favoritesTab = document.getElementById("favorites");
  const logoutTab = document.getElementById("logout");
  if (getCookie("login") != null) {
    if (getCookie("login") == 1) {
      loginTab.style.display = "none";
      indexTab.style.display = "inline";
      walletTab.style.display = "inline";
      favoritesTab.style.display = "inline";
      logoutTab.style.display = "inline";
    }
  } else {
    loginTab.style.display = "inline";
    indexTab.style.display = "inline";
    walletTab.style.display = "none";
    favoritesTab.style.display = "none";
    logoutTab.style.display = "none";
  }
}
