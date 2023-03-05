const { createApp } = Vue;

createApp({
  data() {
    return {
      client: {},
      accounts: [],
      typeTransaction: "Is the transaction to a proprietary account or a third party account?",
      originAccountNumber: "Open this menu",
      targetAccountNumber: "Open this menu",
      amount: "",
      description: "",
      accountOrigin: "",
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
        })
        .catch((err) => console.log(err));
    },
    getAccounts() {
      axios
        .get("/api/clients/current/accounts")
        .then((response) => {
          this.accounts = response.data;
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
          }).then((response) => {
            location.href = "./index.html";
          });
        })
        .catch((err) => console.log(err));
    },
    confirmTransaction() {
      Swal.fire({
        title: "Are you sure you want to make this transaction?",
        text: "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, I understand!",
      }).then((result) => {
        if (result.isConfirmed) {
          this.postTransaction();
        }
      });
    },
    postTransaction() {
      axios
        .post(
          "/api/transactions",
          `originAccountNumber=${this.originAccountNumber.toUpperCase()}&targetAccountNumber=${this.targetAccountNumber.toUpperCase()}&description=${
            this.description
          }&amount=${this.amount}`
        )
        .then(() => {
          Swal.fire({
            text: "Transaction successfully completed.",
            icon: "success",
          }).then(() => {
            location.href = "./accounts.html";
          });
        })
        .catch((err) => {
          Swal.fire({
            icon: "warning",
            title: `${err.response.data}`,
          });
        });
    },
    searchSourceAccount(number) {
      this.accountOrigin = this.accounts.find((acc) => acc.number == number);
    },
  },
}).mount("#app");
