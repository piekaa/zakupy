import Header from "/structure/header.js";
import BacklogInput from "/backlog/backlog-input.js";
import BacklogItem from "/backlog/backlog-item.js";
import Line from "/common-elements/line.js";

export default class BacklogPage extends HTMLElement {

    constructor() {
        super();
    }

    connectedCallback() {
        this.innerHTML = `
            <style>
                .items-container {
                    margin-top: 32px;
                    display: flex;
                    flex-direction: column;
                    gap: 4px;
                }
            </style>
        `;

        this.append(new Header("Backlog"));

        this.append(new BacklogInput((newItems) => {
            console.log(newItems);
                fetch("/api/item",
                    {
                        method: "POST",
                        body: JSON.stringify(newItems)
                    })
                    .then(res => {
                        window.location.reload();
                    })
            })
        )

        const itemsContainer = document.createElement("div");
        itemsContainer.classList.add("items-container");

        this.append(itemsContainer);

        this.classList.add("piekoszek-page");

        fetch("/api/item")
            .then(res => res.json())
            .then(items => {
                items.forEach(item => {
                    itemsContainer.append(new BacklogItem(item._id, item.name, item.categories));
                    itemsContainer.append(new Line());
                })
                window.scrollTo(0, parseInt(sessionStorage.getItem("scrollTo") ?? "0"));
            })
    }

}

customElements.define("piekoszek-backlog-page", BacklogPage);