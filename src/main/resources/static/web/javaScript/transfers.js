const { createApp } = Vue
createApp({
    data() {
        return {
            dataClients: [],
            totalBalance: 0,
            accountOut: "Open this select menu",
            accountIn: null,
            amountTranfer: null,
            description: "",
            isChecked: false

        }
    },
    created() {
        this.loadData();




    },
    methods: {
        loadData() {
            axios.get("http://localhost:8080/api/clients/current")
                .then(res => {
                    this.dataClients = res.data
                    this.finalAmount(this.dataClients.accounts)
                })
        },

        newtransfer() {
            axios.post('/api/clients/transaction', `description=${this.description}&amount=${this.amountTranfer}.0&numberAccountOut=${this.accountOut}&numberAccountIn=${this.accountIn}`,
                { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    Swal.fire(response.data)
                    window.location.href = '/web/transfers.html';
                })
                .catch(error => {
                    Swal.fire(error.response.data)
                    window.location.href = '/web/transfers.html';
                })
        },

        finalAmount(accounts) {
            let template = 0
            for (data of accounts) {
                template += data.balance
            }
            this.totalBalance = template
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

        newTrasferAlert() {
            Swal.fire({
                title: 'Are you sure?',
                text: "Confirm the transaction",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Confirm!'
            }).then((result) => {
                if (result.isConfirmed) {
                    this.newtransfer()

                }
            })
        }
    },





}).mount("#app")