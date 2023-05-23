$.createHook("@init", () => {
    $.logger.info("Hello plugin")

    $.createUri("/test/dev", () => {
        utils.sendOutput(exchange, "test!", false, 200);
    });

});