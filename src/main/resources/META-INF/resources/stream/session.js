import { Dom } from "./dom.js";

export class Session {
    constructor(nbRuns, buildHandler, callback) {
        this.nbRuns = nbRuns;
        this.currentRun = 0;
        this.buildHandler = buildHandler;
        this.callback = callback;

        this.times = [];

        // Update DOM
        let button = Dom.getElement(".time > .reset")
        button.onclick = () => this.reset();
        button.style.display = "block";
        this.refresh();
    }
}

Session.prototype.startRun = function () {
    this.startTime = window.performance.now();
    this.currentRun++;
    this.refresh();
}

Session.prototype.endRun = function () {
    this.times.push(window.performance.now() - this.startTime);
    this.refresh();

    if (this.currentRun < this.nbRuns) this.run();
    else if (this.callback) this.callback();
}

Session.prototype.reset = function () {
    this.times = [];
    this.refresh();
}

Session.prototype.current = function () { return (window.performance.now() - this.startTime).strTime(); }
Session.prototype.run = function () { this.buildHandler(this).run(); }

Session.prototype.refresh = function () {
    Dom.setText(".time > .average", `Avg: ${this.times.average().strTime()} | Run: ${this.currentRun}/${this.nbRuns}`);
}