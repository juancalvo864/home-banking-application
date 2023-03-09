
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
                    this.finalAmount(this.dataClients.accounts)
                    console.log(this.dataClients.accounts[0].transactions.sort((a, b) => b.id - a.id).map(transaction => transaction.date))



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

        charts() {
            let options = {
                series: [this.dataClients.accounts[0].balance, this.dataClients.accounts[1].balance, this.dataClients.accounts[2].balance],
                chart: {
                    foreColor: '#e6e5de',
                    width: 500,
                    type: 'pie',
                },
                labels: [this.dataClients.accounts[0].number, this.dataClients.accounts[1].number, this.dataClients.accounts[2].number],
                responsive: [{
                    breakpoint: 480,
                    options: {
                        chart: {
                            width: 200

                        },
                        legend: {
                            position: 'bottom'
                        }
                    }
                }]
            };


            let chart = new ApexCharts(document.querySelector("#chart"), options);
            console.log(chart)
            chart.render();

        },




    },
    mounted() {
        this.charts()

    }



}).mount("#app")


/* 
let options = {
    chart: {
        type: 'line'
    },
    stroke: {
        curve: 'smooth',
    },
    series: [{
        name: 'Main account',
        data: this.dataClients.accounts[0].transactions.sort((a, b) => b.id - a.id).map(transaction => transaction.amount.toFixed(0))
    },
    {
        name: 'Other account',
        data: this.dataClients.accounts[1].transactions.sort((a, b) => b.id - a.id).map(transaction => transaction.amount.toFixed(0))

    ],
    xaxis: {
        categories: this.dataClients.accounts[0].transactions.sort((a, b) => b.id - a.id).map(transaction => this.dateTimeTransactions(transaction.date))
    },
    dropShadow: {
        enabled: true,
        top: 0,
        left: 0,
        blur: 3,
        opacity: 0.5
    }
} */
