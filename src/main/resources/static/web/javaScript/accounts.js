
const { createApp } = Vue
createApp({
    data() {
        return {
            data: null,
            dataClients: null,
            accounts: null,
            loans: null,
            cards: null,
            totalBalance: 0,

        }
    },
    created() {
        this.loadData();



    },
    methods: {
        loadData() {
            axios.get("http://localhost:8080/api/clients/current")
                .then(res => {
                    this.data = res
                    this.dataClients = res.data
                    this.sortArray(this.dataClients.accounts)
                    this.loans = this.dataClients.loans
                    this.cards = this.dataClients.cards
                    console.log(this.cards)
                    this.finalAmount(this.dataClients.accounts)
                })
        },
        createAccount() {
            axios.post('/api/clients/current/accounts',
                { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    window.location.href = '/web/accounts.html';
                })
        },


        sortArray(data) {
            data.sort((a, b) => a.id - b.id)
        },

        logout() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
        },
        dateTimeTransactions(date) {
            let template = date.split("T")
            return `${template[0]}`

        },


        finalAmount(accounts) {
            let template = 0
            for (data of accounts) {
                template += data.balance
            }
            this.totalBalance = template
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