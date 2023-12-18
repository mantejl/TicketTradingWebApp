function favEventDetails() {
  displayMenu(); 
  fetch('http://localhost:8080/a4/FavoritesServlet')
    .then(response => response.json())
    .then(data => displayFavoriteEvents(data))
    .catch(error => console.error(error));
}


function displayFavoriteEvents(data) {
  if (data.length == 0) {
    alert("You have no favorites!"); 
  }
  for (const item of data) {
    const card = document.createElement('div');
    card.id = item.event.eventID;
    card.setAttribute('onclick', "cardClicked('" + item.event.eventID + "')");
    card.style.marginTop = "10px";
    card.style.marginBottom = "10px";
    card.style.marginLeft = "100px";
    card.style.width = "1000px";
    card.style.border = "1px solid #ccc";
    card.style.padding = "10px"; 

    const name = document.createElement('label');
    name.textContent = item.event.name;
    name.style.marginBottom = "30px"; 
    card.appendChild(name);
    

    const x = document.createElement('button');
    x.innerText = 'x';
    x.style.float = "right"; 
    x.style.fontSize = "xx-small"; 
    x.style.backgroundColor = "transparent"; 
    x.onclick = function () {
      document.getElementById("favEventDetailsDiv").style.display = "none";
    };
    x.setAttribute('onclick', "deleteFav('" + item.event.eventID + "')");
    card.appendChild(x);

    const brOne = document.createElement('br'); 
    card.appendChild(brOne);
    

    const date = document.createElement('label');
    date.textContent = item.date.localDate + " " + item.date.localTime;
    date.style.fontSize = "small"; 
    card.appendChild(date);

    const priceLabel = document.createElement('label'); 
    priceLabel.style.float = "right"; 
    priceLabel.style.fontSize = "x-large"; 
    if (item.price.min < 0 || item.price.max < 0) {
      priceLabel.textContent = "N/A"; 
    } else {
      priceLabel.textContent = item.price.min + " - " + item.price.max; 
    }
    
    card.appendChild(priceLabel);

    document.getElementById("favEvents").appendChild(card);
  }
}

function deleteFav(delEventID) {
  let data = {};
  data.eventID = delEventID;
  fetch('http://localhost:8080/a4/FavoritesServlet', {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  })
    .then(response => {
      if (response.ok) {
        document.getElementById(delEventID).remove();
      }
    })
    .catch(error => {
      console.error("Error:", error);
    });
}

function cardClicked(value) {
  fetch('https://us-west2-csci201-376723.cloudfunctions.net/explore-events/eventDetail/' + value)
    .then(response => response.json())
    .then(data => displayEventInfo(data, value))
    .catch(error => console.error(error))
}

function displayEventInfo(data, eventID) {
  var eventDiv = document.getElementById("favEventDetailsDiv");
  var eventName = document.getElementById("favEventName");
  eventName.textContent = data.event.name;
  var dateValue = document.getElementById("favDateValue");
  dateValue.textContent = data.date.localDate + " " + data.date.localTime;
  var venueValue = document.getElementById("favVenueValue");
  venueValue.textContent = data.event.venue;
  var priceValue = document.getElementById("favPriceValue");
  if (data.price.min < 0 || data.price.max < 0) {
    priceValue.textContent = "N/A"; 
  } else {
    priceValue.textContent = data.price.min + " - " + data.price.max; 
  }
  var moreInfoLink = document.getElementById("favMoreInfoLink");
  moreInfoLink.setAttribute('href', data.event.url);
  var eventIDInput = document.getElementById("favEventID");
  eventIDInput.value = eventID;
  var minPriceInput = document.getElementById("favMinPrice");
  minPriceInput.value = data.price.min;
  var maxPriceInput = document.getElementById("favMaxPrice");
  maxPriceInput.value = data.price.max;

  if (data.price.min < 0 || data.price.max < 0) {
    document.getElementById("favPurchaseButton").disabled = true;
  } else {
    document.getElementById("favPurchaseButton").disabled = false;
  }
  eventDiv.style.display = "block";
}

function favPurchase() {
  const quantity = document.getElementById("favQuantity").value;
  if (quantity < 1 || quantity == "") {
    alert("FAILED: Purchase not possible");
  } else {
    let data = {};
    data.eventID = document.getElementById("favEventID").value;
    data.numTickets = quantity;
    data.minPrice = document.getElementById("favMinPrice").value;
    data.maxPrice = document.getElementById("favMaxPrice").value;
    fetch('http://localhost:8080/a4/WalletServlet', {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    })
      .then(response => {
        if (response.ok) {
          alert("Bought " + quantity + " " + document.getElementById("favEventName").textContent + " tickets");
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