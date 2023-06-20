var isAdmin = false;

    var adminCode = window.sessionStorage.getItem('role');
    if (adminCode === 'admin') {
      isAdmin = true; 
    }
    if (!isAdmin) {
      window.location.href = 'no-authorisatie.html';
    }