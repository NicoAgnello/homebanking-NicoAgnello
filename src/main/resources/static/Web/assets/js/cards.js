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
        .get("http://localhost:8080/api/clients/1")
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
  },
}).mount("#app");
