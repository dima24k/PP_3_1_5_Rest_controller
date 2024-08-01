let currentUser = "";

fetch("/api/user")
    .then(response => response.json() )
    .then(data => {
        currentUser = data
        console.log(data)
        showUser(data)
        document.getElementById('headUserName').innerText = currentUser.name
        document.getElementById('headUserRoles').innerText = currentUser.listRoles
            .map(role => role.name).join(" ")
    })
    .catch(error => {
        console.error('Пользователь с таким id не был найден' + error)
    })

function showUser(user) {
    let temp = "";

    temp += "<tr>"
    temp += "<td>" + user.id + "</td>"
    temp += "<td>" + user.username + "</td>"
    temp += "<td>" + user.age + "</td>"
    temp += "<td>" + user.listRoles.map(role => role.name).join(" ") + "</td>"
    temp += "</tr>"

    document.getElementById('oneUserBody').innerHTML = temp
}