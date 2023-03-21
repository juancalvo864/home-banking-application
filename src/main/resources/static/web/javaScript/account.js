
const { createApp } = Vue

createApp({


    data() {
        return {
            dataClient: [],
            accounts: [],
            accountSelect: null,
            startDate: null,
            endDate: null,
            dateValueCalendar: null,
            transactionFilter: [],
            id: "",

        }
    },
    created() {
        let parameterUrl = location.search
        let parameters = new URLSearchParams(parameterUrl)
        this.id = parameters.get("id")
        this.loadData();

    },

    methods: {
        loadData() {
            axios.get("http://localhost:8080/api/clients/current")
                .then(res => {
                    this.dataClient = res.data
                    this.accounts = this.dataClient.accounts
                    this.params();


                })
        },

        filterByDate() {

            this.dateValueCalendar = this.dateValueCalendar.split(" ")
            this.startDate = this.dateValueCalendar[0].replace('Z', '')
            this.endDate = this.dateValueCalendar[2].replace('Z', '')

            axios.get(`/api/account/` + this.id + `?startDate=${this.startDate}&endDate=${this.endDate}`,
                { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(res => {
                    this.transactionFilter = res.data
                    console.log(this.transactionFilter)
                })

        },

        deleteAcoount() {
            axios.patch('/api/clients/current/accounts', `accountId=${this.id}`,
                { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(res => {
                    Swal.fire(res.data).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = "/web/accounts.html"
                        }
                    })

                })
                .catch(error => {
                    Swal.fire(error.response.data)
                    console.error(error.response.data)
                })


        },

        params() {

            axios.get("http://localhost:8080/api/accounts/" + this.id)
                .then(res => {
                    this.accountSelect = res.data
                    this.transactionFilter = this.accountSelect.transactions

                })
        },

        pdf() {
            let element = document.getElementById('pdf-table');
            let opt = {
                margin: [0, 0, 5, 0],
                filename: 'numbaTransaction.pdf',
                image: { type: 'jpeg', quality: 0.98 },
                html2canvas: { scale: 1 },
                jsPDF: { unit: 'in', format: 'a4', orientation: 'portrait' }
            };
            // New Promise-based usage:
            html2pdf().from(element).set(opt).save();
        },

        flicker() {
            flatpickr(".datepicker", {
                mode: "range",
                altInput: true,
                altFormat: "F j, Y",
                enableTime: true,
                dateFormat: "Z",
            });
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
                    data: this.transactionFilter.sort((a, b) => b.id - a.id).filter(transaction => transaction.type == "CREDIT").map(transaction => transaction.amount.toFixed(0))
                },
                {

                    name: "Debit",
                    data: this.transactionFilter.sort((a, b) => b.id - a.id).filter(transaction => transaction.type == "CREDIT").filter(transaction => transaction.type == "DEBIT").map(transaction => transaction.amount.toFixed(0))
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


    },

    mounted() {
        this.flicker()
        this.charts()

    },



}).mount("#app")

