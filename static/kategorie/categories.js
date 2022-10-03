export default class Categories {

    static cache = undefined;

    static fetchAll() {
        if (Categories.cache === undefined) {
            Categories.cache = fetch("/api/category")
                .then(res => res.json());
        }
        return Categories.cache;
    }

}