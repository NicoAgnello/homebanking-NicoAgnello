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
    logIn(email, password) {
      axios
        .post("/api/login", `email=${email}&password=${password}`, {
          headers: { "content-type": "application/x-www-form-urlencoded" },
        })
        .then(() => {
          if ((this.email = "admin@midnhub.com")) {
            location.href = "./accounts.html";
          } else {
            location.href = "../manager.html";
          }
        })
        .catch(() => {
          this.modal();
        });
    },
    register() {
      if (this.validarMail(this.userEmail)) {
        axios
          .post(
            "/api/clients",
            `firstName=${this.userFirstName}&lastName=${this.userLastName}&email=${this.userEmail}&password=${this.userPassword}`,
            {
              headers: { "content-type": "application/x-www-form-urlencoded" },
            }
          )
          .then(() => {
            const Toast = Swal.mixin({
              toast: true,
              position: "top-end",
              showConfirmButton: false,
              timer: 3000,
              timerProgressBar: true,
              didOpen: (toast) => {
                toast.addEventListener("mouseenter", Swal.stopTimer);
                toast.addEventListener("mouseleave", Swal.resumeTimer);
              },
            });

            Toast.fire({
              icon: "success",
              title: "Signed in successfully",
            }).then(() => {
              this.logIn(this.userEmail, this.userPassword);
            });
          })
          .catch((err) => {
            Swal.fire({
              icon: "warning",
              title: `${err.response.data}`,
            });
          });
      } else {
        Swal.fire({
          icon: "error",
          title: "Oops...",
          text: "Recheck your data!",
        });
      }
    },
    modal() {
      Swal.fire({
        title: "User not found",
        text: `Missing or incorrect data`,
        icon: "error",
        confirmButtonText: "OK",
      });
    },
    validarMail(email) {
      return /^[a-zA-Z0-9.!#$%&'+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:.[a-zA-Z0-9-]+)$/.test(email);
    },
    loginOrRegister() {
      $(document).ready(function () {
        $(".veen .rgstr-btn button").click(function () {
          $(".veen .wrapper").addClass("move");
          $(".veen .login-btn button").removeClass("active");
          $(this).addClass("active");
        });
        $(".veen .login-btn button").click(function () {
          $(".veen .wrapper").removeClass("move");
          $(".veen .rgstr-btn button").removeClass("active");
          $(this).addClass("active");
        });
      });
    },
  },
}).mount("#app");
