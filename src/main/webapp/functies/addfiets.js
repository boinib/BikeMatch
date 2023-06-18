document.querySelector("#addFietsForm").addEventListener("submit", function (event) {
    event.preventDefault();
    let request = {};
    let form = document.querySelector("#addFietsForm");

    new FormData(form).forEach((value, key) => {
        request[key] = value;
    });

    fetch("https://ipasss-1685617513032.azurewebsites.net/restservices/fiets", {
      method: "POST",
      body: JSON.stringify(request),
      headers: { 'Content-Type': 'application/json' }
    })
      .then(response => response.json())
      .then(data => {
        console.log("Fiets succesvol toegevoegd:", data);
      })
      .catch(error => {
        console.error("Fout bij het toevoegen van de fiets:", error);
      });
  });