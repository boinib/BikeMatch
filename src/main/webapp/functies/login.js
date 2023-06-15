document.addEventListener("load", event =>{
    document.querySelector("login_button").addEventListener("click",login)
});

    async function login(){
        console.log("login");
        var formData = new FormData(document.querySelector("formLogin"));
        var encData = new URLSearchParams(formData.entries())

        let response = await fetch("https://ipasss-1685617513032.azurewebsites.net//restservices/login",{method: "POST", body: encData})

        if(response.status === 200){
            let myJson = response.json();
            window.sessionStorage.setItem("abc",myJson.JWT)
        }else{
            console.log("Login failed")
        }
    }
