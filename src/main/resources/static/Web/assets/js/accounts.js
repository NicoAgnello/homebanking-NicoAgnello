const { createApp } = Vue;

createApp({
  data() {
    return {
      client: {},
      clientLoans: [],
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
          this.client.accounts.sort((a, b) => b.id - a.id);
          this.clientLoans = response.data.loans;
        })
        .catch((err) => console.log(err));
    },
    parseDate(fecha) {
      let date = fecha.split("T")[0];
      let newDate = date.split("-").reverse().join("/");
      return newDate;
    },
    singout() {
      axios.post("/api/logout")
      .then((response) => {
        if (response) {
          location.href = "./index.html";
        }
      });
    },
  },
}).mount("#app");
