function walletDetails() {
    fetch('http://localhost:8080/a4/WalletServlet')
        .then(response => response.json())
        .then(data => displayWalletInfo(data))
        .catch(error => console.error(error));
    displayMenu();
}


function displayWalletInfo(data) {
    document.getElementById("cashBalanceVal").textContent = "$" + data.cashBalance;
    document.getElementById("totalAccountVal").textContent = "$"  + data.accountValue;

    for (const item of data.tickets) {  
        const cardContainer = document.createElement('div');
        cardContainer.id = 'wallet-container';
        cardContainer.style.marginTop = "10px"; 
        cardContainer.style.marginLeft = "200px"; 
        cardContainer.style.display = "flex"; 
        cardContainer.style.flexDirection = "column"; 
        cardContainer.style.width = "500px"; 
        cardContainer.style.border = "1px solid #ccc"; 

        const divOne = document.createElement('div');
        divOne.style.background = "#f7f7f7";
        divOne.style.width = "500px";

        const name = document.createElement('label');
        name.setAttribute('id', item.eventID + "_name");
        name.textContent = item.eventName;
        name.style.marginLeft = "10px";
        name.style.fontSize = "small";

        divOne.appendChild(name);
        cardContainer.appendChild(divOne);

        const divTwo = document.createElement('div');
        divTwo.style.width = "500px";
        divTwo.style.marginTop = "20px";
        divTwo.style.marginLeft = "125px";
        divTwo.style.marginBottom = "20px";

        const table = document.createElement("table");
        const row = document.createElement('tr');
        const quantity = document.createElement('td');
        quantity.textContent = "Quantity:";
        row.appendChild(quantity);
        const quantityValue = document.createElement('td');
        quantityValue.textContent = item.quantity;
        row.appendChild(quantityValue);
        const change = document.createElement('td');
        change.textContent = "Change:";
        row.appendChild(change);
        
        const changeValue = document.createElement('td');
        const newTriangle = document.createElement('i'); 
        newTriangle.classList.add("fa"); 
        newTriangle.classList.add("fa-solid")
        newTriangle.classList.add("fa-caret-up"); 
        newTriangle.style.color = "#008000"; 
        changeValue.appendChild(newTriangle);

        const triLabel = document.createElement('label'); 
        triLabel.textContent = item.change;
        triLabel.style.color = "#008000"; 

        changeValue.appendChild(triLabel);
        row.appendChild(changeValue);
        table.appendChild(row);

        const rowTwo = document.createElement('tr');
        const avgCost = document.createElement('td');
        avgCost.textContent = "Avg. Cost:";
        const avgCostValue = document.createElement('td');
        avgCostValue.textContent = item.avgCost;
        const currentPrice = document.createElement('td');
        currentPrice.textContent = "Current Price:";
        const currentPriceValue = document.createElement('td');
        currentPriceValue.textContent = item.currentPrice;
        rowTwo.appendChild(avgCost);
        rowTwo.appendChild(avgCostValue);
        rowTwo.appendChild(currentPrice);
        rowTwo.appendChild(currentPriceValue);
        table.appendChild(rowTwo);

        const rowThree = document.createElement('tr');
        const totalCost = document.createElement('td');
        totalCost.textContent = "Total Cost:";
        const totalCostValue = document.createElement('td');
        totalCostValue.textContent = item.totalCost;
        const marketValue = document.createElement('td');
        marketValue.textContent = "Market Value:";
        const marketValueValue = document.createElement('td');
        marketValueValue.textContent = item.marketValue;
        rowThree.appendChild(totalCost);
        rowThree.appendChild(totalCostValue);
        rowThree.appendChild(marketValue);
        rowThree.appendChild(marketValueValue);
        table.appendChild(rowThree);

        divTwo.appendChild(table);
        cardContainer.appendChild(divTwo);


        const divThree = document.createElement('div');
        divThree.style.background = "#f7f7f7";
        divThree.style.width = "500px";
        divThree.style.marginBottom = "10px"; 

        const minPrice = document.createElement('input');
        minPrice.setAttribute('id', item.eventID + "_min");
        minPrice.value = item.minPrice;
        minPrice.setAttribute('type', "hidden");
        divThree.appendChild(minPrice);

        const maxPrice = document.createElement('input');
        maxPrice.setAttribute('id', item.eventID + "_max");
        maxPrice.value = item.currentPrice;
        maxPrice.setAttribute('type', "hidden");
        divThree.appendChild(maxPrice);

        const quantityLabel = document.createElement('label');
        quantityLabel.textContent = "Quantity: ";
        quantityLabel.style.marginLeft = "200px";
        quantityLabel.style.fontSize = "small";
        divThree.appendChild(quantityLabel);

        const purchaseInput = document.createElement('input');
        purchaseInput.setAttribute('id', item.eventID + "_qty");
        purchaseInput.style.width = "30px"; 
        divThree.appendChild(purchaseInput);
        
        const brOne = document.createElement('br'); 
        divThree.appendChild(brOne); 

        const radioValues = ["Buy", "Sell"];
        radioValues.forEach((value) => {
            // Create the radio button element
            const radioButton = document.createElement("input");
            radioButton.type = "radio";
            radioButton.name = item.eventID + "_bs";
            radioButton.value = value;

            if (value == "Buy") {
                radioButton.style.marginLeft = "200px"; 
            }
            radioButton.style.fontSize = "small"; 
            radioButton.style.marginTop = "10px"; 

            // Create the label element
            const label = document.createElement("label");
            label.textContent = value;
            label.style.fontSize = "x-small"; 

            // Add the radio button and label to the parent element
            divThree.appendChild(radioButton);
            divThree.appendChild(label);
        });

        const br2 = document.createElement('br'); 
        divThree.appendChild(br2); 

        const submitButton = document.createElement('button');
        submitButton.setAttribute('onclick', "submitPurchaseSell('" + item.eventID + "')");
        submitButton.textContent = "Submit";
        submitButton.style.marginLeft = "200px"; 
        submitButton.style.fontSize = "x-small"; 
        submitButton.style.marginTop = "10px"; 
        submitButton.style.marginBottom = "10px"; 
        submitButton.style.width = "100px"; 
        divThree.appendChild(submitButton); 

        cardContainer.appendChild(divThree);
        document.getElementById("walletDetailsDiv").appendChild(cardContainer); 
    }
}

function submitPurchaseSell(value) {
    const selectedOption = document.querySelector('input[name=' + value + '_bs]:checked');
    const quantity = document.getElementById(value + "_qty").value;
    if (quantity < 1 || quantity == "") {
        alert("FAILED: Purchase not possible");
    } else {
        let data = {};
        data.eventID = value;
        data.numTickets = quantity;
        data.minPrice = document.getElementById(value + "_min").value;
        data.maxPrice = document.getElementById(value + "_max").value;
        data.buySell = selectedOption.value;
        fetch('http://localhost:8080/a4/WalletServlet', {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
            .then(response => {
                const buySellOption = document.querySelector('input[name=' + value + '_bs]:checked');
                if (buySellOption.value == "Buy") {
                    if (response.ok) {
                        alert("Bought " + quantity + " " + document.getElementById(value + "_name").textContent + " tickets");
                        location.reload();
                    } else {
                        alert("FAILED: Purchase not possible");
                    }
                } else {
                    if (response.ok) {
                        alert("Sold " + quantity + " " + document.getElementById(value + "_name").textContent + " tickets");
                        location.reload();
                    } else {
                        alert("FAILED: Sale not possible");
                    }
                }
            })
            .catch(error => {
                console.error("Error:", error);
            });
    }

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