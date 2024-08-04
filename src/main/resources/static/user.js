let currentUser

fetch("/api/user/current")
    .then(response => response.json() )
    .then(data => {
        currentUser = data
        console.log(data)
        showUser(data)
        document.getElementById('headUserName').innerText = currentUser.email
        document.getElementById('headUserRoles').innerText = currentUser.rolesName
    })
    .catch(error => {
        console.error('Пользователь с таким id не был найден' + error)
    })

function showUser(user) {
    let temp = "";

    temp += "<tr>"
    temp += "<td>" + user.id + "</td>"
    temp += "<td>" + user.userName + "</td>"
    temp += "<td>" + user.age + "</td>"
    temp += "<td>" + user.email + "</td>"
    temp += "<td>" + user.rolesName + "</td>"
    temp += "</tr>"

    document.getElementById('oneUserBody').innerHTML = temp
}