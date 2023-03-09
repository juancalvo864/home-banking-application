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
                axios.get('http://localhost:8080/api/clients/current'),
                axios.get('http://localhost:8080/api/loans')
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

                    window.location.href = '/web/loanApplication.html';
                })
                .catch(error => console.error(error))
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
            this.interestPay = (this.amountLoan / this.paymentsLoan)
            this.amountPercent = this.amountLoan * 1.2
            console.log(this.amountLoan)
            console.log(this.paymentsLoan)
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