plugin.createHook("@load", function() {

    var au = database.getAccountByUsername("Wartuu");
    if(au != null) {
        plugin.logger.info(au)
    }
});