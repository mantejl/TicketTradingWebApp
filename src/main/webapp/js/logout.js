function logout() {
    fetch('http://localhost:8080/a4/Logout', {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: null
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