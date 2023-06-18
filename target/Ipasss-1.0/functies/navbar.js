const navbarPlaceholder = document.querySelector('#navbar-placeholder');
const navbarURL = 'navbar.html';
fetch(navbarURL)
  .then(response => response.text())
  .then(html => navbarPlaceholder.innerHTML = html);

document.addEventListener('DOMContentLoaded', function() {
  var gebruikersnaam = sessionStorage.getItem('username');
  console.log("fkid")
  console.log(gebruikersnaam)
  if (gebruikersnaam) {
    console.log("weirdo")
    var titelElement = document.getElementById('TITEL');
    console.log(titelElement)
    console.log("1")
    if (titelElement) {
      console.log("2")
      titelElement.textContent = gebruikersnaam;
    }
  }
});
  