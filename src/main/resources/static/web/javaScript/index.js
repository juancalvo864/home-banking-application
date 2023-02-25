const { createApp } = Vue
createApp({
    data() {
        return {
            data: [],
            client: [],
            accounts: [],
            password: null,
            firstName: null,
            lastName: null,
            email: null,
            password: null,
            password1: null,
            user: null

        }
    },
    created() {


    },
    methods: {
        login() {
            axios.post('/api/login', `email=${this.user}&password=${this.password1}`,
                { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    window.location.href = '/web/accounts.html';

                })
        },

        signUp() {
            console.log("click")
            axios.post('/api/clients', `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`,
                { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    console.log(response)
                    this.user = this.email
                    this.password1 = this.password
                    this.login()
                })
                .catch(error => console.log(error))
        }


    }
}).mount("#app")


