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
          location.href = "./accounts.html";
        })
        .catch((error) => {
          this.modal(error.response);
          console.log(error.response.data.error + " " + error.response.data.status);
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
          .then((response) => {
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
            }).then((response) => {
              this.logIn(this.userEmail, this.userPassword);
            });
          })
          .catch((err) => console.log(err));
      } else {
        Swal.fire({
          icon: "error",
          title: "Oops...",
          text: "The email is invalid!",
        });
      }
    },
    modal(error) {
      // if (!this.password || this.validarMail(this.email)) {
      Swal.fire({
        title: "User not found",
        text: `${error.data.error}`,
        icon: "error",
        confirmButtonText: "OK",
      });
      // }
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
