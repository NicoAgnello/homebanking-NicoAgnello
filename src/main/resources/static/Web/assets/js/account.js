const { createApp } = Vue;

createApp({
  data() {
    return {
      client: {},
      accountId: "",
      account: {},
      clientLoans: [],
      transactions: [],
      startDate: "",
      endDate: "",
    };
  },
  created() {
    this.getClient();
    this.getAccounts();
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
    getAccounts() {
      let stringUrlWithID = location.search;
      let generateUrl = new URLSearchParams(stringUrlWithID);
      this.accountId = generateUrl.get("id");
      axios
        .get("/api/clients/current/accounts")
        .then((response) => {
          this.account = response.data.find((account) => account.id == this.accountId);
          this.transactions = this.account.transactions.sort((a, b) => b.id - a.id);
        })
        .catch((err) => console.log(err));
    },
    parseDate(fecha) {
      let date = fecha.split("T")[0];
      let newDate = date.split("-").reverse().join("/");
      return newDate;
    },
    parseTime(fecha) {
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
    filterTransactions() {
      let startDate = this.startDate ? new Date(this.startDate).toJSON() : "";
      let endDate = this.endDate ? new Date(this.endDate).toJSON() : "";
      axios
        .get(`/api/transactions/filter?accountId=${this.accountId}&startDate=${startDate}&endDate=${endDate}`)
        .then((res) => {
          this.transactions = res.data.sort((a, b) => b.id - a.id);
        })
        .catch((err) => console.log(err));
    },
    generatePDF() {
      const elementToConvert = document.getElementById("table-transactions");
      let opt = {
        margin: 1,
        filename: "transactions-MB.pdf",
        image: { type: "jpeg", quality: 1 },
        html2canvas: { scale: 2 },
        jsPDF: { unit: "in", format: "a4", orientation: "portrait" },
      };
      html2pdf()
        .set(opt)
        .from(elementToConvert)
        .save()
        .catch((err) => console.log(err));
    },
  },
}).mount("#app");
