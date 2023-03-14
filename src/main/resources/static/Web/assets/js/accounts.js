const { createApp } = Vue;

createApp({
  data() {
    return {
      client: {},
      clientLoans: [],
      accounts: [],
      accountType: "",
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
          this.accounts = this.client.accounts.filter((account) => account.active == true);
          this.accounts.sort((a, b) => b.id - a.id);
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
    newAccount() {
      Swal.fire({
        title: "Type of account",
        input: "select",
        inputOptions: {
          SAVING: "Savings Account",
          CHECKING: "Checking Account",
        },
        inputPlaceholder: "Select a type of account",
        showCancelButton: true,
        confirmButtonText: "Create",
      }).then((result) => {
        if (result.isConfirmed) {
          this.accountType = result.value;
          axios
            .post("/api/clients/current/accounts", `accountType=${this.accountType}`)
            .then(() => {
              this.getClient();
            })
            .catch((err) => {
              console.log(err);
            });
        }
      });
    },
    confirmDeleteAccount(id) {
      Swal.fire({
        title: "¿Are you sure you want to delete this account?",
        text: "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete it!",
      }).then((result) => {
        if (result.isConfirmed) {
          axios
            .patch(`/api/clients/current/accounts/${id}`)
            .then(() => {
              Swal.fire("Deleted!", "Your file has been deleted.", "success");
              this.getClient();
            })
            .catch((err) => console.log(err));
        }
      });
    },
  },
}).mount("#app");
