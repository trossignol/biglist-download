import { Dom } from "./dom.js";
import { GetAllHandler } from "./getall.js";
import { Session } from "./session.js";
import { SseHandler } from "./sse.js";

class StreamHandler {
    constructor(session, requestHandler) {
        this.count = 0
        this.list = [];
        this.session = session;
        this.requestHandler = requestHandler;
        this.requestHandler.rowHandler = this;
        StreamHandler.setResult();

        // Update DOM
        Dom.setMessage("Streaming in progress ...");
    }
}

StreamHandler.isErrorMessage = msg => msg.startsWith("ERROR");
StreamHandler.isEndMessage = msg => msg == "<< END OF STREAM >>";

export function initStreamHandler() {
    if (typeof (EventSource) == "undefined") {
        StreamHandler.getButton().disabled = true;
        Dom.setError("Your browser does not support SSE");
    } else {
        StreamHandler.getButton().onclick = StreamHandler.stream;
    }
}

StreamHandler.stream = function () {
    let mode = Dom.getElement(".launch > select").value;
    let nbRuns = Dom.getElement(".launch > input").value;
    if (!nbRuns.isInt()) return Dom.setError("Nb runs should be an integer");
    Dom.setError();
    StreamHandler.getButton().disabled = true;
    new Session(nbRuns,
        session => new StreamHandler(session, StreamHandler.initHandlerMode(mode)),
        () => StreamHandler.getButton().disabled = false)
        .run();
}

StreamHandler.initHandlerMode = function (mode) {
    switch (mode) {
        case "SSE": return new SseHandler();
        case "GetAll": return new GetAllHandler();
        default: throw new Error("Unknown mode=" + mode);
    }
}

StreamHandler.prototype.run = function () {
    this.session.startRun();
    this.requestHandler.run();
}

StreamHandler.prototype.end = function () {
    this.requestHandler.close();
    Dom.setMessage("Streaming completed")
    this.session.endRun();
}

StreamHandler.prototype.addRow = function (row) {
    Dom.catch(() => {
        if (StreamHandler.isErrorMessage(row)) throw new Error(row);
        if (StreamHandler.isEndMessage(row)) return this.end();

        this.count++;
        this.list.unshift(row);
        if (this.list.length > 20) this.list.pop();

        StreamHandler.setResult(`<p> ${this.count.toLocaleString()} elements (${this.session.current()})</p>`
            + "<ul>" + this.list.map(row => `<li>${row}</li>`).join("") + "</ul>");
    });
}

StreamHandler.getButton = text => Dom.getElement(".launch > button");
StreamHandler.setResult = text => Dom.setText(".result", text);