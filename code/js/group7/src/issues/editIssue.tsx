import React, { useEffect, useState, useContext } from "react";
import { Redirect } from "react-router-dom";
import { Issue } from "../issues/issue";
import { IssueType } from "../common/types/issue";
import { NavbarContext } from "../navbar/navbarContext";
import { UserSessionContext } from "../users/userSessionContext";

/// Edit Issue component props
type editIssueProps = {
  uri: string;
  issue: IssueType;
};

export function EditIssue({ uri, issue }: editIssueProps) {
  /// If user is not logged in, redirect to Login / Register page
  const { activeUserSession } = useContext(UserSessionContext);
  if (!activeUserSession.isLoggedIn) return <Redirect to="/loginRegister" />;

  const [cancelState, setCancelState] = useState(false);

  const [newName, setNewName] = useState(issue.properties.name);
  const [newDescription, setNewDescription] = useState(
    issue.properties.description
  );
  const [submit, setSubmit] = useState(false);

  /// Update navbar active item
  const { activeItem, updateActiveItem } = useContext(NavbarContext);
  useEffect(() => {
    /// Avoid component side-effects
    if (activeItem !== "projects") updateActiveItem("projects");
  }, []);

  let body = {
    name: newName || issue.properties.name,
    description: newDescription || issue.properties.description,
  };

  useEffect(() => {
    if (!submit) return;

    setSubmit(false);

    const options = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Basic " + activeUserSession.authCredentials,
      },
      body: JSON.stringify(body),
    };

    fetch(
      uri
        .replace("{userid}", activeUserSession.userID.toString())
        .replace("{projectid}", issue.properties.project_id.toString())
        .replace("{issueid}", issue.properties.issue_id.toString()),
      options
    )
      .then((resp) => {
        if (resp.status > 300) return resp.json(); //to handle the errors
      })
      .then((json) => {
        if (json) {
          alert(json.detail);
        } else {
          handleCancel(); //since we don't use the router to browse between project edit/details, we need to use this function to go back to the project page
        }
      })
      .catch((err) => alert(err));
  }, [submit]);

  function handleIssueEditSubmit() {
    setSubmit(true);
  }

  function handleCancel() {
    setCancelState(true);
  }

  /*
  Since we need to pass the project content to the Edit project page, in order to avoid
  another request to the API, we can render the EditProject component and the Project component
  in the "same" page

  In this case, when the user clicks "Cancel" we render the project component and pass through the props
  all the data required to re-render the project details
  */
  if (cancelState) {
    return <Issue uri={uri} />;
  }

  return (
    <div className="ui form">
      <div className="fields">
        <div className="required field">
          <label>New Name</label>
          <input
            type="text"
            name="name"
            defaultValue={issue.properties.name}
            onChange={(event) => {
              setNewName(event.target.value); //since the fields are already filled, we can use the value prop (unless this fucks up bc we are using the value prop and it triggers on change)
            }}
          />
        </div>
        <div className="required field">
          <label>New Description</label>
          <input
            type="text"
            name="description"
            defaultValue={issue.properties.description}
            onChange={(event) => {
              setNewDescription(event.target.value);
            }}
          />
        </div>
      </div>
      <button
        className="ui button"
        type="submit"
        onClick={handleIssueEditSubmit}
      >
        Edit!
      </button>
      <button className="negative ui button" onClick={handleCancel}>
        Cancel
      </button>
    </div>
  );
}
