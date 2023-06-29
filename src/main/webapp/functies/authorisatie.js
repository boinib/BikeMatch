var isAdmin = false;

var adminCode = window.sessionStorage.getItem('role');
if (adminCode === 'admin' || adminCode === 'winkeleigenaar') {
  isAdmin = true;
}
if (!isAdmin) {
  window.location.href = 'no-authorisatie.html';
}

const role = window.sessionStorage.getItem("role");
const addAccessoireBtn = document.getElementById("add-accessoire-btn");

if (role === "winkeleigenaar") {
  addAccessoireBtn.style.display = "none";
} else {
  addAccessoireBtn.style.display = "block";
}

function logout() {
  window.sessionStorage.removeItem('JWT');
  window.sessionStorage.removeItem('role');
  window.location.href = 'login.html';
}
window.logout = logout;
