const { createApp } = Vue;

createApp({
  data() {
    return {
      jsonClients: [],
      clients: [],
      firstName: "",
      lastName: "",
      email: "",
      clientDetails: {},
      firstNameEdit: "",
      lastNameEdit: "",
      emailEdit: "",
    };
  },
  created() {
    this.loadData();
  },
  methods: {
    loadData() {
      axios
        .get("http://localhost:8080/rest/clients")
        .then((response) => {
          this.jsonClients = response;
          this.clients = response.data._embedded.clients;
        })
        .catch((error) => console.log(error));
    },

    validateEmail(email) {
      return /^[a-zA-Z0-9.!#$%&'+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:.[a-zA-Z0-9-]+)$/.test(email);
    },
    
    addClient() {
      if (this.validateEmail(this.email)) {
        this.postClient();
      }
    },

    postClient() {
      axios
        .post("http://localhost:8080/rest/clients", {
          firstName: this.firstName,
          lastName: this.lastName,
          email: this.email,
        })
        .then(() => this.loadData())
        .catch((error) => console.log(error));
    },

    getClientId(client) {
      const id = client._links.client.href.split("/").pop();
      return id;
    },

    removeClient(id) {
      axios
        // .delete(`http://localhost:8080/rest/clients/${id}/accounts`)
        .delete(`http://localhost:8080/rest/clients/${id}`)
        .then(() => this.loadData())
        .catch((err) => console.log(err));
    },

    bringClient(id) {
      this.clientDetails = this.clients.filter((client) => this.getClientId(client) == id)[0];
      axios
        .get(`/rest/clients/${id}`)
        .then((res) => {
          this.firstNameEdit = res.data.firstName;
          this.lastNameEdit = res.data.lastName;
          this.emailEdit = res.data.email;
        })
        .catch((err) => console.log(err));
    },

    editClient(id) {
      if (this.validateEmail(this.emailEdit)) {
        axios
          .put(`http://localhost:8080/rest/clients/${id}`, {
            firstName: this.firstNameEdit,
            lastName: this.lastNameEdit,
            email: this.emailEdit,
          })
          .then(() => this.loadData())
          .catch((err) => console.log(err));
      }
    },
  },
  computed: {},
}).mount("#app");
