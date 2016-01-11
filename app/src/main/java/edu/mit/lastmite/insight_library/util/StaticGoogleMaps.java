package edu.mit.lastmite.insight_library.util;

import java.util.HashMap;
import java.util.Iterator;

public class StaticGoogleMaps {
    protected static final String BASE_URL = "https://maps.googleapis.com/maps/api/staticmap";

    protected HashMap<String, Object> mArguments;

    private StaticGoogleMaps(HashMap<String, Object> arguments) {
        mArguments = arguments;
    }

    public String getUrl() {
        if (mArguments == null || mArguments.isEmpty()) {
            throw new IllegalStateException("You need to specify at least one argument.");
        }
        return BASE_URL + buildArgumentsString();
    }

    protected String buildArgumentsString() {
        String arguments = "?";
        Iterator<String> iterator = mArguments.keySet().iterator();
        while (iterator.hasNext()) {
            arguments += buildArgumentString(iterator.next());
        }
        return arguments;
    }

    protected String buildArgumentString(String key) {
        return String.format(
                "&%s=%s",
                key,
                mArguments.get(key).toString()
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected HashMap<String, Object> mArguments;

        public Builder() {
            mArguments = new HashMap<>();
        }

        public Builder addArgument(String key, Object value) {
            mArguments.put(key, value);
            return this;
        }

        public StaticGoogleMaps build() {
            return new StaticGoogleMaps(mArguments);
        }
    }
}
