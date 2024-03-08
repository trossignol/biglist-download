export class Dom {

}

Dom.getElement = selector => document.querySelector("#stream > " + selector);
Dom.setText = (selector, text) => Dom.getElement(selector).innerHTML = text?.length ? text : "&nbsp;";
Dom.setError = (text) => Dom.setMessage(text, true);
Dom.setMessage = (text, error) => {
    Dom.getElement("#message").className = error ? "error" : "";
    Dom.setText("#message", text);
}

Dom.catch = function (fn) {
    try {
        fn();
    } catch (error) {
        console.error(error);
        Dom.setError(error.message);
    }
}
