const { createApp } = Vue;

createApp({
  data() {
    return {
      jsonLoans: [],
      loanName: "",
      maxAmountLoan: "",
      paymentsLoan: "",
      interestRateLoan: "",
    };
  },
  created() {
    this.loadData();
  },
  methods: {
    loadData() {
      axios
        .get("http://localhost:8080/api/loans")
        .then((response) => {
          this.jsonLoans = response;
        })
        .catch((error) => console.log(error));
    },
    newLoan() {
      Swal.fire({
        title: "Are you sure you want to apply for this loan?",
        showDenyButton: true,
        confirmButtonText: "Apply Loan",
        denyButtonText: `Cancel loan`,
        imageUrl: "./Web/assets/img/home/g10.svg",
        imageWidth: 400,
        imageHeight: 200,
        confirmButtonColor: "#1F7241",
      }).then((result) => {
        if (result.isConfirmed) {
          axios
            .post("/api/loans/add", {
              loanName: this.loanName,
              maxAmount: this.maxAmountLoan,
              payments: this.paymentsLoan.split(",").map((num) => Number(num)),
              interestRate: this.interestRateLoan,
            })
            .then(() => {
              Swal.fire("Loan created!", "", "success");
              this.loadData();
            })
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
            location.href = "./web/index.html";
          });
        })
        .catch((err) => console.log(err));
    },
  },
}).mount("#app");
