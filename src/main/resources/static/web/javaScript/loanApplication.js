const { createApp } = Vue
createApp({
    data() {
        return {
            dataClient: [],
            dataLoans: [],
            dataPayments: [],
            maxAmount: 0,
            interestPay: 0,
            amountPercent: 0,
            loanId: "Type loan",
            amountLoan: null,
            paymentsLoan: null,
            accountIn: null,


        }
    },
    created() {
        this.loadData();




    },
    methods: {
        loadData() {
            axios.all([
                axios.get('/api/clients/current'),
                axios.get('/api/loans')
            ])
                .then(axios.spread((response1, response2) => {
                    this.dataClient = response1.data
                    this.dataLoans = response2.data
                    /*                  console.log(this.dataClient);
                                     console.log(this.dataLoans); */
                }))
                .catch(error => {
                    console.log(error);
                });
        },

        /*----------------NEW LOAN----------------*/

        newLoan() {
            axios.post('/api/clients/current/loans', {
                id: this.loanId,
                amount: this.amountLoan,
                payments: this.paymentsLoan,
                numberAccountIn: this.accountIn,
            },
                { headers: { 'content-type': 'application/json' } })
                .then(() => { this.CheckLoanApplication() })
                .then(() => {
                    // Espera 1000 milisegundos (1 segundo)
                    return new Promise((resolve) => setTimeout(resolve, 1000));
                })
                .then(response => {
                    window.location.href = '/web/accounts.html';
                })
                .catch(error => {
                    Swal.fire(error.response.data).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = "/web/loanApplication.html"
                        }
                    })
                    console.error(error.response.data)
                })
        },




        /*--------------------SELECT PAYMENT-----------------*/

        selectPayment() {
            for (let i = 0; i < this.dataLoans.length; i++) {
                if (this.dataLoans[i].id === this.loanId) {
                    this.dataPayments = this.dataLoans[i].payments
                    this.maxAmount = this.dataLoans[i].maxAmount
                    break;
                }
            }
        },

        /*------------------INTEREST PYMENT--------------------*/

        interestPyment() {
            let selectLoan = this.dataLoans.find(loan => loan.id == this.loanId)
            this.amountPercent = (this.amountLoan * selectLoan.porcentage / 100) + this.amountLoan
            this.interestPay = (this.amountPercent / this.paymentsLoan)
        },


        /*-----------------MAX AMOUNT LOAN---------------*/

        maxAmountValue() {
            let template;
            if (this.amountLoan > this.maxAmount) {
                this.amountExceeded()
                this.amountLoan = null;
            }
        },

        /*-------------------LOGOUT----------------------------*/

        logout() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
        },


        /*--------------------ALERTS -----------------------------*/

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

        CheckLoanApplication() {
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'The loan was credit to your account',
                showConfirmButton: false,
                timer: 1500
            })
        },

        amountExceeded() {
            Swal.fire('The amount entered exceeds the maximun amount of the loan!!')
        }


    },



}).mount("#app")