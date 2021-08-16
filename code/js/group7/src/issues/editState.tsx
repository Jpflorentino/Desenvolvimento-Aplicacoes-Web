import React, { useEffect, useState, useContext } from "react";
import { Redirect } from "react-router-dom";
import { Issue } from "../issues/issue";
import { IssueType } from "../common/types/issue";
import { NavbarContext } from "../navbar/navbarContext";
import { UserSessionContext} from "../users/userSessionContext";

/// EditState component props
type EditStateProps = {
  uri: string;
  issue: IssueType;
  changeStateURI: string;
}

export function EditState( { 
  uri, 
  issue, 
  changeStateURI,
 }: EditStateProps) {

  /// If user is not logged in, redirect to Login / Register page
  const { activeUserSession } = useContext(UserSessionContext);
  if (!activeUserSession.isLoggedIn) return <Redirect to="/loginRegister"/>;

  /// States
  const [cancelState, setCancelState] = useState(false);
  const [newState, setNewState] = useState("");
  const [submit, setSubmit] = useState(false);

  /// Update navbar active item
  const { activeItem, updateActiveItem } = useContext(NavbarContext);
  useEffect(() => { /// Avoid component side-effects
    if(activeItem !== "projects") updateActiveItem("projects");
  }, [])

  let body = {
    new_state: newState,
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
      changeStateURI
        .replace("{userid}",    activeUserSession.userID.toString())
        .replace("{projectid}", issue.properties.project_id.toString())
        .replace("{issueid}",   issue.properties.issue_id.toString()),
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

  function handleStateSubmit() {
    if (newState == "") {
      alert("State field can't be empty");
    } else {
      setSubmit(true);
    }
  }

  function handleCancel() {
    setCancelState(true);
  }

  if (cancelState) {
    return <Issue uri={uri}/>;
  }

  return (
    <div className="ui form">
      <div className="fields">
        <div className="required field">
          <label>New State</label>
          <input
            type="text"
            name="name"
            placeholder="The state to transition to"
            onChange={(event) => {
              setNewState(event.target.value);
            }}
          />
        </div>
        <button className="ui button" type="submit" onClick={handleStateSubmit}>
          Change State
        </button>
        <button className="negative ui button" onClick={handleCancel}>
          Cancel
        </button>
      </div>
    </div>
  );
}
