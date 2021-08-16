# **Project Collection**

The object Project Collection is a representation of a list of [Project](https://github.com/isel-leic-daw/daw-project-li61d-g07/blob/main/docs/Documentation/Projects/Project.md)

## **Project Collection representation object**

This object is used to represent a page from a collection of projects. Since this class is used for representing all the projects in the API and a single user's projects, its representation will change according to context: when returning all the projects, the "create Project" action will not be present and the "Links" property will not have the userID in the URI. In addition, each project will have a subentity with the owner's info,

## **Properties property**

Properties property defined in siren documentation ([Properties doc](https://github.com/kevinswiber/siren#properties)). In this case the properties of the class user are:

### **collectionSize :**

- Total number of projects with this user as their owner

### **totalPages :**

- Total number of pages populated with projects

### **pageIndex :**

- Index of the page of results as described in the query string

### **pageSize :**

- Number of results per page as described in the query string

### **user_id :**

- ID of the owner of the projects in the list

## **Actions property**

Actions property defined in siren documentation ([Actions doc](https://github.com/kevinswiber/siren#actions-1)):

### **Create Project:**

The create project action is a part of the project collection representation, requiring the fields represented in the example below:

**Name** : A project's name has to be **unique** within the database and the API will return a 409 Conflict error if the name provided in the request matches any other project

**Description** : A short description of the project

**allowed_labels_set** : A string with the labels that can be added to issues within the project. _Must_ follow the format "[label1,label2,...]"

**allowed_states_set** : A string with the states that can be used to organize issues within the project. _Must_ follow the format "[state1,state2,...]". The API requires the "open","closed" and "archived" states, they will be added automatically if not present in the request

**allowed_transitions_set** : A string with the transitions between states that can be used within the project. _Must_ follow the format "[state1:state2,state2:state3,...]". The transitions must use the states in the allowed_states_set field, otherwise the API will return a Bad Request response. The transitions between the mandatory states "open", "closed" and "archived" are mandatory and they will be added if not present in the request

## **Links property**

Links property defined in siren documentation ([Links doc](https://github.com/kevinswiber/siren#links-1)):

- **Self :** URI to this page of projects from user with ID defined by the variable user_id
- **Previous :** URI to the previous page (redirects to this page if the page index is less than 1)
- **Next :** URI to the next page of user projects

## **Example User Project Collection object**

- endpoint: /group7api/users/:user_id/projects?PageIndex={index}&PageSize={pageSize}

- Method: GET

- Requires authentication

- Response:

```json
{
  "class": ["project", "collection"],
  "properties": {
    "collectionSize": 42,
    "totalPages": 1,
    "pageIndex": 1,
    "pageSize": 5,
    "user_id": 11
  },
  "entities": [
    {
      "class": ["project"],
      "rel": ["item"],
      "properties": {
        "name": "the project name",
        "description": "the project description"
      },
      "links": [
        { "rel": ["self"], "href": "/group7api/:user_id/projects/:project_id" }
      ]
    },
    {
      "class": ["project"],
      "rel": ["item"],
      "properties": {
        "name": "another-name",
        "description": "another-description"
      },
      "links": [
        { "rel": ["self"], "href": "/group7api/:user_id/projects/:project_id" }
      ]
    }
  ],
  "actions": [
    {
      "name": "create-project",
      "title": "Create project",
      "method": "POST",
      "href": "/group7api/:user_id/projects/",
      "type": "application/json",
      "fields": [
        { "name": "name", "type": "text" },
        { "name": "description", "type": "text" },
        { "name": "allowed_labels_set", "type": "text" },
        { "name": "allowed_states_set", "type": "text" },
        { "name": "allowed_transitions_set", "type": "text" }
      ]
    }
  ],
  "links": [
    {
      "rel": ["self"],
      "href": "/group7api/:user_id/projects?pageIndex=1&pageSize=5"
    },
    {
      "rel": ["previous"],
      "href": "/group7api/:user_id/projects?pageIndex=1&pageSize=5"
    },
    {
      "rel": ["next"],
      "href": "/group7api/:user_id/projects?pageIndex=2&pageSize=5"
    }
  ]
}
```

## **Example All Projects Collection object**

- endpoint: /group7api/projects?PageIndex={index}&PageSize={pageSize}

- Method: GET

- Requires authentication

- Response:

```json
{
  "class": ["project", "collection"],
  "properties": {
    "collectionSize": 5,
    "totalPages": 1,
    "pageIndex": 1,
    "pageSize": 5
  },
  "entities": [
    {
      "class": ["project"],
      "rel": ["item"],
      "properties": {
        "user_id": 1,
        "project_id": 1,
        "name": "Daw Phase1",
        "description": "The evaluation focus will be on the HTTP API documentation and implementation.",
        "labels": [],
        "states": [],
        "transitions": []
      },
      "entities": [
        {
          "class": ["user"],
          "rel": ["owner"],
          "properties": {
            "user_id": 1,
            "username": "Alice"
          },
          "links": [
            {
              "rel": ["self"],
              "href": "/group7api/users/1/"
            }
          ]
        }
      ],
      "links": [
        {
          "rel": ["self"],
          "href": "/group7api/1/projects/1"
        }
      ]
    }
  ],
  "links": [
    {
      "rel": ["self"],
      "href": "/group7api/projects?pageIndex=1&pageSize=5"
    },
    {
      "rel": ["previous"],
      "href": "/group7api/projects?pageIndex=1&pageSize=5"
    },
    {
      "rel": ["next"],
      "href": "/group7api/projects?pageIndex=2&pageSize=5"
    }
  ]
}
```
