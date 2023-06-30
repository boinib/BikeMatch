function getFietsById(id) {
  const url = `/restservices/fiets/${id}`;
  return fetch(url)
    .then(response => {
      if (!response.ok) {
        throw new Error('Fout bij het ophalen van de fiets.');
      }
      return response.json();
    })
    .catch(error => {
      console.error(error.message);
    });
}

function addFiets(fiets) {
  const token = window.sessionStorage.getItem('JWT');
  const role = window.sessionStorage.getItem('role');

  if (role === 'admin' || role === 'winkeleigenaar') {
    fetch("/restservices/fiets", {
      method: "POST",
      body: JSON.stringify(fiets),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token
      }
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Fout bij het toevoegen van de fiets.');
        }
        return response.json();
      })
      .then(data => {
        console.log("Fiets succesvol toegevoegd:", data);
      })
      .catch(error => {
        console.error("Fout bij het toevoegen van de fiets:", error);
      });
  } else {
    console.log("Alleen admin of winkeleigenaar kan toevoegen");
  }
}

function updateFiets(fiets, id) {
  const token = window.sessionStorage.getItem('JWT');
  const role = window.sessionStorage.getItem('role');

  if (role === 'admin' || role === 'winkeleigenaar') {
    fetch(`/restservices/fiets/${id}`, {
      method: "PUT",
      body: JSON.stringify(fiets),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token
      }
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Fout bij het bijwerken van de fiets.');
        }
        return response.json();
      })
      .then(data => {
        console.log("Fiets succesvol bijgewerkt:", data);
      })
      .catch(error => {
        console.error("Fout bij het bijwerken van de fiets:", error);
      });
  } else {
    console.log("Alleen admin of winkeleigenaar kan bijwerken");
  }
}

document.querySelector("#addFietsForm").addEventListener("submit", function (event) {
  event.preventDefault();
  let request = {};
  let form = document.querySelector("#addFietsForm");

  new FormData(form).forEach((value, key) => {
    request[key] = value;
  });

  const fietsId = document.querySelector("#fietsId").value;

  if (fietsId) {
    updateFiets(request, fietsId);
  } else {
    addFiets(request);
  }
});

function setFietsId(id) {
  document.querySelector("#fietsId").value = id;
}

const urlParams = new URLSearchParams(window.location.search);
const id = urlParams.get('id');
if (id) {
  getFietsById(id)
    .then(fiets => {
      document.querySelector("#merk").value = fiets.merk;
      document.querySelector("#type").value = fiets.type;
      document.querySelector("#beschrijving").value = fiets.beschrijving;
      document.querySelector("#gewicht").value = fiets.gewicht;
      document.querySelector("#versnellingen").value = fiets.versnellingen;
      document.querySelector("#wielmaat").value = fiets.wielmaat;
      document.querySelector("#framemaat").value = fiets.framemaat;
      document.querySelector("#materiaalFrame").value = fiets.materiaalFrame;
      document.querySelector("#voorvork").value = fiets.voorvork;
      document.querySelector("#verlichting").value = fiets.verlichting;
      document.querySelector("#bagagedrager").value = fiets.bagagedrager;
      document.querySelector("#remmen").value = fiets.remmen;
      document.querySelector("#slot").value = fiets.slot;
      document.querySelector("#prijs").value = fiets.prijs;
      document.querySelector("#link").value = fiets.link;
      document.querySelector("#afbeelding").value = fiets.afbeelding;
      setFietsId(id);
      document.querySelector("#deleteFietsButton").style.display = "inline-block"; // Maak de knop "Fiets verwijderen" zichtbaar
    })
    .catch(error => {
      console.error("Fout bij het ophalen van de fiets:", error);
    });
}

document.querySelector("#deleteFietsButton").addEventListener("click", function () {
  const fietsId = document.querySelector("#fietsId").value;
  deleteAccessoire(fietsId);
});


async function deleteAccessoire(fietsId) {
  const token = window.sessionStorage.getItem('JWT');
  const confirmDelete = confirm('Weet je zeker dat je deze fiets wilt verwijderen?');

  if (confirmDelete) {
    try {
      const url = `/restservices/fiets/${fietsId}`;
      const response = await fetch(url, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + token
        }
      });

      if (!response.ok) {
        throw new Error('Error bij het verwijderen van de fiets.');
      }

      window.location.href = 'producten.html';
    } catch (error) {
      console.error(error);
    }
  }
}
