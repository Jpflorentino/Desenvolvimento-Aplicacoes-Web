import React, { useEffect, useState, useContext } from "react";
import { useHistory, Redirect, useParams } from "react-router-dom";
import { ProjectRouteParams } from "../common/types/utils";
import { NavbarContext } from "../navbar/navbarContext";
import { UserSessionContext } from "../users/userSessionContext";

/// CreateIssue component props
type CreateIssueProps = {
  uri: string;
};

export function CreateIssue({ uri }: CreateIssueProps) {
  /// If user is not logged in, redirect to Login / Register page
  const { activeUserSession } = useContext(UserSessionContext);
  if (!activeUserSession.isLoggedIn) return <Redirect to="/loginRegister" />;

  let { user_id, project_id } = useParams<ProjectRouteParams>();

  const history = useHistory();

  const [newName, setNewName] = useState("");
  const [newDescription, setNewDescription] = useState("");
  const [newClosedOn, setNewClosedOn] = useState<string | number>("");
  const [submit, setSubmit] = useState(false);

  /// Update navbar active item
  const { activeItem, updateActiveItem } = useContext(NavbarContext);
  useEffect(() => {
    /// Avoid component side-effects
    if (activeItem !== "projects") updateActiveItem("projects");
  }, []);

  let body = {
    name: newName,
    description: newDescription,
    closed_on: newClosedOn,
  };

  useEffect(() => {
    if (!submit) return;

    setSubmit(false);

    const options = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Basic " + activeUserSession.authCredentials,
      },
      body: JSON.stringify(body),
    };

    fetch(
      uri
        .replace("{userid}", activeUserSession.userID.toString())
        .replace("{projectid}", project_id)
        .replace("/{issueid}", ""),
      options
    )
      .then((resp) => {
        if (resp.status > 300) return resp.json(); //to handle the errors
      })
      .then((json) => {
        if (json) {
          alert(json.detail);
        } else {
          history.push(`/${user_id}/projects/${project_id}`);
        }
      })
      .catch((err) => alert(err));
  }, [submit]);

  function handleIssueCreateSubmit() {
    if (body.name == "" || body.description == "") {
      alert("The issue must have a name and description");
    } else if (body.closed_on == "") {
      delete body.closed_on; //the API throws an exception when the property is present but without a value, we need to remove the prop altogether for a request with no date
      setSubmit(true);
    } else {
      setNewClosedOn(new Date(newClosedOn).getTime() / 1000);
      setSubmit(true);
    }
  }

  function handleCancel() {
    history.push(`/${user_id}/projects/${project_id}`);
  }

  return (
    <div className="ui form">
      <div className="fields">
        <div className="required field">
          <label>New Name</label>
          <input
            type="text"
            name="name"
            placeholder="The issue's name"
            onChange={(event) => {
              setNewName(event.target.value);
            }}
          />
        </div>
        <div className="required field">
          <label>New Description</label>
          <input
            type="text"
            name="name"
            placeholder="The issue's description"
            onChange={(event) => {
              setNewDescription(event.target.value);
            }}
          />
        </div>
        <div className="field">
          <label>Closed on</label>
          <input
            type="date"
            name="name"
            onChange={(event) => {
              setNewClosedOn(event.target.value);
            }}
          />
        </div>
      </div>
      <button
        className="ui button"
        type="submit"
        onClick={handleIssueCreateSubmit}
      >
        Create an issue!
      </button>
      <button className="negative ui button" onClick={handleCancel}>
        Cancel
      </button>
    </div>
  );
}
