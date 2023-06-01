function veranderAchtergrond() {
  // Haalt het selectievakje op
  var select = document.querySelector('select');

  // Haalt de waarde van de geselecteerde optie op
  var value = select.options[select.selectedIndex].value;

  // Haalt het <body> element op
  var body = document.getElementsByTagName("body")[0];

  // Verandert tussen de verschillende achtergrondafbeeldingen bij selectie in de combobox
  switch(value) {
    case "default":
      body.style.backgroundImage = "url('/Images/Logo/logo-lichter.png')";
      break;
    case "background1":
      body.style.backgroundImage = "url('/Images/Logo/logo-white.png')";
      break;
    case "background2":
      body.style.backgroundImage = "url('/Images/Logo/logo-color.png')";
      break;
    case "background3":
      body.style.backgroundImage = "url('/Images/Logo/logo-black.png')";
      break;
    default:
      body.style.backgroundImage = "url('/Images/Logo/logo-lichter.png')";
  }

  // Slaat de geselecteerde achtergrond op in de local storage
  localStorage.setItem('background', value);
}

window.onload = function() {
  // Haalt het <body> element op
  var body = document.getElementsByTagName("body")[0];

  // Haalt de opgeslagen achtergrond op uit de local storage
  var background = localStorage.getItem('background');

  // Controleert of er een opgeslagen achtergrond is
  if (background) {
    // Haalt het selectievakje op
    var select = document.querySelector('select');
    
    // Stelt de waarde van het selectievakje in op de opgeslagen waarde
    select.value = background;

    // Verandert de achtergrond naar de opgeslagen achtergrond in de localstorage
    veranderAchtergrond();
  }
};
