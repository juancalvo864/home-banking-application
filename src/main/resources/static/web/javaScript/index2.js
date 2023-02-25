
const { createApp } = Vue
createApp({
    data() {
        return {
            dataClients: [],

            clients: [],
            transactions: null,
            transactions1: null,
            account1: null,
            accountNext: [],
            accounts: [],
            fechaHora: null,
            loans: null,
            balance: [],
            totalBalance: null,
            cards: []
        }
    },
    created() {
        this.loadData();
        this.finalAmount()
        console.log(this.totalBalance)







    },
    methods: {
        loadData() {
            axios.get("http://localhost:8080/api/clients/1")
                .then(res => {
                    this.dataClients = res
                    this.clients = this.dataClients.data
                    this.accounts = this.clients.accounts.sort((a, b) => a.id - b.id)
                    this.account1 = this.accounts.find(account => account.id == 1)
                    this.accountNext = this.accounts.slice(1)
                    this.balance = this.accounts.map(account => account.balance)
                    console.log(this.balance)
                    this.transactions1 = this.account1.transactions
                    this.transactions1.sort((a, b) => a.id - b.id);
                    this.transactions1 = this.transactions1.slice(0, 2)
                    this.loans = this.clients.loans
                    this.cards = this.clients.cards
                    console.log(this.cards)
                })
        },
        params() {
            let parameterUrl = location.search
            let parameters = new URLSearchParams(parameterUrl)
            let id = parameters.get("id")
            axios.get("http://localhost:8080/api/accounts/" + id)
                .then(res => {
                    this.transactions = res.data.transactions
                    this.transactions.sort((a, b) => a.id - b.id);

                })
        },
        logout() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
        },
        dateTimeTransactions(date) {
            let template = date.split("T")
            return `${template[0]}`

        },

        dateTimeAccounts(date) {
            let template = date.split("T")
            return `${template[0]} `
        },

        dateYear(date) {
            let template = date.split(4)

            return `${template[0]}`

        },


        finalAmount() {
            let template = 0
            for (balance of this.accounts) {
                template += balance.amount
            }
            this.totalBalance = template
        },



    },



}).mount("#app")
