const { createApp } = Vue;

createApp({
  data() {
    return {
      client: {},
      cardType: "",
      cardColor: "",
    };
  },
  created() {
    let script = document.createElement("script");
    script.setAttribute("src", "assets/js/argon-dashboard.js");
    document.head.appendChild(script);
    this.getClient();
  },
  methods: {
    getClient() {
      axios
        .get("/api/clients/current")
        .then((response) => {
          this.client = response.data;
        })
        .catch((err) => console.log(err));
    },
    singout() {
      axios
        .post("/api/logout")
        .then(() => {
          const Toast = Swal.mixin({
            toast: true,
            position: "top-end",
            showConfirmButton: false,
            timer: 1500,
            timerProgressBar: true,
            didOpen: (toast) => {
              toast.addEventListener("mouseenter", Swal.stopTimer);
              toast.addEventListener("mouseleave", Swal.resumeTimer);
            },
          });
          Toast.fire({
            icon: "error",
            title: "Closing session",
          }).then(() => {
            location.href = "./index.html";
          });
        })
        .catch((err) => console.log(err));
    },
    createCard() {
      axios
        .post("/api/clients/current/cards", `cardColor=${this.cardColor}&cardType=${this.cardType}`)
        .then(() => {
          location.href = "./cards.html";
        })
        .catch((err) => this.modal(err));
    },
    modal(error) {
      Swal.fire({
        title: `${error.response.data}`,
        icon: "error",
        confirmButtonText: "OK",
      });
    },
  },
}).mount("#app");
