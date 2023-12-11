import Authorization from "/auth/Authorization.js";

export default class Categories {

    static cache = undefined;

    static fetchAll() {
        if (Categories.cache === undefined) {
            Categories.cache = fetch("/api/category",
                {
                    headers: Authorization.header(),
                })
                .then(res => res.json());
        }
        return Categories.cache;
    }

}