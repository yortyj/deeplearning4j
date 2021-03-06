package org.deeplearning4j.rl4j.space;

import lombok.Value;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * @author rubenfiszel (ruben.fiszel@epfl.ch) on 7/8/16.
 *
 * Contain contextual information about the environment from which Observations are observed and must know how to build an Observation from json.
 *
 * @param <O> the type of Observation
 */

@Value
public class GymObservationSpace<O> implements ObservationSpace<O> {

    String name;
    int[] shape;
    INDArray low;
    INDArray high;


    public GymObservationSpace(JSONObject jsonObject) {

        name = jsonObject.getString("name");

        JSONArray arr = jsonObject.getJSONArray("shape");
        int lg = arr.length();

        shape = new int[lg];
        for (int i = 0; i < lg; i++) {
            this.shape[i] = arr.getInt(i);
        }

        low = Nd4j.create(shape);
        high = Nd4j.create(shape);

        JSONArray lowJson = jsonObject.getJSONArray("low");
        JSONArray highJson = jsonObject.getJSONArray("high");

        int size = shape[0];
        for (int i = 1; i < shape.length; i++) {
            size *= shape[i];
        }

        for (int i = 0; i < size; i++) {
            low.putScalar(i, lowJson.getDouble(i));
            high.putScalar(i, highJson.getDouble(i));
        }

    }

    public O getValue(JSONObject o, String key) {
        switch (name) {
            case "Box":
                JSONArray arr = o.getJSONArray(key);
                return (O) new Box(arr);
            default:
                throw new RuntimeException("Invalid environment name: " + name);
        }
    }

}
