function showAccessoires(accessoires) {
  const accessoireElements = accessoires.map(accessoire => `
    <div class="accessoire">
      <a href="accessoire.html?id=${accessoire.id}">
        <img src="${accessoire.afbeelding}" alt="${accessoire.alt}" style="${accessoire.style}">
        <p>Naam: ${accessoire.naam}</p>
        <p>Beschrijving: ${accessoire.beschrijving}</p>
        </a>
        <p class="price">${accessoire.prijs}</p>
    </div>
  `);
  document.querySelector('.accessoires').innerHTML = accessoireElements.join('');
}
async function getAccessoires() {
  const url = 'https://ipasss-1685617513032.azurewebsites.net/restservices/accessoires';
  
  try {
    const response = await fetch(url, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' }
    });

    if (!response.ok) {
      throw new Error('Fout bij het ophalen van accessoires.');
    }

    const accessoires = await response.json();
    showAccessoires(accessoires);
  } catch (error) {
    console.error(error.message);
  }
}
getAccessoires();





