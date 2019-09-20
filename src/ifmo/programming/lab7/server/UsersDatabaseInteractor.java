package ifmo.programming.lab7.server;

import ifmo.programming.lab7.Utils.StringEntity;
import ifmo.programming.lab7.Utilities;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ifmo.programming.lab7.Message;

public class UsersDatabaseInteractor {

        private static String PASSWORD_SALT = "EDfvcpoi456GESChgfgv10";

        static String register(String name, String email, Connection connection) {

            if (name.length() < 2) {
                return "Вы ввели неверное имя.";
            }
            if (!Utilities.isValidEmailAddress(email)){
                return "Вы ввели некорректный e-mail.";
            }

            if (connection == null){
                return "Сервер не подключён к базе данных.";
            }

            try {
                PreparedStatement statement = connection.prepareStatement("select email from users WHERE email = ?");
                statement.setString(1, email);

                if (statement.executeQuery().next())
                    return "Этот email уже зарегистрирован.";

                String password = RegisterHelper.randomString(9);
                System.out.println(password);

                Server.sendEMail(email, "Подтверждение регистрации",
                        "Ваш пароль " +
                                password
                );
                PreparedStatement statement1 = connection.prepareStatement("insert into users " +
                        "(name, email, password_hash) values (?, ?, ?)");

                statement1.setString(1, name);
                statement1.setString(2, email);
                statement1.setBytes(3, RegisterHelper.hashPassword(password + PASSWORD_SALT));

                statement1.execute();
                statement.execute();

                return "На указанный адрес электронной почты отправлен пароль.\n" +
                        "Введите login, чтобы выполнить вход.\n ";
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return "Возникла внутренняя ошибка сервера.";
            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
                return "Возникла ошибка сервера.";
            } catch (AddressException e) {
//                e.printStackTrace();
                return "Вы ввели некорректный e-mail.";
            } catch (MessagingException | GeneralSecurityException e) {
//                e.printStackTrace();
                return "Возникла внутренняя ошибка сервера.]";
            }
        }

        static Message login(String email, String password, Connection connection) {
            if (connection == null)
                return new Message("Сервер не подключен к базе данных.");
            try {
                PreparedStatement statement = connection.prepareStatement("select * from users " +
                        "where email = ? and password_hash = ?");
                byte[] bytes = RegisterHelper.hashPassword(password + PASSWORD_SALT);

                statement.setString(1, email);
                statement.setBytes(2, bytes);
                statement.execute();
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int userid = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    Message result = new Message(name + " , вход успешно выполнен.", new String[]{Integer.toString(userid), email, password});
                    return result;
                } else {
                    return new Message("Вход не выполнен: неверная пара email/пароль.");
                }
            }catch (UnsupportedEncodingException e){
                return new Message("Возникла внутренняя ошибка сервера.");
            } catch (GeneralSecurityException e) {
                return new Message("Вход не выполнен: возникла внутренняя ошибка сервера.");
            } catch (SQLException e) {
                return new Message("Вход не выполнен: возникла внутренняя ошибка сервера.");
            }
        }

}
