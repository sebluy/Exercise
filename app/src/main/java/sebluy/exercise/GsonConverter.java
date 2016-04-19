package sebluy.exercise;

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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import auto.parcelgson.gson.AutoParcelGsonTypeAdapterFactory;

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
                            context.serialize(src.pageState(), MainState.Page.State.Calisthenic.class));
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
            MainState.Page.State pageState;
            switch (id) {
                case CALISTHENIC_EXERCISE:
                    pageState = context.deserialize(obj.get("pageState"),
                            MainState.Page.State.Calisthenic.class);
                    break;
                default:
                    pageState = MainState.Page.State.Empty.create();
                    break;
            }
            return MainState.Page.create(id, pageState);
        }
    }

    public static Gson buildGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeHierarchyAdapter(fj.data.List.class, new FjListGsonAdapter());
        builder.registerTypeHierarchyAdapter(List.class, new ListGsonAdapter());
        builder.registerTypeAdapter(MainState.Page.class, new PageGsonAdapter());
        builder.registerTypeAdapterFactory(new AutoParcelGsonTypeAdapterFactory());
        return builder.create();
    }

}
