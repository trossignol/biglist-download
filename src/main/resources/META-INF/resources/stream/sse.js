import { Dom } from "./dom.js";

export class SseHandler {
}

SseHandler.URL = "/api/stream";

SseHandler.prototype.run = function () {
    this.source = new EventSource(SseHandler.URL);
    this.source.onmessage = (event) => this.rowHandler.addRow(event.data);
    this.source.onerror = (error) => { Dom.setError(error); this.close(); }
}

SseHandler.prototype.close = function () {
    this.source?.close();
}