# **Issue Collection**

The object Issue Collection is a representation of a list of [Issue](https://github.com/isel-leic-daw/daw-project-li61d-g07/blob/main/docs/Documentation/Issues/IssueCollection.md)

## **Issue Collection representation object**

This object is used to represent a page from the collection of issues of a certain project. Like all other list objects, the "create" action is displayed here and the edit/delete is present in each specific issue's representation.

## **Properties property**

Properties property defined in siren documentation ([Properties doc](https://github.com/kevinswiber/siren#properties)). In this case the properties of the class user are:

### **collectionSize :**

- Total number of issues with this project as their owner

### **totalPages :**

- Total number of pages populated with issues

### **pageIndex :**

- Index of the page of results as described in the query string

### **pageSize :**

- Number of results per page as described in the query string

### **project_id :**

- ID of the owner of the issues in the list

## **Actions property**

Actions property defined in siren documentation ([Actions doc](https://github.com/kevinswiber/siren#actions-1)):

### **Create Issue:**

The create issue action is a part of the issue collection representation, requiring the fields represented in the example below:

**Name** : An issue's name. Since each issue only exists within a project, their names are not required to be unique

**Description** : A short description of the issue

**closed_on** : An integer with the desired date for the closing of the issue, expressed in epoch seconds. Optional

## **Links property**

Links property defined in siren documentation ([Links doc](https://github.com/kevinswiber/siren#links-1)):

- **Self :** URI to this page of issues from user with ID defined by the variable user_id
- **Previous :** URI to the previous page (redirects to this page if the page index is less than 1)
- **Next :** URI to the next page of issues

## **Example Issue Collection object**

- endpoint: /group7api/users/:user_id/projects/:project_id/issues?PageIndex={index}&PageSize={pageSize}

- Method: GET

- Requires authentication

- Response:

```json
{
  "class": ["issue", "collection"],
  "properties": {
    "collectionSize": 1,
    "totalPages": 1,
    "pageIndex": 1,
    "pageSize": 5,
    "project_id": 5
  },
  "entities": [
    {
      "class": ["issue"],
      "rel": ["item"],
      "properties": {
        "user_id": 4,
        "project_id": 5,
        "issue_id": 1,
        "name": "pij",
        "description": "d",
        "opened_on": 1623504370,
        "closed_on": 169546568,
        "state_name": "open"
      },
      "entities": [
        {
          "class": ["user"],
          "rel": ["owner"],
          "properties": {
            "user_id": 4,
            "username": "j"
          },
          "links": [
            {
              "rel": ["self"],
              "href": "/group7api/users/4/"
            }
          ]
        }
      ],
      "links": [
        {
          "rel": ["self"],
          "href": "/group7api/4/projects/5/issues/1"
        }
      ]
    }
  ],
  "actions": [
    {
      "name": "create-issue",
      "href": "/group7api/4/projects/5/issues",
      "title": "Create issue",
      "method": "POST",
      "type": "application/json",
      "fields": [
        {
          "name": "name",
          "type": "text"
        },
        {
          "name": "description",
          "type": "text"
        },
        {
          "name": "closed_on",
          "type": "number"
        }
      ]
    }
  ],
  "links": [
    {
      "rel": ["self"],
      "href": "/group7api/4/projects/5/issues?PageIndex=1&PageSize=5"
    },
    {
      "rel": ["previous"],
      "href": "/group7api/4/projects/5/issues?pageIndex=1&pageSize=5"
    },
    {
      "rel": ["next"],
      "href": "/group7api/4/projects/5/issues?pageIndex=2&pageSize=5"
    },
    {
      "rel": ["owner"],
      "href": "/group7api/4/projects/5/"
    }
  ]
}
```
