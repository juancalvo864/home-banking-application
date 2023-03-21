const { createApp } = Vue
createApp({
    data() {
        return {
            dataClient: [],
            amountLoan: 0,
            payment: [],
            interest: 0,
            nameLoan: "Name Loan",



        }
    },
    created() {
        this.loadData();




    },
    methods: {
        loadData() {

        },

        /*----------------NEW LOAN----------------*/



        newLoanAdmin() {

            this.payment = this.payment.split(",")

            axios.post('/api/admin/loans/new', {
                name: this.nameLoan,
                amount: this.amountLoan,
                payments: this.payment,
                porcentage: this.interest,
            },
                { headers: { 'content-type': 'application/json' } })
                .then(response => {
                    Swal.fire({
                        title: 'Are you sure?',
                        text: response.data,
                        icon: 'warning',
                        showCancelButton: true,
                        confirmButtonColor: '#3085d6',
                        cancelButtonColor: '#d33',
                        confirmButtonText: 'Yes, delete it!'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            Swal.fire(
                                'Created!',
                                'success'
                            )
                            window.location.href = "/web/adminLoan.html";
                        }
                    })

                })
                .catch(error => {
                    Swal.fire(error.response.data)
                    console.error(error.response.data)
                })
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




    },



}).mount("#app")