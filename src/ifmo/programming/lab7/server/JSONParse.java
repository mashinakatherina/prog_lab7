package ifmo.programming.lab7.server;

import ifmo.programming.lab7.json.*;
import ifmo.programming.lab7.client.Human;

import java.util.ArrayList;

public class JSONParse {
    static ArrayList<Human> getHumansFromJSON(String jsonString) throws Exception {

        ArrayList<Human> building = new ArrayList<>();
        JSONEntity entity = JSONParser.parse(jsonString);
        JSONObject object = entity.toObject("Вместо ожидаемого объекта получен элемент типа " + entity.getType().toString().toLowerCase());
        JSONEntity createdEntity = object.getItem("created");

        JSONEntity collectionEntity = object.getItem("collection");
        if (collectionEntity != null) {
            JSONArray collectionArray = collectionEntity.toArray("Вместо ожидаемого массива имеет тип " + collectionEntity.getType().toString().toLowerCase());
//            building.getCollection().clear();

            for (JSONEntity human : collectionArray.getItems()) {

                JSONObject humanObject = human.toObject("Элементы collection должны быть объектами");
                int age = humanObject.getItem("age").toNumber("Поле age элементов коллекции должно быть числом").toInt(),
                        height = humanObject.getItem("height").toNumber("Поле height элементов коллекции должно быть числом, но это").toInt();

                int x = humanObject.getItem("x").toNumber("Координата x элементов коллекции должна быть числом").toInt();
                int y = humanObject.getItem("y").toNumber("Координата y элементов коллекции должна быть числом").toInt();

                String humanName = "";
                JSONEntity humanNameEntity = humanObject.getItem("name");
                if (humanNameEntity != null) {
                    humanName = humanNameEntity.toString("Поле name элементов массива collection должно быть строкой").getContent();
                }

                JSONEntity skillEntity = humanObject.getItem("skill");
                Human.Skill skill = null;
                String skillName = "";
                int skillcount;

                if (skillEntity != null) {
                    if (!skillEntity.isObject()) {
                        throw new IllegalArgumentException("skill должен быть объектом, но имеет тип" + skillEntity.getType().toString().toLowerCase());
                    }
                    JSONObject skillObject = (JSONObject) skillEntity;
                    JSONEntity skillNameEntity = skillObject.getItem("name");
                    if (skillNameEntity != null) {
                        if (skillNameEntity.isString()) {
                            skillName = ((JSONString) skillNameEntity).getContent();
                        } else {
                            throw new IllegalArgumentException("name должен быть строкой, но имеет тип " + skillNameEntity.getType().toString().toLowerCase());
                        }
                    }

                    JSONEntity skillThingCountEntity = skillObject.getItem("thingcount");
                    if (skillThingCountEntity == null) {
                        skillcount = 0;
                    }

                    if (skillThingCountEntity.isNumber()) {
                        skillcount = (int) ((JSONNumber) skillThingCountEntity).getValue();
                    } else {
                        throw new IllegalArgumentException(" thingcount  должны быть числами, но одно из них имеет тип " + skillThingCountEntity.getType().toString().toLowerCase());
                    }
                    skill = new Human.Skill(skillcount, skillName);
                }
                building.add(new Human(age, height, x, y, humanName, skill));
            }
        }

        return building;

    }

}
