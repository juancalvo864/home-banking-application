const { createApp } = Vue
createApp({
    data() {
        return {
            data: [],
            listClient: [],

            client: { firstName: "", lastName: "", email: "" },
            exitClient: { firstName: "", lastName: "", email: "", id: "" }
        }
    },
    created() {
        this.loadData();


    },
    methods: {
        loadData() {
            axios.get("http://localhost:8080/rest/clients")
                .then(res => {
                    this.data = res;
                    this.listClient = this.data.data._embedded.clients;
                    console.log(this.listClient)
                })
        },
        saveData() {
            if (this.client.firstName.length !== 0 && this.client.lastName.length !== 0 && this.client.email.length) {
                axios.post("http://localhost:8080/rest/clients", {
                    firstName: this.client.firstName,
                    lastName: this.client.lastName,
                    email: this.client.email,

                })
                    .then(res => { this.loadData() })
            } else {
                alert("No ha ingresado todos los datos")
            }
        },
        deleteClient(client) {
            if (confirm("Usted quiere eliminar el cliente : " + client.firstName + " " + client.lastName)) {
                axios.delete(client._links.client.href)
                    .then(res => { this.loadData() })

            }
        },
        modifyClient() {

            axios.put(this.exitClient.id, {
                firstName: this.exitClient.firstName,
                lastName: this.exitClient.lastName,
                email: this.exitClient.email,

            })
                .then(res => {
                    this.loadData()

                })
        },
        clientButton(client) {
            this.exitClient = { firstName: client.firstName, lastName: client.lastName, email: client.email, id: client._links.client.href }


        },




    },



}).mount("#app")

