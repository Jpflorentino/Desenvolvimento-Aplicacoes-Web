# **Issue**

The object Issue is a representation of only a single issue within a project.

## **Issue representation object**

This object is created using siren, a hypermedia specification for representing entities, in this specific example the object json is divided into four properties: class, properties, actions, links

## **Class property**

Class property defined in siren documentation ([Class doc](https://github.com/kevinswiber/siren#class))

## **Properties property**

Properties property defined in siren documentation ([Properties doc](https://github.com/kevinswiber/siren#properties)). In this case the properties of the class user are:

### **Name :**

- The issue's name

### **Description :**

- The issue's description

### **issue ID :**

- The issues's ID

### **project ID :**

- The owner project's ID

### **state :**

- The issues's state, chosen from the list of avaliable states defined in the project creation

### **labels :**

- An array with the issues's labels, chosen from the list of avaliable labels defined in the project creation

### **opened_on :**

- The date of the issue's closure, in epoch seconds

### **closed_on :**

- The date of the issue creation, in epoch seconds

## **Actions property**

Actions property defined in siren documentation ([Actions doc](https://github.com/kevinswiber/siren#actions-1)):

### **Edit Issue:**

- The users have the ability to edit the issue they created, using the same type of request used for the creation of the issue (with the method indicated by the action object)

### **Delete Issue:**

- The users have the ability to delete the issue they created. Only the owner of the issue can delete an issue

### **Change state:**

- The new state field is required and has to obey by the list of avaliable states and transitions described by the project. Only the owner of the issue can change it's state

### **Add label:**

- The label field is required and has to obey by the list of avaliable labels described by the project. Only the owner of the issue can change the labels

### **Remove label:**

- The label field is required and a label can only be removed from the issue, not the list of possible labels described in the project creation. Only the owner of the issue can change the labels

## **Links property**

Links property defined in siren documentation ([Links doc](https://github.com/kevinswiber/siren#links-1)):

- **Self :** URI to this Siren representation of this issue
- **Issue Collection :** URI to the first page of the owner's issues ([Issue Collection](https://github.com/isel-leic-daw/daw-project-li61d-g07/blob/main/docs/Documentation/Issues/IssueCollection.md))
- **Owner :** URI to the owner project ([Project](https://github.com/isel-leic-daw/daw-project-li61d-g07/blob/main/docs/Documentation/Projects/Project.md))
- **Comments Collection :** URI to the first page of this issue's comments ([Comments Collection](https://github.com/isel-leic-daw/daw-project-li61d-g07/blob/main/docs/Documentation/Comments/CommentCollection.md))

## **Example Issue object**

- endpoint: /group7api/users/:user_id/projects/:project_id/issues/:issue_id

- Method: GET

- Requires authentication

- Response:

```json
{
  "class": ["issue"],
  "properties": {
    "user_id": 4,
    "project_id": 43,
    "issue_id": 40,
    "name": "a",
    "description": "a",
    "labels": [
      "label1",
      "label2"
    ],
    "opened_on": 1623162751,
    "state_name": "state1"
    "closed_on": 1619400543
  },
  "entities": [
    {
      "class": ["user"],
      "rel": ["author"],
      "properties": {
        "user_id": "user_id",
        "username": "username"
      },
      "links": [{ "rel": ["self"], "href": "/group7api/user/:user_id" }]
    }
  ],
  "actions": [
    {
      "name": "edit-issue",
      "title": "Edit issue",
      "method": "PUT",
      "href": "/group7api/:user_id/projects/:project_id/issue/:issue_id",
      "type": "application/json",
      "fields": [
        { "name": "name", "type": "text" },
        { "name": "description", "type": "text" },
        { "name": "endDate", "type": "number" }
      ]
    },
    {
      "name": "delete-issue",
      "title": "Delete issue",
      "method": "DELETE",
      "href": "/group7api/:user_id/projects/:project_id/issue/:issue_id"
    },
    {
      "name": "change-state",
      "title": "Change state",
      "method": "POST",
      "href": "/group7api/:user_id/projects/:project_id/issue/:issue_id/state",
      "type": "application/json",
      "fields": [{ "name": "new-state", "type": "text" }]
    },
    {
      "name": "add-label",
      "title": "Add label",
      "method": "POST",
      "href": "/group7api/:user_id/projects/:project_id/issue/:issue_id/labels",
      "type": "application/json",
      "fields": [{ "name": "label", "type": "text" }]
    },
    {
      "name": "remove-label",
      "title": "Delete label",
      "method": "DELETE",
      "href": "/group7api/:user_id/projects/:project_id/issue/:issue_id/labels",
      "type": "application/json",
      "fields": [{ "name": "label", "type": "text" }]
    }
  ],
  "links": [
    {
      "rel": ["self"],
      "href": "/group7api/user/:user_id/projects/:project_id/issues/:issue_id"
    },
    {
      "rel": ["issue", "collection"],
      "href": "/group7api/4/projects/43/issues/40?PageIndex={index}&PageSize={pageSize}"
    },
    {
      "rel": ["owner"],
      "href": "/group7api/user/:user_id/projects/:project_id"
    },
    {
      "rel": ["comments", "collection"],
      "href": "/group7api/4/projects/43/issues/40/comments?PageIndex={index}&PageSize={pageSize}"
    }
  ]
}
```

## **Create Issue Request**

- endpoint: /group7api/users/:user_id/projects/:project_id/issues

- Method: POST

- Requires authentication

- Note: the "closed_on" parameter requires the date for the issue to be closed on
  in the epoch seconds format. It is **optional** and, if the user does not wish to add a closing date, the client needs to send the value of the property as an empty string

- Request body:

```json
{
  "name": "IssueTeste2",
  "description": "issu",
  "closed_on": 169546568
}
```

## **Delete Issue Request**

- endpoint: /group7api/users/:user_id/projects/:project_id/issues/:issue_id

- Method: DELETE

- Requires authentication

## **Edit Issue Request**

- endpoint: /group7api/users/:user_id/projects/:project_id/issues/:issue_id

- Method: PUT

- Requires authentication

- Note: In this case, if the user does not wish to edit the closing date, the client can omit the property from the body object

- Request body:

```json
{
  "name": "IssueTeste2",
  "description": "issu"
}
```

## **Add Label Request**

- endpoint: /group7api/users/:user_id/projects/:project_id/issues/:issue_id/labels

- Method: POST

- Requires authentication

- Request body:

```json
{
  "label": "label1"
}
```

## **Remove Label Request**

- endpoint: /group7api/users/:user_id/projects/:project_id/issues/:issue_id/labels

- Method: DELETE

- Requires authentication

- Request body:

```json
{
  "label": "label1"
}
```

## **Change State Request**

- endpoint: /group7api/users/:user_id/projects/:project_id/issues/:issue_id/state

- Method: PUT

- Requires authentication

- Note: The API will automatically check the project's transitions and return an error in case the transition is not allowed or the state doesn't exist. However, it doesn't hurt to be mindfull of the changes we intend to make to an issue

- Request body:

```json
{
  "new_state": "closed"
}
```
