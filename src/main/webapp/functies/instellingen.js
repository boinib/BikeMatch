// Functie om de achtergrond te wijzigen en op te slaan in de local storage
function veranderAchtergrond() {
  // Haalt het selectievakje op
  var select = document.getElementById("background-select");

  // Haalt de waarde van de geselecteerde optie op
  var value = select.options[select.selectedIndex].value;

  // Haalt het <body> element op
  var body = document.getElementsByTagName("body")[0];

  // Verandert tussen de verschillende achtergrondafbeeldingen bij selectie in de combobox
  switch(value) {
    case "default":
      body.style.backgroundImage = "url('/Images/Logo/logo-lichter.png')";
      break;
    case "background1":
      body.style.backgroundImage = "url('/Images/Logo/logo-white.png')";
      break;
    case "background2":
      body.style.backgroundImage = "url('/Images/Logo/logo-color.png')";
      break;
    case "background3":
      body.style.backgroundImage = "url('/Images/Logo/logo-black.png')";
      break;
    default:
      body.style.backgroundImage = "url('/Images/Logo/logo-lichter.png')";
  }

  // Slaat de geselecteerde achtergrond op in de local storage
  localStorage.setItem('background', value);
}

// Functie om de gebruikersnaam weer te geven
function decodeToken(token) {
  const payload = token.split('.')[1];
  const decodedPayload = atob(payload);
  return JSON.parse(decodedPayload);
}


function displayUsername() {
  var token = window.sessionStorage.getItem('JWT');
  var decodedToken = decodeToken(token);
  var username = decodedToken.naam;
  var role = decodedToken.role;

  var loginButton = document.getElementById("login-button");
  var userSpan = document.getElementById("user-info");

  if (role === "admin") {
    loginButton.textContent = "Admin";
    loginButton.setAttribute("href", "admin.html");
  } else {
    loginButton.textContent = "Logout";
    loginButton.setAttribute("href", "logout.html");
  }

  userSpan.textContent = "Welkom, " + username + "!";
}



window.onload = function() {
  displayUsername();

  var body = document.getElementsByTagName("body")[0];

  // Haalt de opgeslagen achtergrond op uit de local storage
  var background = localStorage.getItem('background');

  if (background) {
    var select = document.getElementById("background-select");
    
    select.value = background;

    veranderAchtergrond();
  }
};
