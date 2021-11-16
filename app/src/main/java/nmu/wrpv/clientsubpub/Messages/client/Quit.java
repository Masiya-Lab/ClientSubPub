package nmu.wrpv.clientsubpub.Messages.client;

import androidx.annotation.NonNull;

import nmu.wrpv.clientsubpub.Messages.Message;

public class Quit  extends Message {

    private static final long serialVersionUID=4L;

    @NonNull
    @Override
    public String toString() {
        return "Quit()";
    }

}
