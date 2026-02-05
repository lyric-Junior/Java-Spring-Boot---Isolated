//animando um botão em js só pra testar minhas capacidades. como front-end.

let headerButtons = window.document.querySelectorAll(".header-button");

headerButtons.forEach((headerBtn) => {
  addEventListener("mouseneter", subir);
  addEventListener("mouseleave", descer);
});
