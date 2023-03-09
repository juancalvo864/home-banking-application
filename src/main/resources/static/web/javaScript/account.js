




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
        charts() {
            let optionsLine = {
                chart: {
                    foreColor: '#e6e5de',
                    height: 328,
                    type: 'line',
                    zoom: {
                        enabled: false
                    },
                    dropShadow: {
                        enabled: true,
                        top: 3,
                        left: 2,
                        blur: 4,
                        opacity: 1,
                    }
                },
                stroke: {
                    curve: 'smooth',
                    width: 2
                },
                colors: ["#44ca83", '#ff0a0a'],
                series: [{

                    name: "Credit",
                    data: this.accountSelect.transactions.sort((a, b) => b.id - a.id).filter(transaction => transaction.type == "CREDIT").map(transaction => transaction.amount.toFixed(0))
                },
                {

                    name: "Debit",
                    data: this.accountSelect.transactions.filter(transaction => transaction.type == "DEBIT").map(transaction => transaction.amount.toFixed(0))
                },





                ],
                title: {
                    text: 'Transactions',
                    align: 'center',
                    offsetY: 25,
                    offsetX: 20

                },
                subtitle: {
                    text: 'Amount',
                    offsetY: 55,
                    offsetX: 20
                },
                markers: {
                    size: 6,
                    strokeWidth: 0,
                    hover: {
                        size: 9
                    }
                },
                grid: {
                    show: true,
                    padding: {
                        bottom: 0
                    }
                },
                labels: Array.from(new Set(this.accountSelect.transactions.map(transaction => this.dateTransactions(transaction.date)))),
                xaxis: {
                    tooltip: {
                        enabled: false
                    }
                },
                legend: {
                    position: 'top',
                    horizontalAlign: 'right',
                    offsetY: -20
                }
            }

            let chartLine = new ApexCharts(document.querySelector('#line-adwords'), optionsLine);
            chartLine.render();


        },



    },
    mounted() {
        this.charts()

    }



}).mount("#app")

