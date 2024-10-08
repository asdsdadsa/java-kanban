import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servers.HttpTasksServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskTypes;
import utility.JsonCreater;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;  // Спасибо большое за ответ. А то самостоятельно никак не успеваю всё делать без помощи. И думал это очень плохо.

public class HttpTasksServerTest {          // Там где можно лучше (403 и другие коды ответов) разберу, спасибо.
    private HttpTasksServer server;         // удалил file.
    private Subtask subtask1;              // исключение в конструкторе исправил.
    private Subtask subtask2;
    private Task task1;
    private Epic epic1;
    private Gson gson;

    @BeforeEach
    void load() throws IOException {
        TaskManager manager = new InMemoryTaskManager();
        server = new HttpTasksServer(manager);
        gson = JsonCreater.createGson();

        task1 = new Task("t", "1t", Instant.ofEpochSecond(5454545), 45646);
        manager.addTask(task1);
        epic1 = new Epic("e", "1e", TaskTypes.EPIC);
        manager.addEpic(epic1);
        subtask1 = new Subtask("s", "1s", Instant.EPOCH, 15, epic1.getId());
        manager.addSubtask(subtask1);
        subtask2 = new Subtask("s1", "2s", Instant.ofEpochSecond(55555), 555, epic1.getId());
        manager.addSubtask(subtask2);

        manager.taskById(task1.getId());
        manager.epicById(epic1.getId());
        manager.subtaskById(subtask1.getId());
        manager.subtaskById(subtask2.getId());
        server.start();
    }

    @AfterEach
    void serverStop() {
        server.stop();
    }

    @Test
    void getTasksTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type type = new TypeToken<Map<Integer, Task>>() {
        }.getType();
        Map<Integer, Task> tasks = gson.fromJson(response.body(), type);

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
    }

    @Test
    void epicByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type type = new TypeToken<Map<Integer, Epic>>() {
        }.getType();
        Map<Integer, Epic> epics = gson.fromJson(response.body(), type);

        assertNotNull(epics);
        assertEquals(1, epics.size());
    }

    @Test
    void subtaskByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type type = new TypeToken<Map<Integer, Subtask>>() {
        }.getType();
        Map<Integer, Subtask> subtasks = gson.fromJson(response.body(), type);

        assertNotNull(subtasks);
        assertEquals(2, subtasks.size());
    }

    @Test
    void taskByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type type = new TypeToken<Task>() {
        }.getType();
        Task task = gson.fromJson(response.body(), type);

        assertEquals(task1, task);
    }

    @Test
    void getHistoryTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type type = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(response.body(), type);

        assertNotNull(tasks);
        assertEquals(4, tasks.size());
    }

    @Test
    void getPrioritizedTasksTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Type type = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(response.body(), type);

        assertNotNull(tasks);
        assertEquals(3, tasks.size());
    }

    @Test
    void addTaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(task1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    void addEpicTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(epic1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    void addSubtaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String json = gson.toJson(subtask1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(body)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    void removeTaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    void removeTasksTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Type type = new TypeToken<Map<Integer, Task>>() {
        }.getType();
        Map<Integer, Task> tasks = gson.fromJson(response.body(), type);

        assertNull(tasks);
    }
}