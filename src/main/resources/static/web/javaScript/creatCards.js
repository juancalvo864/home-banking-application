const { createApp } = Vue
createApp({
    data() {
        return {
            dataClient: [],
            clients: [],
            cards: [],
            selectColor: "",
            selectType: "",
            type: "Select Type",
            color: "Select color",
            debit: null,
            credit: null,



        }
    },
    created() {
        this.loadData();


    },


    methods: {
        loadData() {
            axios.get("http://localhost:8080/api/clients/current")
                .then(res => {
                    this.dataClient = res.data
                    this.debitFilter()
                })
        },

        cardCreate() {
            axios.post('/api/clients/current/cards', `type=${this.type}&color=${this.color}`,
                { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(res => {
                    Swal.fire(res.data)
                })
                .catch(error => {
                    Swal.fire(error.response.data)
                })
        },



        logout() {
            axios.post('/api/logout').then(response => console.log('signed out!!!'))

        },

        selectCard() {
            if (this.type == "DEBIT") {
                this.selectType = "DEBIT"
            }
            if (this.type == "CREDIT") {
                this.selectType = "CREDIT"
            }
        },

        colorCard() {
            if (this.color == "GOLD") {
                this.selectColor = "GOLD"
            }
            if (this.color == "SILVER") {
                this.selectColor = "SILVER"
            }
            if (this.color == "TITANIUM") {
                this.selectColor = "TITANIUM"
            }
        },

        debitFilter() {
            this.debit = this.dataClient.cards.filter(card => card.type == "DEBIT").map(card => card.color)
            this.credit = this.dataClient.cards.filter(card => card.type == "CREDIT").map(card => card.color)

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
                    this.logout()
                    window.location.href = "/web/index.html"
                }
            })
        },



        sureCreateCard() {
            Swal.fire({
                title: 'Are you sure?',
                text: "Create a new card?",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'yes I want'
            }).then((result) => {
                if (result.isConfirmed) {
                    this.cardCreate()
                }
            }).then(() => {
                // Espera 1000 milisegundos (1 segundo)
                return new Promise((resolve) => setTimeout(resolve, 2500));
            })
                .then(res => {
                    window.location.href = "/web/cards.html"
                })
        }





    },

}).mount("#app")