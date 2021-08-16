# **Home**

The home resource is the entry point of the API. It allows for any client application to navigate through different resources with no out-of-band knowledge of the API.

The home resource is represented in the application/vnd.siren+json media type.

The paths to the different resources will start with a reference to the api (e.g. /group7api/). This prevents breaking compatibility with clients using the API, allowing them to gradually adapt to the resources they want to access.

Since the API allows consultation of the projects in the platform without an account, the Home resource changes depending on the status of the end user: if the request has a valid "Authentication" header, it returns a navigation link to the user's Siren representation (which itself has links to the user's project list); if the request doesn't have such a header, this navigation link won't be present in the Home representation.

### **For the endpoints where an account is necessary, the API only supports [Basic Authentication](https://datatracker.ietf.org/doc/html/rfc7617), which should be included in every request for protected endpoints**.

## **API Resource Graph**

![Resource Graph](https://github.com/isel-leic-daw/daw-project-li61d-g07/blob/main/docs/DAW_trab1.png)

This image shows how the entities in the API relate to one another: from the Home resource a user can get a list with all the projects or create an account, and from then on navigate to their own projects.

## **Home representation object**

This object is created using siren, a hypermedia specification for representing entities, in this specific example the object json is divided into three properties: class, actions, links

## **Class property**

Class property defined in siren documentation ([Class doc](https://github.com/kevinswiber/siren#class))

## **Actions property**

Actions property defined in siren documentation ([Actions doc](https://github.com/kevinswiber/siren#actions-1)):

### **Create User Account:**

- In this case the users can only register themselves to start using the whole API that includes the possibility to create their own projects, create issues on other's projects and add comments

## **Links property**

Links property defined in siren documentation ([Links doc](https://github.com/kevinswiber/siren#links-1)):

- **Self :** The URI to this representation of the Home Resource ([Home doc](https://github.com/isel-leic-daw/daw-project-li61d-g07/blob/main/docs/Documentation/Home/Home.md))
- **Projects :** The URI to a Siren representation of the API's project list ([Projects doc](https://github.com/isel-leic-daw/daw-project-li61d-g07/tree/main/docs/Documentation/Projects/ProjectsCollection.md))

## **API Links**

- endpoint: /group7api

- Method: GET

- Does not require authentication

- This endpoint returns a json object with templates for every API functionality. However, each Siren representation will also have the relevant templates in the "Links" object

- Response:

```json
{
  "home": "/group7api/home",
  "allProjects": "/group7api/projects?PageIndex={index}&PageSize={pageSize}",
  "userDetails": "/group7api/users/{userid}",
  "userLogin": "/group7api/users/login",
  "userProjects": "/group7api/{userid}/projects?PageIndex={index}&PageSize={pageSize}",
  "userIssues": "/group7api/{userid}/projects/{projectid}/issues?PageIndex={index}&PageSize={pageSize}",
  "projectDetails": "/group7api/{userid}/projects/{projectid}",
  "issueDetails": "/group7api/{userid}/projects/{projectid}/issues/{issueid}",
  "issueCollection": "/group7api/{userid}/projects/{projectid}/issues?PageIndex={index}&PageSize={pageSize}",
  "issueCommentsCollection": "/group7api/{userid}/projects/{projectid}/issues/{issueid}/comments?PageIndex={index}&PageSize={pageSize}",
  "commentDetails": "/group7api/{userid}/projects/{projectid}/issues/{issueid}/comments/{commentid}",
  "changeLabel": "/group7api/{userid}/projects/{projectid}/issues/{issueid}/labels",
  "changeState": "/group7api/{userid}/projects/{projectid}/issues/{issueid}/state"
}
```

## **Home object**

- endpoint: /group7api/home

- Method: GET

- Does not require authentication

- This object contains the relevant endpoints for a landing page: a link to a collection of all the database's projects and an action to create a user account. Note that only authenticated users can access the project details and their issues/comments.

- Response:

```json
{
  "class": ["home"],
  "properties": {},
  "actions": [
    {
      "name": "create-user-account",
      "title": "Create user account",
      "method": "POST",
      "href": "/group7api/users/",
      "type": "application/json",
      "fields": [
        { "name": "username", "type": "text" },
        { "name": "password", "type": "text" }
      ]
    }
  ],
  "links": [
    { "rel": ["self"], "href": "/group7api/home" },
    {
      "rel": ["projects"],
      "href": "/group7api/projects?PageIndex={index}&PageSize={pageSize}"
    }
  ]
}
```
