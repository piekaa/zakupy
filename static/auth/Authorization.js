export default class Authorization {
    static header() {

        const basic = localStorage.getItem("auth");

        if(!basic) {
            window.location.href = "/login";
        }

        return {
            "Authorization": `Basic ${basic}`
        };
    }
}