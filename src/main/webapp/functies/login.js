document.addEventListener("DOMContentLoaded", () => {
  document.querySelector("#formLogin").addEventListener("submit", login);
});

async function login(event) {
  event.preventDefault();

  console.log("login");
  var formData = new FormData(document.querySelector("#formLogin"));
  var encData = new URLSearchParams(formData.entries());

  let response = await fetch("/restservices/login", {
    method: "POST",
    body: encData
  });

  if (response.status === 200) {
    let myJson = await response.json();
    let token = myJson.JWT;
    let role = extractRoleFromToken(token);
    window.sessionStorage.setItem("JWT", token);
    window.sessionStorage.setItem("role", role); 
    if (role === "admin" || role === "winkeleigenaar") {
      window.location.href = 'admin.html';
    } else {
      window.location.href = 'index.html';
    }    
  } else {
    document.querySelector("#login_error").style.display = "block";
    console.log("Login failed");
  }
}
//van het internet, ik kon niet uitvogelen hoe je nou de role kunt ophalen uit een token
function extractRoleFromToken(token) {
  const [, payload] = token.split('.');
  const decodedPayload = atob(payload);
  const payloadObj = JSON.parse(decodedPayload);
  const role = payloadObj.role;
  return role;
}
