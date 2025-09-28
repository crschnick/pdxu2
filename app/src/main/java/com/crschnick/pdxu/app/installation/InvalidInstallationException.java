package com.crschnick.pdxu.app.installation;


import com.crschnick.pdxu.app.core.AppI18n;

public final class InvalidInstallationException extends Exception {

    private final String msgId;
    private final String[] variables;

    @SuppressWarnings("all")
    public InvalidInstallationException(String msgId, String... vars) {
        super(AppI18n.get(msgId, vars));
        this.msgId = msgId;
        this.variables = vars;
    }

    public InvalidInstallationException(Throwable cause) {
        super(cause);
        this.msgId = "ERROR_OCCURED";
        this.variables = new String[]{cause.getMessage()};
    }

    public String getMessageId() {
        return msgId;
    }

    public String getLocalisedMessage() {
        return AppI18n.get(msgId, variables);
    }
}
