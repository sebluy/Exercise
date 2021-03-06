package sebluy.exercise;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import auto.parcelgson.gson.AutoParcelGsonTypeAdapterFactory;

import static sebluy.exercise.CalisthenicExercise.PULL_UPS_REPS_TO_SETS_F;
import static sebluy.exercise.CalisthenicExercise.DEFAULT_REPS_TO_SETS_F;
import static sebluy.exercise.CalisthenicExercise.Template.RepsToSetsF;
import static sebluy.exercise.MainState.Page;
import static sebluy.exercise.MainState.Page.State;
import static sebluy.exercise.MainState.Page.State.CalisthenicFeedback;
import static sebluy.exercise.MainState.Page.State.CalisthenicWorkout;
import static sebluy.exercise.MainState.Page.State.Empty;
import static sebluy.exercise.MainState.Page.Id;

public class GsonConverter {

    public static class FjListGsonAdapter<E> implements
            JsonSerializer<fj.data.List<E>>,
            JsonDeserializer<fj.data.List<E>> {

        @Override
        public fj.data.List<E>
        deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            List<E> l = new ArrayList<>();
            Type elemType = ((ParameterizedType)typeOfT).getActualTypeArguments()[0];
            for (JsonElement e : json.getAsJsonArray()) {
                l.add(context.deserialize(e, elemType));
            }
            return fj.data.List.iterableList(l);
        }

        @Override
        public JsonElement
        serialize(fj.data.List<E> src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray json = new JsonArray();
            for (E e : src) {
                json.add(context.serialize(e));
            }
            return json;
        }
    }

    public static class ListGsonAdapter<E> implements
            JsonSerializer<List<E>>,
            JsonDeserializer<List<E>> {

        @Override
        public List<E>
        deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            Type elemType = ((ParameterizedType)typeOfT).getActualTypeArguments()[0];
            List<E> l = new ArrayList<>();
            for (JsonElement e : json.getAsJsonArray()) {
                l.add(context.deserialize(e, elemType));
            }
            return Collections.unmodifiableList(l);
        }

        @Override
        public JsonElement
        serialize(List<E> src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray array = new JsonArray();
            for (E e : src) {
                array.add(context.serialize(e));
            }
            return array;
        }
    }

    public static class PageGsonAdapter implements
            JsonSerializer<Page>,
            JsonDeserializer<Page> {

        @Override
        public JsonElement
        serialize(Page src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.add("id", context.serialize(src.id()));
            JsonElement state;
            switch (src.id()) {
                case CALISTHENIC_WORKOUT:
                    state = context.serialize(src.state(), CalisthenicWorkout.class);
                    break;
                case CALISTHENIC_FEEDBACK:
                    state = context.serialize(src.state(), CalisthenicFeedback.class);
                    break;
                default:
                    state = context.serialize(src.state());
                    break;
            }
            obj.add("state", state);
            return obj;
        }

        @Override
        public Page
        deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            Id id = context.deserialize(obj.get("id"), Page.Id.class);
            State state;
            switch (id) {
                case CALISTHENIC_WORKOUT:
                    state = context.deserialize(obj.get("state"), CalisthenicWorkout.class);
                    break;
                case CALISTHENIC_FEEDBACK:
                    state = context.deserialize(obj.get("state"), CalisthenicFeedback.class);
                    break;
                default:
                    state = Empty.create();
                    break;
            }
            return Page.create(id, state);
        }
    }

    public static class RepsToSetsFGsonAdapter implements
            JsonSerializer<RepsToSetsF>,
            JsonDeserializer<RepsToSetsF> {

        @Override
        public JsonElement
        serialize(RepsToSetsF src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == PULL_UPS_REPS_TO_SETS_F) {
                return new JsonPrimitive("pull-ups");
            } else {
                return new JsonPrimitive("default");
            }
        }

        @Override
        public RepsToSetsF
        deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            String name = json.getAsString();
            switch (name) {
                case "pull-ups":
                    return PULL_UPS_REPS_TO_SETS_F;
                default:
                    return DEFAULT_REPS_TO_SETS_F;
            }
        }
    }

    public static Gson buildGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeHierarchyAdapter(fj.data.List.class, new FjListGsonAdapter());
        builder.registerTypeHierarchyAdapter(List.class, new ListGsonAdapter());
        builder.registerTypeHierarchyAdapter(RepsToSetsF.class, new RepsToSetsFGsonAdapter());
        builder.registerTypeAdapter(Page.class, new PageGsonAdapter());
        builder.registerTypeAdapterFactory(new AutoParcelGsonTypeAdapterFactory());
        return builder.create();
    }

}
