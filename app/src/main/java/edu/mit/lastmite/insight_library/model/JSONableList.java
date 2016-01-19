package edu.mit.lastmite.insight_library.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON interface for the Model layer.
 */
public interface JSONableList<T extends JSONable> extends List<T> {
}