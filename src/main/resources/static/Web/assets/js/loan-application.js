const { createApp } = Vue;

createApp({
  data() {
    return {
      client: "",
      clientLoans: "",
      accounts: "",
      loans: "",
      loan: "",
      typeLoans: "",
      paymentsLoan: "",
      amountLoan: 0,
      accountLoan: "",
    };
  },
  created() {
    let script = document.createElement("script");
    script.setAttribute("src", "assets/js/argon-dashboard.js");
    document.head.appendChild(script);
    this.getClient();
    this.getLoans();
  },
  methods: {
    getClient() {
      axios
        .get("/api/clients/current")
        .then((response) => {
          this.client = response.data;
          this.clientLoans = response.data.loans;
          this.accounts = this.client.accounts;
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
          }).then(() => {
            location.href = "./index.html";
          });
        })
        .catch((err) => console.log(err));
    },
    getLoans() {
      axios
        .get("/api/loans")
        .then((res) => {
          this.loans = res.data;
        })
        .catch((err) => console.log(err));
    },
    confirmLoan() {
      Swal.fire({
        title: "Are you sure you want to apply for this loan?",
        showDenyButton: true,
        confirmButtonText: "Apply Loan",
        denyButtonText: `Cancel loan`,
        imageUrl: "./assets/img/home/g10.svg",
        imageWidth: 400,
        imageHeight: 200,
        confirmButtonColor: "#1F7241",
      }).then((result) => {
        if (result.isConfirmed) {
          axios
            .post("/api/loans", {
              id: this.typeLoans.id,
              amount: this.amountLoan,
              payments: this.paymentsLoan,
              targetAccountNumber: this.accountLoan,
            })
            .then(() => Swal.fire("Loan aproved!", "", "success"))
            .catch((err) => {
              Swal.fire({
                icon: "error",
                title: "Oops...",
                text: err.response.data,
              });
            });
        } else if (result.isDenied) {
          Swal.fire("Loan cancelled", "", "warning");
        }
      });
    },
  },
}).mount("#app");
