function login() {
    let username = document.querySelector("#login-username").innerHTML;
    let password = document.querySelector("#login-password").innerHTML;


    let requestBody = `{"username": "` +  username + `", "password": "` + password + `"}`
    fetch("/api/v1/login")
}

