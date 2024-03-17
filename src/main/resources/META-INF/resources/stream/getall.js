import { Dom } from "./dom.js";

export class GetAllHandler {
}

GetAllHandler.URL = "/api/list";

GetAllHandler.prototype.run = function () {
  fetch(GetAllHandler.URL)
    .then(response => {
      if (!response.ok) return Dom.setError(`HTTP error (status=${response.status})`);
      response.json().then(rows => this.addRows(rows));
    })
    .catch(error => Dom.setError(error));
}

GetAllHandler.prototype.addRows = function (rows) {
  rows.map(JSON.stringify).forEach(row => this.rowHandler.addRow(row));
  this.rowHandler.end();
}

GetAllHandler.prototype.close = function () { }