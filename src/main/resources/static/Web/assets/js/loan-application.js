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
      amountLoan: null,
      accountLoan: "",
      loansInfo: [
        {
          title: "Mortgage loans",
          description:
            "This loan will allow you to buy a real estate property, build it from scratch or make a total renovation of your current home.",
          imgSrc: "./assets/img/icons/icons8-hipoteca-50.png",
          payments: [12, 24, 36, 48, 60],
          maxAmount: 500_000,
        },
        {
          title: "Personal loans",
          description:
            "This loan will allow you to use the funds for various purposes, such as buying a car, making home improvements, paying off debts, among others.",
          imgSrc: "./assets/img/icons/icons8-crecimiento-personal-50.png",
          payments: [6, 12, 24],
          maxAmount: 100_000,
        },
        {
          title: "Automotive loans",
          description:
            "This loan will allow you to buy a car up to 1000kg of load, they can be 0 km or up to 10 years old. Leave your car as collateral and get a discount on the interest rate.",
          imgSrc: "./assets/img/icons/icons8-garaje-50.png",
          payments: [6, 12, 24, 36],
          maxAmount: 300_000,
        },
      ],
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
            .then(() => (location.href = "./accounts.html"))
            .catch((err) => {
              console.log(err);
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
    parseInterest(float) {
      let number = float.toString().split(".")[1];
      if (number.length < 2) {
        return number * 10 + "%";
      } else {
        return number + "%";
      }
    },
  },
}).mount("#app");
