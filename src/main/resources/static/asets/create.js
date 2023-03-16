const { createApp } = Vue;

createApp({
  data() {
    return {
      jsonClients: [],
      clients: [],
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
  },
  computed: {},
}).mount("#app");
