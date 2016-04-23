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
import static sebluy.exercise.MainState.Page.Id;

public class GsonConverter {

    public static class FjListGsonAdapter implements
            JsonSerializer<fj.data.List<?>>,
            JsonDeserializer<fj.data.List<?>> {

        @Override
        public fj.data.List<?>
        deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            List l = new ArrayList<>();
            Type elemType = ((ParameterizedType)typeOfT).getActualTypeArguments()[0];
            for (JsonElement e : json.getAsJsonArray()) {
                l.add(context.deserialize(e, elemType));
            }
            return fj.data.List.iterableList(l);
        }

        @Override
        public JsonElement
        serialize(fj.data.List<?> src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray json = new JsonArray();
            for (Object e : src) {
                json.add(context.serialize(e));
            }
            return json;
        }
    }

    public static class ListGsonAdapter implements
            JsonSerializer<List<?>>,
            JsonDeserializer<List<?>> {

        @Override
        public List<?>
        deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            Type elemType = ((ParameterizedType)typeOfT).getActualTypeArguments()[0];
            List l = new ArrayList<>();
            for (JsonElement e : json.getAsJsonArray()) {
                l.add(context.deserialize(e, elemType));
            }
            return Collections.unmodifiableList(l);
        }

        @Override
        public JsonElement
        serialize(List<?> src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray array = new JsonArray();
            for (Object e : src) {
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
            switch (src.id()) {
                case CALISTHENIC_WORKOUT:
                    obj.add("state",
                            context.serialize(src.state(), State.CalisthenicWorkout.class));
                    break;
                default:
                    obj.add("state", context.serialize(src.state()));
            }
            return obj;
        }

        @Override
        public Page
        deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            Id id = context.deserialize(obj.get("id"), Page.Id.class);
            State pageState;
            switch (id) {
                case CALISTHENIC_WORKOUT:
                    pageState = context.deserialize(obj.get("state"),
                            State.CalisthenicWorkout.class);
                    break;
                default:
                    pageState = Page.State.Empty.create();
                    break;
            }
            return Page.create(id, pageState);
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
