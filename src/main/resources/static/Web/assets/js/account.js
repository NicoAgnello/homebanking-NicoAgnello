const { createApp } = Vue;

createApp({
  data() {
    return {
      client: {},
      accountId: "",
      account: {},
      clientLoans: [],
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
        .get("http://localhost:8080/api/clients/1")
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
        .get(`http://localhost:8080/api/accounts/${this.accountId}`)
        .then((response) => {
          this.account = response.data;
          this.account.transactions.sort((b, a) => a.id - b.id);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
}).mount("#app");
