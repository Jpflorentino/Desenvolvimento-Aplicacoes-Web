# **Comment Collection**

The object Comment Collection is a representation of a list of [Comment](https://github.com/isel-leic-daw/daw-project-li61d-g07/blob/main/docs/Documentation/Comments/Comment.md)

## **Comment Collection representation object**

This object is used to represent a page from a collection of comments. The creation of a new comment is supported from within the list but any action regarding a specific item is contained within the representation of itself.

## **Properties property**

Properties property defined in siren documentation ([Properties doc](https://github.com/kevinswiber/siren#properties)). In this case the properties of the class user are:

### **collectionSize :**

- Total number of comments in this issue

### **totalPages :**

- Total number of pages populated with issues

### **pageIndex :**

- Index of the page of results as described in the query string

### **pageSize :**

- Number of results per page as described in the query string

### **issue_id :**

- ID of the owner of the issue where the comments have been written

## **Actions property**

Actions property defined in siren documentation ([Actions doc](https://github.com/kevinswiber/siren#actions-1)):

### **Create Comment:**

The create comment action is a part of the comment collection representation, requiring the fields represented in the example below:

**comment_text** : the text of the comment

## **Links property**

Links property defined in siren documentation ([Links doc](https://github.com/kevinswiber/siren#links-1)):

- **Self :** URI to this page of projects from user with ID defined by the variable user_id
- **Previous :** URI to the previous page (redirects to this page if the page index is less than 1)
- **Next :** URI to the next page of user projects

## **Example Comment Collection object**

- endpoint: /group7api/users/:user_id/projects/:project_id/issues/:issue_id/comments?PageIndex={index}&PageSize={pageSize}

- Method: GET

- Requires authentication

- Response:

```json
{
  "class": ["comment", "collection"],
  "properties": {
    "collectionSize": 1,
    "totalPages": 1,
    "pageIndex": 1,
    "pageSize": 5,
    "issue_id": 2
  },
  "entities": [
    {
      "class": ["comment"],
      "rel": ["item"],
      "properties": {
        "user_id": 3,
        "issue_id": 2,
        "comment_id": 2,
        "comment_text": "comment pij",
        "creation_date": 1623505214
      },
      "entities": [
        {
          "class": ["user"],
          "rel": ["owner"],
          "properties": {
            "user_id": 3,
            "username": "john"
          },
          "links": [
            {
              "rel": ["self"],
              "href": "/group7api/users/3/"
            }
          ]
        }
      ],
      "links": [
        {
          "rel": ["self"],
          "href": "/group7api/3/projects/4/issues/2/comments/2"
        }
      ]
    }
  ],
  "actions": [
    {
      "name": "create-comment",
      "href": "/group7api/3/projects/4/issues/2/comments",
      "title": "Create comment",
      "method": "POST",
      "type": "application/json",
      "fields": [
        {
          "name": "comment_text",
          "type": "text"
        }
      ]
    }
  ],
  "links": [
    {
      "rel": ["self"],
      "href": "/group7api/3/projects/4/issues?PageIndex={index}&PageSize={pageSize}"
    },
    {
      "rel": ["previous"],
      "href": "/group7api/3/projects/4/issues?pageIndex=1&pageSize=5"
    },
    {
      "rel": ["next"],
      "href": "/group7api/3/projects/4/issues?pageIndex=2&pageSize=5"
    },
    {
      "rel": ["owner"],
      "href": "/group7api/3/projects/4/issues/2"
    }
  ]
}
```
