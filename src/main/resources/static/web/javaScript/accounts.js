
const { createApp } = Vue
createApp({

    components: { Datepicker: VueDatePicker },
    data() {
        return {
            dataExchange: null,
            dataClient: null,
            accounts: null,
            loans: null,
            cards: null,
            exchange: null,
            totalBalance: 0,
            date: null,
            type: ""

        }
    },
    created() {
        this.loadData();



    },
    methods: {
        loadData() {
            axios.all([
                axios.get('/api/clients/current'),
                axios.get('https://api.apilayer.com/exchangerates_data/timeseries?base=USD&start_date=2023-03-01&end_date=2023-03-20&symbols=EUR,GBP,ARS', { headers: { 'apikey': 'bBUZoSyHTESo4H7elpuiGhpDcZMkVJok' } })

            ])
                .then(axios.spread((response1, response2) => {
                    this.dataClient = response1.data
                    this.dataExchange = response2.data
                    this.loans = this.dataClient.loans
                    this.cards = this.dataClient.cards
                    this.finalAmount(this.dataClient.accounts)
                    this.sortArray(this.dataClient.accounts)
                    this.infoExchange()
                    console.log(this.dataClient.accounts)


                }))
        },


        createAccount() {
            axios.post('/api/clients/current/accounts', `accountType=${this.type}`,
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


        logout() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
        },


        sortArray(data) {
            data.sort((a, b) => a.id - b.id)
        },

        infoExchange() {
            let exchange = Object.entries(this.dataExchange.rates)
            this.exchange = exchange.slice(-1)
            this.exchange = this.exchange[0]
            this.exchange = this.exchange[1]

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

        checkExpire(info) {
            let date = new Date()
            return info.thruDate < date.toISOString()
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
            let seriesBalance = this.dataClient.accounts.map(account => account.balance)
            let labelsNumber = this.dataClient.accounts.map(account => account.number)

            let options = {
                series: seriesBalance,
                chart: {
                    foreColor: '#e6e5de',
                    width: 500,
                    type: 'pie',
                },
                labels: labelsNumber,
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


        chartEur() {
            let fechasEur = [];
            for (let [key, value] of Object.entries(this.dataExchange.rates)) {
                fechasEur.push(value.EUR)
            };

            let options2 = {
                series: [{
                    data: fechasEur,
                }],
                chart: {
                    type: 'line',
                    width: 100,
                    height: 35,
                    sparkline: {
                        enabled: true
                    }
                },
                tooltip: {
                    fixed: {
                        enabled: false
                    },
                    x: {
                        show: false
                    },
                    y: {
                        title: {
                            formatter: function (seriesName) {
                                return ''
                            }
                        }
                    },
                    marker: {
                        show: false
                    }
                }
            };

            var chart2 = new ApexCharts(document.querySelector("#chart-1"), options2);
            chart2.render();
        },

        chartGbp() {
            let fechasGbp = [];
            for (let [key, value] of Object.entries(this.dataExchange.rates)) {
                fechasGbp.push(value.GBP)
            };

            let options2 = {
                series: [{
                    data: fechasGbp,
                }],
                chart: {
                    type: 'line',
                    width: 100,
                    height: 35,
                    sparkline: {
                        enabled: true
                    }
                },
                tooltip: {
                    fixed: {
                        enabled: false
                    },
                    x: {
                        show: false
                    },
                    y: {
                        title: {
                            formatter: function (seriesName) {
                                return ''
                            }
                        }
                    },
                    marker: {
                        show: false
                    }
                }
            };

            var chart2 = new ApexCharts(document.querySelector("#chart-2"), options2);
            chart2.render();
        },

        chartArs() {
            let fechasArs = [];
            for (let [key, value] of Object.entries(this.dataExchange.rates)) {
                fechasArs.push(value.ARS)
            };

            let options2 = {
                series: [{
                    data: fechasArs,
                }],
                chart: {
                    type: 'line',
                    width: 100,
                    height: 35,
                    sparkline: {
                        enabled: true
                    }
                },
                tooltip: {
                    fixed: {
                        enabled: false
                    },
                    x: {
                        show: false
                    },
                    y: {
                        title: {
                            formatter: function (seriesName) {
                                return ''
                            }
                        }
                    },
                    marker: {
                        show: false
                    }
                }
            };

            var chart2 = new ApexCharts(document.querySelector("#chart-3"), options2);
            chart2.render();
        }


    },
    mounted() {
        this.charts()
        this.chartEur()
        this.chartGbp()
        this.chartArs()

    },



}).mount("#app")



