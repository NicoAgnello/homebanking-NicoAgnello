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
          this.clientCardsDebit = response.data.cards.filter((card) => card.cardType == "DEBIT" && card.active == true);
          this.clientCardsCredit = response.data.cards.filter(
            (card) => card.cardType == "CREDIT" && card.active == true
          );
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
  },
}).mount("#app");
