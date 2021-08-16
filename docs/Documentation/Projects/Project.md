# **Project**

The object Project is a representation of only a single project.

## **Project representation object**

This object is created using siren, a hypermedia specification for representing entities, in this specific example the object json is divided into four properties: class, properties, actions, links

## **Class property**

Class property defined in siren documentation ([Class doc](https://github.com/kevinswiber/siren#class))

## **Properties property**

Properties property defined in siren documentation ([Properties doc](https://github.com/kevinswiber/siren#properties)). In this case the properties of the class user are:

### **Name :**

- The project's name

### **Description :**

- The project's description

### **Project ID :**

- The project's ID

## **Actions property**

Actions property defined in siren documentation ([Actions doc](https://github.com/kevinswiber/siren#actions-1)):

### **Edit Project:**

- The owners have the ability to edit the project they created. To avoid database errors caused by deleting labels and states which are in use, the API **does not** allow you to delete such objects: you can only _add_ more labels, states and transitions. Name and description are always editable.

### **Delete Project:**

- The users have the ability to delete the project they created. Only the owner can delete a project

## **Links property**

Links property defined in siren documentation ([Links doc](https://github.com/kevinswiber/siren#links-1)):

- **Self :** URI to this Siren representation of this project ([User doc](https://github.com/isel-leic-daw/daw-project-li61d-g07/blob/main/docs/Documentation/Users/User.md))
- **User Projects :** URI to the first page of the owner's projects ([Projects doc](https://github.com/isel-leic-daw/daw-project-li61d-g07/blob/main/docs/Documentation/Projects/ProjectsCollection.md))

## **Example Project object**

- endpoint: /group7api/users/:user_id/projects/:project_id

- Method: GET

- Requires authentication

- Response:

```json
{
  "class": ["project"],
  "properties": {
    "name": "the project name",
    "description": "the project description",
    "project_id": 43,
    "labels": ["label1", "label2"],
    "states": ["state1", "state2"],
    "transitions": [
      {
        "project_id": 43,
        "state_name": "archived",
        "transits_to": "closed"
      }
    ]
  },
  "entities": [
    {
      "class": ["user"],
      "rel": ["owner"],
      "properties": {
        "user_id": "user_id",
        "username": "username"
      },
      "links": [{ "rel": ["self"], "href": "/group7api/user/:user_id" }]
    }
  ],
  "actions": [
    {
      "name": "edit-project",
      "title": "Edit project",
      "method": "PUT",
      "href": "/group7api/:user_id/projects/:project_id",
      "type": "application/json",
      "fields": [
        { "name": "name", "type": "text" },
        { "name": "description", "type": "text" },
        { "name": "allowed_labels_set", "type": "text" },
        { "name": "allowed_states_set", "type": "text" },
        { "name": "allowed_transitions_set", "type": "text" }
      ]
    },
    {
      "name": "delete-project",
      "title": "Delete project",
      "method": "DELETE",
      "href": "/group7api/:user_id/projects/:project_id"
    }
  ],
  "links": [
    { "rel": ["self"], "href": "/group7api/user/:user_id/projects/project_id" },
    {
      "rel": ["projects", "collection"],
      "href": "/group7api/:user_id/projects?PageIndex={index}&PageSize={pageSize}"
    },
    {
      "rel": ["issues", "collection"],
      "href": "/group7api/:user_id/projects/:project_id/issues?PageIndex={index}&PageSize={pageSize}"
    }
  ]
}
```

## **Create Project Request**

- endpoint: /group7api/users/:user_id/projects

- Method: POST

- Requires authentication

- Note: in order to guarantee basic functionality, the API automatically inserts default states and transitions between them ('open', 'closed' and 'archived'). If the user does not wish to provide any additional states or labels, the client can send this request with the relevant properties as empty strings. If the client omits the properties, the API will return a bad request error.

- Request body:

```json
{
  "name": "projeto11Teste",
  "description": "description",
  "allowed_labels_set": "[label1,label2]",
  "allowed_states_set": "[open,closed]",
  "allowed_transitions_set": "[open:closed]"
}
```

## **Edit Project Request**

- endpoint: /group7api/users/:user_id/projects/:project_id

- Method: PUT

- Requires authentication

- Note: In order to properly edit a project, the client has to provide a request body equal to create Project. The name and description must be present and must have a value (either new values or the old ones) and any property that does not need to be altered must be sent as an empty string. This action only adds new labels, states or transitions: it is not possible to delete or alter the project's states, as this would cause database issues.

- Request body:

```json
{
  "name": "projeto11Teste",
  "description": "description",
  "allowed_labels_set": "[label1,label2]",
  "allowed_states_set": "[open,closed]",
  "allowed_transitions_set": "[open:closed]"
}
```

## **Delete Project Request**

- endpoint: /group7api/users/:user_id/projects/:project_id

- Method: DELETE

- Requires authentication
