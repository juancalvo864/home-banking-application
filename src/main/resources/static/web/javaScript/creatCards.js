const { createApp } = Vue
createApp({
    data() {
        return {
            dataClients: [],
            clients: [],
            cards: [],
            type: "",
            color: ""

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
                    this.cards = this.clients.cards

                })
        },

        cardCreate() {
            axios.post('/api/clients/current/cards', `type=${this.type}&color=${this.color}`,
                { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
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
                    this.logout()
                    window.location.href = "/web/index.html"
                }
            })
        },

        creado() {
            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Your work has been saved',
                showConfirmButton: false,
                timer: 2500
            })

        },

        sureCreateCard() {
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
            }).then((result) => {
                if (result.isConfirmed) {
                    this.creado()
                    this.cardCreate()

                }
            }).then(() => {
                // Espera 1000 milisegundos (1 segundo)
                return new Promise((resolve) => setTimeout(resolve, 2500));
            })
                .then(res => {
                    window.location.href = "/web/createCards.html"
                })
        }





    },
}).mount("#app")