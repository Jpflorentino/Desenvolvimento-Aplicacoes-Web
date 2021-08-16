# DAW Phase2 Presentation Group 7

### How are credentials provided and stored?

The user's credentials aren't provided directly to the user through the client. The user inputs the username and password combo and then those values are converted
to base64 and sent as the value of the 'Authentication' header in a HTTP request to a `login` endpoint in the API, which returns a sucessful response or an error
if the credentials are not valid. If the login is successful, an `activeUserSession` object is created which stores these values (in base64), the username, a boolean with the `isLoggedIn`
state and the `userId` for future requests:

```typescript
export type UserSession = {
  isLoggedIn: boolean;
  userID: number;
  authCredentials: string;
  username: string;
};
```

This session object is then stored in session storage, which looked slightly safer than local storage since it deletes the data when the window is closed as opposed to local storage, which is available after the window has been closed. However, the credentials are stored together and unencrypted (although in base 64), so the most dangerous security measure we took isn't actually _where_ we stored the credentials.

Within the application, this session is made available to all components through the `useContext` and `useState` hooks. Using this session, we can use the userID for any
conditional rendering (not idempontent actions are locked to all user's except the resource's owner) and the `authCredentials` for any authenticated request, since they are
stored in base64 for ease of use.

### How is Deep Linking supported?

The first step to include deep linking in this application was customizing the webpack configuration file, adding the flag `historyApiFallback` with `true` so all 404 errors
are redirected to the home component. In the home component, a React Router does the client side routing and renders the desired component for that URL.

In addition to this flag, a new endpoint was added to the API which provides an object with templates for all resources of the application. With these templates,
we can parse the URL and render the components based entirely on the URL, thus implementing deep linking.

```kotlin
data class APILinks(
        val home: String = "/group7api/home",
        val allProjects: String = "/group7api/projects?PageIndex={index}&PageSize={pageSize}",
        val userDetails: String = "/group7api/users/{userid}",
        val userLogin: String = "/group7api/users/login",
        val userProjects: String = "/group7api/{userid}/projects?PageIndex={index}&PageSize={pageSize}",
        val userIssues: String = "/group7api/{userid}/projects/{projectid}/issues?PageIndex={index}&PageSize={pageSize}",
        val projectDetails: String = "/group7api/{userid}/projects/{projectid}",
        val issueDetails: String = "/group7api/{userid}/projects/{projectid}/issues/{issueid}",
        val issueCollection: String = "/group7api/{userid}/projects/{projectid}/issues?PageIndex={index}&PageSize={pageSize}",
        val issueCommentsCollection: String = "/group7api/{userid}/projects/{projectid}/issues/{issueid}/comments?PageIndex={index}&PageSize={pageSize}",
        val commentDetails: String = "/group7api/{userid}/projects/{projectid}/issues/{issueid}/comments/{commentid}",
        val changeLabel: String = "/group7api/{userid}/projects/{projectid}/issues/{issueid}/labels", //same URL for add and delete label
        val changeState: String = "/group7api/{userid}/projects/{projectid}/issues/{issueid}/state"
)
```

One improvement to be made in this section of the client application would be the use of nested routers: in it's current state, the application's router relies heavily on the `exact path` notation to properly route requests, which in turn creates repetitive code:

```typescript
<Router>

              <Navbar activeItem={navbarActiveItem} user={userSession} />

              <div>
                <Switch>

                  <Route exact path="/">
                    <Home/>
                  </Route>

                  <Route exact path="/logout">
                    <Logout/>
                  </Route>

                  <Route exact path="/:user_id/projects/:project_id/issues/:issue_id/comments/create">
                    <CreateComment uri={host + links.commentDetails}/>
                  </Route>

                  <Route exact path="/:user_id/projects/:project_id/issues/:issue_id/comments/:comment_id">
                    <Comment uri={host + links.commentDetails}/>
                  </Route>

                  <Route exact path="/:user_id/projects/:project_id/issues/create">
                    <CreateIssue uri={links.issueDetails} />
                  </Route>
```

There are two specific pages that don't support deep linking in this application: the edit issue page and the edit project page. This limitation is a consequence of a design decision by the developers: we wanted an edit page that automatically filled the edit form with the current resource's data. In order to avoid a second (redundant) HTTP request
to get a resource's details, just to show them in a form, we decided to conditionally render the components in the same page as the resource's details, in order to pass the
details as a prop. This doesn't allow us to have a specific route for the edit pages, which means they aren't acessible through deep linking.

### How are forms handled and HTTP requests performed?

These two items are together because, in our application, a form is just a simpler use case of the general approach to HTTP requests:

```typescript
useEffect(() => {
  if (!submit) return;

  setSubmit(false);

  const body = {
    username: username,
    password: password,
  };

  const options = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  };

  fetch(getUserURI, options)
    .then((resp) => {
      return resp.json();
    })
    .then((json) => {
      if (json.status == null) {
        let userid = json.properties.user_id;

        const creds = base64.encode(body.username + ":" + body.password);

        /// Update user session
        updateUserSession({
          /// Already inside a useEffect
          isLoggedIn: true,
          username: body.username,
          userID: userid,
          authCredentials: creds,
        });
        history.push("/");
      } else {
        alert(json.detail);
      }
    })
    .catch((err) => alert("Erro: " + err));
}, [submit]);
```

Our application deals almost exclusively with requests that depend on data from a previous request (for example, in order to render a project's details page in full, we also have to render an issues list and that URL is only available after the first request has been completed). This approach is also how forms work: we can only perform a request after a certain criteria is met, in this case, after the user inputs their credentials.

Our approach to HTTP requests depends on the `useState` and the `useEffect` hooks, in a similar fashion as the custom `useFetch` hook developed in class. We chose to not use that custom hook because we had a hard time adapting it to our needs, so instead we used this approach on all components that require HTTP requests (which is almost all of them).

First we define what states the component has (in the forms case, it's just the `submit` state). After that, we create a `useEffect` hook that is triggered by a change in that state, along with some logic that prevents the hook from triggering in other unwanted circumstances. Finally, we create the `options` object with the method and body and call the `fetch` function.

### How are errors handled?

Errors are handled within the `useEffect` hook from the previous section. For responses that don't have a body, we evaluate the status code and, if it's any higher than 300, we know it's an error in `problem+json` format which needs to be parsed:

```typescript
fetch(
  uri
    .replace("{userid}", activeUserSession.userID.toString())
    .replace("{projectid}", project.properties.project_id.toString()),
  options
)
  .then((resp) => {
    if (resp.status > 300) return resp.json(); //to handle the errors
  })
  .then((json) => {
    if (json) {
      alert(json.detail);
    } else {
      handleCancel();
    }
  })
  .catch((err) => alert(err));
```

In cases where the response has a body, and we need to parse it regardless, we check if it's an error thought the presence of the `status` property:

```typescript
fetch(getUserURI, options)
    .then((resp) => {
      return resp.json();
    })
    .then((json) => {
      if (json.status == null) {
       (...)
      } else {
        alert(json.detail);
      }
    })
    .catch((err) => alert("Erro: " + err));
```

In any case, when an error is detected we create an `alert` with the detail of the error object

### How is hypermedia used?

Hypermedia is used in our application in every single instance where a resource has to be displayed. The application doesn't store any type of API info except for the
user's ID and username in the session, which goes to show that we use the backend data and the Siren format to it's full extent.

In a project's page, the specific data of the resource is always the values of the `properties` of the Siren representation. A project's issues list (or an issue's comments list)
is always fetched based on the value present in the `links` object of the Siren representation, which in turn makes the client a bit more robust and impervious to any non-breaking change in the API.

One possible improvement in this area would be the use of the `actions` for any of the CRUD operations. Since we support deep-linking on most of the application's pages, including the create pages, using the `actions` would add a layer of complexity to the code, given that each page is a different component and it would require an extra HTTP request to get the actions from the Siren representation (for a second time).
