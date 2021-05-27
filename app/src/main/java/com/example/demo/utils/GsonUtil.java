package com.example.demo.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Created by guotingzhu@evergrande.cn on 2017/8/30.
 */

public class GsonUtil {

    private static final String TAG = "GsonUtil";
    private static Gson sGson = new GsonBuilder().disableHtmlEscaping().create();
    private static JsonParser sJsonParser = new JsonParser();

    /**
     * String类型的json转为JsonObject类型
     *
     * @param jsonData
     * @return
     */
    public static JsonObject str2JsonObj(String jsonData) {
        try {
            JsonReader reader = new JsonReader(new StringReader(jsonData));
            reader.setLenient(true);
            JsonElement element = sJsonParser.parse(reader);
            if (element == null || element instanceof JsonNull) {
                return null;
            }
            JsonObject jsonObj = element.getAsJsonObject();
            return jsonObj;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * 将jsonArray字符串转为集合
     */
    public static <T> List<T> jsonArray2List(JsonArray jsonArray, Class<T[]> clazz) {
        try {
            T[] array = sGson.fromJson(jsonArray, clazz);
            return Arrays.asList(array);
        } catch (Exception e) {
            Log.w(TAG, e.toString());
            return null;
        }
    }

    /**
     * 用例:
     * TypeToken<ArrayList<MyItem>> typeToken = new TypeToken<ArrayList<MyItem>>(){};
     * ArrayList<MyItem> arrayList = getArray(typeToken);
     *
     * @param jsonStr
     * @param typeToken
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> jsonToListObject(String jsonStr, TypeToken typeToken) {
        if (jsonStr != null) {
            try {
                return (ArrayList<T>) sGson.fromJson(jsonStr, typeToken.getType());
            } catch (Exception e) {
                Log.e(TAG, "json parse error= " + e.toString(),new Throwable());
            }
        }
        return null;
    }


    /**
     * String类型的Jsonist转json数组
     *
     * @param listJsonStr
     * @return
     */
    public static JsonArray jsonStr2JsonArray(String listJsonStr) {
        try {
            JsonReader reader = new JsonReader(new StringReader(listJsonStr));
            reader.setLenient(true);
            JsonElement element = sJsonParser.parse(reader);
            if (element == null || element instanceof JsonNull) {
                return null;
            }
            JsonArray contentJo = element.getAsJsonArray();
            return contentJo;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * int数祖转json数组
     *
     * @param longArray
     * @return
     */
    public static JsonArray longArray2JsonArray(long[] longArray) {
        try {
            String contentJson = jsonObj2Str(longArray);
            JsonReader reader = new JsonReader(new StringReader(contentJson));
            reader.setLenient(true);
            JsonElement element = sJsonParser.parse(reader);
            if (element == null || element instanceof JsonNull) {
                return null;
            }
            JsonArray contentJo = element.getAsJsonArray();
            return contentJo;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * list转json数组
     *
     * @param list
     * @return
     */
    public static JsonArray list2JsonArray(List list) {
        try {
            String contentJson = jsonObj2Str(list);
            JsonReader reader = new JsonReader(new StringReader(contentJson));
            reader.setLenient(true);
            JsonElement element = sJsonParser.parse(reader);
            if (element == null || element instanceof JsonNull) {
                return null;
            }
            JsonArray contentJo = element.getAsJsonArray();
            return contentJo;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * 特定接口，获取响应json content部分中的 result 属性部分，返回结果为JsonObject类型
     *
     * @param contentJson
     * @return
     */
   /* public static JsonObject getResultJsonObj(String contentJson) {
        try {
            JsonObject resJo = str2JsonObj(contentJson);
            if (resJo == null) {
                return null;
            }
            JsonElement resultEL = resJo.get(IProtocol.PARAMS_KEY_COMNON_RESULT);
            if (resultEL == null || resultEL instanceof JsonNull) {
                return null;
            }
            JsonObject resultObj = resultEL.getAsJsonObject();
            return resultObj;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }*/

   /* public static JsonElement getResultJsonElement(String contentJson){
        try {
            JsonObject resJo = str2JsonObj(contentJson);
            if (resJo == null) {
                return null;
            }
            JsonElement resultEL = resJo.get(IProtocol.PARAMS_KEY_COMNON_RESULT);
            return resultEL;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }*/

    /**
     * 特定接口，获取响应json content部分中的 result 属性部分，返回结果为String类型
     *
     * @param contentJson
     * @return
     */
   /* public static String getResultJsonStr(String contentJson) {
        try {
            JsonElement resultObj = getResultJsonElement(contentJson);
            String resultStr = sGson.toJson(resultObj);
            return resultStr;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }*/

    /**
     * @param jsonStr
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T jsonToObject(String jsonStr, Type type) {
        T t = null;
        try {
            t = sGson.fromJson(jsonStr, type);
        } catch (Exception e) {
            Log.e(TAG, "json parse error= " + e.toString());
        }
        return t;
    }


    /**
     * 特定接口，获取响应json content部分下的result部分下的attribute属性部分，返回结果为JsonObject类型
     * <p>
     * "content":{
     * "result":{
     * "attribute": {
     * }
     * }
     * }
     *
     * @param contentJson
     * @return
     */
 /*   public static JsonObject getAttributeJsonObj(String contentJson) {
        try {
            JsonObject resultJo = getResultJsonObj(contentJson);
            if (resultJo == null) {
                return null;
            }
            JsonElement resultEL = resultJo.get(IProtocol.PARAMS_KEY_ATTRIBUTE);
            if (resultEL == null) {
                return null;
            }
            JsonObject resultObj = resultEL.getAsJsonObject();
            return resultObj;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }*/

    /**
     * 特定接口，获取响应json content部分下的result部分下的attribute属性部分，返回结果为String类型
     * <p>
     * "content":{
     * "result":{
     * "attribute": {
     * }
     * }
     * }
     *
     * @param contentJson
     * @return
     */
    /*public static String getAttributeJsonStr(String contentJson) {
        try {
            JsonObject resultObj = getAttributeJsonObj(contentJson);
            String resultStr = sGson.toJson(resultObj);

            return resultStr;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }*/

    /**
     * 获取响应的josn中指定名称属性值，json的形式返回
     *
     * @param propertyName
     * @param contentJson
     * @return
     */
    public static String getStrJsonProp(String propertyName, String contentJson) {
        try {
            JsonObject resultJo = GsonUtil.str2JsonObj(contentJson);
            if (resultJo == null) {
                return null;
            }
            JsonElement resultEL = resultJo.get(propertyName);
            if (resultEL == null) {
                return null;
            }
            JsonObject resultObj = resultEL.getAsJsonObject();
            String resultStr = sGson.toJson(resultObj);
            return resultStr;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * 获取响应的josn中String类型的属性值
     *
     * @param propertyName
     * @param jsonStr
     * @return
     */
    public static String getStringProp(String propertyName, String jsonStr) {
        try {
            JsonObject jsonObj = GsonUtil.str2JsonObj(jsonStr);
            if (jsonObj == null) {
                return null;
            }
            JsonElement element = jsonObj.get(propertyName);
            if (element == null) {
                return null;
            }
            String propertyValue = element.getAsString();
            return propertyValue;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    /**
     * 获取响应的josn中String类型的属性值
     *
     * @param propertyName
     * @param contentJson
     * @return
     */
    public static boolean getBooleanProp(String propertyName, String contentJson) {
        try {
            JsonObject resultJsonObj = GsonUtil.str2JsonObj(contentJson);
            if (resultJsonObj == null) {
                return false;
            }
            JsonElement element = resultJsonObj.get(propertyName);
            if (element == null) {
                return false;
            }
            boolean propertyValue = element.getAsBoolean();
            return propertyValue;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return false;
    }

    /**
     * 获取响应的josn中int类型的属性值
     *
     * @param propertyName
     * @param contentJson
     * @return -1表示没有查到，但有可能查询的该属性值本来就是-1，可能存在bug。
     */
    public static int getIntProp(String propertyName, String contentJson) {
        try {
            JsonObject resultJsonObj = GsonUtil.str2JsonObj(contentJson);
            if (resultJsonObj == null) {
                return -1;
            }
            JsonElement element = resultJsonObj.get(propertyName);
            if (element == null) {
                return -1;
            }
            int propertyValue = element.getAsInt();
            return propertyValue;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return -1;
    }

    /**
     * 获取响应的josn中Long类型的属性值
     *
     * @param propertyName
     * @param contentJson
     * @return -1表示没有查到，但有可能查询的该属性值本来就是-1，可能存在bug。
     */
    public static long getLongProp(String propertyName, String contentJson) {
        try {
            JsonObject resultJsonObj = GsonUtil.str2JsonObj(contentJson);
            if (resultJsonObj == null) {
                return -1;
            }
            JsonElement element = resultJsonObj.get(propertyName);
            if (element == null) {
                return -1;
            }
            long propertyValue = element.getAsLong();
            return propertyValue;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return -1;
    }

    /**
     * 获取响应的json中list<Long>类型的属性值
     *
     * @param propertyName
     * @param contentJson
     * @return null表示没有查到
     */
    public static long[] getLongArrayProp(String propertyName, String contentJson) {
        try {
            JsonObject resultJsonObj = GsonUtil.str2JsonObj(contentJson);
            if (resultJsonObj == null) {
                return null;
            }
            JsonElement element = resultJsonObj.get(propertyName);
            if (element == null) {
                return null;
            }
            JsonArray jsonArray = element.getAsJsonArray();
            List<Long> memberIdList = sGson.fromJson(GsonUtil.jsonObj2Str(jsonArray), new TypeToken<List<Long>>() {
            }.getType());
            if (memberIdList == null) {
                return null;
            }
            long[] array = new long[memberIdList.size()];
            for (int i = 0; i < memberIdList.size(); i++) {
                array[i] = memberIdList.get(i).longValue();
            }
            return array;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * 获取响应的josn中JsonObject类型的属性值
     *
     * @param propertyName
     * @param contentJson
     * @return
     */
    public static JsonObject getJsonObjProp(String propertyName, String contentJson) {
        try {
            JsonObject resultJsonObj = GsonUtil.str2JsonObj(contentJson);
            if (resultJsonObj == null) {
                return null;
            }
            JsonElement element = resultJsonObj.get(propertyName);
            if (element == null) {
                return null;
            }
            JsonObject propertyValue = element.getAsJsonObject();
            return propertyValue;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * 获取相应的josn中jsonarray类型的属性值
     *
     * @param propertyName
     * @param contentJson
     * @return
     */
    public static JsonArray getJsonArrayProp(String propertyName, String contentJson) {
        try {
            JsonObject resultJsonObj = GsonUtil.str2JsonObj(contentJson);
            if (resultJsonObj == null) {
                return null;
            }
            JsonElement element = resultJsonObj.get(propertyName);
            if (element == null) {
                return null;
            }
            JsonArray propertyValue = element.getAsJsonArray();
            return propertyValue;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * 生成content的json字符串
     */
   /* public static String generateContentJsonStr(int req_id, String reqMethod, JsonObject paramsJsonObj) {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(IProtocol.PARAMS_KEY_COMNON_METHOD, reqMethod);
            jsonObject.addProperty(IProtocol.PARAMS_KEY_COMNON_TIMESTAMP, System.currentTimeMillis());
            jsonObject.addProperty(IProtocol.PARAMS_KEY_COMNON_REQ_ID, req_id);
            jsonObject.add(IProtocol.PARAMS_KEY_COMNON_PARAMS, paramsJsonObj);

            String contentStr = sGson.toJson(jsonObject);
            return contentStr;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return null;
    }

    *//**
     * 生成content的json字符串
     *
     * @param nodeId 带nodeid的，用于设备控制
     *//*
    public static String generateContentJsonStr(int req_id, String reqMethod, String nodeId, JsonObject paramsJsonObj) {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(IProtocol.PARAMS_KEY_COMNON_METHOD, reqMethod);
            jsonObject.addProperty(IProtocol.PARAMS_KEY_COMNON_TIMESTAMP, System.currentTimeMillis());
            jsonObject.addProperty(IProtocol.PARAMS_KEY_COMNON_REQ_ID, req_id);
            jsonObject.addProperty(IProtocol.PARAMS_KEY_COMMON_NODE_ID, nodeId);
            jsonObject.add(IProtocol.PARAMS_KEY_COMNON_PARAMS, paramsJsonObj);

            String contentStr = sGson.toJson(jsonObject);
            return contentStr;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return null;
    }

    *//**
     * 生成content的json字符串
     *//*
    public static String generateContentJsonStr(int req_id, String reqMethod, String paramsJson) {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(IProtocol.PARAMS_KEY_COMNON_METHOD, reqMethod);
            jsonObject.addProperty(IProtocol.PARAMS_KEY_COMNON_TIMESTAMP, System.currentTimeMillis());
            jsonObject.addProperty(IProtocol.PARAMS_KEY_COMNON_REQ_ID, req_id);

            StringBuffer temp = new StringBuffer(",\"params\":");
            temp = temp.append(paramsJson).append("}");

            String contentStr = sGson.toJson(jsonObject).replace("}", temp.toString());
            return contentStr;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return null;
    }


    *//**
     * 生成content的json字符串
     *
     * @param nodeId 带nodeid的，用于设备控制
     *//*
    public static String generateContentJsonStr(int req_id, String reqMethod, String nodeId, String paramsJson) {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(IProtocol.PARAMS_KEY_COMNON_METHOD, reqMethod);
            jsonObject.addProperty(IProtocol.PARAMS_KEY_COMNON_TIMESTAMP, System.currentTimeMillis());
            jsonObject.addProperty(IProtocol.PARAMS_KEY_COMNON_REQ_ID, req_id);
            jsonObject.addProperty(IProtocol.PARAMS_KEY_COMMON_NODE_ID, nodeId);

            StringBuffer temp = new StringBuffer(",\"params\":");
            temp = temp.append(paramsJson).append("}");

            String contentStr = sGson.toJson(jsonObject).replace("}", temp.toString());
            return contentStr;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return null;
    }*/

    /**
     * json对象转换为json String
     *
     * @param object
     * @return
     */
    public static String jsonObj2Str(Object object) {
        try {
            return sGson.toJson(object);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * @param object
     * @param type
     * @return 将object转换成json格式的字符串，并将该字符串返回
     */
    public static String objectToJson(Object object, Type type) {
        String result = null;
        try {
            result = sGson.toJson(object, type);
        } catch (Exception e) {
            Log.e(TAG, "convert obj to json exception");
        }
        return result;
    }

    public static <T> String listObjToJson(List<T> datas) {
        try {
            return sGson.toJson(datas);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * jsonObject转换为bean
     *
     * @param jsonObject
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T getObjectFromJson(JsonObject jsonObject, Class<T> tClass) {
        try {
            return sGson.fromJson(jsonObject, tClass);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public static String getJsonFromMap(Map map) {
        return sGson.toJson(map);
    }
}
