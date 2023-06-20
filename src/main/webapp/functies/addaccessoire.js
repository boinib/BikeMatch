document.querySelector("#addAccessoireForm").addEventListener("submit", function (event) {
  event.preventDefault();
  let request = {};
  let form = document.querySelector("#addAccessoireForm");

  new FormData(form).forEach((value, key) => {
    request[key] = value;
  });

  const token = window.sessionStorage.getItem('JWT');
  const role = window.sessionStorage.getItem('role');

  if (role === 'admin') {
    fetch("/restservices/accessoires", {
      method: "POST",
      body: JSON.stringify(request),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token
      }
    })
      .then(response => response.json())
      .then(data => {
        console.log("Accessoire succesvol toegevoegd:", data);
      })
      .catch(error => {
        console.error("Fout bij het toevoegen van de accessoire:", error);
      });
  } else {
    console.log("Alleen admin kan toevoegen");
  }
});
