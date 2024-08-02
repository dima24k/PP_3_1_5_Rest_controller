let currentUser;

fetch("/api/user/current")
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        currentUser = data;
        console.log(data);
        showUser(data); // Уберем лишнюю часть кода из этого места
    })
    .catch(error => {
        console.error('Пользователь с таким id не был найден: ' + error);
    });

function showUser(user) {
    const roles = user.listRoles.map(role => role.name).join(" "); // Используем roles

    document.getElementById('oneUserBody').innerHTML = `
        <tr>
            <td>${}</td>
            <td>${}</td> <!-- Убедитесь, что это правильное свойство -->
            <td>${}</td>
            <td>${}</td> <!-- Убедитесь, что это правильное свойство -->
            <td>${}</td> <!-- Используем roles -->
        </tr>
    `;
}