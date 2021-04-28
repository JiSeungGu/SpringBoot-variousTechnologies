package net.sejongtelecom.backendapi.common.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CommonUtil {

  public static JSONObject convertStringToJSONObject(String JSONString) {

    JSONObject jsonObject = new JSONObject();
    JSONParser parser = new JSONParser();

    try {
      jsonObject = (JSONObject) parser.parse(JSONString);
    }catch (Exception e){
      e.printStackTrace();
    }

    return jsonObject;
  }

}
