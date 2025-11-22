const SERVER_URL = "http://localhost:8089";
const token = localStorage.getItem("token");

function login() {
    const email  = document.getElementById("email").value;
    const password  = document.getElementById("password").value;

    fetch(`${SERVER_URL}/auth/login` , {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body:JSON.stringify({email,password}),
    })
    .then(response => {
        if(!response.ok){
             throw new Error(data.message || "Login failed");
        }
        return response.json();
    })  
    .then(data => {
        localStorage.setItem("token" , data.token);
        window.location.href = "todos.html"
    })
    .catch(error => {
        alert(error.message);
    })
}

function register() {
    const email  = document.getElementById("email").value;
    const password  = document.getElementById("password").value;

    fetch(`${SERVER_URL}/auth/register` , {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body:JSON.stringify({email,password}),
    })
    .then(response => {
        if(response.ok){
            alert("Registration Successful, Please Login!");
            window.location.href = "login.html"
        } else {
            return response.json().then(data => {throw new Error(data.message || "Registration failed")});
        }
    })
    .catch(error => {
        alert(error.message);
    })
}

function createTodoCard(todo) {
    const card = document.createElement("div");
    card.className = "todo-card";

    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.checked = todo.isCompleted;
    checkbox.addEventListener("change" , function() {
        const updatedTodo = {...todo, isCompleted: checkbox.checked}
        updateTodoStatus(updatedTodo);
    });

    const span = document.createElement("span");
    span.textContent = todo.title;

    if(todo.isCompleted) {
        span.style.textDecoration = "line-through";
        span.style.color = "#aaa";
    }

    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "X";
    deleteBtn.onclick = function() {deleteTodo(todo.id);};

    card.appendChild(checkbox);
    card.appendChild(span);
    card.appendChild(deleteBtn);

    return card;
}

function loadTodos() {
    if(!token) {
        alert("Please login");
        window.location.href = "login.html";
        return;
    }

    fetch(`${SERVER_URL}/api/todo/page?page=0&size=100`, {
        method: "GET",
        headers: {
            Authorization:`Bearer ${token}`
        }
    })
    .then(response => {
        if(!response.ok){
             throw new Error("Failed to get todos");
        }
        return response.json();
    })  
    .then((pageData) => {
        const todoList = document.getElementById("todo-list");
        todoList.innerHTML = "";

        const todos = pageData.content || [];
        
        if(!todos || todos.length === 0){
            todoList.innerHTML = `<p id="empty-message">No Todos yet. Add one below!</p>`;
        }
        else {
            todos.forEach(todo => {
                todoList.appendChild(createTodoCard(todo)); 
            });
        }
    })
    .catch(error => {
        document.getElementById("todo-list").innerHTML = `<p style="color:red">Failed to load Todos!</p>`
        console.error("Error loading todos:", error);
    })
}

function addTodo() {
    const input = document.getElementById("new-todo");
    const todoText = input.value.trim();

    if(!todoText) return;

    fetch(`${SERVER_URL}/api/todo/create`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization:`Bearer ${token}`
        },
        body: JSON.stringify({title: todoText, isCompleted: false})
    })
    .then(response => {
        if(!response.ok){
             throw new Error("Failed to create todo");
        }
        return response.json();
    })  
    .then((newTodo) => {
        input.value = "";
        loadTodos();
    })
    .catch(error => {
        alert(error.message);
        console.error("Error creating todo:", error);
    })
}

function updateTodoStatus(todo) {
    fetch(`${SERVER_URL}/api/todo` , {
        method: "PUT",
        headers: {"Content-Type": "application/json",
            Authorization:`Bearer ${token}`
        },
        body: JSON.stringify(todo)
    })
    .then(response => {
        if(!response.ok){
             throw new Error(data.message || "Failed to update todo");
        }
        return response.json();
    })  
    .then(() => loadTodos())
    .catch(error => {
        alert(error.message);
    })
}

function deleteTodo(id) {

    fetch(`${SERVER_URL}/api/todo/${id}` , {
        method: "DELETE",
        headers: {Authorization:`Bearer ${token}`},
    })
    .then(response => {
        if(!response.ok){
             throw new Error(data.message || "Failed to delete todo");
        }
        return response.text();
    })  
    .then(() => loadTodos())
    .catch(error => {
        alert(error.message);
    })
}
function logout() {
    localStorage.removeItem("token");
    window.location.href = "login.html";
}

document.addEventListener("DOMContentLoaded", function () {
    if (document.getElementById("todo-list")) {
        loadTodos();
    }
});
