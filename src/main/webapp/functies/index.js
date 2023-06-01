window.addEventListener("load", () => {
    document.querySelectorAll(".bike").forEach((bike) => {
      bike.addEventListener("click",(e) => {
        let type = bike.getAttribute("data-type")
        window.location.href = `producten.html?type=${type}`
      })
    })
  })
 