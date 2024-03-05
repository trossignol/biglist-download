class StreamHandler {
    constructor() {
        this.count = 0
        this.list = []
        this.start = window.performance.now();
        this.source = null;
        StreamHandler.setResult();
    }
}

StreamHandler.URL = "/api/stream";
StreamHandler.isErrorMessage = msg => msg.startsWith("ERROR");
StreamHandler.isEndMessage = msg => msg == "<< END OF STREAM >>";

StreamHandler.init = () => {
    if (typeof (EventSource) == "undefined") {
        StreamHandler.getButton().disabled = true;
        StreamHandler.setError("Your browser does not support SSE");
    } else {
        StreamHandler.getButton().onclick = StreamHandler.stream;
    }
}

StreamHandler.stream = function () {
    StreamHandler.setMessage("Streaming in progress ...");
    new StreamHandler().run();
}

StreamHandler.prototype.run = function () {
    this.source = new EventSource(StreamHandler.URL);
    this.source.onmessage = (event) => StreamHandler.catch(() => this.addRow(event.data));
    this.source.onerror = (error) => { StreamHandler.setError(error); this.source.close(); }
}

StreamHandler.prototype.addRow = function (row) {
    if (StreamHandler.isErrorMessage(row)) throw new Error(row);
    if (StreamHandler.isEndMessage(row)) return this.close();

    this.addToList(row);
    StreamHandler.setResult(`<p>${this.count.toLocaleString()} elements (${this.getDuration()})</p>`
        + this.listToString());
}

StreamHandler.prototype.addToList = function (row) {
    this.count++;
    this.list.unshift(row);
    if (this.list.length > 20) this.list.pop();
}

StreamHandler.prototype.listToString = function () {
    return "<ul>" + this.list.map(row => `<li>${row}</li>`).join("") + "</ul>";
}

StreamHandler.prototype.getDuration = function () {
    return `${Math.floor((window.performance.now() - this.start) / 1000)}s`;
}

StreamHandler.prototype.close = function () {
    this.source?.close();
    this.source = null;
    StreamHandler.setMessage("Streaming completed")
}

StreamHandler.getElement = selector => document.querySelector("#stream > " + selector);
StreamHandler.getButton = () => StreamHandler.getElement("button");
StreamHandler.setText = (selector, text) => StreamHandler.getElement(selector).innerHTML = text?.length ? text : "&nbsp;";
StreamHandler.setResult = text => StreamHandler.setText(".result", text);
StreamHandler.setMessage = (text, error) => {
    StreamHandler.getElement("#message").className = error ? "error" : "";
    StreamHandler.setText("#message", text);
}
StreamHandler.setError = (text) => StreamHandler.setMessage(text, true);


StreamHandler.catch = function (fn) {
    try {
        fn();
    } catch (error) {
        console.error(error);
        StreamHandler.setError(error.message);
    }
}