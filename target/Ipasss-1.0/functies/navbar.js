const navbarPlaceholder = document.querySelector('#navbar-placeholder');
const navbarURL = 'navbar.html';
fetch(navbarURL)
  .then(response => response.text())
  .then(html => navbarPlaceholder.innerHTML = html);

  
  document.addEventListener("DOMContentLoaded", () => {
    updateNavbar();
  });
  
  function updateNavbar() {
    const usernameElement = document.querySelector("#username_element");
    const loginElement = document.querySelector("#login_element");
  
    const username = window.sessionStorage.getItem("username");
    if (username) {
      usernameElement.textContent = username;
      usernameElement.className = "visible";
      loginElement.className = "hidden";
    } else {
      usernameElement.className = "hidden";
      loginElement.className = "visible";
    }
  }
  
  

