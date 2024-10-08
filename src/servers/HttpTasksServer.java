package servers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import utility.JsonCreater;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class HttpTasksServer {
    private static final int PORT = 8080;
    private TaskManager manager;
    private final HttpServer server;
    private final Gson gson;
    private byte[] response = new byte[0];

    public HttpTasksServer(TaskManager manager) throws IOException {
        this.manager = manager;
        server = HttpServer.create();
        server.bind(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TasksHandler());
        gson = JsonCreater.createGson();
    }

    public void start() {
        server.start();
        System.out.println("Сервер запущен.");
    }

    public void stop() {
        server.stop(0);
        System.out.println("Сервер остановлен.");
    }


    class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            try {
                switch (method) {
                    case "GET":
                        if (Pattern.matches("^/tasks$", path)) {
                            String prioritizedTasksToJson = gson.toJson(manager.getPrioritizedTasks());

                            if (!prioritizedTasksToJson.isEmpty()) {
                                response = prioritizedTasksToJson.getBytes(StandardCharsets.UTF_8);
                                exchange.getResponseHeaders().add("Content-Type", "application/json");
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                exchange.sendResponseHeaders(400, 0);
                            }
                        }
                        if (Pattern.matches("^/tasks/task$", path)) {
                            String tasksToJson = gson.toJson(manager.getTasks());

                            if (!tasksToJson.isEmpty()) {
                                response = tasksToJson.getBytes(StandardCharsets.UTF_8);
                                exchange.getResponseHeaders().add("Content-Type", "application/json");
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                exchange.sendResponseHeaders(400, 0);
                            }
                        }
                        if (Pattern.matches("^/tasks/epic$", path)) {
                            String epicsToJson = gson.toJson(manager.getEpics());

                            if (!epicsToJson.isEmpty()) {
                                response = epicsToJson.getBytes(StandardCharsets.UTF_8);
                                exchange.getResponseHeaders().add("Content-Type", "application/json");
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                exchange.sendResponseHeaders(400, 0);
                            }
                        }
                        if (Pattern.matches("^/tasks/subtask$", path)) {
                            String subtasksToJson = gson.toJson(manager.getSubtasks());

                            if (!subtasksToJson.isEmpty()) {
                                response = subtasksToJson.getBytes(StandardCharsets.UTF_8);
                                exchange.getResponseHeaders().add("Content-Type", "application/json");
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                exchange.sendResponseHeaders(400, 0);
                            }
                        }
                        if (Pattern.matches("^/tasks/task/\\d+$", path)) {
                            int id = Integer.parseInt(path.replaceFirst("/tasks/task/", ""));
                            String taskByIDToJson = gson.toJson(manager.getTasks().get(id));

                            if (!taskByIDToJson.isEmpty()) {
                                response = taskByIDToJson.getBytes(StandardCharsets.UTF_8);
                                exchange.getResponseHeaders().add("Content-Type", "application/json");
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                exchange.sendResponseHeaders(400, 0);
                            }
                        }
                        if (Pattern.matches("^/tasks/history$", path)) {
                            String historyToJson = gson.toJson(manager.getHistory());

                            if (!historyToJson.isEmpty()) {
                                response = historyToJson.getBytes(StandardCharsets.UTF_8);
                                exchange.getResponseHeaders().add("Content-Type", "application/json");
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                exchange.sendResponseHeaders(400, 0);
                            }
                        }
                        if (Pattern.matches("^/tasks/subtask/epic/\\d+$", path)) {
                            int id = Integer.parseInt(path.replaceFirst("/tasks/subtask/epic/", ""));
                            String subtaskEpicToJson = gson.toJson(manager.getSubtasks().get(id));

                            if (!subtaskEpicToJson.isEmpty()) {
                                response = subtaskEpicToJson.getBytes(StandardCharsets.UTF_8);
                                exchange.getResponseHeaders().add("Content-Type", "application/json");
                                exchange.sendResponseHeaders(200, 0);
                            } else {
                                exchange.sendResponseHeaders(400, 0);
                            }
                        }
                        break;

                    case "POST":
                        if (Pattern.matches("^/tasks/task$", path)) {
                            InputStream inputStream = exchange.getRequestBody();
                            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                            Task task = gson.fromJson(body, Task.class);
                            exchange.sendResponseHeaders(200, 0);

                            if (!manager.getTasks().containsKey(task.getId())) {
                                manager.addTask(task);
                                System.out.println("Задача " + task.getId() + " создана.");
                            } else {
                                manager.updateTask(task);
                                System.out.println("Задача " + task.getId() + " обновлена.");
                            }
                        }
                        if (Pattern.matches("^/tasks/epic$", path)) {
                            InputStream inputStream = exchange.getRequestBody();
                            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                            Epic epic = gson.fromJson(body, Epic.class);
                            exchange.sendResponseHeaders(200, 0);
                            if (!manager.getEpics().containsKey(epic.getId())) {
                                manager.addEpic(epic);
                                System.out.println("Задача " + epic.getId() + " создана.");
                            } else {
                                manager.updateEpic(epic);
                                System.out.println("Задача " + epic.getId() + " обновлена.");
                            }
                        }
                        if (Pattern.matches("^/tasks/subtask$", path)) {
                            InputStream inputStream = exchange.getRequestBody();
                            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                            Subtask subtask = gson.fromJson(body, Subtask.class);
                            exchange.sendResponseHeaders(200, 0);
                            if (!manager.getSubtasks().containsKey(subtask.getId())) {
                                manager.addSubtask(subtask);
                                System.out.println("Задача " + subtask.getId() + " создана.");
                            } else {
                                manager.updateSubtask(subtask);
                                System.out.println("Задача " + subtask.getId() + " обновлена.");
                            }
                        }
                        break;

                    case "DELETE":
                        if (Pattern.matches("^/tasks/task$", path)) {
                            manager.clearAll();
                            exchange.sendResponseHeaders(200, 0);
                        }
                        if (Pattern.matches("^/tasks/task/\\d+$", path)) {
                            int id = Integer.parseInt(path.replaceFirst("/tasks/task/", ""));
                            if (manager.getTasks().containsKey(id)) {
                                manager.deleteTask(id);
                                System.out.println("Задача " + id + " удалена.");
                            }
                            if (manager.getEpics().containsKey(id)) {
                                manager.deleteEpic(id);
                                System.out.println("Задача " + id + " удалена.");
                            }
                            if (manager.getSubtasks().containsKey(id)) {
                                manager.deleteSubtask(id);
                                System.out.println("Задача " + id + " удалена.");
                            }
                            exchange.sendResponseHeaders(200, 0);
                        }
                        break;
                    default:
                        System.out.println("Ждем метод запрос,а получили - " + method);
                }
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response);
                }
            } catch (
                    IOException e) {
                System.out.println("Ошибка запроса: " + e.getMessage());
            } finally {
                exchange.close();
            }
        }
    }
}