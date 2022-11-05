$.createHook("@init", () => {
    $.logger.info("Hello plugin")

    $.createUri("/test/dev", () => {
        utils.sendOutput(exchange, "works!", false, 200);
    });

});