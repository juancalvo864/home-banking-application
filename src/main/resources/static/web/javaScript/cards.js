const { createApp } = Vue
createApp({
    data() {
        return {
            dataClient: [],
            cards: [],
            idCard: null,

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
                    this.cards = this.dataClient.cards

                })
        },

        cardDelete() {
            axios.patch('/api/clients/current/cards', `cardId=${this.idCard}`,
                { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(res => {
                    Swal.fire(res.data)
                })
                .catch(error => {
                    Swal.fire(error.response.data)

                    console.error(error.response.data)
                })
        },

        checkExpire(info) {
            let date = new Date()
            return info.thruDate < date.toISOString()
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
        sureDeleteCard() {
            Swal.fire({
                title: 'Are you sure?',
                text: "Do you want to delete the card?",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'yes I want'
            }).then((result) => {
                if (result.isConfirmed) {
                    this.cardDelete()
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