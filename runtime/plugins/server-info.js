core.createHook("@init", function() {
    http.createContext("/dev/test", function() {
        core.triggerHook("@dev.test")


        exchange.getResponseHeaders().set("Content-Type", "application/json");
        http.sendOutput(exchange, core.jsonToString(database.getAccountBySession(http.getSession(exchange))), false, 200);
    });
});