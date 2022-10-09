export default class ElemUtils {

    static div(text = "", classes = []) {
        const div = document.createElement("div");
        div.innerText = text;
        if (classes.length === undefined) {
            div.classList.add(classes);
        }
        div.classList.add(...classes);
    }
}