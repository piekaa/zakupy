export default class Header extends HTMLElement {

    name

    constructor(name = "Page") {
        super();
        this.name = name;
        console.trace(this.name);
    }

    connectedCallback() {

        this.innerHTML = `
        <style>
            piekoszek-header {
                font-size: 1.4em;
                display: flex;
                gap: 16px;
                position: relative;
            }
            
            .hamburger {
                display: flex;
                flex-direction: column;
                gap: 8px;
            }
            
            .hamburger div {
                width: 30px;
                height: 3px;
                background: black;
            }
            
            .hamburger-content {
                position:absolute;
                background: #2d2e2aee;
                width: calc(85vw - 60px);
                height: 90vh;
                left: -100%;
                color: white;
                font-size: 1.3em;
                padding: 30px;
                display: flex;
                flex-direction: column;
                gap: 8px;
                transition: 0.8s;
            }
            
            .hamburger-content a {
                text-decoration: none;
                color: white;
            }
            
            .hamburger-open {
                transition: 0.8s;
                left: -8px;
            }
            
            #hamburgerClose {
                align-self: flex-end;
            }
            
        </style>
        <div class="hamburger">
            <div></div>
            <div></div>
            <div></div>
        </div>
        <div>${this.getAttribute("name") || this.name}</div>
        <div id="hamburgerContent" class="hamburger-content">
            <div id="hamburgerClose">X</div>
            <a href="/backlog">Backlog</a>
            <a href="/kategorie">Kategorie</a>
            <a href="/zakupy">Zakupy</a>
            <br/>
            <br/>
            <a href="/logout">Logout</a>
        </div>
        `;

        this.getElementsByClassName("hamburger")[0].onclick = () => {
            document.getElementById("hamburgerContent").classList.add("hamburger-open");
        }

        document.getElementById("hamburgerClose").onclick = () => {
            document.getElementById("hamburgerContent").classList.remove("hamburger-open");
        }

    }

}

customElements.define("piekoszek-header", Header);