




const { createApp } = Vue
createApp({
    data() {
        return {
            dataClients: [],
            accounts: [],
            clients: [],
            transactions: null,
            accountSelect: null,

        }
    },
    created() {
        this.loadData();
        this.params();


    },
    methods: {
        loadData() {
            axios.get("http://localhost:8080/api/clients/1")
                .then(res => {
                    this.dataClients = res
                    this.clients = this.dataClients.data
                    this.accounts = this.clients.accounts

                })
        },
        params() {
            let parameterUrl = location.search
            let parameters = new URLSearchParams(parameterUrl)
            let id = parameters.get("id")
            axios.get("http://localhost:8080/api/accounts/" + id)
                .then(res => {
                    this.accountSelect = res.data
                    this.transactions = res.data.transactions.sort((a, b) => a.id - b.id);

                    console.log(this.accountSelect)
                    console.log(this.transactions)
                })
        },
        dateTimeTransactions(date) {
            let template = date.split("T")
            let time = template[1].split(".")[0]
            return `${template[0]} / ${time} hs`

        },
        dateTransactions(date) {
            let template = date.split("T")
            return `${template[0]}`

        },
        dateTimeAccounts(date) {
            let template = date.split("T")
            return `${template[0]}`
        },
        logout() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
        },
        alert() {
            Swal.fire({
                title: 'Are you sure?',
                text: "Is closing the sesion",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes'
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = "/web/index.html"
                }
            })
        },


    },



}).mount("#app")

