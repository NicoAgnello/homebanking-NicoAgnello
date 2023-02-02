const {createApp} = Vue;

createApp ({
    data (){
        return {
            client: {},
            // clientId : 0,
        }
    },
    created (){
        let script = document.createElement('script');
        script.setAttribute('src', 'assets/js/argon-dashboard.js')
        document.head.appendChild(script)
        this.getClient()
    },
    methods : {
        getClient (){
            axios
            .get("http://localhost:8080/api/clients/1")
            .then(response => {
                this.client = response.data
                // this.clientId = response.data.id
            })
            .catch(err => console.log(err))
        },
        parseDate(fecha){
            let date = fecha.split("T")[0];
            let newDate = date.split("-").reverse().join("/");
            return newDate;
        }
    }
}).mount("#app")