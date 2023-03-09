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
  },
  mounted() {
    let script = document.createElement("script");
    script.setAttribute("src", "assets/js/argon-dashboard.js");
    document.head.appendChild(script);
  },
  methods: {
    getClient() {
      let stringUrlWithID = location.search;
      let generateUrl = new URLSearchParams(stringUrlWithID);
      this.accountId = generateUrl.get("id");
      console.log(this.accountId);
      axios
        .get("/api/clients/current")
        .then((response) => {
          this.client = response.data;
          this.clientLoans = response.data.loans;
          this.account = this.client.accounts.find((account) => account.id == this.accountId);
          this.transactions = this.account.transactions;
        })
        .catch((err) => console.log(err));
    },
    parseDate(fecha) {
      let date = fecha.split("T")[0];
      let newDate = date.split("-").reverse().join("/");
      return newDate;
    },
    parseTime(fecha){
      let date = fecha.split("T")[1].split(".")[0];
      let newDate = date.split("-").reverse().join("/");
      return newDate;
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
          }).then((response) => {
            location.href = "./index.html";
          });
        })
        .catch((err) => console.log(err));
    },
  },
}).mount("#app");
