function userLogin() {
  const data = {
    username: document.getElementById("username").value,
    password: document.getElementById("password").value
  }
  fetch('http://localhost:8080/a4/LoginServlet', {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  })
    .then(response => {
      if (response.ok) {
        window.location.href = "index.html";
      }
    })
    .catch(error => {
      console.error("Error:", error);
    });
}

function registerUser() {
  const data = {
    username: document.getElementById("usernameSignUp").value,
    password: document.getElementById("passwordSignUp").value,
    email: document.getElementById("email").value,
  }
  fetch('http://localhost:8080/a4/RegisterServlet', {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  })
    .then(response => response.json())
    .then(data => showRegisterMessage(data))
    .catch(error => console.error(error))
}

function showRegisterMessage(data) {
  if (data.userID != 0) {
    window.location.href = "index.html";
  } else {
    alert(data.errorMessage);
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