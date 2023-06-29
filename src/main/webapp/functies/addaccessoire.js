function getAccessoireById(id) {
  const url = `/restservices/accessoires/${id}`;
  return fetch(url)
    .then(response => {
      if (!response.ok) {
        throw new Error('Fout bij het ophalen van het accessoire.');
      }
      return response.json();
    })
    .catch(error => {
      console.error(error.message);
    });
}

function addAccessoire(accessoire) {
  const token = window.sessionStorage.getItem('JWT');
  const role = window.sessionStorage.getItem('role');

  if (role === 'admin') {
    fetch("/restservices/accessoires", {
      method: "POST",
      body: JSON.stringify(accessoire),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token
      }
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Fout bij het toevoegen van de accessoire.');
        }
        return response.json();
      })
      .then(data => {
        console.log("Accessoire succesvol toegevoegd:", data);
      })
      .catch(error => {
        console.error("Fout bij het toevoegen van de accessoire:", error);
      });
  } else {
    console.log("Alleen admin kan toevoegen");
  }
}
function updateAccessoire(accessoire, id) {
  const token = window.sessionStorage.getItem('JWT');
  const role = window.sessionStorage.getItem('role');

  if (role === 'admin') {
    fetch(`/restservices/accessoires/${id}`, {
      method: "PUT",
      body: JSON.stringify(accessoire),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token
      }
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Fout bij het bijwerken van de accessoire.');
        }
        return response.json();
      })
      .then(data => {
        console.log("Accessoire succesvol bijgewerkt:", data);
      })
      .catch(error => {
        console.error("Fout bij het bijwerken van de accessoire:", error);
      });
  } else {
    console.log("Alleen admin kan bijwerken");
  }
}

document.querySelector("#addAccessoireForm").addEventListener("submit", function (event) {
  event.preventDefault();
  let request = {};
  let form = document.querySelector("#addAccessoireForm");

  new FormData(form).forEach((value, key) => {
    request[key] = value;
  });

  const accessoireId = document.querySelector("#accessoireId").value;

  if (accessoireId) {
    updateAccessoire(request, accessoireId);
  } else {
    addAccessoire(request);
  }
});

function setAccessoireId(id) {
  document.querySelector("#accessoireId").value = id;
}

const urlParams = new URLSearchParams(window.location.search);
const id = urlParams.get('id');
if (id) {
  getAccessoireById(id)
    .then(accessoire => {
      document.querySelector("#naam").value = accessoire.naam;
      document.querySelector("#beschrijving").value = accessoire.beschrijving;
      document.querySelector("#voorraad").value = accessoire.voorraad;
      document.querySelector("#prijs").value = accessoire.prijs;
      document.querySelector("#afbeelding").value = accessoire.afbeelding;
      setAccessoireId(id);
    })
    .catch(error => {
      console.error("Fout bij het ophalen van het accessoire:", error);
    });
}
document.querySelector("#deleteAccessoireButton").addEventListener("click", function () {
  const accessoireId = document.querySelector("#accessoireId").value;
  deleteFiets(accessoireId);
});


async function deleteFiets(accessoireId) {
  const token = window.sessionStorage.getItem('JWT');
  const confirmDelete = confirm('Weet je zeker dat je deze fiets wilt verwijderen?');

  if (confirmDelete) {
    try {
      const url = `/restservices/accessoire/${accessoireId}`;
      const response = await fetch(url, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + token
        }
      });

      if (!response.ok) {
        throw new Error('Error bij het verwijderen van de accessoire.');
      }

      window.location.href = 'accessoires.html';
    } catch (error) {
      console.error(error);
    }
  }
}
