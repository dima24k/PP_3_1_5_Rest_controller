let table = []
let currentUser = '';

function getAllUsers() {
    fetch('/api/admin')
        .then(response => response.json() )
        .then(data => {
            if (data.length > 0) {
                table = data;
            } else {
                table = [];
            }

            showUsers(table)
            console.log(data)
        })
        .catch(error => {
            console.error(error)
        })
}

getAllUsers();

function showUsers(table) {
    let temp = '';

    for (let user of table) {
        temp += `
            <tr>
                <td>${user.id}</td>
                <td>${user.userName}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${user.rolesName}</td>
                <td><a onclick='showEditModal(${user.id})' class="btn btn-sm btn-primary" id="edit" style="color: white;">Edit</a></td>
                <td><a onclick='showDeleteModal(${user.id})' class="btn btn-sm btn-danger" id="delete" style="color: white;">Delete</a></td>
            </tr>
        `;
    }

    document.getElementById('allUsersBody').innerHTML = temp;
}

fetch('/api/admin/current')
    .then(response => response.json() )
    .then(data => {
        currentUser = data
        console.log(data)

        document.getElementById('headAdminName').innerText = currentUser.email
        document.getElementById('headAdminRoles').innerText = currentUser.rolesName

        showUser(currentUser)
    }).catch(error => {
    console.error('Пользователь с таким id не был найден' + error)
})

function showUser(user) {
    document.getElementById('oneUserBody').innerHTML = `
        <tr>
            <td>${user.id}</td>
            <td>${user.userName}</td>
            <td>${user.age}</td>   
            <td>${user.email}</td>
            <td>${user.rolesName}</td>
        </tr>
    `;
}

fetch("api/admin/roles").then(response => response.json())
    .then(roles => {
        let rolesSelect = document.getElementById("roles");
        let rolesSelectEdit = document.getElementById("editRoles");

        roles.forEach(role => {
            role.name = role.name.replace('ROLE_', '');

            // Создаем отдельные элементы option для каждого select
            let option1 = document.createElement("option");
            option1.value = role.id;
            option1.text = role.name;
            rolesSelect.appendChild(option1);

            let option2 = document.createElement("option");
            option2.value = role.id;
            option2.text = role.name;
            rolesSelectEdit.appendChild(option2);
        });
    });

function showEditModal(id) {

    let request = new Request("/api/admin/" + id, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    });

    fetch(request)
        .then(response => response.json())
        .then(editUser => {
            console.log(editUser)
            const editIdElement = document.getElementById('editId');

            if (editIdElement) {
                editIdElement.value = editUser.id;
                document.getElementById('editUserName').value = editUser.userName;
                document.getElementById('editEmail').value = editUser.email;
                document.getElementById('editAge').value = editUser.age;
                document.getElementById('editPassword').value = editUser.password;

                // Показываем модальное окно
                $('#editModal').modal('show');
            } else {
                console.error('Element with ID "editId" is not found.');
            }
        })
    document.getElementById('editUser').addEventListener('submit', editUser);
}

function newUser(event) {
    event.preventDefault();

    let newUserForm = new FormData(event.target);

    const selectedRoles = [];
    const selectElement = document.getElementById('roles');

    for (let i = 0; i < selectElement.options.length; i++) {
        if (selectElement.options[i].selected) {
            selectedRoles.push({ id: selectElement.options[i].value, name: selectElement.options[i].text });
        }
    }

    let user = {
        id: null,
        userName: newUserForm.get('userName'),
        age: newUserForm.get('age'),
        email: newUserForm.get('email'),
        password: newUserForm.get('password'),
        roles: selectedRoles
    };

    console.log(user);

    fetch('api/admin', {
        method: 'POST',
        body: JSON.stringify(user),
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(() => {
            getAllUsers();
            event.target.reset();

            const triggerE1 = document.querySelector('a[href="#usersTable"]');
            $(triggerE1).tab('show');
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}

// Обработчик отправки формы
function editUser(event) {
    event.preventDefault();

    let editUserForm = new FormData(event.target);

    const selectedRoles = [];
    const selectElement = document.getElementById('editRoles');

    for (let i = 0; i < selectElement.options.length; i++) {
        if (selectElement.options[i].selected) {
            selectedRoles.push({id: selectElement.options[i].value, name: selectElement.options[i].text});
        }
    }

    let user = {
        id: editUserForm.get('id'),
        userName: editUserForm.get('userName'),
        age: editUserForm.get('age'),
        email: editUserForm.get('email'),
        password: editUserForm.get('password'),
        roles: selectedRoles
    };

    fetch('api/admin', {
        method: 'PUT',
        body: JSON.stringify(user),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (response.ok) {
            event.target.reset();
            getAllUsers();
            $('#editModal').modal('hide');
        }
    });
}

getAllUsers();

function showDeleteModal(id) {
    document.getElementById('closeDeleteModal').onclick = function() {
        $('#deleteModal').modal('hide');
        document.getElementById('deleteUserForm').reset();
    };

    let request = new Request("/api/admin/" + id, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    });

    fetch(request).then(response => response.json()).then(deleteUser => {

        document.getElementById('idDel').setAttribute('value', deleteUser.id);
        document.getElementById('nameDel').setAttribute('value', deleteUser.userName);
        document.getElementById('ageDel').setAttribute('value', deleteUser.age);
        document.getElementById('emailDel').setAttribute('value', deleteUser.email)
        document.getElementById('passwordDel').setAttribute('value', deleteUser.password);

        let roles = deleteUser.roles;
        if (roles.includes("USER") ) {
            document.getElementById('rolesDel1').setAttribute('selected', 'true');
        }
        if (roles.includes("ADMIN") ) {
            document.getElementById('rolesDel2').setAttribute('selected', 'true');
        }
        $('#deleteModal').modal('show');
    });

    let isDelete = false;
    document.getElementById('deleteUserForm').addEventListener('submit', event => {
        event.preventDefault();
        if (!isDelete) {
            isDelete = true;
            let request = new Request("/api/admin/" + id, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            fetch(request).then(() => {
                event.target.reset();
                getAllUsers();
                $('#deleteModal').modal('hide');
            });
        }
    });
}
