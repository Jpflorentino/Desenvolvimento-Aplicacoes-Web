# **User**

The object User is a representation of only a single user.

## **User representation object**

This object is created using siren, a hypermedia specification for representing entities, in this specific example the object json is divided into four properties: class, properties, actions, links

## **Class property**

Class property defined in siren documentation ([Class doc](https://github.com/kevinswiber/siren#class))

## **Properties property**

Properties property defined in siren documentation ([Properties doc](https://github.com/kevinswiber/siren#properties)). In this case the properties of the class user are:

### **user_id :**

- user_id is used to represent a user inside the databse, it is a unique value which is defined automatically by the database when the user creates an account.

### **username :**

- username is used to represent a user in the website, it is a unique value which is defined by the user when he/she creates an account.

## **Actions property**

Actions property defined in siren documentation ([Actions doc](https://github.com/kevinswiber/siren#actions-1)):

### **Edit User Account:**

- The users have the ability to edit the account they created, they can change their password if they want

### **Delete User Account:**

- The users have the ability to delete the account they created

## **Links property**

Links property defined in siren documentation ([Links doc](https://github.com/kevinswiber/siren#links-1)):

- **Self :** URI to this Siren representation of a user ([User doc](https://github.com/isel-leic-daw/daw-project-li61d-g07/blob/main/docs/Documentation/Users/User.md))
- **User Projects :** URI to the first page of the user's projects ([Projects doc](https://github.com/isel-leic-daw/daw-project-li61d-g07/blob/main/docs/Documentation/Projects/ProjectsCollection.md))

## **Example User object**

- endpoint: /group7api/users/:user_id

- Method: GET

- Requires authentication

- Response:

```json
{
  "class": ["user"],
  "properties": {
    "user_id": 42,
    "username": "user1"
  },
  "actions": [
    {
      "name": "edit-user-account",
      "title": "Edit user account",
      "method": "PUT",
      "href": "/group7api/users/:user_id",
      "type": "application/json",
      "fields": [{ "name": "newPassword", "type": "text" }]
    },
    {
      "name": "delete-item",
      "title": "Delete user account",
      "method": "DELETE",
      "href": "/group7api/users/user_id"
    }
  ],
  "links": [
    { "rel": ["self"], "href": "/group7api/users/:user_id" },
    {
      "rel": ["user", "projects"],
      "href": "/group7api/4/projects?PageIndex={index}&PageSize={pageSize}"
    }
  ]
}
```

## **Create User Request**

- endpoint: /group7api/users

- Method: POST

- Does not require authentication

- Request body:

```json
{
  "username": "example username",
  "password": "example password"
}
```

## **Edit User Request**

- endpoint: /group7api/users/:user_id

- Method: PUT

- Requires Authentication

- Request body:

```json
{
  "newPassword": "example password"
}
```

## **Delete User Request**

- endpoint: /group7api/users/:user_id

- Method: DELETE

- Requires Authentication

## **Get User Projects Request**

- endpoint: /group7api/users/:user_id/projects?PageIndex={index}&PageSize={pageSize}

- Method: GET

- Requires Authentication

- Response body: see [Projects doc](https://github.com/isel-leic-daw/daw-project-li61d-g07/tree/main/docs/Documentation/Projects/ProjectsCollection.md)

## **Login User Request**

- endpoint: /group7api/users/login

- Method: POST

- Does not require authentication

- Request body:

```json
{
  "username": "example username",
  "password": "example password"
}
```
