package ifmo.programming.lab7.server;

import ifmo.programming.lab7.client.Human;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;

import static ifmo.programming.lab7.server.RegisterHelper.hashPassword;

public class HumanDatabaseInteractor {

    private static String PASSWORD_SALT = "EDfvcpoi456GESChgfgv10";

    static String makeImport(ArrayList<Human> humans, Connection connection, Integer user_id, String login, String password)throws Exception{
        if (connection == null) return "Сервер не подключён к базе данных.";
        if (user_id == -1 || login == null || password == null) return "Вы не авторизованы. Чтобы войти, введите login.";
        else {
            PreparedStatement s = connection.prepareStatement("select name from users where email = ? and password_hash = ?");
            s.setString(1, login);
            s.setBytes(2, hashPassword(password + PASSWORD_SALT));

            if (s.execute()) {
                try {
                    for (Human human : humans) {
                        PreparedStatement statement = connection.prepareStatement(
                                "insert into humans " +
                                        "(name, height, age, x, y, creationdate, thingcount, skillname, user_id) " +
                                        "values (?, ?, ?, ?, ?, ?, ?, ?, ?)"
                        );
                        statement.setString(1, human.getName());
                        statement.setInt(2, human.getHeight());
                        statement.setInt(3, human.getAge());
                        statement.setInt(4, human.getX());
                        statement.setInt(5, human.getY());
                        statement.setTimestamp(6, new Timestamp(human.getCreationDate().toEpochSecond(ZoneOffset.UTC) * 1000L));
                        statement.setInt(7, human.getSkill().getThingcount());
                        statement.setString(8, human.getSkill().getName());
                        statement.setInt(9, user_id);
                        statement.execute();
                    }

                    return "Загрузка " + humans.size() + " прошла успешно.";
                } catch (SQLException e) {
                    return "Возникла внутренняя ошибка сервера.";
                }

            } else {
                return "Вы не авторизованы. Чтобы войти, введите login.";
            }

        }
    }

    static String addHuman(Human human, Connection connection, Integer user_id, String login, String password){
        if (connection == null) return "Сервер не подключён к базе данных.";
        if (user_id == -1 || login == null || password == null) return "Вы не авторизованы. Чтобы войти, введите login.";
        else { try{
            try {
                PreparedStatement statement = connection.prepareStatement(
                        "select * from " + "users where id = ? and email = ? and password_hash = ?"
                );
                statement.setInt(1, user_id);
                statement.setString(2, login);
                statement.setBytes(3, hashPassword(password + PASSWORD_SALT));

                ResultSet resultSet = statement.executeQuery();
                if (!resultSet.next()) return "Вы не авторизованы. Чтобы войти, введите login.";

                statement = connection.prepareStatement(
                        "insert into " + "humans " +
                                "(name, height, age, x, y, creationdate, thingcount, skillname, user_id) " +
                                "values (?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );
                statement.setString(1, human.getName());
                statement.setInt(2, human.getHeight());
                statement.setInt(3, human.getAge());
                statement.setInt(4, human.getX());
                statement.setInt(5, human.getY());
                statement.setTimestamp(6,new Timestamp(human.getCreationDate().toEpochSecond(ZoneOffset.UTC)*1000L));
                statement.setInt(7, human.getSkill().getThingcount());
                statement.setString(8, human.getSkill().getName());
                statement.setInt(9, user_id);

                statement.execute();
                return "Человек " + human.getName() + " успешно добавлен.";
            } catch (SQLException | NoSuchAlgorithmException| UnsupportedEncodingException e) {
                return "Возникла внутренняя ошибка сервера.";
            } } catch (Exception e){return "Что-то пошло не так.";}
        }
    }

    static String removeHuman(Human human, Connection connection, Integer user_id, String login, String password) {
        if (connection == null) {
            return "Сервер не подключён к базе данных";
        }
        if (user_id == -1 || login == null || password == null) return "Вы не авторизованы. Чтобы войти, введите login.";
        else {
            try {

                PreparedStatement statement = connection.prepareStatement(
                        "select * from " + "users where id = ? and email = ? and password_hash = ?"
                );
                statement.setInt(1, user_id);
                statement.setString(2, login);
                statement.setBytes(3, hashPassword(password + PASSWORD_SALT));

                ResultSet resultSet = statement.executeQuery();
                if (!resultSet.next()) return "Вы не авторизованы. Чтобы войти, введите login. ";

                statement = connection.prepareStatement(
                        "delete from humans where " +
                                "name = ? and height = ? and age = ? and x = ? and y = ? and " +
                                "thingcount = ? and skillname = ? and user_id = ?"
                );
                statement.setString(1, human.getName());
                statement.setInt(2, human.getHeight());
                statement.setInt(3, human.getAge());
                statement.setInt(4, human.getX());
                statement.setInt(5, human.getY());
                statement.setInt(6, human.getSkill().getThingcount());
                statement.setString(7, human.getSkill().getName());
                statement.setInt(8, user_id);

                int removed = statement.executeUpdate();
                return "Удалено " + removed + " человек.";

            } catch (SQLException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
                return "Возникла внутренняя ошибка сервера.";
            }
        }
    }

    static String removeGreaterThanHuman(Human human, Connection connection, Integer user_id, String login, String password) {
        if (connection == null) {
            return "Сервер не подключён к базе данных";
        }
        if (user_id == -1 || login == null || password == null) return "Вы не авторизованы. Чтобы войти, введите login.";
        else {
            try {
                PreparedStatement statement = connection.prepareStatement(
                        "select count(*) from " + "users where id = ? and email = ? and password_hash = ?"
                );
                statement.setInt(1, user_id);
                statement.setString(2, login);
                statement.setBytes(3, hashPassword(password + PASSWORD_SALT));

                ResultSet resultSet = statement.executeQuery();
                if (!resultSet.next()) return "Вы не авторизованы. Чтобы войти, введите login. ";
                statement = connection.prepareStatement(
                        "delete from humans where user_id = ? and name > ?"
                );
                statement.setInt(1, user_id);
                statement.setString(2, human.getName());

                int removed = statement.executeUpdate();
                statement.execute();

                return "Удалено " + removed + " человек.";

            } catch (SQLException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
                return "Возникла внутренняя ошибка сервера.";
            }
        }
    }


    static String removeLowerThanHuman(Human human, Connection connection, Integer user_id, String login, String password) {
        if (connection == null) {
            return "Сервер не подключён к базе данных";
        }
        if (user_id == -1 || login == null || password == null) return "Вы не авторизованы. Чтобы войти, введите login.";
        else {
            try {
                PreparedStatement statement = connection.prepareStatement(
                        "select count(*) from " + "users where id = ? and email = ? and password_hash = ?"
                );
                statement.setInt(1, user_id);
                statement.setString(2, login);
                statement.setBytes(3, hashPassword(password + PASSWORD_SALT));

                ResultSet resultSet = statement.executeQuery();
                if (!resultSet.next()) return "Вы не авторизованы. Чтобы войти, введите login.";

                statement = connection.prepareStatement(
                        "delete from humans where user_id = ? and name < ?"
                );
                statement.setInt(1, user_id);
                statement.setString(2, human.getName());

                int removed = statement.executeUpdate();
                statement.execute();

                return "Удалено " + removed + " человек.";

            } catch (SQLException | NoSuchAlgorithmException | UnsupportedEncodingException e){
                return "Возникла внутренняя ошибка сервера.";
            }
        }
    }


    static String makeShow(Connection connection){
        try {
            Statement statement = connection.createStatement();
            ResultSet humansResultSet = statement.executeQuery("select * from humans");
            StringBuilder builder = new StringBuilder();
            if (!humansResultSet.next()) return ("Коллекция пуста.");
            do {
                Human human = new Human(humansResultSet.getInt("age"),
                        humansResultSet.getInt("height"),
                        humansResultSet.getInt("x"),
                        humansResultSet.getInt("y"),
                        humansResultSet.getString("name"),
                        new Human.Skill(humansResultSet.getInt("thingcount"), humansResultSet.getString("skillname")));
                builder.append(human.toString() + "\n");
            } while (humansResultSet.next());
            return builder.toString();
        } catch (SQLException e) {
            return "Произошла ошибка SQL.";
        }

    }

    /**
     * @return Информация о сервере в читабельном виде
     */
    static String getInfo(Connection connection) {
        if (connection == null)
            return ("Сервер не подключён к базе данных");
        try {
            ResultSet humansResult = connection.createStatement().executeQuery("select count(*) from " + "humans");
            ResultSet usersResult = connection.createStatement().executeQuery("select count(*) from " + "users");
            humansResult.next();
            usersResult.next();

            int humans = humansResult.getInt(1);
            int users = usersResult.getInt(1);

            return "Зарегистрировано пользователей: " + users + "\n" +
                    "Хранится человек: " + humans;
        } catch (SQLException e) {
            System.err.println("Во время выдачи информации произошла ошибка SQL: " + e.toString());
            return ("Возникла внутренняя ошибка сервера.");
        }
    }

    /**
     * справка по командам, реализуемым приложением
     * @return справка по командам, реализуемым приложением
     */
    static String getHelp() {
        return "Оу, похоже, вам нужна помощь?" +
                "\nПриложение поддерживает выполнение следующих команд:" +
                "\n\t• add {element}: добавить новый элемент в коллекцию; пример:" +
                "{\"x\": 10, \"y\": 12, \"age\": 5, \"name\": \"Иван-Александр\", \"height\": 10," +
                " \n\t\"skill\": { \"thingcount\": 1, \"name\": \"eat\" } } " +
                "\n\t• remove_lower: удалить из коллекции все элементы, меньшие, чем заданный;" +
                "\n\t• remove_greater {element}: удалить из коллекции все элементы, превышающие заданный;" +
                "\n\t• show: вывести в стандартный поток вывода все элементы коллекции в строковом представлении;" +
                "\n\t• info: вывести в стандартный поток вывода информацию о коллекции;" +
                "\n\t• load: перечитать коллекцию из файла;" +
                "\n\t• remove {element}: удалить элемент из коллекции по его значению;" +
                "\n\t• import: добавить данные из файла клиента в коллекцию;" +
                "\n\t• register: зарегистрировать пользователя;" +
                "\n\t• login: войти в аккаунт;" +
                "\n\t• logout: выйти из аккаунта;" +
                "\n\t• help: вызов справки.";
    }

}
