const { createApp } = Vue;

createApp({
  data() {
    return {
      email: "",
      password: "",
      userFirstName: "",
      userLastName: "",
      userEmail: "",
      userPassword: "",
    };
  },
  created() {
    this.loginOrRegister();
  },
  methods: {
    logIn() {
      axios
        .post("/api/login", `email=${this.email}&password=${this.password}`, {
          headers: { "content-type": "application/x-www-form-urlencoded" },
        })
        .then((response) => {
          location.href = "./accounts.html";
        })
        .catch((error) => this.modal(error.response));
    },
    register() {
      axios
        .post(
          "/api/clients",
          `firstName=${this.userFirstName}&lastName=${this.userLastName}&email=${this.userEmail}&password=${this.userPassword}`,
          {
            headers: { "content-type": "application/x-www-form-urlencoded" },
          }
        )
        .then((response) => {
          axios
            .post("/api/login", `email=${this.userEmail}&password=${this.userPassword}`, {
              headers: { "content-type": "application/x-www-form-urlencoded" },
            })
            .then((response) => {
              location.href = "./accounts.html";
            })
            .catch((error) => this.modal(error.response));
        })
        .catch((err) => console.log(err));
    },
    modal(error) {
      if (!this.password || this.validarMail) {
        Swal.fire({
          title: "User not found",
          text: `${error.data.error}` + " " + `${error.status}`,
          icon: "error",
          confirmButtonText: "OK",
        });
      }
    },
    validarMail() {
      return /^[a-zA-Z0-9.!#$%&'+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:.[a-zA-Z0-9-]+)$/.test(this.email);
    },
    loginOrRegister() {
      $(document).ready(function () {
        $(".veen .rgstr-btn button").click(function () {
          $(".veen .wrapper").addClass("move");
          $(".body").css("background", "#6c87b9");
          $(".veen .login-btn button").removeClass("active");
          $(this).addClass("active");
        });
        $(".veen .login-btn button").click(function () {
          $(".veen .wrapper").removeClass("move");
          $(".body").css("background", "#2e4783");
          $(".veen .rgstr-btn button").removeClass("active");
          $(this).addClass("active");
        });
      });
    },
  },
}).mount("#app");
