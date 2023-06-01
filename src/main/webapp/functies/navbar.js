const navbarPlaceholder = document.querySelector('#navbar-placeholder');
const navbarURL = 'navbar.html';
fetch(navbarURL)
  .then(response => response.text())
  .then(html => navbarPlaceholder.innerHTML = html);
