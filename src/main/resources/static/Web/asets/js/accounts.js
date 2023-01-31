const {createApp} = Vue;

createApp({
    data (){
        return{
            client : {}
        }
    },
    created (){
        this.getClient()
    },
    methods: {
        getClient (){
            axios
            .get("http://localhost:8080/api/clients/1")
            .then(response => {
                this.client = response.data
            })
            .catch(err => console.log(err))
        }
    }
}).mount("#app")