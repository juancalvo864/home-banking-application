const { createApp } = Vue
createApp({
    data() {
        return {
            dataClients: [],
            cards: []
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
                    this.cards = this.dataClients.cards

                })
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


    },



}).mount("#app")