import { initStreamHandler } from "./stream/handler.js";

Array.prototype.average = function () { return this.length ? this.reduce((sum, value) => sum + value, 0) / this.length : 0; }
String.prototype.isInt = function () { return /^\d+$/.test(this); }
Number.prototype.strTime = function () { return Math.floor(this).toLocaleString() + " ms"; }

initStreamHandler();