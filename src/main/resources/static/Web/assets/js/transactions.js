const {createApp} = Vue;

createApp ({
    data (){
        return {
            client: {},
            account : {},
            accountId : "",
        }
    },
    created (){
        let script = document.createElement('script');
        script.setAttribute('src', 'assets/js/argon-dashboard.js')
        document.head.appendChild(script)
        this.getClient();
        this.getURLId();
        this.getAccount();
    },
    methods : {
        getClient (){
            axios
            .get("http://localhost:8080/api/clients/1")
            .then(response => {
                this.client = response.data
                this.clientId = response.data.id
            })
            .catch(err => console.log(err))
        },
        getURLId (){
            let stringUrlWithID = location.search;
            let generateUrl = new URLSearchParams(stringUrlWithID);
            this.accountId = generateUrl.get("id");  
        },
        getAccount (){
            axios
            .get(`http://localhost:8080/web/accounts.html/${this.accountId}`)
            .then(response => {
                this.account = response.data
                console.log(response.data)
            } )
            .catch(err => {console.log(err)})
        }
    }
}).mount("#app")