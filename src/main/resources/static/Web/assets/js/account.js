const { createApp } = Vue;

createApp({
  data() {
    return {
      client: {},
      accountId: "",
      account: {},
      clientLoans: [],
      transactions: [],
    };
  },
  created() {
    this.getClient();
    this.getURLId();
    this.getAccount();
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
          this.clientLoans = response.data.loans;
        })
        .catch((err) => console.log(err));
    },
    parseDate(fecha) {
      let date = fecha.split("T")[0];
      let newDate = date.split("-").reverse().join("/");
      return newDate;
    },
    getURLId() {
      let stringUrlWithID = location.search;
      let generateUrl = new URLSearchParams(stringUrlWithID);
      this.accountId = generateUrl.get("id");
    },
    getAccount() {
      axios
        .get(`/api/accounts/${this.accountId}`)
        .then((response) => {
          this.account = response.data;
          this.account.transactions.sort((b, a) => a.id - b.id);
          this.transactions = response.data.transactions;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    singout() {
      axios.post("/api/logout").then((response) => {
        if (response) {
          location.href = "./index.html";
        }
      });
    },
  },
}).mount("#app");
