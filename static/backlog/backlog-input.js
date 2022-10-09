export default class BacklogInput extends HTMLElement {

    pickedFunction = undefined;

    constructor(onPick) {
        super();
        this.pickedFunction = onPick;
    }

    connectedCallback() {
        this.innerHTML = `
        <style>
            piekoszek-backlog-input {
                margin-top: 16px;
                display: flex;
                gap: 16px;
            }
            
            piekoszek-backlog-input input {
                flex: 1;
                font-size: 1.3em;
                max-width: 65%;
            }
        </style>
        `;

        const textInput = document.createElement("input")

        const button = document.createElement("button");
        button.innerText = "ok";
        button.onclick = () => {
            this.pickedFunction?.(textInput.value);
        };

        this.append(textInput, button);

    }
}

customElements.define("piekoszek-backlog-input", BacklogInput);