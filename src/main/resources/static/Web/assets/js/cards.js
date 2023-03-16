const { createApp } = Vue;

createApp({
  data() {
    return {
      client: {},
      clientCardsDebit: [],
      clientCardsCredit: [],
      clientCards: [],
      actualDate: "",
    };
  },
  created() {
    this.getClient();
    this.getCards();
    this.createActualDate();
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
        })
        .catch((err) => console.log(err));
    },
    getCards() {
      axios
        .get("/api/clients/current/cards")
        .then((response) => {
          this.clientCards = response.data;
          this.clientCardsDebit = this.clientCards.filter((card) => card.cardType == "DEBIT");
          this.clientCardsCredit = this.clientCards.filter((card) => card.cardType == "CREDIT");
        })
        .catch((err) => console.log(err));
    },
    parseDate(date) {
      let fecha = date.split("-").reverse().join("-");
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
    confirmDeleteCard(id) {
      console.log(id);
      Swal.fire({
        title: "¿Are you sure you want to delete this card?",
        text: "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete it!",
      }).then((result) => {
        if (result.isConfirmed) {
          axios
            .patch(`/api/clients/current/cards/${id}`)
            .then(() => {
              Swal.fire("Deleted!", "Your file has been deleted.", "success");
              this.getClient();
            })
            .catch((err) => console.log(err));
        }
      });
    },
    createActualDate() {
      let fecha = new Date().toLocaleString().split(",")[0].split("/").reverse().join("-");
      this.actualDate = fecha;
    },
  },
}).mount("#app");
