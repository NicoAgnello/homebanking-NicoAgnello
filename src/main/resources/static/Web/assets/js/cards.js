const { createApp } = Vue;

createApp({
  data() {
    return {
      client: {},
      clientCardsDebit: [],
      clientCardsCredit: [],
      clientCards: [],
    };
  },
  created() {
    this.getClient();
  },
  mounted() {
    let script = document.createElement("script");
    script.setAttribute("src", "assets/js/argon-dashboard.js");
    document.head.appendChild(script);
  },
  methods: {
    getClient() {
      axios
        .get("/api/clients/current")
        .then((response) => {
          this.client = response.data;
          this.clientCards = response.data.cards;
          this.clientCardsDebit = response.data.cards.filter((card) => card.cardType == "DEBIT");
          this.clientCardsCredit = response.data.cards.filter((card) => card.cardType == "CREDIT");
        })
        .catch((err) => console.log(err));
    },
    parseDate(date) {
      let fecha = date.split("-").slice(0, 2).reverse().join("-");
      return fecha;
    },
    singout() {
      axios
        .post("/api/logout")
        .then((response) => {
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
          }).then((response) => {
            location.href = "./index.html";
          });
        })
        .catch((err) => console.log(err));
    },
  },
}).mount("#app");
