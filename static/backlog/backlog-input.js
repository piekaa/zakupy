import Circle from "/common-elements/circle.js";
import DefaultCategoryPicker from "/backlog/default-category-picker.js";

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
                flex-direction: column;
                gap: 16px;
            }
            
            piekoszek-backlog-input textarea {
                resize: none;
                padding: 5px;
                overflow: hidden;
                box-sizing: border-box;
                font-size: 1.3em;
            }
            
            piekoszek-backlog-input .backlog-input-panel {
                width: 100%;
                display: flex;
                justify-content: space-evenly;
                gap: 16px;
            }
            
            .backlog-input-panel select {
                flex: 1;
            }
            
        </style>
        `;

        const textInput = document.createElement("textarea")
        textInput.addEventListener('keyup', () => {
            textInput.style.height = `${textInput.scrollHeight}px`;
        });

        const backlogPanel = document.createElement("div");

        backlogPanel.innerHTML = `
        <select id="newItemsDelimiter">
            <option value=""></option>
            <option value="dalej">dalej</option>
            <option value="i">i</option>
        </select>
        `;

        backlogPanel.classList.add("backlog-input-panel")

        const defaultCategory = new Circle("#000", 40);
        let selectedCategory = undefined;
        defaultCategory.onclick = () => {
            this.append(new DefaultCategoryPicker((category) => {
                selectedCategory = category;
                defaultCategory.setColor(category.color);
                defaultCategory._id = category._id;
            }));
        };

        backlogPanel.append(defaultCategory);

        const button = document.createElement("button");
        button.innerText = "ok";
        button.onclick = () => {
            this.pickedFunction?.({
                    text: textInput.value,
                    delimiter: document.getElementById("newItemsDelimiter").value,
                    defaultCategory: selectedCategory
                });
        };

        backlogPanel.append(button);

        this.append(textInput, backlogPanel);

    }
}

customElements.define("piekoszek-backlog-input", BacklogInput);