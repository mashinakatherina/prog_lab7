package ifmo.programming.lab7.client;

import ifmo.programming.lab7.json.*;

/**
 *
 */
class HumanFactory {

    /**
     * создает объект-человека из json-представления
     * @param json - json-представление объекта
     * @return - объект класса Human
     * @throws JSONParseException
     * @throws IllegalArgumentException
     */
    static Human makeHumanFromJSON(String json) throws JSONParseException, IllegalArgumentException {
        JSONEntity entity = JSONParser.parse(json);
        Human.Skill skill;
        if (!entity.isObject()) {
            throw new IllegalArgumentException("Данный json должен быть объектом, но имеет тип " + entity.getType().toString().toLowerCase());
        }

        JSONObject object = (JSONObject)entity;

        JSONEntity coordXEntity = object.getItem("x");
        JSONEntity coordYEntity = object.getItem("y");

        if (coordXEntity == null || coordYEntity == null) {
            throw new IllegalArgumentException("Координаты должны быть обязательно указаны.");
        }

        if (!(coordXEntity.isNumber()&&coordYEntity.isNumber())) {
            throw new IllegalArgumentException("Координаты должны быть целыми числами.");
        }

        int coordX = (int)((JSONNumber)coordXEntity).getValue();
        int coordY = (int)((JSONNumber)coordYEntity).getValue();

        JSONEntity ageEntity = object.getItem("age");
        JSONEntity heightEntity = object.getItem("height");

        if (ageEntity == null || heightEntity == null /*|| lengthEntity == null*/) {
            throw new IllegalArgumentException("age, height должны быть обязательно указаны.");
        }

        if (!(ageEntity.isNumber() && heightEntity.isNumber()/* && lengthEntity.isNumber()*/)) {
            throw new IllegalArgumentException("age, height должны быть числами.");
        }

        int age = (int)((JSONNumber)ageEntity).getValue();
        int height = (int)((JSONNumber)heightEntity).getValue();
        int length = /*(int)((JSONNumber)lengthEntity).getValue();*/ 50;

        String name = "";
        JSONEntity nameEntity = object.getItem("name");
        if (nameEntity != null) {
            if (nameEntity.isString()) {
                name = ((JSONString)nameEntity).getContent();
            }
            else {
                throw new IllegalArgumentException("name должен быть строкой, но имеет тип " + nameEntity.getType().toString().toLowerCase());
            }
        }

        JSONEntity skillEntity = object.getItem("skill");
        String skillName = "";
        int skillcount;

        if (skillEntity != null) {
            if (!skillEntity.isObject()) {
                throw new IllegalArgumentException("skill должен быть объектом, но имеет тип" + skillEntity.getType().toString().toLowerCase());
            }
            JSONObject skillObject = (JSONObject)skillEntity;
            JSONEntity skillNameEntity = skillObject.getItem("name");
            if (skillNameEntity != null) {
                if (skillNameEntity.isString()) {
                    skillName = ((JSONString)skillNameEntity).getContent();
                }
                else {
                    throw new IllegalArgumentException("name  должен быть строкой, но имеет тип " + nameEntity.getType().toString().toLowerCase());
                }
            }

            JSONEntity skillThingCountEntity = skillObject.getItem("thingcount");
            if (skillThingCountEntity == null ) {
                skillcount = 0;
            }

            if (skillThingCountEntity.isNumber()) { skillcount = (int)((JSONNumber) skillThingCountEntity).getValue();}
            else {
                throw new IllegalArgumentException("thingcount  должны быть числами, но одно из них имеет тип " + skillThingCountEntity.getType().toString().toLowerCase());
            }
            skill = new Human.Skill(skillcount, skillName);
        } else skill = null;

        return new Human(age, height, coordX, coordY,  name, skill);
    }

    /**
     * Создаёт людей из их json-представления. Если получен json-объект, сгенерируется один человек.
     * Если получен json-массив объектов, будет прочтён каждый объект внутри массива и возвращён
     * массив людей, сгенерированных для каждого объекта
     * @param json json-представление
     * @return массив людей
     * @throws Exception если что-то пошло не по плану
     */
    static Human[] makeHumansFromJSON(String json) throws Exception {
        JSONEntity entity = JSONParser.parse(json);

        if (entity == null) {throw new IllegalArgumentException("Требуется json-объект, но получен null"); }
        if (entity.isObject()) { return new Human[]{makeHumanFromJSON(entity.toString())}; }
        else {
            if (entity.isArray()) {
                JSONArray humanArray = entity.toArray();
                Human[] humans = new Human[humanArray.size()];
                for (int i = 0; i < humanArray.size(); i++) {
                    humans[i] = makeHumanFromJSON(String.valueOf(humanArray.getItem(i).toObject()));
                }
                return humans;
            }
            else {
                throw new IllegalArgumentException("Ошибка: не все элементы массива являются объектами.");
            }
        }
    }
}
