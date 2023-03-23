const { createApp } = Vue
createApp({
    data() {
        return {
            data: [],
            client: [],
            accounts: [],

        }
    },
    created() {
        this.loadData();
        this.size();



    },
    methods: {
        loadData() {
            axios.get("http://localhost:8080/api/clients/1")
                .then(res => {
                    this.data = res;
                    this.client = this.data.data;
                    this.accounts = this.data.data.accounts;
                    this.accounts.sort((a, b) => a.id - b.id)

                })
        },

        dateTimeAccounts(date) {
            let template = date.split("T")
            return `${template[0]}`
        },
        size() {
            console.log(window.screen.width);
        }
    },
}).mount("#app")
