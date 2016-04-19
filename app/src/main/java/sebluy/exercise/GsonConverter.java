package sebluy.exercise;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import auto.parcelgson.gson.AutoParcelGsonTypeAdapterFactory;

public class GsonConverter {

    public static class ListGsonAdapter implements
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

    public static class MainStateGsonAdapter implements
            JsonSerializer<MainState>,
            JsonDeserializer<MainState> {

        @Override
        public JsonElement
        serialize(MainState src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.add("page", context.serialize(src.page()));
            obj.add("history", context.serialize(src.history()));
            return obj;
        }

        @Override
        public MainState
        deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            Type historyType = new TypeToken<fj.data.List<MainState.Page>>(){}.getType();
            return MainState.create(
                    context.deserialize(obj.get("page"), MainState.Page.class),
                    context.deserialize(obj.get("history"), historyType));
        }
    }

    public static class PageGsonAdapter implements
            JsonSerializer<MainState.Page>,
            JsonDeserializer<MainState.Page> {

        @Override
        public JsonElement
        serialize(MainState.Page src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.add("ID", context.serialize(src.ID()));
            switch (src.ID()) {
                case CALISTHENIC_EXERCISE:
                    obj.add("pageState",
                            context.serialize(src.pageState(), MainState.Page.CalisthenicPageState.class));
                    break;
                default:
                    obj.add("pageState", context.serialize(src.pageState()));
            }
            return obj;
        }

        @Override
        public MainState.Page
        deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            MainState.Page.ID id = context.deserialize(obj.get("ID"), MainState.Page.ID.class);
            MainState.Page.PageState pageState;
            switch (id) {
                case CALISTHENIC_EXERCISE:
                    pageState = context.deserialize(obj.get("pageState"),
                            MainState.Page.CalisthenicPageState.class);
                    break;
                default:
                    pageState = MainState.Page.PageState.empty;
                    break;
            }
            return MainState.Page.create(id, pageState);
        }
    }

    public static class CalisthenicPageStateGsonAdapter implements
            JsonSerializer<MainState.Page.CalisthenicPageState>,
            JsonDeserializer<MainState.Page.CalisthenicPageState> {

        @Override
        public JsonElement
        serialize(MainState.Page.CalisthenicPageState src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray array = new JsonArray();
            for (CalisthenicExercise e : src.workout()) {
                array.add(context.serialize(e));
            }
            return array;
        }

        @Override
        public MainState.Page.CalisthenicPageState
        deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
             Type t = new TypeToken<List<CalisthenicExercise>>(){}.getType();
             List<CalisthenicExercise> exercises = context.deserialize(json.getAsJsonArray(), t);
             return MainState.Page.CalisthenicPageState.create(
                     Collections.unmodifiableList(exercises));
        }
    }

    public static class CalisthenicExerciseGsonAdapter implements
            JsonSerializer<CalisthenicExercise>,
            JsonDeserializer<CalisthenicExercise> {

        @Override
        public JsonElement
        serialize(CalisthenicExercise src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.add("type", context.serialize(src.type()));
            object.addProperty("name", src.name());
            object.addProperty("set", src.set());
            object.addProperty("repetitions", src.repetitions());
            return object;
        }

        @Override
        public CalisthenicExercise
        deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            return CalisthenicExercise.create(
                    context.deserialize(object.get("type"), CalisthenicExercise.Type.class),
                    object.get("repetitions").getAsInt(),
                    object.get("set").getAsInt(),
                    object.get("name").getAsString());
        }

    }

    public static Gson buildGson() {
        GsonBuilder gbuilder = new GsonBuilder();
        gbuilder.registerTypeHierarchyAdapter(fj.data.List.class, new ListGsonAdapter());
        gbuilder.registerTypeAdapter(MainState.class, new MainStateGsonAdapter());
        gbuilder.registerTypeAdapter(MainState.Page.class, new PageGsonAdapter());
        gbuilder.registerTypeAdapter(
                MainState.Page.CalisthenicPageState.class,
                new CalisthenicPageStateGsonAdapter());
        gbuilder.registerTypeAdapterFactory(new AutoParcelGsonTypeAdapterFactory());
/*        gbuilder.registerTypeAdapter(
                CalisthenicExercise.class,
                new CalisthenicExerciseGsonAdapter());*/
        return gbuilder.create();
    }

}
