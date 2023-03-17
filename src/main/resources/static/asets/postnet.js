const { createApp } = Vue;

createApp({
  data() {
    return {
      currentCardBackground: Math.floor(Math.random() * 25 + 1), // just for fun :D
      cardNumber: "",
      cardCvv: "",
      amount: "",
      description: "",
      minCardYear: new Date().getFullYear(),
      amexCardMask: "#### ##### ####",
      otherCardMask: "#### #### #### ####",
      cardNumberTemp: "",
      isCardFlipped: false,
      focusElementStyle: null,
      isInputFocused: false,
    };
  },
  created() {},
  mounted() {
    this.cardNumberTemp = this.otherCardMask;
    document.getElementById("cardNumber").focus();
  },
  methods: {
    flipCard(status) {
      this.isCardFlipped = status;
    },
    focusInput(e) {
      this.isInputFocused = true;
      let targetRef = e.target.dataset.ref;
      let target = this.$refs[targetRef];
    },
    blurInput() {
      let vm = this;
      setTimeout(() => {
        if (!vm.isInputFocused) {
          vm.focusElementStyle = null;
        }
      }, 300);
      vm.isInputFocused = false;
    },
    confirmPayWithPostnet() {
      Swal.fire({
        title: "Are you sure ?",
        showDenyButton: true,
        confirmButtonText: "PAY",
        denyButtonText: `Cancel`,
        imageUrl: "./Web/assets/img/home/g10.svg",
        imageWidth: 400,
        imageHeight: 200,
        confirmButtonColor: "#1F7241",
      }).then((result) => {
        if (result.isConfirmed) {
          axios
            .post("http://localhost:8080/api/cards/postnet", {
              cardNumber: this.cardNumber,
              cvv: this.cardCvv,
              amount: this.amount,
              description: this.description,
            })
            .then(() => {
              Swal.fire("Successful payment!", "", "success");
            })
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
  computed: {
    getCardType() {
      let number = this.cardNumber;
      let re = new RegExp("^4");
      if (number.match(re) != null) return "visa";

      re = new RegExp("^(34|37)");
      if (number.match(re) != null) return "amex";

      re = new RegExp("^5[1-5]");
      if (number.match(re) != null) return "mastercard";

      re = new RegExp("^6011");
      if (number.match(re) != null) return "discover";

      re = new RegExp("^9792");
      if (number.match(re) != null) return "troy";

      return "visa"; // default type
    },
    generateCardNumberMask() {
      return this.getCardType === "amex" ? this.amexCardMask : this.otherCardMask;
    },
    minCardMonth() {
      if (this.cardYear === this.minCardYear) return new Date().getMonth() + 1;
      return 1;
    },
  },
  watch: {
    cardYear() {
      if (this.cardMonth < this.minCardMonth) {
        this.cardMonth = "";
      }
    },
  },
}).mount("#app");
