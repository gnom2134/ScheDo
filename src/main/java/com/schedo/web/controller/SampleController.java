package com.schedo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class SampleController {
    @GetMapping("/sample")
    public String showForm(ModelMap model) {
        StringBuilder result = new StringBuilder();
        try {
            DBConnector connector = new DBConnector();
            connector.createPreparedStatement("SELECT * FROM public.\"USER\";");
            ResultSet resultSet = connector.executePreparedStatementWithResult();

            result.append("<table border=\"2\">");
            while (resultSet.next()) {
                result.append("<tr>");
                result.append("<td>");
                result.append(resultSet.getString(2));
                result.append("</td>");
                result.append("<td>");
                result.append(resultSet.getString(3));
                result.append("</td>");
                result.append("<td>");
                result.append(resultSet.getString(4));
                result.append("</td>");
                result.append("<td>");
                result.append(resultSet.getBoolean(5));
                result.append("</td>");
                result.append("</tr>");
            }
            result.append("</table>");

            connector.endSession();

            model.addAttribute("Table", result.toString());
            return "sample";
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.append("Whooops!");
        model.addAttribute("Table", result.toString());
        return "sample";
    }
}

@Controller
class IndexController {
    @GetMapping({"/", "/index"})
    public String showForm(HttpSession session) {
        session.invalidate();
        return "index";
    }
}

@Controller
class RegistrationController {
    @GetMapping("/registration")
    public String showForm() {
        return "registrationWindow";
    }
}

@Controller
class RegistrationControllerHandler {
    @PostMapping("/registration")
    public String showForm(WebRequest request) {
        try {
            DBConnector connector = new DBConnector();

            Map<String, String[]> params = request.getParameterMap();
            String email = params.get("email")[0];
            String login = params.get("login")[0];
            String password = params.get("password")[0];
            String is_company = params.get("is_company")[0];

            connector.createPreparedStatement
                    ("INSERT INTO public.\"USER\"(name, email, password, is_company) VALUES (?, ?, ?, ?);");

            connector.setParameter(1, login);
            connector.setParameter(2, email);
            connector.setParameter(3, password);
            connector.setParameter(4, is_company.equals("Yes"));

            connector.executePreparedStatement();

            connector.endSession();

            return "index";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "registrationWindow";
    }
}

@Controller
class LogInController {
    @GetMapping("/log_in")
    public String showForm() {
        return "log_in";
    }
}

@Controller
class LogInControllerHandler {
    @PostMapping("/log_in")
    public String showForm(HttpServletRequest request, ModelMap model, HttpSession session) {
        try {
            session.invalidate();

            DBConnector connector = new DBConnector();

            Map<String, String[]> params = request.getParameterMap();
            String login = params.get("login")[0];
            String password = params.get("password")[0];

            connector.createPreparedStatement("SELECT * FROM \"USER\" WHERE name = ? AND password = ?;");

            connector.setParameter(1, login);
            connector.setParameter(2, password);
            connector.executePreparedStatement();

            ResultSet resultSet = connector.executePreparedStatementWithResult();

            if (resultSet.isLast()) throw new IllegalArgumentException("No such user exception");
            else resultSet.next();

            model.addAttribute("name", resultSet.getString(2));
            int user_id = resultSet.getInt(1);
            boolean isCompany = resultSet.getBoolean(5);

            connector.endSession();

            session = request.getSession();
            session.setAttribute("is_company", isCompany);
            session.setAttribute("name", login);
            session.setAttribute("password", password);
            session.setAttribute("user_id", user_id);

            if (isCompany) {
                return "company_profile";
            } else {
                return "userProfile";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "log_in";
    }
}

@Controller
class UserPageController {
    @GetMapping({"/user_profile", "/company_profile"})
    public String showForm(HttpSession session, ModelMap model) {
        try {
            DBConnector connector = new DBConnector();

            String login = (String) session.getAttribute("name");
            String password = (String) session.getAttribute("password");

            System.out.println(login + " " + password);

            connector.createPreparedStatement("SELECT * FROM \"USER\" WHERE name = ? AND password = ?;");

            connector.setParameter(1, login);
            connector.setParameter(2, password);
            ResultSet resultSet = connector.executePreparedStatementWithResult();

            if (resultSet.isLast()) throw new IllegalArgumentException("No such user exception");
            else resultSet.next();

            model.addAttribute("name", resultSet.getString(2));

            connector.endSession();
            if ((Boolean) session.getAttribute("is_company")) {
                return "company_profile";
            } else {
                return "userProfile";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }
}

@Controller
class MySchedulesController {
    @GetMapping({"/user_schedules", "/company_schedules"})
    public String showForm(HttpSession session, ModelMap model) {
        try {
            DBConnector connector = new DBConnector();
            connector.createPreparedStatement("SELECT * FROM \"SCHEDULE\" INNER JOIN \"USER\" " +
                    "ON (\"USER\".id = \"SCHEDULE\".user_id) " +
                    "WHERE \"USER\".name = ? AND \"USER\".password = ?; ");

            String login = (String) session.getAttribute("name");
            String password = (String) session.getAttribute("password");

            connector.setParameter(1, login);
            connector.setParameter(2, password);
            ResultSet resultSet = connector.executePreparedStatementWithResult();

//            if (resultSet.isLast()) throw new IllegalArgumentException("No such user exception");

            List<Schedule> scheduleNames = new ArrayList<Schedule>();

            while (resultSet.next()) {
                scheduleNames.add(new Schedule(resultSet.getInt(1), resultSet.getString(3)));
            }

            model.addAttribute("schedules", scheduleNames);

            connector.endSession();

            if ((Boolean) session.getAttribute("is_company")) {
                return "company_schedules";
            } else {
                return "my_schedules";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "my_schedules";
    }
}

@Controller
class ScheduleController {
    @GetMapping({"/schedule", "/company_schedule"})
    public String showForm(HttpSession session, ModelMap model, HttpServletRequest request) {
        try {
            DBConnector connector = new DBConnector();
            connector.createPreparedStatement("SELECT * FROM \"DAY\" INNER JOIN (" +
                    "SELECT \"SCHEDULE\".id FROM \"SCHEDULE\" INNER JOIN \"USER\"" +
                    " ON (\"USER\".id = \"SCHEDULE\".user_id)" +
                    " WHERE \"USER\".name = ? AND \"USER\".password = ?" +
                    " AND \"SCHEDULE\".id = ?) AS some_schedule " +
                    "ON (\"DAY\".schedule_id = some_schedule.id)" +
                    "ORDER BY \"DAY\".id");

            Map<String, String[]> params = request.getParameterMap();

            String login = (String) session.getAttribute("name");
            String password = (String) session.getAttribute("password");
            int schedule_id = Integer.parseInt(params.get("id")[0]);
            session.removeAttribute("schedule_id");
            session.setAttribute("schedule_id", schedule_id);

            connector.setParameter(1, login);
            connector.setParameter(2, password);
            connector.setParameter(3, schedule_id);
            ResultSet resultSet = connector.executePreparedStatementWithResult();

            List<Day> days = new ArrayList<Day>();

            while (resultSet.next()) {
                days.add(new Day(resultSet.getInt("id"), resultSet.getString("day_of_the_week")));
            }

            connector.createPreparedStatement("SELECT * FROM \"ITEM\" INNER JOIN (" +
                    "SELECT \"DAY\".id FROM \"DAY\" INNER JOIN (" +
                    "SELECT \"SCHEDULE\".id FROM \"SCHEDULE\" INNER JOIN \"USER\"" +
                    " ON (\"USER\".id = \"SCHEDULE\".user_id)" +
                    " WHERE \"USER\".name = ? AND \"USER\".password = ?" +
                    " AND \"SCHEDULE\".id = ?) AS some_schedule " +
                    "ON (\"DAY\".schedule_id = some_schedule.id)" +
                    "WHERE \"DAY\".id = ?) AS some_day " +
                    "ON \"ITEM\".day_id = some_day.id " +
                    "ORDER BY end_time");

            for (Day day : days) {
                connector.setParameter(1, login);
                connector.setParameter(2, password);
                connector.setParameter(3, schedule_id);
                connector.setParameter(4, day.getId());
                resultSet = connector.executePreparedStatementWithResult();

                while (resultSet.next()) {
                    day.addItem(new Item(resultSet.getInt("id"), resultSet.getString("activity"), resultSet.getString("end_time")));
                }
            }

            model.addAttribute("days", days);

            connector.endSession();

            if ((Boolean) session.getAttribute("is_company")) {
                return "company_schedule";
            } else {
                return "schedule";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "schedule";
    }
}

@Controller
class DeleteScheduleController {
    @GetMapping("/delete_schedule")
    public String showForm(HttpSession session, ModelMap model) {
        try {
            DBConnector connector = new DBConnector();
            int schedule_id = (Integer) session.getAttribute("schedule_id");
            session.removeAttribute("schedule_id");

            connector.createPreparedStatement("SELECT id FROM \"DAY\" WHERE schedule_id = ?;");
            connector.setParameter(1, schedule_id);

            List<Integer> days_to_delete = new ArrayList<Integer>();
            ResultSet resultSet = connector.executePreparedStatementWithResult();

            while (resultSet.next()) {
                days_to_delete.add(resultSet.getInt(1));
            }

            connector.createPreparedStatement("DELETE FROM \"ITEM\" WHERE day_id = ?;");
            for (Integer i : days_to_delete) {
                connector.setParameter(1, i);
                connector.executePreparedStatement();
            }

            connector.createPreparedStatement("DELETE FROM \"DAY\" WHERE schedule_id = ?;");
            connector.setParameter(1, schedule_id);
            connector.executePreparedStatement();

            connector.createPreparedStatement("DELETE FROM \"RECOMMENDATIONS\" WHERE schedule_id = ?");
            connector.setParameter(1, schedule_id);
            connector.executePreparedStatement();

            connector.createPreparedStatement("DELETE FROM \"SCHEDULE\" WHERE id = ?;");
            connector.setParameter(1, schedule_id);
            connector.executePreparedStatement();

            String user_name = (String) session.getAttribute("name");
            model.addAttribute("name", user_name);
            return "userProfile";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "userProfile";
    }
}

@Controller
class ChangeScheduleController {
    @GetMapping({"/change_schedule", "/change_company_schedule"})
    public String showForm(HttpSession session, ModelMap model) {
        try {
            DBConnector connector = new DBConnector();
            connector.createPreparedStatement("SELECT * FROM \"DAY\" INNER JOIN (" +
                    "SELECT \"SCHEDULE\".id FROM \"SCHEDULE\" INNER JOIN \"USER\"" +
                    " ON (\"USER\".id = \"SCHEDULE\".user_id)" +
                    " WHERE \"USER\".name = ? AND \"USER\".password = ?" +
                    " AND \"SCHEDULE\".id = ?) AS some_schedule " +
                    "ON (\"DAY\".schedule_id = some_schedule.id)" +
                    "ORDER BY \"DAY\".id");

            String login = (String) session.getAttribute("name");
            String password = (String) session.getAttribute("password");
            int schedule_id = (Integer) session.getAttribute("schedule_id");

            connector.setParameter(1, login);
            connector.setParameter(2, password);
            connector.setParameter(3, schedule_id);
            ResultSet resultSet = connector.executePreparedStatementWithResult();

            List<Day> days = new ArrayList<Day>();

            while (resultSet.next()) {
                days.add(new Day(resultSet.getInt(1), resultSet.getString(2)));
            }

            connector.createPreparedStatement("SELECT * FROM \"ITEM\" INNER JOIN (" +
                    "SELECT \"DAY\".id FROM \"DAY\" INNER JOIN (" +
                    "SELECT \"SCHEDULE\".id FROM \"SCHEDULE\" INNER JOIN \"USER\"" +
                    " ON (\"USER\".id = \"SCHEDULE\".user_id)" +
                    " WHERE \"USER\".name = ? AND \"USER\".password = ?" +
                    " AND \"SCHEDULE\".id = ?) AS some_schedule " +
                    "ON (\"DAY\".schedule_id = some_schedule.id)" +
                    "WHERE \"DAY\".id = ?) AS some_day " +
                    "ON \"ITEM\".day_id = some_day.id " +
                    "ORDER BY end_time");

            for (Day day : days) {
                connector.setParameter(1, login);
                connector.setParameter(2, password);
                connector.setParameter(3, schedule_id);
                connector.setParameter(4, day.getId());
                resultSet = connector.executePreparedStatementWithResult();

                while (resultSet.next()) {
                    day.addItem(new Item(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
                }
                resultSet.close();
            }

            model.addAttribute("days", days);

            connector.endSession();
            if ((Boolean) session.getAttribute("is_company")) {
                return "change_company_schedule";
            } else {
                return "change_schedule";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((Boolean) session.getAttribute("is_company")) {
            return "company_profile";
        } else {
            return "userProfile";
        }
    }
}

@Controller
class ChangeScheduleControllerHandler {
    private boolean contains(String[] strings, String found) {
        for (String i : strings) {
            if (i.equals(found)) return true;
        }
        return false;
    }

    @PostMapping({"/change_schedule", "/change_company_schedule"})
    public String showForm(HttpSession session, ModelMap model, HttpServletRequest request) {
        try {
            DBConnector connector = new DBConnector();

            Map<String, String[]> params = request.getParameterMap();
            String[] is_delete = params.get("is_delete");
            String[] activity_name = params.get("activity");
            String[] time_value = params.get("time");
            String[] item_id = params.get("item_id");
            int day_id = Integer.parseInt(params.get("day_id")[0]);
            System.out.println(activity_name.length);
            System.out.println(time_value.length);
            System.out.println(item_id.length);
            System.out.println(day_id);

            connector.createPreparedStatement("INSERT INTO \"ITEM\" (activity, end_time, day_id) VALUES (?, ?, ?);");

            for (int i = 0; i < activity_name.length; i++) {
                if (is_delete != null && contains(is_delete, item_id[i])) continue;
                if (Integer.parseInt(item_id[i]) <= 0) {
                    connector.setParameter(1, activity_name[i]);
                    connector.setParameter(2, time_value[i]);
                    connector.setParameter(3, day_id);
                    connector.executePreparedStatement();
                }
            }

            connector.createPreparedStatement("UPDATE \"ITEM\" SET activity = ?, end_time = ? WHERE id = ?;");

            for (int i = 0; i < activity_name.length; i++) {
                if (is_delete != null && contains(is_delete, item_id[i])) continue;
                connector.setParameter(1, activity_name[i]);
                connector.setParameter(2, time_value[i]);
                connector.setParameter(3, Integer.parseInt(item_id[i]));
                connector.executePreparedStatement();
            }

            connector.createPreparedStatement("DELETE FROM \"ITEM\" WHERE id = ?;");

            for (int i = 0; i < activity_name.length; i++) {
                if (Integer.parseInt(item_id[i]) <= 0) continue;
                if (is_delete != null && contains(is_delete, item_id[i])) {
                    connector.setParameter(1, Integer.parseInt(item_id[i]));
                    connector.executePreparedStatement();
                }
            }

            connector.createPreparedStatement("SELECT * FROM \"DAY\" INNER JOIN (" +
                    "SELECT \"SCHEDULE\".id FROM \"SCHEDULE\" INNER JOIN \"USER\"" +
                    " ON (\"USER\".id = \"SCHEDULE\".user_id)" +
                    " WHERE \"USER\".name = ? AND \"USER\".password = ?" +
                    " AND \"SCHEDULE\".id = ?) AS some_schedule " +
                    "ON (\"DAY\".schedule_id = some_schedule.id)" +
                    "ORDER BY \"DAY\".id");

            String login = (String) session.getAttribute("name");
            String password = (String) session.getAttribute("password");
            int schedule_id = (Integer) session.getAttribute("schedule_id");

            connector.setParameter(1, login);
            connector.setParameter(2, password);
            connector.setParameter(3, schedule_id);
            ResultSet resultSet = connector.executePreparedStatementWithResult();

            List<Day> days = new ArrayList<Day>();

//            if (resultSet.isLast()) throw new IllegalArgumentException("No such user exception");

            while (resultSet.next()) {
                days.add(new Day(resultSet.getInt(1), resultSet.getString(2)));
            }

            connector.createPreparedStatement("SELECT * FROM \"ITEM\" INNER JOIN (" +
                    "SELECT \"DAY\".id FROM \"DAY\" INNER JOIN (" +
                    "SELECT \"SCHEDULE\".id FROM \"SCHEDULE\" INNER JOIN \"USER\"" +
                    " ON (\"USER\".id = \"SCHEDULE\".user_id)" +
                    " WHERE \"USER\".name = ? AND \"USER\".password = ?" +
                    " AND \"SCHEDULE\".id = ?) AS some_schedule " +
                    "ON (\"DAY\".schedule_id = some_schedule.id)" +
                    "WHERE \"DAY\".id = ?) AS some_day " +
                    "ON \"ITEM\".day_id = some_day.id " +
                    "ORDER BY end_time");

            for (Day day : days) {
                connector.setParameter(1, login);
                connector.setParameter(2, password);
                connector.setParameter(3, schedule_id);
                connector.setParameter(4, day.getId());
                resultSet = connector.executePreparedStatementWithResult();

                while (resultSet.next()) {
                    day.addItem(new Item(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
                }
                resultSet.close();
            }

            model.addAttribute("days", days);

            connector.endSession();

            if ((Boolean) session.getAttribute("is_company")) {
                return "change_company_schedule";
            } else {
                return "change_schedule";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "userProfile";
    }
}

@Controller
class NewScheduleController {
    @GetMapping({"/new_schedule", "/new_company_schedule"})
    public String showForm(HttpSession session) {
        if ((Boolean) session.getAttribute("is_company")) {
            return "new_company_schedule";
        } else {
            return "new_schedule";
        }
    }
}

@Controller
class NewScheduleControllerHandler {
    private boolean contains(String[] array, String value) {
        for (String i : array) {
            if (i.equals(value)) return true;
        }
        return false;
    }

    private void setParameters(Item item, DBConnector connector, int number) throws SQLException {
        connector.setParameter(1, item.getActivity());
        connector.setParameter(2, item.getEnd_time());
        connector.setParameter(3, number);
        connector.executePreparedStatement();
    }

    @PostMapping({"/new_schedule", "/new_company_schedule"})
    public String showForm(HttpSession session, ModelMap model, HttpServletRequest request) {
        try {
            DBConnector connector = new DBConnector();
            connector.createPreparedStatement("INSERT INTO \"SCHEDULE\"(user_id, name) VALUES (?, ?);");

            Map<String, String[]> params = request.getParameterMap();
            int user_id = (Integer) session.getAttribute("user_id");
            String schedule_name = params.get("name")[0];

            connector.setParameter(1, user_id);
            connector.setParameter(2, schedule_name);
            connector.executePreparedStatement();

            connector.createPreparedStatement("SELECT max(id) FROM \"SCHEDULE\";");
            ResultSet resultSet = connector.executePreparedStatementWithResult();
            resultSet.next();
            int schedule_id = resultSet.getInt(1);

            String[] days_of_the_week = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
            String[] work_days = params.get("work_days");

            connector.createPreparedStatement("INSERT INTO \"DAY\"(day_of_the_week, schedule_id) VALUES (?, ?);");

            for (String i : days_of_the_week) {
                connector.setParameter(1, i);
                connector.setParameter(2, schedule_id);
                connector.executePreparedStatement();
            }

            connector.createPreparedStatement("SELECT max(id) FROM \"DAY\";");
            resultSet = connector.executePreparedStatementWithResult();
            resultSet.next();
            int max_day_id = resultSet.getInt(1);

            int mealTimes = Integer.parseInt(params.get("meals")[0]); // from 2 to 5

            Day workDay = new Day(1, "");
            workDay.addItem(new Item(1, "Sleep", "00-00"));
            workDay.addItem(new Item(1, "Morning business", params.get("sleep_over_time")[0])); // required wake up time < work start time
            workDay.addItem(new Item(1, "Sleep", params.get("sleep_time")[0]));

            Day freeDay = new Day(1, "");
            freeDay.addItem(new Item(1, "Sleep", "00-00"));
            freeDay.addItem(new Item(1, "Morning business", params.get("sleep_over_time")[0])); // required wake up time < work start time
            freeDay.addItem(new Item(1, "Sleep", params.get("sleep_time")[0]));

            int dayTime = workDay.getItems().get(2).getMinutes() - workDay.getItems().get(1).getMinutes();
            for (int i = 0; i < mealTimes; i++) {
                Item prevItem = workDay.getItems().get(1 + i);
                workDay.addItem(new Item(1, "Meal time", Item.timeWithMinutes(prevItem.getMinutes() + dayTime / (mealTimes + 1))), i + 2);
                freeDay.addItem(new Item(1, "Meal time", Item.timeWithMinutes(prevItem.getMinutes() + dayTime / (mealTimes + 1))), i + 2);
            }

            workDay.sortItems();

            int static_size = workDay.getItems().size() - 1;
            for (int i = 0; i < static_size; i++) {
                if (workDay.getItems().get(i + 1).compareByTime(params.get("work_start")[0]) > 0 &&
                        workDay.getItems().get(i).compareByTime(params.get("work_start")[0]) < 0) {
                    workDay.addItem(new Item(1, "Work", params.get("work_start")[0]));
                    continue;
                }
                if (workDay.getItems().get(i).compareByTime(params.get("work_start")[0]) > 0
                        &&
                        workDay.getItems().get(i + 1).compareByTime(
                                Item.timeWithMinutes(workDay.getItems().get(i).getMinutes() + 60)) > 0
                        &&
                        Item.compareTime(Item.timeWithMinutes(workDay.getItems().get(i).getMinutes() + 60), params.get("work_end")[0]) < 0
                        ) {
                    workDay.addItem(new Item(1, "Work", Item.timeWithMinutes(workDay.getItems().get(i).getMinutes() + 60)));
                }
            }
            workDay.sortItems();

            connector.createPreparedStatement("INSERT INTO \"ITEM\" (activity, end_time, day_id) VALUES (?, ?, ?)");
            for (int i = 0; i < 7; i++) {
                if (contains(work_days, days_of_the_week[6 - i])) {
                    for (Item item : workDay.getItems()) {
                        setParameters(item, connector, max_day_id - i);
                    }
                } else {
                    for (Item item : freeDay.getItems()) {
                        setParameters(item, connector, max_day_id - i);
                    }
                }
            }

            connector.endSession();
            String user_name = (String) session.getAttribute("name");

            model.addAttribute("name", user_name);
            if ((Boolean) session.getAttribute("is_company")) {
                return "company_profile";
            } else {
                return "userProfile";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "new_schedule";
    }
}

@Controller
class CompanyMembersController {
    @GetMapping("/company_members")
    public String showForm(HttpSession session, ModelMap model) {
        try {
            DBConnector connector = new DBConnector();
            connector.createPreparedStatement(
                    "SELECT * FROM \"USER\" INNER JOIN \"EMPLOYMENT\" " +
                            "ON user_id = \"USER\".id WHERE \"EMPLOYMENT\".company_id = ? AND is_request = FALSE;");
            connector.setParameter(1, (Integer) session.getAttribute("user_id"));
            ResultSet resultSet = connector.executePreparedStatementWithResult();

            List<Person> members = new ArrayList<Person>();

            while (resultSet.next()) {
                members.add(new Person(resultSet.getString("name"),
                        resultSet.getInt("id"), resultSet.getString("email")));
            }

            model.addAttribute("members", members);
            connector.endSession();

            return "company_members";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "company_profile";
    }
}

@Controller
class CompanyMemberRequestController {
    @PostMapping("/company_members")
    public String showForm(HttpServletRequest request, HttpSession session, ModelMap model) {
        try {
            DBConnector connector = new DBConnector();
            Map<String, String[]> params = request.getParameterMap();
            int company_id = (Integer) session.getAttribute("user_id");
            String user_email = params.get("email")[0];

            connector.createPreparedStatement("SELECT id FROM \"USER\" WHERE email = ?;");
            connector.setParameter(1, user_email);
            ResultSet resultSet = connector.executePreparedStatementWithResult();
            if (resultSet.next()) {
                int user_id = resultSet.getInt(1);
                connector.createPreparedStatement(
                        "INSERT INTO \"EMPLOYMENT\"(user_id, company_id, is_request) VALUES (?, ?, TRUE);");
                connector.setParameter(1, user_id);
                connector.setParameter(2, company_id);
                connector.executePreparedStatement();
            }

            connector.createPreparedStatement(
                    "SELECT * FROM \"USER\" INNER JOIN \"EMPLOYMENT\" " +
                            "ON user_id = \"USER\".id WHERE \"EMPLOYMENT\".company_id = ? AND is_request = FALSE;");
            connector.setParameter(1, (Integer) session.getAttribute("user_id"));
            resultSet = connector.executePreparedStatementWithResult();

            List<Person> members = new ArrayList<Person>();

            while (resultSet.next()) {
                members.add(new Person(resultSet.getString("name"),
                        resultSet.getInt("id"), resultSet.getString("email")));
            }

            model.addAttribute("members", members);
            connector.endSession();

            return "company_members";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "company_profile";
    }
}

@Controller
class MemberSchedulesViewController {
    @GetMapping("/member_schedules")
    public String showForm(HttpServletRequest request, ModelMap model) {
        try {
            DBConnector connector = new DBConnector();
            connector.createPreparedStatement("SELECT * FROM \"SCHEDULE\" INNER JOIN \"USER\" " +
                    "ON (\"USER\".id = \"SCHEDULE\".user_id) " +
                    "WHERE \"USER\".id = ?; ");
            Map<String, String[]> params = request.getParameterMap();

            int user_id = Integer.parseInt(params.get("id")[0]);
            connector.setParameter(1, user_id);
            ResultSet resultSet = connector.executePreparedStatementWithResult();

//            if (resultSet.isLast()) throw new IllegalArgumentException("No such user exception");

            List<Schedule> scheduleNames = new ArrayList<Schedule>();

            while (resultSet.next()) {
                scheduleNames.add(new Schedule(resultSet.getInt(1), resultSet.getString(3)));
            }

            model.addAttribute("schedules", scheduleNames);
            model.addAttribute("user_id", user_id);
            connector.endSession();

            return "member_schedules";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "company_profile";
    }
}

@Controller
class MemberScheduleViewController {
    @GetMapping("/member_schedule")
    public String showForm(ModelMap model, HttpServletRequest request) {
        try {
            DBConnector connector = new DBConnector();
            connector.createPreparedStatement("SELECT * FROM \"DAY\" INNER JOIN (" +
                    "SELECT \"SCHEDULE\".id FROM \"SCHEDULE\" INNER JOIN \"USER\" " +
                    "ON (\"USER\".id = \"SCHEDULE\".user_id) " +
                    "WHERE \"SCHEDULE\".id = ?) AS some_schedule " +
                    "ON (\"DAY\".schedule_id = some_schedule.id)" +
                    "ORDER BY \"DAY\".id");

            Map<String, String[]> params = request.getParameterMap();

            int schedule_id = Integer.parseInt(params.get("id")[0]);

            connector.setParameter(1, schedule_id);
            ResultSet resultSet = connector.executePreparedStatementWithResult();

            List<Day> days = new ArrayList<Day>();

            while (resultSet.next()) {
                days.add(new Day(resultSet.getInt("id"), resultSet.getString("day_of_the_week")));
            }

            connector.createPreparedStatement("SELECT * FROM \"ITEM\" INNER JOIN (" +
                    "SELECT \"DAY\".id FROM \"DAY\" INNER JOIN (" +
                    "SELECT \"SCHEDULE\".id FROM \"SCHEDULE\" INNER JOIN \"USER\"" +
                    " ON (\"USER\".id = \"SCHEDULE\".user_id)" +
                    " WHERE \"SCHEDULE\".id = ?) AS some_schedule " +
                    "ON (\"DAY\".schedule_id = some_schedule.id)" +
                    "WHERE \"DAY\".id = ?) AS some_day " +
                    "ON \"ITEM\".day_id = some_day.id " +
                    "ORDER BY end_time");

            for (Day day : days) {
                connector.setParameter(1, schedule_id);
                connector.setParameter(2, day.getId());
                resultSet = connector.executePreparedStatementWithResult();

                while (resultSet.next()) {
                    day.addItem(new Item(resultSet.getInt("id"), resultSet.getString("activity"), resultSet.getString("end_time")));
                }
                resultSet.close();
            }

            model.addAttribute("days", days);

            connector.endSession();

            return "member_schedule";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "company_profile";
    }
}

@Controller
class RecommendScheduleController {
    @GetMapping("/recommend_schedule")
    public String showForm(HttpSession session, HttpServletRequest request, ModelMap model) {
        try {
            DBConnector connector = new DBConnector();
            connector.createPreparedStatement("SELECT * FROM \"SCHEDULE\" INNER JOIN \"USER\" " +
                    "ON (\"USER\".id = \"SCHEDULE\".user_id) " +
                    "WHERE \"USER\".id = ?;");

            int company_id = (Integer) session.getAttribute("user_id");

            Map<String, String[]> params = request.getParameterMap();
            int user_id = Integer.parseInt(params.get("id")[0]);

            connector.setParameter(1, company_id);
            ResultSet resultSet = connector.executePreparedStatementWithResult();

            List<Schedule> scheduleNames = new ArrayList<Schedule>();

            while (resultSet.next()) {
                scheduleNames.add(new Schedule(resultSet.getInt(1), resultSet.getString(3)));
            }

            model.addAttribute("user_to_recommend", user_id);
            model.addAttribute("schedules", scheduleNames);

            connector.endSession();

            return "recommend_schedule";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "company_profile";
    }
}

@Controller
class RecommendController {
    @GetMapping("/recommend_schedule_to_user")
    public String showForm(HttpServletRequest request) {
        try {
            DBConnector connector = new DBConnector();
            Map<String, String[]> params = request.getParameterMap();
            int user = Integer.parseInt(params.get("user")[0]);
            int schedule = Integer.parseInt(params.get("schedule")[0]);

            connector.createPreparedStatement("INSERT INTO \"RECOMMENDATIONS\"(comment, user_id, schedule_id) VALUES (?, ?, ?);");
            connector.setParameter(1, "SOME COMMENT");
            connector.setParameter(2, user);
            connector.setParameter(3, schedule);
            connector.executePreparedStatement();

            return "company_profile";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "company_profile";
    }
}

@Controller
class UserCompaniesInformationController {
    @GetMapping("/user_company")
    public String showForm(HttpSession session, ModelMap model) {
        try {
            DBConnector connector = new DBConnector();
            connector.createPreparedStatement(
                    "SELECT * FROM \"USER\" INNER JOIN \"EMPLOYMENT\" " +
                            "ON company_id = \"USER\".id WHERE \"EMPLOYMENT\".user_id = ? AND is_request = TRUE;");

            int user_id = (Integer) session.getAttribute("user_id");

            connector.setParameter(1, user_id);
            ResultSet resultSet = connector.executePreparedStatementWithResult();

            List<Request> requests = new ArrayList<Request>();
            List<Person> companies = new ArrayList<Person>();

            while (resultSet.next()) {
                requests.add(new Request(resultSet.getInt(6), resultSet.getInt(7), resultSet.getString(2)));
            }

            connector.createPreparedStatement(
                    "SELECT * FROM \"USER\" INNER JOIN(" +
                            "SELECT company_id FROM \"USER\" INNER JOIN \"EMPLOYMENT\" " +
                            "ON user_id = \"USER\".id WHERE \"EMPLOYMENT\".user_id = ? AND is_request = FALSE) company " +
                            "ON company.company_id = \"USER\".id;");

            connector.setParameter(1, user_id);
            resultSet = connector.executePreparedStatementWithResult();

            while (resultSet.next()) {
                companies.add(new Person(resultSet.getString("name"), resultSet.getInt("id"), resultSet.getString("email")));
            }

            model.addAttribute("requests", requests);
            model.addAttribute("companies", companies);

            connector.endSession();

            return "user_employment";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "userProfile";
    }
}

@Controller
class UserRecommendationsController {
    @GetMapping("/user_recommendation")
    public String showForm(ModelMap model, HttpSession session) {
        try {
            DBConnector connector = new DBConnector();
            connector.createPreparedStatement("SELECT * FROM \"SCHEDULE\" INNER JOIN " +
                    "(SELECT id, schedule_id, comment FROM \"RECOMMENDATIONS\" WHERE user_id = ?) recommendation " +
                    "ON \"SCHEDULE\".id = recommendation.schedule_id;");

            int user_id = (Integer) session.getAttribute("user_id");

            connector.setParameter(1, user_id);
            ResultSet resultSet = connector.executePreparedStatementWithResult();

            List<Schedule> recommendations = new ArrayList<Schedule>();

            while (resultSet.next()) {
                recommendations.add(new Schedule(resultSet.getInt("id"), resultSet.getString("name")));
                recommendations.get(recommendations.size() - 1).setRecommendationId(resultSet.getInt(4));
            }

            model.addAttribute("recommendations", recommendations);

            connector.endSession();

            return "user_recommendations";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "userProfile";
    }
}

@Controller
class UserRecommendedScheduleController{
    @GetMapping("/recommended_schedule")
    public String showForm(HttpServletRequest request, ModelMap model) {
        try {
            DBConnector connector = new DBConnector();
            connector.createPreparedStatement("SELECT * FROM \"DAY\" INNER JOIN (" +
                    "SELECT \"SCHEDULE\".id FROM \"SCHEDULE\" INNER JOIN \"USER\" " +
                    "ON (\"USER\".id = \"SCHEDULE\".user_id) " +
                    "WHERE \"SCHEDULE\".id = ?) AS some_schedule " +
                    "ON (\"DAY\".schedule_id = some_schedule.id)" +
                    "ORDER BY \"DAY\".id");

            Map<String, String[]> params = request.getParameterMap();

            int recommendation_id = Integer.parseInt(params.get("recommendation_id")[0]);
            int schedule_id = Integer.parseInt(params.get("id")[0]);

            connector.setParameter(1, schedule_id);
            ResultSet resultSet = connector.executePreparedStatementWithResult();

            List<Day> days = new ArrayList<Day>();

            while (resultSet.next()) {
                days.add(new Day(resultSet.getInt("id"), resultSet.getString("day_of_the_week")));
            }

            connector.createPreparedStatement("SELECT * FROM \"ITEM\" INNER JOIN (" +
                    "SELECT \"DAY\".id FROM \"DAY\" INNER JOIN (" +
                    "SELECT \"SCHEDULE\".id FROM \"SCHEDULE\" INNER JOIN \"USER\"" +
                    " ON (\"USER\".id = \"SCHEDULE\".user_id)" +
                    " WHERE \"SCHEDULE\".id = ?) AS some_schedule " +
                    "ON (\"DAY\".schedule_id = some_schedule.id)" +
                    "WHERE \"DAY\".id = ?) AS some_day " +
                    "ON \"ITEM\".day_id = some_day.id " +
                    "ORDER BY end_time");

            for (Day day : days) {
                connector.setParameter(1, schedule_id);
                connector.setParameter(2, day.getId());
                resultSet = connector.executePreparedStatementWithResult();

                while (resultSet.next()) {
                    day.addItem(new Item(resultSet.getInt("id"), resultSet.getString("activity"), resultSet.getString("end_time")));
                }
                resultSet.close();
            }

            model.addAttribute("days", days);
            model.addAttribute("schedule_id", schedule_id);
            model.addAttribute("recommendation_id", recommendation_id);

            connector.endSession();

            return "recommended_schedule";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "userProfile";
    }
}

@Controller
class AcceptRecommendationController {
    @GetMapping("/accept")
    public String showForm(HttpServletRequest request, HttpSession session) {
        try {
            DBConnector connector = new DBConnector();
            connector.createPreparedStatement("SELECT name FROM \"SCHEDULE\" WHERE id = ?;");

            Map<String, String[]> params = request.getParameterMap();

            int schedule_id = Integer.parseInt(params.get("id")[0]);
            int recommendation_id = Integer.parseInt(params.get("recommendation_id")[0]);
            int user_id = (Integer)session.getAttribute("user_id");
            connector.setParameter(1, schedule_id);
            ResultSet resultSet = connector.executePreparedStatementWithResult();
            resultSet.next();
            String schedule_name = resultSet.getString("name");

            connector.createPreparedStatement("SELECT * FROM \"DAY\" INNER JOIN (" +
                    "SELECT \"SCHEDULE\".id FROM \"SCHEDULE\" INNER JOIN \"USER\" " +
                    "ON (\"USER\".id = \"SCHEDULE\".user_id) " +
                    "WHERE \"SCHEDULE\".id = ?) AS some_schedule " +
                    "ON (\"DAY\".schedule_id = some_schedule.id)" +
                    "ORDER BY \"DAY\".id");

            connector.setParameter(1, schedule_id);
            resultSet = connector.executePreparedStatementWithResult();

            List<Day> days = new ArrayList<Day>();

            while (resultSet.next()) {
                days.add(new Day(resultSet.getInt("id"), resultSet.getString("day_of_the_week")));
            }

            connector.createPreparedStatement("SELECT * FROM \"ITEM\" INNER JOIN (" +
                    "SELECT \"DAY\".id FROM \"DAY\" INNER JOIN (" +
                    "SELECT \"SCHEDULE\".id FROM \"SCHEDULE\" INNER JOIN \"USER\"" +
                    " ON (\"USER\".id = \"SCHEDULE\".user_id)" +
                    " WHERE \"SCHEDULE\".id = ?) AS some_schedule " +
                    "ON (\"DAY\".schedule_id = some_schedule.id)" +
                    "WHERE \"DAY\".id = ?) AS some_day " +
                    "ON \"ITEM\".day_id = some_day.id " +
                    "ORDER BY end_time");

            for (Day day : days) {
                connector.setParameter(1, schedule_id);
                connector.setParameter(2, day.getId());
                resultSet = connector.executePreparedStatementWithResult();

                while (resultSet.next()) {
                    day.addItem(new Item(resultSet.getInt("id"), resultSet.getString("activity"), resultSet.getString("end_time")));
                }
                resultSet.close();
            }

            connector.createPreparedStatement("INSERT INTO \"SCHEDULE\"(user_id, name) VALUES (?, ?);");
            connector.setParameter(1, user_id);
            connector.setParameter(2, schedule_name);
            connector.executePreparedStatement();

            connector.createPreparedStatement("SELECT max(id) FROM \"SCHEDULE\";");
            resultSet = connector.executePreparedStatementWithResult();
            resultSet.next();
            schedule_id = resultSet.getInt(1);

            connector.createPreparedStatement("INSERT INTO \"DAY\" (day_of_the_week, schedule_id) VALUES (?, ?);");
            for (Day day : days) {
                connector.setParameter(1, day.getDay_of_the_week());
                connector.setParameter(2, schedule_id);
                connector.executePreparedStatement();
            }

            connector.createPreparedStatement("SELECT max(id) FROM \"DAY\";");
            resultSet = connector.executePreparedStatementWithResult();
            resultSet.next();
            int max_day_id = resultSet.getInt(1);

            connector.createPreparedStatement("INSERT INTO \"ITEM\" (activity, end_time, day_id) VALUES (?, ?, ?)");
            for (int i = 0; i < 7; i++) {
                for (Item item : days.get(6 - i).getItems()) {
                    connector.setParameter(1, item.getActivity());
                    connector.setParameter(2, item.getEnd_time());
                    connector.setParameter(3, max_day_id - i);
                    connector.executePreparedStatement();
                }
            }

            connector.createPreparedStatement("DELETE FROM \"RECOMMENDATIONS\" WHERE id = ?;");
            connector.setParameter(1, recommendation_id);
            connector.executePreparedStatement();

            connector.endSession();
            return "userProfile";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "userProfile";
    }
}

@Controller
class DeclineRecommendationController {
    @GetMapping("/decline")
    public String showForm(HttpServletRequest request) {
        try {
            DBConnector connector = new DBConnector();
            Map<String, String[]> params = request.getParameterMap();
            int recommendation_id = Integer.parseInt(params.get("id")[0]);
            connector.createPreparedStatement("DELETE FROM \"RECOMMENDATIONS\" WHERE id = ?;");
            connector.setParameter(1, recommendation_id);
            connector.executePreparedStatement();

            connector.endSession();
            return "userProfile";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "userProfile";
    }
}

@Controller
class RequestPageController {
    @GetMapping("/request_page")
    public String showForm(HttpServletRequest request, ModelMap model) {
        try {
            Map<String, String[]> params = request.getParameterMap();
            int user_id = Integer.parseInt(params.get("user_id")[0]);
            int company_id = Integer.parseInt(params.get("company_id")[0]);
            String comment = params.get("comment")[0];

            model.addAttribute("user_id", user_id);
            model.addAttribute("company_id", company_id);
            model.addAttribute("comment", comment);

            return "request_page";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "userProfile";
    }
}

@Controller
class AcceptRequestController {
    @GetMapping("/accept_request")
    public String showForm(HttpServletRequest request) {
        try {
            DBConnector connector = new DBConnector();
            Map<String, String[]> params = request.getParameterMap();
            int user_id = Integer.parseInt(params.get("user_id")[0]);
            int company_id = Integer.parseInt(params.get("company_id")[0]);

            connector.createPreparedStatement("UPDATE \"EMPLOYMENT\" SET is_request = FALSE WHERE user_id = ? AND company_id = ?;");
            connector.setParameter(1, user_id);
            connector.setParameter(2, company_id);
            connector.executePreparedStatement();

            connector.endSession();
            return "userProfile";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "userProfile";
    }
}

@Controller
class DeclineRequestController {
    @GetMapping("decline_request")
    public String showForm(HttpServletRequest request) {
        try {
            DBConnector connector = new DBConnector();
            Map<String, String[]> params = request.getParameterMap();
            int user_id = Integer.parseInt(params.get("user_id")[0]);
            int company_id = Integer.parseInt(params.get("company_id")[0]);

            connector.createPreparedStatement("DELETE FROM \"EMPLOYMENT\" WHERE user_id = ? AND company_id = ?;");
            connector.setParameter(1, user_id);
            connector.setParameter(2, company_id);
            connector.executePreparedStatement();

            connector.endSession();
            return "userProfile";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "userProfile";
    }
}
