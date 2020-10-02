
const url = "api/person";
const tb = document.getElementById('tb');
getAllUsers();

document.getElementById("reloadTableButton").addEventListener("click", getAllUsers);
document.getElementById("getUserButton").addEventListener("click", getUserById);
document.getElementById("addUserButton").addEventListener("click", addUser);
document.getElementById("editUserButton").addEventListener("click", editUser);
document.getElementById("deleteUserButton").addEventListener("click", deleteUser);



function getAllUsers() {
    fetch(`${url}/all`)
            .then(res => fetchWithErrorCheck(res))
            .then(data => {
                const trs = data.all.map((user) => {
                    return `<tr><td>${user.id}</td>
            <td>${user.fName}</td><td>${user.lName}</td>
            <td>${user.phone}</td><td>${user.street}</td>
            <td>${user.zip}</td><td>${user.city}</td></tr>`;
                });
                const trStr = trs.join('');
                tb.innerHTML = trStr;
            });
}

function getUserById(evnt) {
    evnt.preventDefault();
    const table = `<thead><tr><th>ID</th><th>First Name</th><th>Last Name</th><th>Phone</th><th>Street</th><th>Zip</th><th>City</th></tr></thead>
                <tbody>`;
    const tableClose = `</tbody>`;
    const singleUserTable = document.getElementById("singleUserTable");
    const idField = document.getElementById("userId");
    fetch(url + `/${idField.value}`)
            .then(res => fetchWithErrorCheck(res))
            .then((user) => {
                const ts = `<tr><td>${user.id}</td>
            <td>${user.fName}</td><td>${user.lName}</td>
            <td>${user.phone}</td><td>${user.street}</td>
            <td>${user.zip}</td><td>${user.city}</td></tr>`;
                const tableStr = table + ts + tableClose;
                singleUserTable.innerHTML = tableStr;
            }).catch(exception => {
        singleUserTable.innerHTML = "";
        document.getElementById("getUserError").innerHTML = "Error: No user by that ID";
    });

}

function addUser(evnt) {
    evnt.preventDefault();
    const fname = document.getElementById("fname").value;
    const lname = document.getElementById("lname").value;
    const phone = document.getElementById("phone").value;
    const street = document.getElementById("street").value;
    const zip = document.getElementById("zip").value;
    const city = document.getElementById("city").value;
    let options = {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            fName: fname,
            lName: lname,
            phone: phone,
            street: street,
            zip: zip,
            city: city
        })
    }
    fetch(`${url}/add`, options)
            .then(res => fetchWithErrorCheck(res))
            .then(data => {
                getAllUsers();
            })
            .catch(exception => {
                document.getElementById("addUserError").innerHTML = "Error: Something went wrong, " + exception;
            });
}

function editUser(evnt) {
    evnt.preventDefault();
    const id = document.getElementById("eid").value;
    const fname = document.getElementById("efname").value;
    const lname = document.getElementById("elname").value;
    const phone = document.getElementById("ephone").value;
    const street = document.getElementById("estreet").value;
    const zip = document.getElementById("ezip").value;
    const city = document.getElementById("ecity").value;
    let options = {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            fName: fname,
            lName: lname,
            phone: phone,
            street: street,
            zip: zip,
            city: city
        })
    }
    fetch(url + `/edit/${id}`, options)
            .then(res => fetchWithErrorCheck(res))
            .then(data => {
                console.log(data);
                getAllUsers();
            }).catch(exception => {
                document.getElementById("editUserError").innerHTML = "Error: No user by that ID";
            });
}

function deleteUser(evnt) {
    evnt.preventDefault();
    const id = document.getElementById("did").value;
    let options = {
        method: "DELETE",
        headers: {
            'Content-Type': 'application/json'
        }
    }
    fetch(url + `/delete/${id}`, options)
            .then(res => fetchWithErrorCheck(res))
            .then(data => {
                getAllUsers();
            })
            .catch(exception => {
                document.getElementById("deleteUserError").innerHTML = "Error: No user by that ID";
            });

}



function fetchWithErrorCheck(res) {
    if (!res.ok) {
        return Promise.reject({status: res.status, fullError: res.json()})
    }
    return res.json();
}