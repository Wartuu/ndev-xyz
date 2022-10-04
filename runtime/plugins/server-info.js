plugin.createHook("@load", function() {
    plugin.createUri("/dev/off", "turning off server", false, "@server-info.dev.off", "text/html");
});
plugin.createHook("@server-info.dev.off", function(){
   plugin.logger.info("server-info.dev.off output test");
   plugin.stopHttpServer();
});