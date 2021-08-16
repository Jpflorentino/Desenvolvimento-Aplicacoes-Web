# **Comment**

The object Comment is a representation of only a single comment within an issue.

## **Comment representation object**

This object is created using siren, a hypermedia specification for representing entities, in this specific example the object json is divided into four properties: class, properties, actions, links

## **Class property**

Class property defined in siren documentation ([Class doc](https://github.com/kevinswiber/siren#class))

## **Properties property**

Properties property defined in siren documentation ([Properties doc](https://github.com/kevinswiber/siren#properties)). In this case the properties of the class user are:

### **user_id :**

- The ID of the person who commented on the issue

### **issue_id :**

- The ID of the issue where the comment was written

### **comment_id :**

- The comment's ID

### **comment_text :**

- The comment

### **creation_date :**

- the date of the creation of the comment, in epoch seconds

## **Actions property**

Actions property defined in siren documentation ([Actions doc](https://github.com/kevinswiber/siren#actions-1)):

### **Edit Comment:**

- The users have the ability to edit the comment they created, using the same type of request used for its creation (with the method indicated by the action object)

### **Delete Comment:**

- The owner of the comment can delete their comment using the endpoint specified by the action object

## **Links property**

Links property defined in siren documentation ([Links doc](https://github.com/kevinswiber/siren#links-1)):

- **Self :** URI to this Siren representation of this comment
- **Collection :** URI to the first page of the owner's commentlist ([Comment Collection](https://github.com/isel-leic-daw/daw-project-li61d-g07/blob/main/docs/Documentation/Comments/CommentCollection.md))
- **Owner :** URI to the owner issue ([Issue](https://github.com/isel-leic-daw/daw-project-li61d-g07/blob/main/docs/Documentation/Issues/IssueCollection.md))

## **Example Comment object**

- endpoint: /group7api/users/:user_id/projects/:project_id/issues/:issue_id/comments/:comment_id

- Method: GET

- Requires authentication

- Response:

```json
{
  "class": ["comment"],
  "properties": {
    "user_id": 1,
    "issue_id": 2,
    "comment_id": 2,
    "comment_text": "sample text",
    "creation_date": 1619196490
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
      "name": "edit-comment",
      "title": "Edit comment",
      "method": "PUT",
      "href": "/group7api/:user_id/projects/:project_id/issue/:issue_id/comments/:comment_id",
      "type": "application/json",
      "fields": [{ "name": "comment_text", "type": "text" }]
    },
    {
      "name": "delete-comment",
      "title": "Delete comment",
      "method": "DELETE",
      "href": "/group7api/:user_id/projects/:project_id/issue/:issue_id/comments/:comment_id"
    }
  ],
  "links": [
    {
      "rel": ["self"],
      "href": "/group7api/:user_id/projects/:project_id/issues/:issue_id/comments/:comment_id"
    },
    {
      "rel": ["owner"],
      "href": "/group7api/:user_id/projects/:project_id/issues/:issue_id"
    },
    {
      "rel": ["comment", "collection"],
      "href": "/group7api/:user_id/projects/:project_id/issues/:issue_id/comments?PageIndex={index}&PageSize={pageSize}"
    }
  ]
}
```

## **Create Comment Request**

- endpoint: /group7api/users/:user_id/projects/:project_id/issues/:issue_id/comments

- Method: POST

- Requires authentication

- Request body:

```json
{
  "comment_text": "test comment"
}
```

## **Delete Comment Request**

- endpoint: /group7api/users/:user_id/projects/:project_id/issues/:issue_id/comments/:comment_id

- Method: DELETE

- Requires authentication

## **Edit Comment Request**

- endpoint: /group7api/users/:user_id/projects/:project_id/issues/:issue_id/comments

- Method: PUT

- Requires authentication

- Request body:

```json
{
  "comment_text": "test comment"
}
```
