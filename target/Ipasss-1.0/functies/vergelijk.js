let aantal = 0;
let gekozen = [];

export async function getFietsById(id) {
  const url = `https://ipasss-1685617513032.azurewebsites.net/restservices/fiets/${id}`;
  const response = await fetch(url, {
    method: 'GET',
    headers: {'Content-Type': 'application/json'}
  });

  if (!response.ok) {
    throw new Error('Error bij het ophalen van de fiets.');
  }

  const fiets = await response.json();
  return fiets;
}

export async function getFietsen() {
  const url = 'https://ipasss-1685617513032.azurewebsites.net/restservices/fiets';
  const response = await fetch(url, {
    method: 'GET',
    headers: {'Content-Type': 'application/json'}
  });

  if (!response.ok) {
    throw new Error('Error bij het ophalen van de fietsen.');
  }

  const fietsen = await response.json();
  return fietsen;
}

export async function selectieBoxFietsen() {
  const fietsen = await getFietsen();
  const select = document.querySelector("#bike-select");

  fietsen.forEach(bike => {
    const option = document.createElement("option");
    option.value = bike.id;
    option.textContent = bike.merk + " " + bike.type;
    select.appendChild(option);
  });
}

export async function vergelijkfietsen(bikeId) {
  if (aantal === 2) {
    // Check if two bikes are already selected
    console.log("You have already selected two bikes.");
    return;
  }

  if (gekozen.includes(bikeId)) {
    // Check if the bike is already selected
    console.error(`Bike with ID ${bikeId} is already selected.`);
    return;
  }

  try {
    const fiets = await getFietsById(bikeId);
    gekozen.push(bikeId);
    aantal++;

    const grid = document.querySelector(".vergelijk-grid");
    // Maakt een nieuw fietsblok voor de geselecteerde fiets
    const fietsblok = document.createElement("div");
    fietsblok.classList.add("bike");
    fietsblok.innerHTML = `
      <div class="bike-info-container">
        <div class="bike-p">${fiets.prijs}</div>
        <img src="${fiets.afbeelding}" alt="${fiets.alt}" class="bike-image">
        <div class="bike-info-table">
          <table>
            <tr>
              <th>Merk</th>
              <td>${fiets.merk}</td>
            </tr>
            <tr>
              <th>Type</th>
              <td>${fiets.type}</td>
            </tr>
            <tr>
              <th>Gewicht</th>
              <td>${fiets.gewicht}</td>
            </tr>
            <tr>
              <th>Versnellingen</th>
              <td>${fiets.versnellingen}</td>
            </tr>
            <tr>
              <th>Remmen</th>
              <td>${fiets.remmen}</td>
            </tr>
            <tr>
              <th>Beschrijving</th>
              <td>${fiets.beschrijving}</td>
            </tr>
            <tr>
              <th>Wielmaat</th>
              <td>${fiets.wielmaat}</td>
            </tr>
            <tr>
              <th>Framemaat</th>
              <td>${fiets.framemaat}</td>
            </tr>
            <tr>
              <th>Materiaalframe</th>
              <td>${fiets.materiaalFrame}</td>
            </tr>
            <tr>
              <th>Voorvork</th>
              <td>${fiets.voorvork}</td>
            </tr>
            <tr>
              <th>Verlichting</th>
              <td>${fiets.verlichting}</td>
            </tr>
            <tr>
              <th>Bagagedrager</th>
              <td>${fiets.bagagedrager}</td>
            </tr>
            <tr>
              <th>Slot</th>
              <td>${fiets.slot}</td>
            </tr>
          </table>
          <a class="bestel-link" href="${fiets.link}" target="_blank"><button>Bestelpagina openen</button></a>
        </div>
      </div>
    `;
    // Voegt het fietsdiv-element toe aan het scherm
    grid.appendChild(fietsblok);
  } catch (error) {
    console.error(`Fiets met id ${bikeId} niet gevonden:`, error);
  }
}

// Checkt of er een fietsId is in de URL
window.addEventListener('DOMContentLoaded', async function() {
  await selectieBoxFietsen();
  const queryParams = new URLSearchParams(window.location.search);
  const bikeId = queryParams.get("bikeId");
  if (bikeId !== null) {
    vergelijkfietsen(bikeId); 
  }
});

const select = document.createElement("select");
select.id = "bike-select";
document.querySelector(".bike-select-container").appendChild(select);

//Voor de 2e fiets
const tweedefiets = document.querySelector("#tweedefiets");
tweedefiets.addEventListener("click", function() {
  const bikeId = document.querySelector("#bike-select").value; 
  if (bikeId !== null) {
    vergelijkfietsen(bikeId); 

    //verwijdert de de fiets van de selectiebox
    const optie = document.querySelector(`#bike-select option[value="${bikeId}"]`);
    if (optie !== null) {
      optie.remove(); 
    }
  }
});
